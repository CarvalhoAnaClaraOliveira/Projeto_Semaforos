package main;

import java.util.Random;
import classes.Cidade;
import classes.GerenciadorDeTrafego;
import classes.Rua;
import classes.Carro;
import classes.Prioridade;
import classes.Moto;
import classes.Onibus;
import classes.Pedestre;
import classes.Ambulancia;
import classes.Bombeiro;
import classes.Policia;
import classes.Percurso;

public class Main {

    public static void main(String[] args) {
        System.out.println("====== INICIANDO SIMULAÇÃO DE TRÁFEGO CONCORRENTE ======");

        // 1. Instancia a Cidade usando a lógica já criada por você
        Cidade cidade = new Cidade();
        cidade.mostrarCidade();

        // === ADIÇÃO: Inicialização de 3 veículos de cada tipo espalhados pela cidade ===
        povoarCidadeInicialmente(cidade);

        // 2. Inicia o Gerenciador de Tráfego como uma Thread independente
        GerenciadorDeTrafego gerenciador = new GerenciadorDeTrafego(cidade);
        Thread threadGerenciador = new Thread(gerenciador, "Thread-Gerenciador");
        threadGerenciador.start();

        // 3. Thread Geradora de Tráfego de Fundo (Injeta eventos aleatórios usando os
        // seus construtores de veículos)
        Thread geradorTráfego = new Thread(() -> {
            Random random = new Random();
            int idSequence = 22; // Começa após os IDs da inicialização para evitar duplicidade

            while (true) {
                try {
                    // Adiciona um novo elemento a cada 1 a 3 segundos
                    Thread.sleep(1000 + random.nextInt(2000));

                    Rua ruaEscolhida = cidade.getRuas().get(random.nextInt(cidade.getRuas().size()));
                    int trajetoId = random.nextInt(10);
                    Percurso trajeto = Percurso.obterTrajetoPreDefinido(trajetoId, cidade);

                    double sorteio = random.nextDouble();
                    synchronized (ruaEscolhida.getVeiculos()) {
                        if (sorteio < 0.35) {
                            // 35% de chance: Carro normal (Prioridade Baixa)
                            Carro carro = new Carro(idSequence++, "FRENTE", Prioridade.BAIXA);
                            carro.setPercurso(trajeto);
                            ruaEscolhida.adicionarVeiculo(carro);
                            System.out.println("-> [" + carro.getIdUnico() + "] Injetado com Trajeto " + trajetoId + " na rua: " + ruaEscolhida.getNome());
                        } else if (sorteio < 0.50) {
                            // 15% de chance: Moto (Prioridade Baixa)
                            Moto moto = new Moto(idSequence++, "FRENTE", Prioridade.BAIXA);
                            moto.setPercurso(trajeto);
                            ruaEscolhida.adicionarVeiculo(moto);
                            System.out.println("-> [" + moto.getIdUnico() + "] Injetada com Trajeto " + trajetoId + " na rua: " + ruaEscolhida.getNome());
                        } else if (sorteio < 0.65) {
                            // 15% de chance: Ônibus (Prioridade Alta)
                            Onibus onibus = new Onibus(idSequence++, "FRENTE", Prioridade.ALTA);
                            onibus.setPercurso(trajeto);
                            ruaEscolhida.adicionarVeiculo(onibus);
                            System.out.println("-> [" + onibus.getIdUnico() + "] Injetado com Trajeto " + trajetoId + " na rua: " + ruaEscolhida.getNome());
                        } else if (sorteio < 0.80) {
                            // 15% de chance: Pedestre atravessando fora do sinal (Prioridade Alta)
                            // CORRIGIDO: Usa o seu construtor correto e adiciona na lista de pedestres da rua
                            Pedestre pedestre = new Pedestre(idSequence++, "TRAVESSIA");
                            ruaEscolhida.adicionarPedestre(pedestre);
                            System.out.println("-> [PE" + pedestre.getId() + "] Iniciou travessia na rua: " + ruaEscolhida.getNome());
                        } else {
                            // 20% de chance distribuídos entre veículos de Emergência (Prioridade
                            // Emergência)
                            double tipoEmergencia = random.nextDouble();
                            if (tipoEmergencia < 0.33) {
                                Ambulancia ambulancia = new Ambulancia(idSequence++, "FRENTE", Prioridade.EMERGENCIA);
                                ambulancia.setPercurso(trajeto);
                                ruaEscolhida.adicionarVeiculo(ambulancia);
                                System.out.println("-> [" + ambulancia.getIdUnico() + "] Iniciou código de emergência na rua: "
                                        + ruaEscolhida.getNome());
                            } else if (tipoEmergencia < 0.66) {
                                Bombeiro bombeiro = new Bombeiro(idSequence++, "FRENTE", Prioridade.EMERGENCIA);
                                bombeiro.setPercurso(trajeto);
                                ruaEscolhida.adicionarVeiculo(bombeiro);
                                System.out.println("-> [" + bombeiro.getIdUnico() + "] Iniciou código de emergência na rua: "
                                        + ruaEscolhida.getNome());
                            } else {
                                Policia policia = new Policia(idSequence++, "FRENTE", Prioridade.EMERGENCIA);
                                policia.setPercurso(trajeto);
                                ruaEscolhida.adicionarVeiculo(policia);
                                System.out.println("-> [" + policia.getIdUnico() + "] Iniciou perseguição/emergência na rua: "
                                        + ruaEscolhida.getNome());
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Thread-Geradora");

        geradorTráfego.start();

        // 4. Thread para exibição periódica de logs dos Sensores no terminal
        try {
            while (true) {
                Thread.sleep(4000);
                System.out.println("\n===== MONITORAMENTO DE SENSORES =====");
                for (Rua r : cidade.getRuas()) {
                    r.getSensor().mostrarLeitura();
                }
                System.out.println("=====================================\n");
            }
        } catch (InterruptedException e) {
            System.out.println("A simulação foi finalizada.");
        }
    }

    // === MÉTODO ADICIONAL PARA POVOAR A CIDADE INICIALMENTE (MÍNIMO 3 DE CADA TIPO) ===
    private static void povoarCidadeInicialmente(Cidade cidade) {
        Random random = new Random();
        System.out.println("Povoando cidade com carga inicial mínima de 3 veículos de cada classe...");

        for (int i = 1; i <= 3; i++) {
            // Escolhe ruas de destino aleatórias
            Rua ruaH = cidade.getRuas().get(random.nextInt(4));
            Rua ruaV = cidade.getRuas().get(4 + random.nextInt(4));

            // Injeta Carro
            Carro c = new Carro(i, "FRENTE", Prioridade.BAIXA);
            c.setPercurso(Percurso.obterTrajetoPreDefinido(random.nextInt(10), cidade));
            cidade.getRuas().get(random.nextInt(8)).adicionarVeiculo(c);

            // Injeta Moto
            Moto m = new Moto(i, "FRENTE", Prioridade.BAIXA);
            m.setPercurso(Percurso.obterTrajetoPreDefinido(random.nextInt(10), cidade));
            cidade.getRuas().get(random.nextInt(8)).adicionarVeiculo(m);

            // Injeta Ônibus
            Onibus o = new Onibus(i, "FRENTE", Prioridade.ALTA);
            o.setPercurso(Percurso.obterTrajetoPreDefinido(random.nextInt(10), cidade));
            cidade.getRuas().get(random.nextInt(8)).adicionarVeiculo(o);

            // Injeta Pedestre (Usando o construtor correto de 2 parâmetros)
            Pedestre p = new Pedestre(i, "TRAVESSIA");
            cidade.getRuas().get(random.nextInt(8)).adicionarPedestre(p);

            // Injeta Ambulância
            Ambulancia am = new Ambulancia(i, "FRENTE", Prioridade.EMERGENCIA);
            am.setPercurso(Percurso.obterTrajetoPreDefinido(random.nextInt(10), cidade));
            cidade.getRuas().get(random.nextInt(8)).adicionarVeiculo(am);

            // Injeta Bombeiro
            Bombeiro b = new Bombeiro(i, "FRENTE", Prioridade.EMERGENCIA);
            b.setPercurso(Percurso.obterTrajetoPreDefinido(random.nextInt(10), cidade));
            cidade.getRuas().get(random.nextInt(8)).adicionarVeiculo(b);

            // Injeta Polícia
            Policia po = new Policia(i, "FRENTE", Prioridade.EMERGENCIA);
            po.setPercurso(Percurso.obterTrajetoPreDefinido(random.nextInt(10), cidade));
            cidade.getRuas().get(random.nextInt(8)).adicionarVeiculo(po);
        }
        System.out.println("Carga de inicialização carregada com sucesso.");
    }
}