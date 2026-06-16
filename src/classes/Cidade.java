package classes;

import java.util.ArrayList;

public class Cidade {

        private ArrayList<Rua> ruas;
        private ArrayList<Cruzamento> cruzamentos;

        private Percurso p1 = new Percurso( ruas.get(4),ruas.get(4),ruas.get(2), ruas.get(2), ruas.get(2), ruas.get(7));

        public Percurso getMinhaLista() {
        return p1;}

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
                        "DIREITA"
                )
        );

        ruas.add(
                new Rua(
                        "H2",
                        "ESQUERDA"
                )
        );

        ruas.add(
                new Rua(
                        "H3",
                        "DIREITA"
                )
        );

        ruas.add(
                new Rua(
                        "H4",
                        "ESQUERDA"
                )
        );

        ruas.add(
                new Rua(
                        "V1",
                        "BAIXO"
                )
        );

        ruas.add(
                new Rua(
                        "V2",
                        "CIMA"
                )
        );

        ruas.add(
                new Rua(
                        "V3",
                        "BAIXO"
                )
        );

        ruas.add(
                new Rua(
                        "V4",
                        "CIMA"
                )
        );

        }

    private void criarCruzamentos() {
        int numero = 1;
        int[] posicao ;
        for (int h = 0; h < 4; h++) {
            for (int v = 4; v < 8; v++) {
                posicao = new int[]{h*40,(v-4)*40};
                cruzamentos.add(new Cruzamento(numero, ruas.get(h), ruas.get(v),posicao));
                numero++;
                
            }

        }

    }

    public void mostrarCruzamentos(){
        for(int i = 0; i< cruzamentos.size(); i++){
                System.out.println(cruzamentos.get(i).getPosicao()[0] + ", " + cruzamentos.get(i).getPosicao()[1]);
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

}
