
//criar metodo abstrato para acelerar e freiar o veiculo, pq ai quando extender a classe veiculo cada um dos veiculos pode ter uma velocidade, 
//quando AMBULANCIA tiver prioridade ela pode acelerar mais rapido do que um carro comum, por exemplo
// o mesmo para policia e bombeiro, que possuem maior grau de prioridade
// porem como definir quando os 3 tem maior prioridade?
// para placa fazer um metodo que aleatorize as placa dos veiculos, para criar uma variedade de veiculos no cruzamento
// oque fazer com a string tipo?
//a parte de direção talvez tera que ser a ultima criada ou criar junto com a estrutura do JavaFX, pq ai teremos mapeado a "matriz" por onde os trem vai passar
// sincar o semaforo com o veiculo e a velocidade
// metodo para modificar a prioridade dos veiculos de emergencia caso estejam com a sirene ligada

/*
// Classe que representa o Veículo
class Veiculo {
private String modelo;
private String placa;

// Construtor do veículo que já gera e vincula a placa aleatória
public Veiculo(String modelo) {
    this.modelo = modelo;
    this.placa = GeradorPlaca.gerarPlacaMercosul();
}

public String getModelo() { return modelo; }
public String getPlaca() { return placa; }

@Override
public String toString() {
    return "Veículo: " + modelo + " | Placa Vinculada: " + placa;
}
}
*/
package classes;

public class TipoVeiculo extends Veiculo {
    private String modelo;

    public TipoVeiculo(
            int id,
            String tipo,
            String direcao,
            int velocidade,
            String placa,
            Prioridade prioridade,
            String modelo) {
        // Envia exatamente os 6 parâmetros que a sua classe pai (Veiculo) exige
        super(id, tipo, direcao, velocidade, placa, prioridade);
        this.modelo = modelo;
    }

    @Override
    public void acelerar() {
        System.out.println("O veículo " + getModelo() + " está andando a " + getVelocidade() + " km/h");
    }

    @Override
    public void freiar() {
        System.out.println("O veículo " + getModelo() + " está freiando. Velocidade: " + getVelocidade() + " km/h");
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}

// Subclasse Ambulancia corrigida (Caso queira usar a placa aleatória, use
// GeradorPlaca.gerarPlacaMercosul())
class Ambulancia extends Veiculo {
    private String tipoAtendimento;
    private boolean emAtendimento = false;
    private int capacidadePacientes;

    public Ambulancia(int id, String direcao, int velocidade, Prioridade prioridade, String tipoAtendimento,
            int capacidadePacientes) {
        // Passa os parâmetros obrigatórios e gera a placa automaticamente usando o
        // Gerador
        super(id, "Ambulância", direcao, velocidade, GeradorPlaca.gerarPlacaMercosul(), prioridade);
        this.tipoAtendimento = tipoAtendimento;
        this.capacidadePacientes = capacidadePacientes;
    }

    public void acionarSirene() {
        if (!emAtendimento) {
            this.emAtendimento = true;
            System.out.println("🚨 Giroflex LIGADO! Ambulância em rota de emergência.");
        }
    }

    public void finalizarAtendimento() {
        this.emAtendimento = false;
        System.out.println("✅ Atendimento finalizado.");
    }
}

// Classe Bombeiro corrigida
class Bombeiro extends Veiculo {
    private int capacidadeAguaLitros;

    public Bombeiro(int id, String direcao, int velocidade, Prioridade prioridade, int capacidadeAguaLitros) {
        super(id, "Bombeiro", direcao, velocidade, GeradorPlaca.gerarPlacaMercosul(), prioridade);
        this.capacidadeAguaLitros = capacidadeAguaLitros;
    }
}

// Classe Policia corrigida
class Policia extends Veiculo {
    private String batalhao;

    public Policia(int id, String direcao, int velocidade, Prioridade prioridade, String batalhao) {
        super(id, "Polícia", direcao, velocidade, GeradorPlaca.gerarPlacaMercosul(), prioridade);
        this.batalhao = batalhao;
    }
}

// Classe Carro convencional corrigida
class Carro extends Veiculo {
    private int quantidadePortas;

    public Carro(int id, String direcao, int velocidade, Prioridade prioridade, int quantidadePortas) {
        super(id, "Carro", direcao, velocidade, GeradorPlaca.gerarPlacaMercosul(), prioridade);
        this.quantidadePortas = quantidadePortas;
    }
}

// Classe Moto corrigida
class Moto extends Veiculo {
    private int cilindradas;

    public Moto(int id, String direcao, int velocidade, Prioridade prioridade, int cilindradas) {
        super(id, "Moto", direcao, velocidade, GeradorPlaca.gerarPlacaMercosul(), prioridade);
        this.cilindradas = cilindradas;
    }
}

// Classe Onibus corrigida
class Onibus extends Veiculo {
    private int capacidadePassageiros;

    public Onibus(int id, String direcao, int velocidade, Prioridade prioridade, int capacidadePassageiros) {
        super(id, "Ônibus", direcao, velocidade, GeradorPlaca.gerarPlacaMercosul(), prioridade);
        this.capacidadePassageiros = capacidadePassageiros;
    }
}

// Classe Bicicleta corrigida (Passa uma string fixa avisando que não tem placa)
class Bicicleta extends Veiculo {
    private int numeroMarchas;

    public Bicicleta(int id, String direcao, int velocidade, Prioridade prioridade, int numeroMarchas) {
        super(id, "Bicicleta", direcao, velocidade, "SEM PLACA", prioridade);
        this.numeroMarchas = numeroMarchas;
    }
}
