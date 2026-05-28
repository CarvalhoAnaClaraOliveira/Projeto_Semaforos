package classes;

public class Cruzamento {
    private int ordem;
    private String rua1;
    private String rua2;
    private Semaforo semaforo1;
    private Semaforo semaforo2;

    public Cruzamento(
            int ordem,
            String rua1,
            String rua2
    ) {

        this.ordem = ordem;
        this.rua1 = rua1;
        this.rua2 = rua2;
        semaforo1 = new Semaforo(rua1, 10);
        semaforo2 = new Semaforo(rua2, 10);

    }

    public void iniciarCruzamento() {
        System.out.println(
                "Cruzamento iniciado."
        );
        semaforo1.abrir();
        semaforo2.fechar();
    }

    public void trocarSemaforos() {
        semaforo1.fechar();
        semaforo2.abrir();

    }

    public void mostrarInformacoes() {
        System.out.println("Ordem: " + ordem);
        semaforo1.mostrarInformacoes();
        semaforo2.mostrarInformacoes();

    }

}