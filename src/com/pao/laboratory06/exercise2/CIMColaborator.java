package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();


        String rest = in.nextLine().trim();
        this.bonus = rest.equalsIgnoreCase("DA");
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return this.bonus;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = venitBrutLunar * 12 * 0.55;
        if (areBonus()) {
            venitNet += venitNet * 0.10;
        }
        return venitNet;
    }
}