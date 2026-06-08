package main;
import classes.Cruzamento;

public class Main {

    public static void main(String[] args) {
        Cruzamento cruzamento = new Cruzamento(
                1,
                "Avenida Central",
                "Rua B"
        );

        cruzamento.iniciarCruzamento();
        System.out.println();
        cruzamento.mostrarInformacoes();
        System.out.println();
        cruzamento.trocarSemaforos();

    }

}
