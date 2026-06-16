package classes;

public class Ambulancia extends Veiculo {
    private int[] posicao;
    

    public Ambulancia(
        int id,
        String direcao,
        Prioridade prioridade
        
    ) 
    {
        super(id, direcao, prioridade);
        setVelocidadeC(20);
        setVelocidadeA();
        this.posicao = new int[]{0,0};
    }


    public int[] getPosicao() {
        return posicao;
    }


    public void setPosicao(int[] posicao) {
        this.posicao = posicao;
    }
    public void setPosicaoH() {
        this.posicao[0] += getVelocidadeA() ;
    }
    public void setPosicaoV() {
        this.posicao[1] += getVelocidadeA() ;
    }
    

    
}
