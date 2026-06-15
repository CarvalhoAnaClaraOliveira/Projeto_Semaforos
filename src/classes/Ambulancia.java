package classes;

public class Ambulancia extends Veiculo {

    public Ambulancia(
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
