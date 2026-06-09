package classes;

public class Sensor {

    private Rua rua;

    public Sensor(Rua rua) {
        this.rua = rua;
    }

    public int contarVeiculos() {
        return rua.getQuantidadeVeiculos();
    }

    public void mostrarLeitura() {

        System.out.println(
                "Rua "
                + rua.getNome()
                + " possui "
                + contarVeiculos()
                + " veículos."
        );

    }

}