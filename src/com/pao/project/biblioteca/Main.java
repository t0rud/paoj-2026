package com.pao.project.biblioteca;

import com.pao.project.biblioteca.model.*;
import com.pao.project.biblioteca.service.CarteService;
import com.pao.project.biblioteca.service.CititorService;

public class Main {
    public static void main(String[] args) {
        CarteService carteService = CarteService.getInstance();
        CititorService cititorService = CititorService.getInstance();

        Autor a1 = new Autor("Eminescu", "Mihai");
        Autor a2 = new Autor("Martin", "Robert C.");

        Carte c1 = new Carte(new ISBN("111-111"), "Poezii", a1, Sectiune.FICTIUNE);
        Carte c2 = new Carte(new ISBN("222-222"), "Clean Code", a2, Sectiune.PROGRAMARE);
        Carte c3 = new Carte(new ISBN("333-333"), "Clean Architecture", a2, Sectiune.PROGRAMARE);

        Cititor cit1 = new Cititor("123456789", "Popescu Ion");
        Cititor cit2 = new Cititor("987654321", "Ionescu Maria");


        carteService.adaugaCarte(c1);
        carteService.adaugaCarte(c2);
        carteService.adaugaCarte(c3);
        System.out.println("Actiunea 1: Carti adaugate cu succes.\n");

        System.out.println("Actiunea 2:");
        cititorService.inregistreazaCititor(cit1);
        cititorService.inregistreazaCititor(cit2);
        System.out.println();

        System.out.println("Actiunea 3:");
        cititorService.imprumutaCarte("123456789", "222-222"); // Ion imprumuta Clean Code

        cititorService.imprumutaCarte("987654321", "222-222"); // Maria incearca sa ia Clean Code
        System.out.println();

        System.out.println("Actiunea 4:");
        cititorService.returneazaCarte("123456789", "222-222"); // Ion returneaza
        System.out.println();

        System.out.println("Actiunea 5:");
        carteService.cautaCartiDupaAutor("Robert");
        System.out.println();

        System.out.println("Actiunea 6:");
        carteService.listeazaCartiDinSectiune(Sectiune.PROGRAMARE);
        System.out.println();

        System.out.println("Actiunea 7:");
        cititorService.imprumutaCarte("123456789", "111-111"); // il imprumutam din nou sa ramana nereturnat
        cititorService.afiseazaIstoric("123456789");
        System.out.println();

        System.out.println("Actiunea 8:");
        carteService.verificaDisponibilitate(new ISBN("111-111")); // Ar trebui sa fie false (imprumutata anterior)
        carteService.verificaDisponibilitate(new ISBN("333-333")); // Ar trebui sa fie true
        System.out.println();

        System.out.println("Actiunea 9:");
        cititorService.listeazaTotiCititorii();
        System.out.println();

        System.out.println("Actiunea 10:");
        cititorService.eliminaCititor("987654321"); // Eliminam pe Maria
        cititorService.listeazaTotiCititorii();
    }
}