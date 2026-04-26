package com.pao.proiect.catalog.model;

import java.util.Objects;


public class Elev extends Persoana implements Comparable<Elev> {
    private Matricola matricola;
    private Clasa clasa;

    public Elev(String nume, String prenume, Matricola matricola, Clasa clasa) {
        super(nume, prenume);
        this.matricola = matricola;
        this.clasa = clasa;
    }

    public Matricola getMatricola() {
        return matricola;
    }

    public void setMatricola(Matricola matricola) {
        this.matricola = matricola;
    }

    public Clasa getClasa() {
        return clasa;
    }

    public void setClasa(Clasa clasa) {
        this.clasa = clasa;
    }

    @Override
    public String getRol() {
        return "ELEV";
    }

    @Override
    public int compareTo(Elev other) {
        int result = this.nume.compareToIgnoreCase(other.nume);
        if (result == 0) {
            result = this.prenume.compareToIgnoreCase(other.prenume);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elev elev = (Elev) o;
        return Objects.equals(matricola, elev.matricola); // Doi elevi sunt identici daca au aceeasi matricola
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricola);
    }

    @Override
    public String toString() {
        return "Elev [" + matricola + "] " + nume + " " + prenume + ", Clasa: " + clasa.getNume();
    }
}