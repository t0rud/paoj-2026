package com.pao.project.biblioteca.service;

import com.pao.project.biblioteca.exception.CarteNedisponibilaException;
import com.pao.project.biblioteca.model.Cititor;
import com.pao.project.biblioteca.repository.CititorRepository;

import java.util.List;

public class CititorService {
    private static CititorService instance;
    private final CititorRepository cititorRepository;

    private CititorService() {
        this.cititorRepository = CititorRepository.getInstance();
    }

    public static CititorService getInstance() {
        if (instance == null) {
            instance = new CititorService();
        }
        return instance;
    }

    public void inregistreazaCititor(Cititor cititor) {
        cititorRepository.save(cititor);
        System.out.println("Cititor inregistrat in baza de date: " + cititor.getNume());
    }

    public void eliminaCititor(String cnp) {
        cititorRepository.delete(cnp);
        System.out.println("Actiune finalizata pentru CNP: " + cnp);
    }

    public void listeazaTotiCititorii() {
        List<Cititor> cititori = cititorRepository.findAll();
        System.out.println("--- Lista Cititori ---");
        for (Cititor c : cititori) {
            System.out.println("Cititor{nume='" + c.getNume() + "', cnp='" + c.getCnp() + "'}");
        }
    }

    public void imprumutaCarte(String cnp, String isbnValoare) {
        try {
            CarteService.getInstance().preiaCartePentruImprumut(isbnValoare);
            cititorRepository.adaugaImprumut(cnp, isbnValoare);
            CarteService.getInstance().setDisponibilitate(isbnValoare, false);
            System.out.println("Cartea cu ISBN " + isbnValoare + " a fost imprumutata cititorului cu CNP " + cnp);
        } catch (CarteNedisponibilaException e) {
            System.out.println("Eroare la imprumut: " + e.getMessage());
        }
    }

    public void returneazaCarte(String cnp, String isbnValoare) {
        boolean succes = cititorRepository.returneazaImprumut(cnp, isbnValoare);
        if (succes) {
            CarteService.getInstance().setDisponibilitate(isbnValoare, true);
            System.out.println("Cartea a fost returnata cu succes.");
        } else {
            System.out.println("Nu exista un imprumut activ pentru acest cititor si aceasta carte.");
        }
    }

    public void afiseazaIstoric(String cnp) {
        List<String> istoric = cititorRepository.getIstoric(cnp);
        System.out.println("--- Istoric Imprumuturi pt CNP " + cnp + " ---");
        if (istoric.isEmpty()) {
            System.out.println("Acest cititor nu are istoric de imprumuturi.");
        } else {
            for (String rand : istoric) {
                System.out.println(rand);
            }
        }
    }
}