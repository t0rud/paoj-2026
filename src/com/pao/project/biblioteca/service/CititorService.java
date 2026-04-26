package com.pao.project.biblioteca.service;

import com.pao.project.biblioteca.exception.CarteNedisponibilaException;
import com.pao.project.biblioteca.exception.CititorNegasitException;
import com.pao.project.biblioteca.model.Carte;
import com.pao.project.biblioteca.model.Cititor;
import com.pao.project.biblioteca.model.Imprumut;

import java.util.*;

public class CititorService {
    private static CititorService instance;
    private final Map<String, Cititor> cititoriMap; // Indexare map

    private CititorService() {
        cititoriMap = new HashMap<>();
    }

    public static CititorService getInstance() {
        if (instance == null) {
            instance = new CititorService();
        }
        return instance;
    }

    public void inregistreazaCititor(Cititor cititor) {
        if (cititor != null && cititor.getCnp() != null) {
            cititoriMap.put(cititor.getCnp(), cititor);
            System.out.println("Cititor inregistrat: " + cititor.getNume());
        }
    }

    public void eliminaCititor(String cnp) {
        Cititor eliminat = cititoriMap.remove(cnp);
        if (eliminat != null) {
            System.out.println("Cititor eliminat cu succes: " + eliminat.getNume());
        }
    }

    public void listeazaTotiCititorii() {
        System.out.println("--- Lista Cititori ---");
        // Sortam in memorie dupa nume (folosind colectie)
        List<Cititor> lista = new ArrayList<>(cititoriMap.values());
        lista.sort(Comparator.comparing(Cititor::getNume));
        for (Cititor c : lista) {
            System.out.println(c);
        }
    }

    public void imprumutaCarte(String cnp, String isbnValoare) {
        try {
            Cititor cititor = cititoriMap.get(cnp);
            if (cititor == null) throw new CititorNegasitException("Nu exista cititor cu CNP " + cnp);

            // Validare din celalalt serviciu
            Carte carte = CarteService.getInstance().preiaCartePentruImprumut(isbnValoare);

            // Logic
            Imprumut imprumut = new Imprumut(carte);
            cititor.adaugaImprumut(imprumut);
            CarteService.getInstance().setDisponibilitate(isbnValoare, false);

            System.out.println(cititor.getNume() + " a imprumutat cartea " + carte.getTitlu());

        } catch (CititorNegasitException | CarteNedisponibilaException e) {
            System.out.println("Eroare la imprumut: " + e.getMessage());
        }
    }

    public void returneazaCarte(String cnp, String isbnValoare) {
        Cititor cititor = cititoriMap.get(cnp);
        if (cititor != null) {
            for (Imprumut imp : cititor.getIstoricImprumuturi()) {
                if (imp.getCarte().getIsbn().getValoare().equals(isbnValoare) && imp.getDataReturnare() == null) {
                    imp.returneazaCarte();
                    CarteService.getInstance().setDisponibilitate(isbnValoare, true);
                    System.out.println(cititor.getNume() + " a returnat cartea.");
                    return;
                }
            }
        }
    }

    public void afiseazaIstoric(String cnp) {
        Cititor cititor = cititoriMap.get(cnp);
        if (cititor != null) {
            System.out.println("--- Istoric Imprumuturi pt " + cititor.getNume() + " ---");
            cititor.getIstoricImprumuturi().forEach(System.out::println);
        }
    }
}