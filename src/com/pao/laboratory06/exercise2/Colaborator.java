package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public abstract TipColaborator getTip();
    public abstract double calculeazaVenitNetAnual();

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei\n",
                getTip(), nume, prenume, calculeazaVenitNetAnual());
    }
}