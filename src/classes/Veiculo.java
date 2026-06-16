package classes;

public abstract class Veiculo {

    ////VER SE VAI MANTER PRIORIDADE ASSIM OU SE JA ADICIONA DIRETO CONFORME O TIPO DE VEICULO

    private int id;
    private String direcao;
    private int velocidadeA;
    private boolean sireneLigada;
    private Prioridade prioridade;
    private int velocidadeC;

    public Veiculo ( 
        int id,
        String direcao,
        Prioridade prioridade)
        {
        this.id=id;
        this.direcao=direcao;
        this.prioridade=prioridade;
        }

    /////////////// SIRENE /////////////////
    public void ligarSirene() {
        sireneLigada = true;
    }

    public void desligarSirene() {
        sireneLigada = false;
    }

    public boolean isSireneLigada() {
        return sireneLigada;
    }

    ///////////////// PRIORIDADE  ///////////////////////
    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(
            Prioridade prioridade
    ) {
        this.prioridade = prioridade;
    }

    //////////// VELOCIDADE  ////////////
    public void pararVeiculo(){
        this.velocidadeA =0;
    }
    public void setVelocidadeA(){
        this.velocidadeA = this.velocidadeC;
    }
    public void setVelocidadeC(int vel){
        this.velocidadeC = vel;
    }
    public int getVelocidadeC(){
        return velocidadeC;
    }
    public int getVelocidadeA(){
        return velocidadeA;
    }

    ///////////////////// OUTROS ///////////////
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

    public void setVelocidadeA(int velocidadeA) {
        this.velocidadeA = velocidadeA;
    }

    public void setSireneLigada(boolean sireneLigada) {
        this.sireneLigada = sireneLigada;
    }
    

}