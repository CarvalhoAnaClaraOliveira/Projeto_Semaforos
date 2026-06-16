package classes;

public class Ambulancia extends Veiculo {
    private int[] posicao;
    private Percurso p1;
    

    public Ambulancia(
        int id,
        String direcao,
        Prioridade prioridade, Percurso p1
        
    ) 
    {
        super(id, direcao, prioridade);
        setVelocidadeC(20);
        setVelocidadeA();
        this.posicao = new int[]{0,0};
        this.p1 = p1;
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
