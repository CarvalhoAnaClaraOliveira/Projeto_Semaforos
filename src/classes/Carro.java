package classes;

public class Carro extends Veiculo {
    public Carro(
        int id,
        String direcao,
        Prioridade prioridade
    ) 
    {
        super(id, direcao, prioridade);
        setVelocidadeC(40);
        setVelocidadeA();
    }
}
