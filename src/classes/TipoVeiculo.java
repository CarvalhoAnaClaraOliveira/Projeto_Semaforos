package classes;

public class TipoVeiculo extends Veiculo{ //tipoVeiculo extende veiculo
    private String modelo;

    public TipoVeiculo ( 
        int id,
        String tipo,
        String direcao,
        int velocidade,
        String placa,
        int prioridade,
        String modelo
    ){
            
        super(id, tipo, direcao, velocidade, placa, prioridade);
        this.modelo=modelo; //criar diferentes modelos de carros
            //exemplo: sedan, hatchback, SUV, etc.
    }
    @Override
    public void acelerar(){
        System.out.println("O veículo " + getModelo() + " está andando." + getVelocidade() + " km/h");
    }

    @Override
    public void freiar(){
        System.out.println("O veículo " + getModelo() + " está freiando." + getVelocidade() + " km/h");
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }


    //criar metodo abstrato para acelerar e freiar o veiculo, pq ai quando extender a classe veiculo cada um dos veiculos pode ter uma velocidade, 
    //quando AMBULANCIA tiver prioridade ela pode acelerar mais rapido do que um carro comum, por exemplo
    // o mesmo para policia e bombeiro, que possuem maior grau de prioridade
    // porem como definir quando os 3 tem maior prioridade?
    // para placa fazer um metodo que aleatorize as placa dos veiculos, para criar uma variedade de veiculos no cruzamento
    // oque fazer com a string tipo?
    //a parte de direção talvez tera que ser a ultima criada ou criar junto com a estrutura do JavaFX, pq ai teremos mapeado a "matriz" por onde os trem vai passar
    // sincar o semaforo com o veiculo e a velocidade


}