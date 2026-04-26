package com.pao.project.biblioteca.model;
import java.util.Objects;

public abstract class Persoana {
    protected String cnp;
    protected String nume;

    public Persoana(String cnp, String nume) {
        this.cnp = cnp;
        this.nume = nume;
    }

    public String getCnp() { return cnp; }
    public String getNume() { return nume; }

    public abstract String getRol();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persoana persoana = (Persoana) o;
        return cnp.equals(persoana.cnp);
    }

    @Override
    public int hashCode() { return Objects.hash(cnp); }
}