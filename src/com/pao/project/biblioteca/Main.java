package com.pao.project.biblioteca;

import com.pao.project.biblioteca.model.Autor;
import com.pao.project.biblioteca.model.Carte;
import com.pao.project.biblioteca.model.Cititor;
import com.pao.project.biblioteca.model.ISBN;
import com.pao.project.biblioteca.model.Sectiune;
import com.pao.project.biblioteca.service.AuditService;
import com.pao.project.biblioteca.service.CarteService;
import com.pao.project.biblioteca.service.CititorService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CarteService carteService = CarteService.getInstance();
        CititorService cititorService = CititorService.getInstance();
        AuditService auditService = AuditService.getInstance();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- MENIU BIBLIOTECA ---");
            System.out.println("1. Adauga o carte noua");
            System.out.println("2. Inregistreaza un cititor nou");
            System.out.println("3. Imprumuta o carte unui cititor");
            System.out.println("4. Returneaza o carte");
            System.out.println("5. Cauta carti dupa autor");
            System.out.println("6. Listeaza toate cartile dintr-o sectiune");
            System.out.println("7. Afiseaza istoricul imprumuturilor unui cititor");
            System.out.println("8. Verifica disponibilitatea unei carti");
            System.out.println("9. Listeaza toti cititorii inregistrati");
            System.out.println("10. Elimina un cititor din sistem");
            System.out.println("0. Iesire");
            System.out.print("Alege o optiune: ");

            int optiune = -1;
            if (scanner.hasNextInt()) {
                optiune = scanner.nextInt();
            }
            scanner.nextLine();

            switch (optiune) {
                case 1:
                    System.out.print("Introdu ISBN: ");
                    String isbnStr = scanner.nextLine();
                    System.out.print("Introdu titlu: ");
                    String titlu = scanner.nextLine();
                    System.out.print("Introdu nume autor: ");
                    String numeAutor = scanner.nextLine();
                    System.out.print("Introdu prenume autor: ");
                    String prenumeAutor = scanner.nextLine();
                    System.out.print("Introdu sectiune (FICTIUNE, STIINTA, ISTORIE, PROGRAMARE): ");
                    String sectiuneStr = scanner.nextLine().toUpperCase();

                    try {
                        Sectiune sectiune = Sectiune.valueOf(sectiuneStr);
                        Autor autor = new Autor(numeAutor, prenumeAutor);
                        Carte carte = new Carte(new ISBN(isbnStr), titlu, autor, sectiune);
                        carteService.adaugaCarte(carte);
                        auditService.logAction("AdaugaCarte");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Sectiune invalida!");
                    }
                    break;
                case 2:
                    System.out.print("Introdu CNP: ");
                    String cnp = scanner.nextLine();
                    System.out.print("Introdu Nume: ");
                    String nume = scanner.nextLine();
                    cititorService.inregistreazaCititor(new Cititor(cnp, nume));
                    auditService.logAction("InregistrareCititor");
                    break;
                case 3:
                    System.out.print("Introdu CNP cititor: ");
                    String cnpImprumut = scanner.nextLine();
                    System.out.print("Introdu ISBN carte: ");
                    String isbnImprumut = scanner.nextLine();
                    cititorService.imprumutaCarte(cnpImprumut, isbnImprumut);
                    auditService.logAction("ImprumutaCarte");
                    break;
                case 4:
                    System.out.print("Introdu CNP cititor: ");
                    String cnpReturnare = scanner.nextLine();
                    System.out.print("Introdu ISBN carte: ");
                    String isbnReturnare = scanner.nextLine();
                    cititorService.returneazaCarte(cnpReturnare, isbnReturnare);
                    auditService.logAction("ReturneazaCarte");
                    break;
                case 5:
                    System.out.print("Introdu nume autor: ");
                    String numeAutorCautat = scanner.nextLine();
                    carteService.cautaCartiDupaAutor(numeAutorCautat);
                    auditService.logAction("CautaCartiDupaAutor");
                    break;
                case 6:
                    System.out.print("Introdu sectiunea (FICTIUNE, STIINTA, ISTORIE, PROGRAMARE): ");
                    String sect = scanner.nextLine().toUpperCase();
                    try {
                        carteService.listeazaCartiDinSectiune(Sectiune.valueOf(sect));
                        auditService.logAction("ListeazaCartiSectiune");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Sectiune invalida!");
                    }
                    break;
                case 7:
                    System.out.print("Introdu CNP cititor: ");
                    String cnpIstoric = scanner.nextLine();
                    cititorService.afiseazaIstoric(cnpIstoric);
                    auditService.logAction("AfiseazaIstoricCititor");
                    break;
                case 8:
                    System.out.print("Introdu ISBN: ");
                    String isbnVerif = scanner.nextLine();
                    carteService.verificaDisponibilitate(new ISBN(isbnVerif));
                    auditService.logAction("VerificaDisponibilitateCarte");
                    break;
                case 9:
                    cititorService.listeazaTotiCititorii();
                    auditService.logAction("ListeazaCititori");
                    break;
                case 10:
                    System.out.print("Introdu CNP cititor de eliminat: ");
                    String cnpEliminare = scanner.nextLine();
                    cititorService.eliminaCititor(cnpEliminare);
                    auditService.logAction("EliminaCititor");
                    break;
                case 0:
                    running = false;
                    System.out.println("Iesire din aplicatie. La revedere!");
                    auditService.logAction("IesireAplicatie");
                    break;
                default:
                    System.out.println("Optiune invalida! Te rog sa incerci din nou.");
            }
        }
        scanner.close();
    }
}