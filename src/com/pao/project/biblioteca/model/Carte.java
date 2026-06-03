package com.pao.project.biblioteca.model;
import java.util.Objects;

public class Carte implements Comparable<Carte> {
    private ISBN isbn;
    private String titlu;
    private Autor autor;
    private Sectiune sectiune;
    private boolean disponibila;

    public Carte(ISBN isbn, String titlu, Autor autor, Sectiune sectiune) {
        this.isbn = isbn;
        this.titlu = titlu;
        this.autor = autor;
        this.sectiune = sectiune;
        this.disponibila = true;
    }

    public ISBN getIsbn() { return isbn; }
    public String getTitlu() { return titlu; }
    public Autor getAutor() { return autor; }
    public Sectiune getSectiune() { return sectiune; }
    public boolean isDisponibila() { return disponibila; }
    public void setDisponibila(boolean disponibila) { this.disponibila = disponibila; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carte carte = (Carte) o;
        return isbn.equals(carte.isbn);
    }

    @Override
    public int hashCode() { return Objects.hash(isbn); }

    @Override
    public String toString() {
        return "Carte{" + "titlu='" + titlu + '\'' + ", autor=" + autor + ", sectiune=" + sectiune + ", disponibila=" + disponibila + '}';
    }

    @Override
    public int compareTo(Carte altaCarte) {
        return this.titlu.compareTo(altaCarte.titlu);
    }
}