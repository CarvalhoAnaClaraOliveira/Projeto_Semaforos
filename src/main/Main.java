package main;
import classes.Ambulancia;
import classes.Cidade;
import classes.Prioridade;
import classes.Veiculo;

public class Main {

    public static void main(String[] args) {

        Cidade c1 = new Cidade();
        /*c1.mostrarCruzamentos();
        c1.mostrarCidade();*/

        Veiculo amb1 = new Ambulancia(1, "Direita", Prioridade.EMERGENCIA, c1.getMinhaLista());
        
        try {
            System.out.println("Iniciando percurso ambulancia teste");
            for(int i=0; i<(40/amb1.getVelocidadeC());i++){
                Thread.sleep(2000);
                if(amb1.getDirecao() == "DIREITA"){
                } else if(amb1.getDirecao() == "ESQUERDA"){
                    
                }else if(amb1.getDirecao() == "CIMA"){
                    
                }else if(amb1.getDirecao() == "BAIXO"){
                }
            }
            
            System.out.println("Continuando após 2 segundos!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /*Cruzamento cruzamento = new Cruzamento(
                "Avenida Central",
                "Rua B");

        cruzamento.iniciarCruzamento();
        System.out.println();
        cruzamento.mostrarInformacoes();
        System.out.println();
        cruzamento.trocarSemaforos();

    
    List<Veiculo> listaDeVeiculos = new ArrayList<>();

        // 2. Instanciando cada um dos veículos com seus dados específicos
        // Nota: O ID é sequencial e a Prioridade vem do Enum
        Carro carroCivil = new Carro(1, "Carro", "Norte", 60, classes.GeradorPlaca.gerarPlacaMercosul(),Prioridade.BAIXA, "Sedan");
        Moto motoEntrega = new Moto(2, "Moto", "Sul", 80, classes.GeradorPlaca.gerarPlacaMercosul(),Prioridade.BAIXA, "Sport");
        Onibus onibusUrbano = new Onibus(3, "Onibus", "Leste", 40, classes.GeradorPlaca.gerarPlacaMercosul(),Prioridade.MEDIA, "Urbano");
        Bicicleta bikeParque = new Bicicleta(4, "Bicicleta", "Oeste", 15, classes.GeradorPlaca.gerarPlacaMercosul(),Prioridade.BAIXA, "Mountain Bike");
        
        // Veículos de emergência
        Ambulancia utiMovel = new Ambulancia(5,  "Centro", 90, Prioridade.ALTA, "UTI Móvel", 1);
        Bombeiro caminhaoFogo = new Bombeiro(6, "Noroeste", 70, Prioridade.ALTA, 5000);
        Policia viaturaPatrulha = new Policia(7, "Sudeste", 0, Prioridade.ALTA, "Tático Móvel");

        // 3. Adicionando todos na mesma lista
        listaDeVeiculos.add(carroCivil);
        listaDeVeiculos.add(motoEntrega);
        listaDeVeiculos.add(onibusUrbano);
        listaDeVeiculos.add(bikeParque);
        listaDeVeiculos.add(utiMovel);
        listaDeVeiculos.add(caminhaoFogo);
        listaDeVeiculos.add(viaturaPatrulha);

        // 4. Testando o comportamento geral 
        System.out.println("--- 📊 Dados dos Veículos Criados ---");
        for (Veiculo v : listaDeVeiculos) {
            // Imprime o toString() de cada veículo (com a placa gerada automaticamente)
            System.out.println(v);
            // Executa o método herdado da classe-mãe
            v.acelerar();
            System.out.println("------------------------------------");
        }

        // 5. Testando comportamentos específicos de emergência
        System.out.println("\n--- 🚨 Situação de Emergência Detectada ---");
        
        // Acionando a sirene da ambulância
        utiMovel.acionarSirene();
        
        // Fazendo o caminhão de bombeiro combater o incêndio
        caminhaoFogo.combaterIncendio();
        
        // Iniciando a patrulha da polícia
        viaturaPatrulha.iniciarPatrulha();
        
        // Simulando uma frenagem geral no trânsito
        System.out.println("\n--- 🛑 Frenagem no Semáforo ---");
        carroCivil.freiar();
        motoEntrega.freiar();*/
    }
}
