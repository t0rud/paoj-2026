package com.pao.project.biblioteca.model;

public class Autor {
    private String nume;
    private String prenume;

    public Autor(String nume, String prenume) {
        this.nume = nume;
        this.prenume = prenume;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getNumeComplet() {
        return prenume + " " + nume;
    }
}