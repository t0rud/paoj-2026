package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();

        // Partea A: 
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String nume = parts[0].trim();
                    int varsta = Integer.parseInt(parts[1].trim());
                    String oras = parts[2].trim();
                    String strada = parts[3].trim();

                    Adresa adresa = new Adresa(oras, strada);
                    Student student = new Student(nume, varsta, adresa);
                    studenti.add(student);
                }
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String comandaLine = scanner.nextLine().trim();
        String[] comandaParts = comandaLine.split(" ", 2);
        String tipComanda = comandaParts[0].toUpperCase();

        if (tipComanda.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }
        } else if (tipComanda.equals("SHALLOW") || tipComanda.equals("DEEP")) {
            if (comandaParts.length < 2) {
                System.out.println("Eroare: Lipseste numele studentului.");
                return;
            }

            String numeCautat = comandaParts[1];
            Student studentGasit = null;

            for (Student s : studenti) {
                if (s.getNume().equals(numeCautat)) {
                    studentGasit = s;
                    break;
                }
            }

            if (studentGasit != null) {
                if (tipComanda.equals("SHALLOW")) {
                    // Partea B:
                    Student clona = studentGasit.shallowClone();
                    clona.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + studentGasit);
                    System.out.println("Clona: " + clona);
                } else if (tipComanda.equals("DEEP")) {
                    // Partea C: Deep clone
                    Student clona = studentGasit.deepClone();
                    clona.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + studentGasit);
                    System.out.println("Clona: " + clona);
                }
            } else {
                System.out.println("Studentul cu numele '" + numeCautat + "' nu a fost gasit.");
            }
        }

        scanner.close();
    }
}