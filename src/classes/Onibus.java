package classes;

public class Onibus extends TipoVeiculo {
    public Onibus(
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
