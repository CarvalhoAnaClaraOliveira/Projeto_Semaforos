package classes;

public class Sensor {

    private Rua rua;

    public Sensor(Rua rua) {
        this.rua = rua;
    }

    public int contarVeiculos() {
        return rua.getQuantidadeVeiculos();
    }

    public void mostrarLeitura() {

        System.out.println(
                "Rua "
                + rua.getNome()
                + " possui "
                + contarVeiculos()
                + " veículos."
        );

    }

    // === ADIÇÕES DE SUPORTE AO TEMPORIZADOR (SEM APAGAR NADA) ===
    private long tempoSinalFechadoInicio = 0;
    private boolean sinalEstaFechado = false;

    // Atualiza o estado do temporizador do sensor
    public void atualizarTimerSinal(boolean fechado) {
        if (fechado) {
            if (!sinalEstaFechado) {
                tempoSinalFechadoInicio = System.currentTimeMillis();
                sinalEstaFechado = true;
            }
        } else {
            sinalEstaFechado = false;
            tempoSinalFechadoInicio = 0;
        }
    }

    // Retorna a quantos segundos o semáforo desta rua está vermelho
    public long getTempoSinalFechadoSegundos() {
        if (!sinalEstaFechado) {
            return 0;
        }
        return (System.currentTimeMillis() - tempoSinalFechadoInicio) / 1000;
    }
}