package com.pao.laboratory08.exercise1;

public class Student implements Cloneable {
    private String nume;
    private int varsta;
    private Adresa adresa;

    public Student(String nume, int varsta, Adresa adresa) {
        this.nume = nume;
        this.varsta = varsta;
        this.adresa = adresa;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    @Override
    public String toString() {
        return "Student{nume='" + nume + "', varsta=" + varsta + ", adresa=" + adresa.toString() + "}";
    }

    // Clonare superficiala (Shallow Clone)
    // Se copiaza datele primitive, dar obiectul "Adresa" este acelasi (aceeasi referinta in memorie)
    public Student shallowClone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }

    // Clonare profunda (Deep Clone)
    // Se copiaza intai studentul, apoi se copiaza explicit si obiectul "Adresa"
    public Student deepClone() throws CloneNotSupportedException {
        Student clona = (Student) super.clone();
        clona.setAdresa((Adresa) this.adresa.clone()); // Clonam adresa pentru a fi independenta
        return clona;
    }
}