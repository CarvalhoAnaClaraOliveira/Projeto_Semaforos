package org.example.semaforos;

import javafx.application.Application;
import classes.Cidade;
import classes.GerenciadorDeTrafego;

public class Launcher {
    public static void main(String[] args) {
        // Initialize simulation state and register in AppState so UI can read it
        Cidade c = new Cidade();
        TrafficSimulationBootstrap.povoarCidadeInicialmente(c);

        GerenciadorDeTrafego g = new GerenciadorDeTrafego(c);

        AppState.get().setCidade(c);
        AppState.get().setGer(g);

        Thread t = new Thread(g);
        t.setDaemon(true);
        t.start();
        TrafficSimulationBootstrap.iniciarGeradorDeTrafego(c);

        Application.launch(DashboardMetrics.class, args);
    }
}
