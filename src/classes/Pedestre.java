package classes;

public class Pedestre{
    private int id;
    private String direcao;
    private int velocidade;

    public Pedestre(int id, String direcao, int velocidade){
        this.id = id;
        this.direcao = direcao;
        this.velocidade = velocidade;
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

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }
    

}