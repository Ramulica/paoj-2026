package com.pao.proiect.catalog.model;

import java.time.LocalDate;

public class Absenta {
    private LocalDate data;
    private Materie materie;
    private boolean motivata;

    public Absenta(LocalDate data, Materie materie) {
        this.data = data;
        this.materie = materie;
        this.motivata = false; // Implicit, o absenta e nemotivata
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

    public boolean isMotivata() {
        return motivata;
    }

    public void setMotivata(boolean motivata) {
        this.motivata = motivata;
    }

    @Override
    public String toString() {
        return "Absenta la " + materie.getNume() + " din " + data + " (Motivata: " + (motivata ? "DA" : "NU") + ")";
    }
}