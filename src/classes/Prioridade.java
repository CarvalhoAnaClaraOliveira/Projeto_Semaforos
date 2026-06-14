package classes;
public enum Prioridade {
    MEDIA(2),
    ALTA(1),
    EMERGENCIA(0);

    private final int nivel;

    Prioridade(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }
}