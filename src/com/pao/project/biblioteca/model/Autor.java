package com.pao.project.biblioteca.model;

public class Autor {
    private String nume;
    private String prenume;

    public Autor(String nume, String prenume) {
        this.nume = nume;
        this.prenume = prenume;
    }

    public String getNumeComplet() {
        return prenume + " " + nume;
    }

    @Override
    public String toString() {
        return getNumeComplet();
    }
}