package classes;

public class Bombeiro extends Veiculo {
    public Bombeiro(
        int id,
        String direcao,
        Prioridade prioridade
    ) 
    {
        super(id, direcao, prioridade);
        setVelocidadeC(60);
        setVelocidadeA();
    }

}
