package com.pao.project.biblioteca.service;

import com.pao.project.biblioteca.exception.CarteNedisponibilaException;
import com.pao.project.biblioteca.model.Carte;
import com.pao.project.biblioteca.model.ISBN;
import com.pao.project.biblioteca.model.Sectiune;
import com.pao.project.biblioteca.repository.CarteRepository;

import java.util.List;

public class CarteService {
    private static CarteService instance;
    private final CarteRepository carteRepository;

    private CarteService() {
        this.carteRepository = CarteRepository.getInstance();
    }

    public static CarteService getInstance() {
        if (instance == null) {
            instance = new CarteService();
        }
        return instance;
    }

    public void adaugaCarte(Carte carte) {
        carteRepository.save(carte);
        System.out.println("Carte adaugata cu succes: " + carte.getTitlu());
    }

    public void listeazaCartiDinSectiune(Sectiune sectiune) {
        List<Carte> carti = carteRepository.findBySectiune(sectiune);
        System.out.println("--- Carti in sectiunea " + sectiune + " ---");
        for (Carte c : carti) {
            System.out.println(c.getTitlu() + " de " + c.getAutor().getNumeComplet() + " (ISBN: " + c.getIsbn() + ") - Disponibila: " + c.isDisponibila());
        }
    }

    public void cautaCartiDupaAutor(String numeAutor) {
        List<Carte> carti = carteRepository.findByAutor(numeAutor);
        System.out.println("--- Carti gasite pentru autorul " + numeAutor + " ---");
        for (Carte c : carti) {
            System.out.println(c.getTitlu() + " de " + c.getAutor().getNumeComplet());
        }
    }

    public void verificaDisponibilitate(ISBN isbn) {
        Carte carte = carteRepository.findById(isbn.getValoare());
        if (carte != null) {
            System.out.println("Cartea '" + carte.getTitlu() + "' disponibila: " + carte.isDisponibila());
        } else {
            System.out.println("Cartea nu exista in inventar.");
        }
    }

    public Carte preiaCartePentruImprumut(String isbnValoare) throws CarteNedisponibilaException {
        Carte carte = carteRepository.findById(isbnValoare);
        if (carte != null) {
            if (!carte.isDisponibila()) {
                throw new CarteNedisponibilaException("Cartea cu ISBN " + isbnValoare + " nu este disponibila.");
            }
            return carte;
        } else {
            throw new CarteNedisponibilaException("Cartea cu ISBN " + isbnValoare + " nu exista.");
        }
    }

    public void setDisponibilitate(String isbnValoare, boolean status) {
        carteRepository.updateDisponibilitate(isbnValoare, status);
    }
}