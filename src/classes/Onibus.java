package classes;

public class Onibus extends Veiculo {
    public Onibus(
        int id,
        String direcao,
        Prioridade prioridade
    ) 
    {
        super(id, direcao, prioridade);
        setVelocidadeC(20);
        setVelocidadeA();
    }

}
