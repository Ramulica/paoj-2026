package com.pao.proiect.catalog.model;

public class Clasa {
    private String nume;
    private Profesor diriginte;

    public Clasa(String nume, Profesor diriginte) {
        this.nume = nume;
        this.diriginte = diriginte;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Profesor getDiriginte() {
        return diriginte;
    }

    public void setDiriginte(Profesor diriginte) {
        this.diriginte = diriginte;
    }

    @Override
    public String toString() {
        return nume + " (Diriginte: " + diriginte.getNume() + ")";
    }
}