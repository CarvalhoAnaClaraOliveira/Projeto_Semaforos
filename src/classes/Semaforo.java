package classes;

public class Semaforo {

    private Rua rua;
    private int status;
    private int tempoAberto;

    public Semaforo(
            Rua rua,
            int tempoAberto
    ) {

        this.rua = rua;
        this.tempoAberto = tempoAberto;
        this.status = 0;

    }
    public int getStatus() {
        return status;
    }

    public int getTempoAberto() {
        return tempoAberto;
    }

    public Rua getRua() {
        return rua;
    }   

    public void abrir() {

        status = 1;

        System.out.println(
                "Semáforo da rua "
                + rua.getNome()
                + " ABERTO"
        );

    }

    public void fechar() {

        status = 0;

        System.out.println(
                "Semáforo da rua "
                + rua.getNome()
                + " FECHADO"
        );

    }

    public void mostrarInformacoes() {

        System.out.println(
                "Rua: "
                + rua.getNome()
        );

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

}