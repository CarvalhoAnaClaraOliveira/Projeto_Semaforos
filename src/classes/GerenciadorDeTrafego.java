package classes;

public class GerenciadorTrafego implements Runnable {

    private final Cidade cidade;
    private boolean rodando = true;

    public GerenciadorTrafego(Cidade cidade) {
        this.cidade = cityGridFix(cidade);
    }

    // Vincula as ruas às listas de cruzamento para referência mútua correta
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
                // Monitora todos os cruzamentos e aplica as decisões autônomas
                for (Cruzamento cruzamento : cidade.getCruzamentos()) {
                    gerenciarCruzamento(cruzamento);
                }

                // Processa o movimento e escoamento físico dos veículos
                escoarVeiculos();

                // Frequência de atualização (Sistemas Operacionais)
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

        // 1. Atualizar temporizadores dos sensores para rastrear envelhecimento (Timer / Aging)
        sensH.atualizarTimerSinal(semH.getStatus() == 0);
        sensV.atualizarTimerSinal(semV.getStatus() == 0);

        boolean pedestreH, pedestreV, emergH, emergV;

        // Sincronização segura para leitura de dados concorrentes das listas
        synchronized (ruaH.getVeiculos()) {
            pedestreH = verificarPedestre(ruaH);
            emergH = verificarEmergencia(ruaH);
        }
        synchronized (ruaV.getVeiculos()) {
            pedestreV = verificarPedestre(ruaV);
            emergV = verificarEmergencia(ruaV);
        }

        // REGRA 1: Pedestres têm prioridade total e travam o fluxo de carros
        if (pedestreH) pararFluxoCarros(ruaH);
        if (pedestreV) pararFluxoCarros(ruaV);

        // REGRA 2: Veículos de emergência passam mesmo no vermelho e forçam o outro lado a parar
        if (emergH && !emergV) {
            abrirManual(semH, semV);
            pararFluxoCarros(ruaV); // Sentido vertical é obrigado a esperar
            return;
        } else if (emergV && !emergH) {
            abrirManual(semV, semH);
            pararFluxoCarros(ruaH); // Sentido horizontal é obrigado a esperar
            return;
        }

        // REGRA 3: Decisão por volume e Aging (temporizador do sensor)
        long tempoFechadoH = sensH.getTempoSinalFechadoSegundos();
        long tempoFechadoV = sensV.getTempoSinalFechadoSegundos();

        // Cálculo de prioridade: quantidade de veículos + tempo esperando no sinal vermelho
        double scoreH = ruaH.getQuantidadeVeiculos() + (tempoFechadoH * 0.7);
        double scoreV = ruaV.getQuantidadeVeiculos() + (tempoFechadoV * 0.7);

        if (scoreH > scoreV) {
            abrirManual(semH, semV);
        } else if (scoreV > scoreH) {
            abrirManual(semV, semH);
        }
    }

    private void abrirManual(Semaforo abrir, Semaforo fechar) {
        if (abrir.getStatus() == 0) {
            fechar.fechar();
            abrir.abrir();
        }
    }

    private boolean verificarPedestre(Rua rua) {
        for (Veiculo v : rua.getVeiculos()) {
            if (v instanceof Pedestre) return true;
        }
        return false;
    }

    private boolean verificarEmergencia(Rua rua) {
        for (Veiculo v : rua.getVeiculos()) {
            if (v.getPrioridade() == Prioridade.EMERGENCIA) return true;
        }
        return false;
    }

    private void pararFluxoCarros(Rua rua) {
        synchronized (rua.getVeiculos()) {
            for (Veiculo v : rua.getVeiculos()) {
                if (!(v instanceof Pedestre) && v.getPrioridade() != Prioridade.EMERGENCIA) {
                    v.pararVeiculo();
                }
            }
        }
    }

    private void escoarVeiculos() {
        for (Cruzamento c : cidade.getCruzamentos()) {
            Rua ruaH = c.getRuaHorizontal();
            Rua ruaV = c.getRuaVertical();

            // Escoamento da rua Horizontal (se sinal aberto)
            synchronized (ruaH.getVeiculos()) {
                if (c.getSemaforoHorizontal().getStatus() == 1 && ruaH.getQuantidadeVeiculos() > 0) {
                    Veiculo v = ruaH.getVeiculos().get(0);
                    if (v instanceof Pedestre) {
                        System.out.println("[Pedestre] Terminou a travessia na rua " + ruaH.getNome());
                        ruaH.removerVeiculo(v);
                    } else {
                        v.setVelocidadeA(); // Carro retoma movimento
                        System.out.println("[" + v.getClass().getSimpleName() + " ID: " + v.getPrioridade() + "] passou pelo Cruzamento #" + c.getNumero() + " (" + ruaH.getNome() + ")");
                        ruaH.removerVeiculo(v);
                    }
                }
            }

            // Escoamento da rua Vertical (se sinal aberto)
            synchronized (ruaV.getVeiculos()) {
                if (c.getSemaforoVertical().getStatus() == 1 && ruaV.getQuantidadeVeiculos() > 0) {
                    Veiculo v = ruaV.getVeiculos().get(0);
                    if (v instanceof Pedestre) {
                        System.out.println("[Pedestre] Terminou a travessia na rua " + ruaV.getNome());
                        ruaV.removerVeiculo(v);
                    } else {
                        v.setVelocidadeA(); // Carro retoma movimento
                        System.out.println("[" + v.getClass().getSimpleName() + " ID: " + v.getPrioridade() + "] passou pelo Cruzamento #" + c.getNumero() + " (" + ruaV.getNome() + ")");
                        ruaV.removerVeiculo(v);
                    }
                }
            }
        }
    }
}