package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        String line = in.nextLine().trim();
        String[] parts = line.split("\\s+");
        this.nume = parts[0];
        this.prenume = parts[1];
        this.venitLunar = Double.parseDouble(parts[2]);
        this.cheltuieliLunare = Double.parseDouble(parts[3]);
    }

    @Override
    public String tipContract() { return "PFA"; }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitLunar - cheltuieliLunare) * 12;

        double salariuMinimAnual = 48600;
        double prag6 = 6 * salariuMinimAnual;
        double prag12 = 12 * salariuMinimAnual;
        double prag24 = 24 * salariuMinimAnual;
        double prag72 = 72 * salariuMinimAnual;

        double impozit = 0.10 * venitNet;

        double cass = 0;
        if (venitNet < prag6) {
            cass = 0.10 * prag6;
        } else if (venitNet <= prag72) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * prag72;
        }

        double cas = 0;
        if (venitNet < prag12) {
            cas = 0;
        } else if (venitNet <= prag24) {
            cas = 0.25 * prag12;
        } else {
            cas = 0.25 * prag24;
        }

        return venitNet - impozit - cass - cas;
    }

    @Override
    public TipColaborator getTip() { return TipColaborator.PFA; }
}