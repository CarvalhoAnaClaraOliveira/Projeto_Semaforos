package classes;

public class Carro extends Veiculo {
    private int andando = 30;
    private int parado =0;

    public Carro(
        int id,
        String direcao,
        Prioridade prioridade
    ) {
        super(id, direcao, prioridade);
        this.setVelocidade(andando);
    }
}
