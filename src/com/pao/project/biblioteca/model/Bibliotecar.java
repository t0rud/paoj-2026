package com.pao.project.biblioteca.model;

public class Bibliotecar extends Persoana {
    private double salariu;

    public Bibliotecar(String cnp, String nume, double salariu) {
        super(cnp, nume);
        this.salariu = salariu;
    }

    public double getSalariu() { return salariu; }
    public void setSalariu(double salariu) { this.salariu = salariu; }

    @Override
    public String getRol() { return "BIBLIOTECAR"; }
}