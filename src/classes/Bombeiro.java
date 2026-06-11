package classes;

public class Bombeiro extends Veiculo {
    private int andando = 60;
    private int parado =0;
    
    public Bombeiro(
        int id,
        String direcao,
        Prioridade prioridade
    ) {
        super(id, direcao, prioridade);
        this.setVelocidade(andando);
    }

}
