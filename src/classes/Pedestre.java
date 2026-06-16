package classes;


/// colocar velocidade atual e constante
public class Pedestre{
    private int id;
    private String direcao;
    private int velocidadeA;
    private int velocidadeC;

    public Pedestre(
        int id,
        String direcao
    ) 
    {
        this.id =id;
        this.direcao = direcao;
        this.velocidadeC = 10;
        this.velocidadeA = this.velocidadeC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirecao() {
        return direcao;
    }

    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    public void setVelocidadeA(){
        this.velocidadeA = this.velocidadeC;
    }
    public int getVelocidadeA(){
        return this.velocidadeA;
    }

}