package classes;

public abstract class Veiculo {

    ////VER SE VAI MANTER PRIORIDADE ASSIM OU SE JA ADICIONA DIRETO CONFORME O TIPO DE VEICULO

    private int id;
    private String direcao;
    private int velocidadeA;
    private boolean sireneLigada;
    private Prioridade prioridade;
    private int velocidadeC;

    public Veiculo ( 
        int id,
        String direcao,
        Prioridade prioridade)
        {
        this.id=id;
        this.direcao=direcao;
        this.prioridade=prioridade;
        }

    /////////////// SIRENE /////////////////
    public void ligarSirene() {
        sireneLigada = true;
    }

    public void desligarSirene() {
        sireneLigada = false;
    }

    public boolean isSireneLigada() {
        return sireneLigada;
    }

    ///////////////// PRIORIDADE  ///////////////////////
    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(
            Prioridade prioridade
    ) {
        this.prioridade = prioridade;
    }

    //////////// VELOCIDADE  ////////////
    public void pararVeiculo(){
        this.velocidadeA =0;
    }
    public void setVelocidadeA(){
        this.velocidadeA = this.velocidadeC;
    }
    public void setVelocidadeC(int vel){
        this.velocidadeC = vel;
    }
    public int getVelocidadeC(){
        return velocidadeC;
    }
    public int getVelocidadeA(){
        return velocidadeA;
    }

    // === ADIÇÕES PARA SUPORTE AO ID ÚNICO E PERCURSO (SEM REMOVER NADA) ===
    private Percurso percurso;
    private double kmPercorridoNaRuaAtual = 0.0;

    // Gera dinamicamente o ID Único (Ex: CA1, AM3, PE1) com base na classe do objeto
    public String getIdUnico() {
        String nomeClasse = getClass().getSimpleName();
        String prefixo;
        if (nomeClasse.equalsIgnoreCase("Ambulancia")) prefixo = "AM";
        else if (nomeClasse.equalsIgnoreCase("Bombeiro")) prefixo = "BO";
        else if (nomeClasse.equalsIgnoreCase("Policia")) prefixo = "PO";
        else if (nomeClasse.equalsIgnoreCase("Pedestre")) prefixo = "PE";
        else if (nomeClasse.equalsIgnoreCase("Onibus")) prefixo = "ON";
        else if (nomeClasse.equalsIgnoreCase("Moto")) prefixo = "MO";
        else prefixo = "CA"; // Carro
        return prefixo + id;
    }

    public Percurso getPercurso() {
        return percurso;
    }

    public void setPercurso(Percurso percurso) {
        this.percurso = percurso;
    }

    public double getKmPercorridoNaRuaAtual() {
        return kmPercorridoNaRuaAtual;
    }

    public void setKmPercorridoNaRuaAtual(double km) {
        this.kmPercorridoNaRuaAtual = km;
    }
}