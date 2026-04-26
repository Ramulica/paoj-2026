package com.pao.proiect.catalog.model;

import java.time.LocalDate;

public class Nota {
    private int valoare;
    private LocalDate data;
    private Materie materie;

    public Nota(int valoare, LocalDate data, Materie materie) {
        this.valoare = valoare;
        this.data = data;
        this.materie = materie;
    }

    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Materie getMaterie() {
        return materie;
    }

    public void setMaterie(Materie materie) {
        this.materie = materie;
    }

    @Override
    public String toString() {
        return valoare + " (" + materie.getNume() + " pe " + data + ")";
    }
}