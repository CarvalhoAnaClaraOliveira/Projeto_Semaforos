package classes;

public class Semaforo {
    private String rua;
    private volatile int status;
    private int tempoAberto;

    public Semaforo(String rua, int tempoAberto) {
        this.rua = rua;
        this.tempoAberto = tempoAberto;
        status = 0;
    }

    public void abrir() {
        status = 1;
        System.out.println(
                "Semáforo da rua "
                + rua
                + " ABERTO"
        );
    }

    public void fechar() {
        status = 0;
        System.out.println(
                "Semáforo da rua "
                + rua
                + " FECHADO"
        );
    }

    public void mostrarInformacoes() {
        System.out.println("Rua: " + rua);
        if (status == 1) {
            System.out.println("Status: ABERTO");
        } else {
            System.out.println("Status: FECHADO");
        }

        System.out.println(
                "Tempo aberto: "
                + tempoAberto
        );
    }

    public int getStatus() {
        return this.status;
    }
}
