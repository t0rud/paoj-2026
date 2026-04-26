package com.pao.project.biblioteca.model;
import java.time.LocalDate;

public class Imprumut {
    private Carte carte;
    private LocalDate dataImprumut;
    private LocalDate dataReturnare; // Poate fi null daca inca nu a fost returnata

    public Imprumut(Carte carte) {
        this.carte = carte;
        this.dataImprumut = LocalDate.now();
    }

    public Carte getCarte() { return carte; }
    public LocalDate getDataImprumut() { return dataImprumut; }
    public LocalDate getDataReturnare() { return dataReturnare; }
    public void returneazaCarte() { this.dataReturnare = LocalDate.now(); }

    @Override
    public String toString() {
        return "Imprumut{carte=" + carte.getTitlu() + ", imprumutatLa=" + dataImprumut + ", returnatLa=" + (dataReturnare != null ? dataReturnare : "NERETURNAT") + '}';
    }
}