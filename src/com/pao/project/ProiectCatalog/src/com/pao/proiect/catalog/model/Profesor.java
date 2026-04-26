package com.pao.proiect.catalog.model;

public class Profesor extends Persoana {
    private String departament;

    public Profesor(String nume, String prenume, String departament) {
        super(nume, prenume);
        this.departament = departament;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    @Override
    public String getRol() {
        return "PROFESOR";
    }

    @Override
    public String toString() {
        return "Profesor: " + nume + " " + prenume + " (" + departament + ")";
    }
}