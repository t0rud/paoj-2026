package com.pao.laboratory05.angajati;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajati =====");
            System.out.println("1. Adauga angajat");
            System.out.println("2. Listare dupa salariu");
            System.out.println("3. Cauta dupa departament");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            // Folosim nextLine pentru a consuma intregul rand (evitam bug-uri de scanner)
            String optiune = scanner.nextLine().trim();

            if (optiune.equals("1")) {
                System.out.print("Nume: ");
                String nume = scanner.nextLine();

                System.out.print("Departament (nume): ");
                String numeDept = scanner.nextLine();

                System.out.print("Departament (locatie): ");
                String locatieDept = scanner.nextLine();

                System.out.print("Salariu: ");
                double salariu = Double.parseDouble(scanner.nextLine());

                Departament dept = new Departament(numeDept, locatieDept);
                Angajat angajat = new Angajat(nume, dept, salariu);

                service.addAngajat(angajat);

            } else if (optiune.equals("2")) {
                service.listBySalary();

            } else if (optiune.equals("3")) {
                System.out.print("Departament: ");
                String numeDept = scanner.nextLine();
                service.findByDepartament(numeDept);

            } else if (optiune.equals("0")) {
                System.out.println("La revedere!");
                break;

            } else {
                System.out.println("Opt1iune invalida! Te rog alege o cifra din meniu.");
            }
        }

        scanner.close();
    }
}