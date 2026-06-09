package classes;

public class Moto  extends TipoVeiculo {
    public Moto(
        int id,
        String tipo, 
        String direcao,
        int velocidade,
        String placa,
        Prioridade prioridade,
        String modelo
    ) {
        super(id, tipo, direcao, velocidade, placa, prioridade, modelo);
    }
    
}
