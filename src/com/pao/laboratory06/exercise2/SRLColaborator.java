package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends Colaborator implements PersoanaJuridica {
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
    public String tipContract() {
        return "SRL";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitLunar - cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.SRL;
    }
}