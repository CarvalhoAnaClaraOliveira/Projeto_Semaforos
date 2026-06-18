package classes;

public class Moto  extends Veiculo {
    public Moto(
        int id,
        String direcao,
        Prioridade prioridade
    ) 
    {
        super(id, direcao, prioridade);
        setVelocidadeC(50);
        setVelocidadeA();
    }
}
