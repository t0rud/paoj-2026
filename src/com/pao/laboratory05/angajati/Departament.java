package com.pao.laboratory05.angajati;

public record Departament(String nume, String locatie) {
    // Record-ul genereaza automat constructor, getteri (nume(), locatie()), toString(), equals(), hashCode()
}