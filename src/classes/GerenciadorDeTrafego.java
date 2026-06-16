package classes;

public class GerenciadorDeTrafego implements Runnable {

    private final Cidade cidade;
    private boolean rodando = true;
    
    // Acelera o tempo da simulação para que os 100km/120km sejam percorridos de forma visível
    private final double fatorEscalaTempo = 180.0; 

    public GerenciadorDeTrafego(Cidade cidade) {
        this.cidade = cityGridFix(cidade);
    }

    private Cidade cityGridFix(Cidade c) {
        for (Cruzamento cruz : c.getCruzamentos()) {
            cruz.getRuaHorizontal().adicionarCruzamento(cruz);
            cruz.getRuaVertical().adicionarCruzamento(cruz);
        }
        return c;
    }

    public void parar() {
        this.rodando = false;
    }

    @Override
    public void run() {
        while (rodando) {
            try {
                // 1. Processar a inteligência dos Semáforos em todos os cruzamentos
                for (Cruzamento cruzamento : cidade.getCruzamentos()) {
                    gerenciarCruzamento(cruzamento);
                }
                
                // 2. Mover fisicamente os veículos ao longo das vias (Km)
                moverFisicamenteVeiculos();

                // 3. Liberar (escoar) os pedestres e veículos que concluíram a travessia
                escoarElementos();
                
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void gerenciarCruzamento(Cruzamento c) {
        Rua ruaH = c.getRuaHorizontal();
        Rua ruaV = c.getRuaVertical();

        Semaforo semH = c.getSemaforoHorizontal();
        Semaforo semV = c.getSemaforoVertical();

        Sensor sensH = ruaH.getSensor();
        Sensor sensV = ruaV.getSensor();

        // Atualizar temporizadores dos sensores para rastrear envelhecimento (Timer / Aging)
        sensH.atualizarTimerSinal(semH.getStatus() == 0);
        sensV.atualizarTimerSinal(semV.getStatus() == 0);

        boolean temPedestreH, temPedestreV;
        boolean emergH_Ativa, emergV_Ativa;

        synchronized (ruaH.getVeiculos()) {
            temPedestreH = !ruaH.getPedestres().isEmpty();
            emergH_Ativa = verificarEmergenciaAtiva(ruaH);
        }
        synchronized (ruaV.getVeiculos()) {
            temPedestreV = !ruaV.getPedestres().isEmpty();
            emergV_Ativa = verificarEmergenciaAtiva(ruaV);
        }

        // REGRA DE PEDESTRES: Param para emergência ativa, mas bloqueiam carros comuns
        if (temPedestreH) {
            if (emergH_Ativa) {
                for (Pedestre p : ruaH.getPedestres()) {
                    p.setVelocidade(p.parado); // Espera na calçada
                }
            } else {
                for (Pedestre p : ruaH.getPedestres()) {
                    p.setVelocidade(p.andando);
                }
                pararFluxoCarrosNormais(ruaH);
            }
        }

        if (temPedestreV) {
            if (emergV_Ativa) {
                for (Pedestre p : ruaV.getPedestres()) {
                    p.setVelocidade(p.parado); // Espera na calçada
                }
            } else {
                for (Pedestre p : ruaV.getPedestres()) {
                    p.setVelocidade(p.andando);
                }
                pararFluxoCarrosNormais(ruaV);
            }
        }

        // REGRA DE EMERGÊNCIA: Passam mesmo no vermelho
        if (emergH_Ativa && !emergV_Ativa) {
            abrirSemaforo(semH, semV);
            pararFluxoCarrosNormais(ruaV);
            // Retoma o fluxo da via aberta se ela não estiver bloqueada por pedestres
            if (!temPedestreH) retomarFluxoCarros(ruaH);
            return;
        } else if (emergV_Ativa && !emergH_Ativa) {
            abrirSemaforo(semV, semH);
            pararFluxoCarrosNormais(ruaH);
            // Retoma o fluxo da via aberta se ela não estiver bloqueada por pedestres
            if (!temPedestreV) retomarFluxoCarros(ruaV);
            return;
        }

        // Decisão padrão por volume + temporizador do sensor (Aging)
        long tempoFechadoH = sensH.getTempoSinalFechadoSegundos();
        long tempoFechadoV = sensV.getTempoSinalFechadoSegundos();

        double scoreH = ruaH.getQuantidadeVeiculos() + (tempoFechadoH * 0.7);
        double scoreV = ruaV.getQuantidadeVeiculos() + (tempoFechadoV * 0.7);

        if (scoreH > scoreV) {
            abrirSemaforo(semH, semV);
            pararFluxoCarrosNormais(ruaV);
            if (!temPedestreH) retomarFluxoCarros(ruaH);
        } else if (scoreV > scoreH) {
            abrirSemaforo(semV, semH);
            pararFluxoCarrosNormais(ruaH);
            if (!temPedestreV) retomarFluxoCarros(ruaV);
        }
    }

    private void abrirSemaforo(Semaforo abrir, Semaforo fechar) {
        if (abrir.getStatus() == 0) {
            fechar.fechar();
            abrir.abrir();
        }
    }

    private boolean verificarEmergenciaAtiva(Rua rua) {
        for (Veiculo v : rua.getVeiculos()) {
            if (v.getPrioridade() == Prioridade.EMERGENCIA && v.isSireneLigada()) {
                return true;
            }
        }
        return false;
    }

    private void pararFluxoCarrosNormais(Rua rua) {
        synchronized (rua.getVeiculos()) {
            for (Veiculo v : rua.getVeiculos()) {
                if (v.getPrioridade() != Prioridade.EMERGENCIA) {
                    v.pararVeiculo(); // Velocidade cai instantaneamente a 0km/h
                }
            }
        }
    }

    // === NOVO MÉTODO: Retoma instantaneamente a velocidade de cruzeiro ao abrir o sinal ===
    private void retomarFluxoCarros(Rua rua) {
        synchronized (rua.getVeiculos()) {
            for (Veiculo v : rua.getVeiculos()) {
                v.setVelocidadeA(); // Retoma velocidade de cruzeiro original de forma imediata
            }
        }
    }

    // Move os veículos pelas ruas em Km
    private void moverFisicamenteVeiculos() {
        for (Rua rua : cidade.getRuas()) {
            double comprimentoRua = rua.getOrientacao().equalsIgnoreCase("HORIZONTAL") ? 120.0 : 100.0;
            synchronized (rua.getVeiculos()) {
                for (Veiculo v : rua.getVeiculos()) {
                    if (v.getVelocidadeA() > 0) {
                        // Avança a quilometragem física com base na velocidade constante
                        double deltaKm = (v.getVelocidadeA() / 3600.0) * fatorEscalaTempo;
                        v.setKmPercorridoNaRuaAtual(Math.min(v.getKmPercorridoNaRuaAtual() + deltaKm, comprimentoRua));
                    }
                }
            }
        }
    }

    private void escoarElementos() {
        for (Cruzamento c : cidade.getCruzamentos()) {
            Rua ruaH = c.getRuaHorizontal();
            Rua ruaV = c.getRuaVertical();

            // 1. Escoar Pedestres na Horizontal (Sincronizado de forma segura no objeto da ruaH)
            synchronized (ruaH) {
                if (!ruaH.getPedestres().isEmpty()) {
                    Pedestre p = ruaH.getPedestres().get(0);
                    if (p.getVelocidade() > 0) {
                        System.out.println("[Pedestre " + p.getId() + "] Atravessou a rua " + ruaH.getNome() + " com velocidade " + p.getVelocidade() + " km/h");
                        ruaH.removerPedestre(p);
                        // Retoma o tráfego de carros da horizontal que estava parado para o pedestre
                        retomarFluxoCarros(ruaH);
                    }
                }
            }

            // 2. Escoar Veículos na Horizontal (Se o sinal estiver aberto e veículo percorreu a rua física)
            synchronized (ruaH.getVeiculos()) {
                if (c.getSemaforoHorizontal().getStatus() == 1 && ruaH.getQuantidadeVeiculos() > 0) {
                    Veiculo v = ruaH.getVeiculos().get(0);
                    double comprimentoRua = 120.0;
                    if (v.getKmPercorridoNaRuaAtual() >= comprimentoRua) {
                        v.setVelocidadeA(); // Salta instantaneamente para a velocidade constante
                        v.setKmPercorridoNaRuaAtual(0.0); // Reseta para a próxima rua
                        ruaH.removerVeiculo(v);

                        avancarOuExcluirVeiculo(v, c, ruaH);
                    }
                }
            }

            // 3. Escoar Pedestres na Vertical (Sincronizado de forma segura no objeto da ruaV)
            synchronized (ruaV) {
                if (!ruaV.getPedestres().isEmpty()) {
                    Pedestre p = ruaV.getPedestres().get(0);
                    if (p.getVelocidade() > 0) {
                        System.out.println("[Pedestre " + p.getId() + "] Atravessou a rua " + ruaV.getNome() + " com velocidade " + p.getVelocidade() + " km/h");
                        ruaV.removerPedestre(p);
                        // Retoma o tráfego de carros da vertical que estava parado para o pedestre
                        retomarFluxoCarros(ruaV);
                    }
                }
            }

            // 4. Escoar Veículos na Vertical (Se o sinal estiver aberto e veículo percorreu a rua física)
            synchronized (ruaV.getVeiculos()) {
                if (c.getSemaforoVertical().getStatus() == 1 && ruaV.getQuantidadeVeiculos() > 0) {
                    Veiculo v = ruaV.getVeiculos().get(0);
                    double comprimentoRua = 100.0;
                    if (v.getKmPercorridoNaRuaAtual() >= comprimentoRua) {
                        v.setVelocidadeA(); // Salta instantaneamente para a velocidade constante
                        v.setKmPercorridoNaRuaAtual(0.0); // Reseta para a próxima rua
                        ruaV.removerVeiculo(v);

                        avancarOuExcluirVeiculo(v, c, ruaV);
                    }
                }
            }
        }
    }

    private void avancarOuExcluirVeiculo(Veiculo v, Cruzamento c, Rua ruaOrigem) {
        Percurso p = v.getPercurso();
        if (p != null) {
            p.avancar();
            if (!p.completou()) {
                Cruzamento proxCruzamento = p.getCruzamentoAtual();
                // Encontra a rua conectada no próximo cruzamento
                Rua proximaRua = proxCruzamento.getRuaHorizontal();
                if (proximaRua.getNome().equals(ruaOrigem.getNome())) {
                    proximaRua = proxCruzamento.getRuaVertical();
                }

                synchronized (proximaRua.getVeiculos()) {
                    proximaRua.adicionarVeiculo(v);
                }
                System.out.println("[" + v.getIdUnico() + "] passou pelo Cruzamento #" + c.getNumero() + " e avançou para a rua " + proximaRua.getNome());
            } else {
                // Chegou ao fim: retorna ao cruzamento inicial para ser excluído
                Cruzamento inicial = p.getCruzamentos().get(0);
                System.out.println("[" + v.getIdUnico() + "] completou seu trajeto pré-definido de 10 cruzamentos, retornou ao ponto inicial (Cruzamento #" + inicial.getNumero() + ") e foi EXCLUÍDO da cidade.");
            }
        } else {
            System.out.println("[" + v.getIdUnico() + "] completou seu trajeto padrão e saiu da cidade.");
        }
    }
}