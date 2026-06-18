package classes;

import java.util.ArrayList;

public class Cidade {

        private ArrayList<Rua> ruas;
        private ArrayList<Cruzamento> cruzamentos;

        public Cidade() {

                ruas = new ArrayList<>();
                cruzamentos = new ArrayList<>();

                criarRuas();
                criarCruzamentos();

        }

        private void criarRuas() {

                ruas.add(
                                new Rua(
                                                "H1",
                                                "HORIZONTAL",
                                                "DIREITA",
                                                120));

                ruas.add(
                                new Rua(
                                                "H2",
                                                "HORIZONTAL",
                                                "ESQUERDA",
                                                120));

                ruas.add(
                                new Rua(
                                                "H3",
                                                "HORIZONTAL",
                                                "DIREITA",
                                                120));

                ruas.add(
                                new Rua(
                                                "H4",
                                                "HORIZONTAL",
                                                "ESQUERDA",
                                                120));

                ruas.add(
                                new Rua(
                                                "V1",
                                                "VERTICAL",
                                                "BAIXO",
                                                100));

                ruas.add(
                                new Rua(
                                                "V2",
                                                "VERTICAL",
                                                "CIMA",
                                                100));

                ruas.add(
                                new Rua(
                                                "V3",
                                                "VERTICAL",
                                                "BAIXO",
                                                100));

                ruas.add(
                                new Rua(
                                                "V4",
                                                "VERTICAL",
                                                "CIMA",
                                                100));

        }

        private void criarCruzamentos() {

                int numero = 1;

                for (int h = 0; h < 4; h++) {

                        for (int v = 4; v < 8; v++) {

                                cruzamentos.add(

                                                new Cruzamento(
                                                                numero,
                                                                ruas.get(h),
                                                                ruas.get(v))

                );
                                );

                                numero++;

                        }

                }

        }

        public void mostrarCidade() {

        System.out.println(
                "Quantidade de ruas: "
                + ruas.size()
        );

        System.out.println(
                "Quantidade de cruzamentos: "
                + cruzamentos.size()
        );

        }

        public ArrayList<Rua> getRuas() {
                return this.ruas;
        }

        public ArrayList<Cruzamento> getCruzamentos() {
                return this.cruzamentos;
        }
}