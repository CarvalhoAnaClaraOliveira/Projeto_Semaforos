package main;

import classes.*;

public class Main {

    public static void main(String[] args) {
        Cruzamento cruzamento = new Cruzamento(
                "Avenida Central",
                "Rua B");

        cruzamento.iniciarCruzamento();
        System.out.println();
        cruzamento.mostrarInformacoes();
        System.out.println();
        cruzamento.trocarSemaforos();

    }

}