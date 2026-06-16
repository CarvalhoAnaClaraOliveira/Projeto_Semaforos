package org.example.semaforos;

import classes.Cidade;
import classes.GerenciadorDeTrafego;

/**
 * Singleton to share application state (Cidade and GerenciadorDeTrafego)
 * between the simulation (main) and the JavaFX UI.
 */
public final class AppState {
    private static final AppState INSTANCE = new AppState();

    private Cidade cidade;
    private GerenciadorDeTrafego gerenciador;

    private AppState() {}

    public static AppState get() { return INSTANCE; }

    public synchronized void setCidade(Cidade c) { this.cidade = c; }
    public synchronized Cidade getCidade() { return this.cidade; }

    public synchronized void setGer(GerenciadorDeTrafego g) { this.gerenciador = g; }
    public synchronized GerenciadorDeTrafego getGer() { return this.gerenciador; }
}
