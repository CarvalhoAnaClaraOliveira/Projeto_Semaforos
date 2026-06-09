package classes;

public class Policia extends TipoVeiculo {
    public Policia(
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
