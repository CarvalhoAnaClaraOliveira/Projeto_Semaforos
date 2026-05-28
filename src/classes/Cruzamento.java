package classes;

public class Cruzamento {

    private int id;

    private Semaforo semaforoNorteSul;
    private Semaforo semaforoLesteOeste;

    public Cruzamento(int id) {

        this.id = id;

        semaforoNorteSul = new Semaforo();
        semaforoLesteOeste = new Semaforo();

    }

    public void liberarNorteSul() {

        semaforoNorteSul.abrir();
        semaforoLesteOeste.fechar();

    }

    public void liberarLesteOeste() {

        semaforoLesteOeste.abrir();
        semaforoNorteSul.fechar();

    }

}