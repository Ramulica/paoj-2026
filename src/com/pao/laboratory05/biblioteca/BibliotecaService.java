package com.pao.laboratory05.biblioteca;

import com.pao.laboratory05.playlist.Song;
import com.pao.laboratory05.playlist.SongDurationComparator;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    
    
//    Constructor privat, getInstance() cu Holder intern (pattern din Lab 01)
//    Câmp: private Carte[] carti (inițializat new Carte[0])
//    void addCarte(Carte carte) — resize + adaugă + printează confirmare
//    void listSortedByRating() — clonează, Arrays.sort(copy) (natural = Comparable), afișează
//    void listSortedBy(Comparator<Carte> comparator) — clonează, Arrays.sort(copy, comparator), afișează
    private Carte[] carti;
    private BibliotecaService() {
        this.carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }
    
    public static BibliotecaService getInstance() {return Holder.INSTANCE;}
    
    public void addCarte(Carte carte) {
        Carte[] newCarte = new Carte[this.carti.length + 1];
        System.arraycopy(this.carti, 0, newCarte, 0, this.carti.length);

        newCarte[newCarte.length - 1] = carte;
        this.carti = newCarte;

        System.out.println("Carte adaugata: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        Carte[] newCarte = this.carti.clone();
        Arrays.sort(newCarte);

        System.out.println(Arrays.toString(newCarte).replace(", C", "\nC"));
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] newCarte = this.carti.clone();
        Arrays.sort(newCarte, comparator);
        System.out.println(Arrays.toString(newCarte).replace(", C", "\nC"));

    }

}
