package com.pao.laboratory06.exercise2;

import java.util.Locale;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitLunar;

    public abstract double calculeazaVenitNetAnual();
    public abstract TipColaborator getTip();

    @Override
    public void afiseaza() {
        System.out.printf(Locale.US, "%s: %s %s, venit net anual: %.2f lei\n",
                getTip(), nume, prenume, calculeazaVenitNetAnual());
    }
}