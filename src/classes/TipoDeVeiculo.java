package classes;
import java.util.ArrayList; 
import java.util.Comparator;

public class TipoVeiculo{
    private String categoria;
    private Prioridade prioridade;

    public TipoDeVeiculo(String categoria) {
        this.categoria = categoria;
        this.prioridade = prioridade;
    }
    public Prioridade getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
    public String get Categoria() {
        return categoria;
    }
    public enum Prioridade{ 
        CARRO(4), MOTO(4), AMBULANCIA(1), ONIBUS(4), PEDESTRE(3), BOMBEIRO(2), VIATURA(2), BICICLETA(3);
        private final int nivel;
        Prioridade(int nivel) {
            this.nivel = nivel;
        }
        public int getNivel() {
            return this.nivel;
        }
    }

    public ArrayList<TipoVeiculo> veiculos = new ArrayList<>();
    veiculos.add(new TipoVeiculo("Carro", Prioridade.CARRO));
    veiculos.add(new TipoVeiculo("Moto", Prioridade.MOTO));
    veiculos.add(new TipoVeiculo("ambulância", Prioridade.AMBULANCIA));
    veiculos.add(new TipoVeiculo("Ônibus", Prioridade.ONIBUS));
    veiculos.add(new TipoVeiculo("Pedestre", Prioridade.PEDESTRE));
    veiculos.add(new TipoVeiculo("Bombeiro", Prioridade.BOMBEIRO));
    veiculos.add(new TipoVeiculo("Viatura", Prioridade.VIATURA));
    veiculos.add(new TipoVeiculo("Bicicleta", Prioridade.BICICLETA));

veiculos.sort(Comparator.comparingInt((TipoVeiculo t) -> t.getPrioridade().getNivel()).thenComparing(Tipo::getCategoria).reversed());

System.out.println("Tipos de veículos ordenados por prioridade:");
for (TipoDeVeiculo t : veiculos) {
    System.out.println("[" + t.getPrioridade() + "] " + t.getCategoria());
    

}
}