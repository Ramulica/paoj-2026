package com.pao.laboratory05.angajati;

public class Angajat implements Comparable<Angajat> {
    private String nume;
    private Departament departament;
    private double salariu;

    public double getSalariu() { return this.salariu; }
    public String getNume() {return this.nume; }
    public Departament getDepartament() {return this.departament; }

    public Angajat(String nume, Departament departament, double salariu) {
        this.nume = nume;
        this.departament = departament;
        this.salariu = salariu;
    }

    @Override
    public String toString() {
        return "Angajat{nume='" + this.getNume() + "', departament=Departament[nume='" + this.getDepartament().nume() +
        ", locatie='" + this.getDepartament().locatie() + "], salariu='" + this.getSalariu() + "}";
    }

    @Override
    public int compareTo(Angajat other) {
        return Double.compare(this.getSalariu(), other.getSalariu());
    }
}
