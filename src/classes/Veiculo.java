package classes;

public abstract class Veiculo{
    private int id;
    private String tipo;
    private String direcao;
    private final int velocidade;
    private String placa;
    private Prioridade prioridade;

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
public abstract void acelerar(); //metodo abstrato para acelerar o veiculo, 
public abstract void freiar(); //metodo abstrato para freiar o veiculo

public int getVelocidade(){
    return velocidade;
}

public Prioridade getPrioridade() {
    return prioridade;
}
public void setPrioridade(Prioridade prioridade) {this.prioridade = prioridade;}
}