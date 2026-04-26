package com.pao.project.biblioteca.model;

public final class ISBN {
    private final String valoare;

    public ISBN(String valoare) {
        this.valoare = valoare;
    }

    public String getValoare() {
        return valoare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ISBN isbn = (ISBN) o;
        return valoare.equals(isbn.valoare);
    }

    @Override
    public int hashCode() {
        return valoare.hashCode();
    }

    @Override
    public String toString() {
        return valoare;
    }
}