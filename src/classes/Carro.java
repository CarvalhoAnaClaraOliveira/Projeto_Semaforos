package classes;

public class Carro extends TipoVeiculo {
    public Carro(
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
