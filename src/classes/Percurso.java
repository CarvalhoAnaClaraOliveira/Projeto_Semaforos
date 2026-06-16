package classes;

import java.util.ArrayList;

public class Percurso {
    private ArrayList<Cruzamento> cruzamentos = new ArrayList<>();
    private int indiceAtual = 0;
    private int idTrajeto;

    public Percurso(int idTrajeto) {
        this.idTrajeto = idTrajeto;
    }

    public void adicionarCruzamento(Cruzamento c) {
        cruzamentos.add(c);
    }

    public Cruzamento getCruzamentoAtual() {
        if (indiceAtual < cruzamentos.size()) {
            return cruzamentos.get(indiceAtual);
        }
        return null;
    }

    public Cruzamento getProximoCruzamento() {
        if (indiceAtual + 1 < cruzamentos.size()) {
            return cruzamentos.get(indiceAtual + 1);
        }
        return null;
    }

    public void avancar() {
        indiceAtual++;
    }

    public boolean completou() {
        return indiceAtual >= cruzamentos.size();
    }

    public void resetar() {
        indiceAtual = 0;
    }

    public int getIdTrajeto() {
        return idTrajeto;
    }

    // Fábrica estática para construir os 10 trajetos pré-programados de 0 a 9
    public static Percurso obterTrajetoPreDefinido(int idTrajeto, Cidade cidade) {
        Percurso p = new Percurso(idTrajeto);
        ArrayList<Cruzamento> lista = cidade.getCruzamentos();
        
        switch (idTrajeto) {
            case 0: // Cruzamentos: 1 -> 2 -> 3 -> 4 -> 8 -> 12 -> 16 -> 15 -> 14 -> 13
                p.adicionarCruzamento(lista.get(0)); p.adicionarCruzamento(lista.get(1));
                p.adicionarCruzamento(lista.get(2)); p.adicionarCruzamento(lista.get(3));
                p.adicionarCruzamento(lista.get(7)); p.adicionarCruzamento(lista.get(11));
                p.adicionarCruzamento(lista.get(15)); p.adicionarCruzamento(lista.get(14));
                p.adicionarCruzamento(lista.get(13)); p.adicionarCruzamento(lista.get(12));
                break;
            case 1: // Cruzamentos: 5 -> 6 -> 7 -> 8 -> 12 -> 11 -> 10 -> 9 -> 13 -> 14
                p.adicionarCruzamento(lista.get(4)); p.adicionarCruzamento(lista.get(5));
                p.adicionarCruzamento(lista.get(6)); p.adicionarCruzamento(lista.get(7));
                p.adicionarCruzamento(lista.get(11)); p.adicionarCruzamento(lista.get(10));
                p.adicionarCruzamento(lista.get(9)); p.adicionarCruzamento(lista.get(8));
                p.adicionarCruzamento(lista.get(12)); p.adicionarCruzamento(lista.get(13));
                break;
            case 2: // Cruzamentos: 2 -> 6 -> 10 -> 14 -> 13 -> 9 -> 5 -> 1 -> 2 -> 3 -> 4
                p.adicionarCruzamento(lista.get(1)); p.adicionarCruzamento(lista.get(5));
                p.adicionarCruzamento(lista.get(9)); p.adicionarCruzamento(lista.get(13));
                p.adicionarCruzamento(lista.get(12)); p.adicionarCruzamento(lista.get(8));
                p.adicionarCruzamento(lista.get(4)); p.adicionarCruzamento(lista.get(0));
                p.adicionarCruzamento(lista.get(1)); p.adicionarCruzamento(lista.get(2));
                p.adicionarCruzamento(lista.get(3));
                break;
            case 3: // Cruzamentos: 3 -> 7 -> 11 -> 15 -> 16 -> 12 -> 8 -> 4 -> 3 -> 2 -> 1
                p.adicionarCruzamento(lista.get(2)); p.adicionarCruzamento(lista.get(6));
                p.adicionarCruzamento(lista.get(10)); p.adicionarCruzamento(lista.get(14));
                p.adicionarCruzamento(lista.get(15)); p.adicionarCruzamento(lista.get(11));
                p.adicionarCruzamento(lista.get(7)); p.adicionarCruzamento(lista.get(3));
                p.adicionarCruzamento(lista.get(2)); p.adicionarCruzamento(lista.get(1));
                p.adicionarCruzamento(lista.get(0));
                break;
            case 4: // Cruzamentos: 1 -> 5 -> 9 -> 13 -> 14 -> 10 -> 6 -> 2 -> 3 -> 7 -> 11
                p.adicionarCruzamento(lista.get(0)); p.adicionarCruzamento(lista.get(4));
                p.adicionarCruzamento(lista.get(8)); p.adicionarCruzamento(lista.get(12));
                p.adicionarCruzamento(lista.get(13)); p.adicionarCruzamento(lista.get(9));
                p.adicionarCruzamento(lista.get(5)); p.adicionarCruzamento(lista.get(1));
                p.adicionarCruzamento(lista.get(2)); p.adicionarCruzamento(lista.get(6));
                p.adicionarCruzamento(lista.get(10));
                break;
            case 5: // Cruzamentos: 4 -> 8 -> 12 -> 16 -> 15 -> 11 -> 7 -> 3 -> 2 -> 6 -> 10
                p.adicionarCruzamento(lista.get(3)); p.adicionarCruzamento(lista.get(7));
                p.adicionarCruzamento(lista.get(11)); p.adicionarCruzamento(lista.get(15));
                p.adicionarCruzamento(lista.get(14)); p.adicionarCruzamento(lista.get(10));
                p.adicionarCruzamento(lista.get(6)); p.adicionarCruzamento(lista.get(2));
                p.adicionarCruzamento(lista.get(1)); p.adicionarCruzamento(lista.get(5));
                p.adicionarCruzamento(lista.get(9));
                break;
            case 6: // Cruzamentos: 1 -> 2 -> 6 -> 5 -> 9 -> 10 -> 14 -> 13 -> 14 -> 15 -> 16
                p.adicionarCruzamento(lista.get(0)); p.adicionarCruzamento(lista.get(1));
                p.adicionarCruzamento(lista.get(5)); p.adicionarCruzamento(lista.get(4));
                p.adicionarCruzamento(lista.get(8)); p.adicionarCruzamento(lista.get(9));
                p.adicionarCruzamento(lista.get(13)); p.adicionarCruzamento(lista.get(12));
                p.adicionarCruzamento(lista.get(13)); p.adicionarCruzamento(lista.get(14));
                p.adicionarCruzamento(lista.get(15));
                break;
            case 7: // Cruzamentos: 13 -> 9 -> 10 -> 11 -> 12 -> 16 -> 15 -> 14 -> 10 -> 6 -> 5
                p.adicionarCruzamento(lista.get(12)); p.adicionarCruzamento(lista.get(8));
                p.adicionarCruzamento(lista.get(9)); p.adicionarCruzamento(lista.get(10));
                p.adicionarCruzamento(lista.get(11)); p.adicionarCruzamento(lista.get(15));
                p.adicionarCruzamento(lista.get(14)); p.adicionarCruzamento(lista.get(13));
                p.adicionarCruzamento(lista.get(9)); p.adicionarCruzamento(lista.get(5));
                p.adicionarCruzamento(lista.get(4));
                break;
            case 8: // Cruzamentos: 4 -> 3 -> 7 -> 8 -> 12 -> 11 -> 15 -> 16 -> 12 -> 8 -> 4
                p.adicionarCruzamento(lista.get(3)); p.adicionarCruzamento(lista.get(2));
                p.adicionarCruzamento(lista.get(6)); p.adicionarCruzamento(lista.get(7));
                p.adicionarCruzamento(lista.get(11)); p.adicionarCruzamento(lista.get(10));
                p.adicionarCruzamento(lista.get(14)); p.adicionarCruzamento(lista.get(15));
                p.adicionarCruzamento(lista.get(11)); p.adicionarCruzamento(lista.get(7));
                p.adicionarCruzamento(lista.get(3));
                break;
            case 9:
            default: // Cruzamentos: 1 -> 2 -> 3 -> 4 -> 8 -> 7 -> 11 -> 12 -> 16 -> 15 -> 11
                p.adicionarCruzamento(lista.get(0)); p.adicionarCruzamento(lista.get(1));
                p.adicionarCruzamento(lista.get(2)); p.adicionarCruzamento(lista.get(3));
                p.adicionarCruzamento(lista.get(7)); p.adicionarCruzamento(lista.get(6));
                p.adicionarCruzamento(lista.get(10)); p.adicionarCruzamento(lista.get(11));
                p.adicionarCruzamento(lista.get(15)); p.adicionarCruzamento(lista.get(14));
                p.adicionarCruzamento(lista.get(10));
                break;
        }
        return p;
    }
}