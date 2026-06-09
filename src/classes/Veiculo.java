package classes;

public abstract class Veiculo {

    private int id;
    private String tipo;
    private String direcao;
    private final int velocidade;
    private String placa;

    private boolean sireneLigada;

    private Prioridade prioridade;

<<<<<<< HEAD
    public Veiculo(
            int id,
            String tipo,
            String direcao,
            int velocidade,
            String placa,
            Prioridade prioridade
    ) {
=======
    public Veiculo ( 
        int id,
        String tipo,
        String direcao,
        int velocidade,
        String placa,
        Prioridade prioridade){
            
        this.id=id;
        this.tipo=tipo;
        this.direcao=direcao;
        this.velocidade=velocidade; 
        this.placa=placa;
        this.prioridade=prioridade;
        }
public void acelerar(){
        System.out.println(" O veículo " + tipo + " está acelerando a " + velocidade + " km/h.");
} //metodo para acelerar o veiculo, 
public void freiar(){ //metodo para freiar o veiculo
        System.out.println(" O veículo " + tipo + " está freiando. Velocidade: " + velocidade + " km/h");
}
>>>>>>> cd3c722bc543da0edf886e085279b865732cff77

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