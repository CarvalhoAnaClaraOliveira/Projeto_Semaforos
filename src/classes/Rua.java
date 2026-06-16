package classes;

import java.util.ArrayList;

public class Rua {

    private String nome;
    private String orientacao;
    private String sentido;
    private ArrayList<Veiculo> veiculos;
    private ArrayList<Cruzamento> cruzamentos;
    private Sensor sensor;

    public Rua(
            String nome,
            String orientacao,
            String sentido) {

        this.nome = nome;
        this.orientacao = orientacao;
        this.sentido = sentido;

        this.veiculos = new ArrayList<>();
        this.cruzamentos = new ArrayList<>();

        // Modificado de null para que cada rua possua de fato o seu sensor instanciado
        this.sensor = new Sensor(this);

    }

    public void adicionarVeiculo(Veiculo veiculo) {
        veiculos.add(veiculo);
    }

    public void removerVeiculo(Veiculo veiculo) {
        veiculos.remove(veiculo);
    }

    public void adicionarCruzamento(Cruzamento cruzamento) {
        cruzamentos.add(cruzamento);
    }

    public int getQuantidadeVeiculos() {
        return veiculos.size();
    }

    public String getNome() {
        return nome;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public String getSentido() {
        return sentido;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public ArrayList<Veiculo> getVeiculos() {
        return veiculos;
    }

    public ArrayList<Cruzamento> getCruzamentos() {
        return cruzamentos;
    }

    public void mostrarInformacoes() {

        System.out.println("Rua: " + nome);

        System.out.println(
                "Orientação: "
                        + orientacao);

        System.out.println(
                "Sentido: "
                        + sentido);

        System.out.println(
                "Quantidade de veículos: "
                        + getQuantidadeVeiculos());

        System.out.println(
                "Quantidade de cruzamentos: "
                        + cruzamentos.size());

    }

}