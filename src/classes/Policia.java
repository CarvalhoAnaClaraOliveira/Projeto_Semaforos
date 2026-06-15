package classes;

public class Policia extends Veiculo {
    public Policia(
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
