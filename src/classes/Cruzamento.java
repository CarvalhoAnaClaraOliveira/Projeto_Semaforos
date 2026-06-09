package classes;

public class Cruzamento {

    private int numero;
    private Rua ruaHorizontal;
    private Rua ruaVertical;
    private Semaforo semaforoHorizontal;
    private Semaforo semaforoVertical;

    public Cruzamento(
            int numero,
            Rua ruaHorizontal,
            Rua ruaVertical) {

        this.numero = numero;
        this.ruaHorizontal = ruaHorizontal;
        this.ruaVertical = ruaVertical;

    }

    public int getNumero() {
        return numero;
    }

    public Rua getRuaHorizontal() {
        return ruaHorizontal;
    }

    public Rua getRuaVertical() {
        return ruaVertical;
    }

    public Semaforo getSemaforoHorizontal() {
        return semaforoHorizontal;
    }

    public void setSemaforoHorizontal(Semaforo semaforoHorizontal) {
        this.semaforoHorizontal = semaforoHorizontal;
    }

    public Semaforo getSemaforoVertical() {
        return semaforoVertical;
    }

    public void setSemaforoVertical(Semaforo semaforoVertical) {
        this.semaforoVertical = semaforoVertical;
    }

    public void mostrarInformacoes() {

        System.out.println("Cruzamento: " + numero);

        System.out.println(
                "Rua Horizontal: "
                        + ruaHorizontal.getNome());

        System.out.println(
                "Rua Vertical: "
                        + ruaVertical.getNome());

    }

}