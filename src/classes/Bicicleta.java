package classes;

public class Bicicleta extends TipoVeiculo {
    public Bicicleta(
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
