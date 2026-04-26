package com.pao.project.biblioteca.model;
import java.util.ArrayList;
import java.util.List;

public class Cititor extends Persoana {
    private List<Imprumut> istoricImprumuturi;

    public Cititor(String cnp, String nume) {
        super(cnp, nume);
        this.istoricImprumuturi = new ArrayList<>();
    }

    public List<Imprumut> getIstoricImprumuturi() { return istoricImprumuturi; }
    public void adaugaImprumut(Imprumut imprumut) { this.istoricImprumuturi.add(imprumut); }

    @Override
    public String getRol() { return "CITITOR"; }

    @Override
    public String toString() {
        return "Cititor{nume='" + nume + "', cnp='" + cnp + "'}";
    }
}