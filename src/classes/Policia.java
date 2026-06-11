package classes;

public class Policia extends Veiculo {
    private int andando = 60;
    private int parado =0;

    public Policia(
        int id,
        String direcao,
        Prioridade prioridade
    ) {
        super(id, direcao, prioridade);
        this.setVelocidade(andando);
    }

}
