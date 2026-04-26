package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    // Array initial gol
    private Carte[] carti = new Carte[0];

    // Constructor privat ca sa nu poata fi apelat cu 'new' din afara
    private BibliotecaService() {}

    // Pattern-ul de Singleton cu Holder
    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] tmp = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, tmp, 0, carti.length);
        tmp[tmp.length - 1] = carte;
        carti = tmp;
        System.out.println("Carte adaugata: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        Carte[] copy = carti.clone();
        Arrays.sort(copy); // Foloseste compareTo din clasa Carte

        for (int i = 0; i < copy.length; i++) {
            // i+1 pentru a afisa 1. , 2. , etc.
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator); // Foloseste comparatorul primit ca parametru

        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }
}