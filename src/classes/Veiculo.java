package classes;

public abstract class Veiculo {

    private int id;
    private String direcao;
    private int velocidade;
    private boolean sireneLigada;
    private Prioridade prioridade;

    public Veiculo ( 
        int id,
        String direcao,
        Prioridade prioridade)
        {
        this.id=id;
        this.direcao=direcao;
        this.prioridade=prioridade;
        }

    public void ligarSirene() {
        sireneLigada = true;
    }

    public void desligarSirene() {
        sireneLigada = false;
    }

    public boolean isSireneLigada() {
        return sireneLigada;
    }

    public int getVelocidade() {
        return velocidade;
    }
    public void setVelocidade(int velocidade){
        this.velocidade = velocidade;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(
            Prioridade prioridade
    ) {
        this.prioridade = prioridade;
    }

}