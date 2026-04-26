package com.pao.project.biblioteca.service;

import com.pao.project.biblioteca.exception.CarteNedisponibilaException;
import com.pao.project.biblioteca.model.Carte;
import com.pao.project.biblioteca.model.ISBN;
import com.pao.project.biblioteca.model.Sectiune;

import java.util.*;

public class CarteService {
    private static CarteService instance;

    // Colectie sortata by default folosind comparable-ul din clasa Carte
    private final Set<Carte> inventarCarti;
    // Map pentru indexare
    private final Map<String, Carte> indexDupaIsbn;

    private CarteService() {
        inventarCarti = new TreeSet<>();
        indexDupaIsbn = new HashMap<>();
    }

    public static CarteService getInstance() {
        if (instance == null) {
            instance = new CarteService();
        }
        return instance;
    }

    public void adaugaCarte(Carte carte) {
        if (carte != null && carte.getIsbn() != null) {
            inventarCarti.add(carte);
            indexDupaIsbn.put(carte.getIsbn().getValoare(), carte);
        }
    }

    public void listeazaCartiDinSectiune(Sectiune sectiune) {
        System.out.println("--- Carti in sectiunea " + sectiune + " ---");
        for (Carte c : inventarCarti) {
            if (c.getSectiune() == sectiune) {
                System.out.println(c);
            }
        }
    }

    public void cautaCartiDupaAutor(String numeAutor) {
        System.out.println("--- Carti de " + numeAutor + " ---");
        for (Carte c : inventarCarti) {
            if (c.getAutor().getNumeComplet().toLowerCase().contains(numeAutor.toLowerCase())) {
                System.out.println(c);
            }
        }
    }

    public void verificaDisponibilitate(ISBN isbn) {
        Carte carte = indexDupaIsbn.get(isbn.getValoare());
        if (carte != null) {
            System.out.println("Cartea '" + carte.getTitlu() + "' disponibila: " + carte.isDisponibila());
        } else {
            System.out.println("Cartea nu exista in inventar.");
        }
    }

    public Carte preiaCartePentruImprumut(String isbnValoare) throws CarteNedisponibilaException {
        Carte carte = indexDupaIsbn.get(isbnValoare);
        if (carte == null || !carte.isDisponibila()) {
            throw new CarteNedisponibilaException("Cartea cu ISBN " + isbnValoare + " nu este disponibila.");
        }
        return carte;
    }

    public void setDisponibilitate(String isbnValoare, boolean status) {
        Carte carte = indexDupaIsbn.get(isbnValoare);
        if (carte != null) {
            carte.setDisponibila(status);
        }
    }
}