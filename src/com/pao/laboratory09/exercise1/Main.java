package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs();

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) return;

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie tranzactie = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            tranzactie.setNote("procesat");
            tranzactii.add(tranzactie);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(tranzactii);
        }

        List<Tranzactie> tranzactiiDeserializate;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            tranzactiiDeserializate = (List<Tranzactie>) ois.readObject();
        }

        while (scanner.hasNext()) {
            String command = scanner.next();

            switch (command) {
                case "LIST":
                    for (Tranzactie t : tranzactiiDeserializate) {
                        System.out.println(t);
                    }
                    break;

                case "FILTER":
                    String prefix = scanner.next();
                    boolean foundFilter = false;
                    for (Tranzactie t : tranzactiiDeserializate) {
                        if (t.getData().startsWith(prefix)) {
                            System.out.println(t);
                            foundFilter = true;
                        }
                    }
                    if (!foundFilter) {
                        System.out.println("Niciun rezultat.");
                    }
                    break;

                case "NOTE":
                    int searchId = scanner.nextInt();
                    boolean foundNote = false;
                    for (Tranzactie t : tranzactiiDeserializate) {
                        if (t.getId() == searchId) {
                            System.out.println("NOTE[" + searchId + "]: " + t.getNote());
                            foundNote = true;
                            break;
                        }
                    }
                    if (!foundNote) {
                        System.out.println("NOTE[" + searchId + "]: not found");
                    }
                    break;
            }
        }
        scanner.close();
    }
}