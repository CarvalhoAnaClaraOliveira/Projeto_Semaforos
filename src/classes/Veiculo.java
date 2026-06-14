package classes;

public abstract class Veiculo {

    private int id;
    private String direcao;

    private int velocidadePadrao;
    private int velocidadeAtual;

    private boolean sireneLigada;

    private Prioridade prioridade;

    public Veiculo(
            int id,
            String direcao,
            int velocidadePadrao,
            Prioridade prioridade
    ) {

        this.id = id;
        this.direcao = direcao;

        this.velocidadePadrao = velocidadePadrao;
        this.velocidadeAtual = 0;

        this.prioridade = prioridade;

        this.sireneLigada = false;
    }

    public void mover() {
        velocidadeAtual = velocidadePadrao;
    }

    public void parar() {
        velocidadeAtual = 0;
    }

    public void ligarSirene() {
        sireneLigada = true;
    }

    public void desligarSirene() {
        sireneLigada = false;
    }

    public int getId() {
        return id;
    }

    public String getDirecao() {
        return direcao;
    }

    public int getVelocidadePadrao() {
        return velocidadePadrao;
    }

    public int getVelocidadeAtual() {
        return velocidadeAtual;
    }

    public boolean isSireneLigada() {
        return sireneLigada;
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