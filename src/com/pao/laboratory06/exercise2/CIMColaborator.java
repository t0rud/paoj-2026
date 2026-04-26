package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        String line = in.nextLine().trim();
        String[] parts = line.split("\\s+");
        this.nume = parts[0];
        this.prenume = parts[1];
        this.venitLunar = Double.parseDouble(parts[2]);
        if (parts.length > 3 && parts[3].equalsIgnoreCase("DA")) {
            this.bonus = true;
        } else {
            this.bonus = false;
        }
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitLunar * 12 * 0.55;
        if (areBonus()) {
            net *= 1.1; // Adăugăm 10% dacă are bonus
        }
        return net;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }
}