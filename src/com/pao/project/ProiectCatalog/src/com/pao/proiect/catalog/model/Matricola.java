package com.pao.proiect.catalog.model;

import java.util.Objects;


public final class Matricola {
    private final String cod;
    private final int anInmatriculare;

    public Matricola(String cod, int anInmatriculare) {
        this.cod = cod;
        this.anInmatriculare = anInmatriculare;
    }

    public String getCod() {
        return cod;
    }

    public int getAnInmatriculare() {
        return anInmatriculare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matricola matricola = (Matricola) o;
        return anInmatriculare == matricola.anInmatriculare && Objects.equals(cod, matricola.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod, anInmatriculare);
    }

    @Override
    public String toString() {
        return cod + "/" + anInmatriculare;
    }
}