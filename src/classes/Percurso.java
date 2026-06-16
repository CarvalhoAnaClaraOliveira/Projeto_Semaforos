package classes;

import java.util.ArrayList;

public class Percurso {
    private ArrayList<Rua> percurso;

    public Percurso(Rua a, Rua b, Rua c, Rua d, Rua e, Rua f){
        percurso.add(a);
        percurso.add(b);
        percurso.add(c);
        percurso.add(d);
        percurso.add(e);
        percurso.add(f);
    }

    public Rua getRua(int indice){
        return this.percurso.get(indice);
    }
}
