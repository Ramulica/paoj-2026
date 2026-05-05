package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected double pret;
    protected OrderState stare = OrderState.PLACED;

    protected Comanda(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
    }

    public abstract double pretFinal();
    public abstract String descriere();
    public OrderState getStare() {
        return stare;
    }
}
