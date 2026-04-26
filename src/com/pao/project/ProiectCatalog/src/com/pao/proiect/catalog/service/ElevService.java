package com.pao.proiect.catalog.service;

import com.pao.proiect.catalog.exception.ElevNotFoundException;
import com.pao.proiect.catalog.model.Elev;
import com.pao.proiect.catalog.model.Matricola;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class ElevService {
    private static ElevService instance;

    private final Map<Matricola, Elev> eleviMap;

    private ElevService() {
        this.eleviMap = new HashMap<>();
    }

    public static ElevService getInstance() {
        if (instance == null) {
            instance = new ElevService();
        }
        return instance;
    }

    public void adaugaElev(Elev elev) {
        if (elev != null && elev.getMatricola() != null) {
            eleviMap.put(elev.getMatricola(), elev);
        }
    }

    public Elev gasesteDupaMatricola(Matricola matricola) throws ElevNotFoundException {
        if (matricola == null || !eleviMap.containsKey(matricola)) {
            throw new ElevNotFoundException("Elevul cu matricola " + matricola + " nu a fost gasit in sistem!");
        }
        return eleviMap.get(matricola);
    }

    public Set<Elev> listeazaEleviOrdonat() {
        return new TreeSet<>(eleviMap.values());
    }

    public void stergeElev(Matricola matricola) throws ElevNotFoundException {
        if (matricola == null || !eleviMap.containsKey(matricola)) {
            throw new ElevNotFoundException("Stergerea a esuat. Elevul nu exista!");
        }
        eleviMap.remove(matricola);
    }
}