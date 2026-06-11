package classes;

public class Moto  extends Veiculo {
    private int andando = 60;
    private int parado =0;

    public Moto(
        int id,
        String direcao,
        Prioridade prioridade
    ) {
        super(id, direcao, prioridade);
        this.setVelocidade(andando);
    }
}
