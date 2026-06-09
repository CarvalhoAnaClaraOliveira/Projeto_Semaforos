package classes;

public abstract class Veiculo {

    private int id;
    private String tipo;
    private String direcao;
    private final int velocidade;
    private String placa;

    private boolean sireneLigada;

    private Prioridade prioridade;

    public Veiculo(
            int id,
            String tipo,
            String direcao,
            int velocidade,
            String placa,
            Prioridade prioridade
    ) {

        this.id = id;
        this.tipo = tipo;
        this.direcao = direcao;
        this.velocidade = velocidade;
        this.placa = placa;
        this.prioridade = prioridade;

        this.sireneLigada = false;

    }

    public abstract void acelerar();

    public abstract void freiar();

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

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(
            Prioridade prioridade
    ) {
        this.prioridade = prioridade;
    }

}