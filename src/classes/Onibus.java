package classes;

public class Onibus extends Veiculo {
    private int andando = 60;
    private int parado =0;

    public Onibus(
        int id,
        String direcao,
        Prioridade prioridade
    ) {
        super(id, direcao, prioridade);
        this.setVelocidade(andando);
    }

}
