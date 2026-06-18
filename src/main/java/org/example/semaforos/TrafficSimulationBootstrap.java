package org.example.semaforos;

import classes.Ambulancia;
import classes.Bombeiro;
import classes.Carro;
import classes.Cidade;
import classes.Moto;
import classes.Onibus;
import classes.Pedestre;
import classes.Percurso;
import classes.Policia;
import classes.Prioridade;
import classes.Rua;
import classes.Veiculo;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

final class TrafficSimulationBootstrap {
    private static final Random RANDOM = new Random();

    private TrafficSimulationBootstrap() {
    }

    static void povoarCidadeInicialmente(Cidade cidade) {
        AtomicInteger ids = new AtomicInteger(1);

        for (int i = 0; i < 3; i++) {
            adicionarVeiculoAleatorio(cidade, new Carro(ids.getAndIncrement(), "FRENTE", Prioridade.BAIXA));
            adicionarVeiculoAleatorio(cidade, new Moto(ids.getAndIncrement(), "FRENTE", Prioridade.BAIXA));
            adicionarVeiculoAleatorio(cidade, new Onibus(ids.getAndIncrement(), "FRENTE", Prioridade.ALTA));
            adicionarPedestreAleatorio(cidade, new Pedestre(ids.getAndIncrement(), "TRAVESSIA"));
            adicionarVeiculoAleatorio(cidade, new Ambulancia(ids.getAndIncrement(), "FRENTE", Prioridade.EMERGENCIA));
            adicionarVeiculoAleatorio(cidade, new Bombeiro(ids.getAndIncrement(), "FRENTE", Prioridade.EMERGENCIA));
            adicionarVeiculoAleatorio(cidade, new Policia(ids.getAndIncrement(), "FRENTE", Prioridade.EMERGENCIA));
        }
    }

    static Thread iniciarGeradorDeTrafego(Cidade cidade) {
        AtomicInteger ids = new AtomicInteger(100);
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000 + RANDOM.nextInt(2000));
                    Rua rua = ruaAleatoria(cidade);
                    double sorteio = RANDOM.nextDouble();

                    if (sorteio < 0.35) {
                        adicionarVeiculo(rua, comPercurso(new Carro(ids.getAndIncrement(), "FRENTE", Prioridade.BAIXA), cidade));
                    } else if (sorteio < 0.50) {
                        adicionarVeiculo(rua, comPercurso(new Moto(ids.getAndIncrement(), "FRENTE", Prioridade.BAIXA), cidade));
                    } else if (sorteio < 0.65) {
                        adicionarVeiculo(rua, comPercurso(new Onibus(ids.getAndIncrement(), "FRENTE", Prioridade.ALTA), cidade));
                    } else if (sorteio < 0.80) {
                        rua.adicionarPedestre(new Pedestre(ids.getAndIncrement(), "TRAVESSIA"));
                    } else {
                        adicionarVeiculo(rua, criarEmergencia(ids.getAndIncrement(), cidade));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Thread-Geradora-UI");

        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    private static void adicionarVeiculoAleatorio(Cidade cidade, Veiculo veiculo) {
        adicionarVeiculo(ruaAleatoria(cidade), comPercurso(veiculo, cidade));
    }

    private static void adicionarPedestreAleatorio(Cidade cidade, Pedestre pedestre) {
        ruaAleatoria(cidade).adicionarPedestre(pedestre);
    }

    private static Rua ruaAleatoria(Cidade cidade) {
        return cidade.getRuas().get(RANDOM.nextInt(cidade.getRuas().size()));
    }

    private static Veiculo comPercurso(Veiculo veiculo, Cidade cidade) {
        veiculo.setPercurso(Percurso.obterTrajetoPreDefinido(RANDOM.nextInt(10), cidade));
        return veiculo;
    }

    private static Veiculo criarEmergencia(int id, Cidade cidade) {
        Veiculo veiculo;
        int tipo = RANDOM.nextInt(3);
        if (tipo == 0) {
            veiculo = new Ambulancia(id, "FRENTE", Prioridade.EMERGENCIA);
        } else if (tipo == 1) {
            veiculo = new Bombeiro(id, "FRENTE", Prioridade.EMERGENCIA);
        } else {
            veiculo = new Policia(id, "FRENTE", Prioridade.EMERGENCIA);
        }
        veiculo.ligarSirene();
        return comPercurso(veiculo, cidade);
    }

    private static void adicionarVeiculo(Rua rua, Veiculo veiculo) {
        synchronized (rua.getVeiculos()) {
            rua.adicionarVeiculo(veiculo);
        }
    }
}
