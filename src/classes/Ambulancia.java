package classes;

public class Ambulancia extends Veiculo {
    private int andando = 60;
    private int parado = 0;
    
    public Ambulancia(
        int id,
        String direcao,
        Prioridade prioridade
    ) {
        super(id, direcao, prioridade);
        this.setVelocidade(andando);
    }
    
}
