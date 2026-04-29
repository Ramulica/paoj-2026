package com.pao.proiect.catalog.service;

import com.pao.proiect.catalog.exception.ElevNotFoundException;
import com.pao.proiect.catalog.exception.NotaInvalidaException;
import com.pao.proiect.catalog.model.Absenta;
import com.pao.proiect.catalog.model.Materie;
import com.pao.proiect.catalog.model.Matricola;
import com.pao.proiect.catalog.model.Nota;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CatalogService {
    private static CatalogService instance;

    private final Map<Matricola, Map<Materie, List<Nota>>> noteCatalog;

    private final Map<Matricola, List<Absenta>> absenteCatalog;

    private CatalogService() {
        this.noteCatalog = new HashMap<>();
        this.absenteCatalog = new HashMap<>();
    }

    public static CatalogService getInstance() {
        if (instance == null) {
            instance = new CatalogService();
        }
        return instance;
    }

    public void adaugaNota(Matricola matricola, Nota nota) throws ElevNotFoundException {
        if (nota == null || nota.getMaterie() == null) return;

        ElevService.getInstance().gasesteDupaMatricola(matricola);

        if (nota.getValoare() < 1 || nota.getValoare() > 10) {
            throw new NotaInvalidaException("Nota " + nota.getValoare() + " nu este permisa. Interval valid: [1, 10].");
        }

        noteCatalog.putIfAbsent(matricola, new HashMap<>());
        Map<Materie, List<Nota>> noteElev = noteCatalog.get(matricola);

        noteElev.putIfAbsent(nota.getMaterie(), new ArrayList<>());
        noteElev.get(nota.getMaterie()).add(nota);
    }

    public List<Nota> listeazaNote(Matricola matricola, Materie materie) {
        if (noteCatalog.containsKey(matricola) && noteCatalog.get(matricola).containsKey(materie)) {
            return noteCatalog.get(matricola).get(materie);
        }
        return new ArrayList<>();
    }

    public double calculeazaMedie(Matricola matricola, Materie materie) {
        List<Nota> note = listeazaNote(matricola, materie);
        if (note.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (Nota n : note) {
            suma += n.getValoare();
        }
        return suma / note.size();
    }

    public void adaugaAbsenta(Matricola matricola, Absenta absenta) throws ElevNotFoundException {
        if (absenta == null) return;
        ElevService.getInstance().gasesteDupaMatricola(matricola);

        absenteCatalog.putIfAbsent(matricola, new ArrayList<>());
        absenteCatalog.get(matricola).add(absenta);
    }

    public void motiveazaAbsenta(Matricola matricola, Materie materie, LocalDate data) {
        if (absenteCatalog.containsKey(matricola)) {
            for (Absenta a : absenteCatalog.get(matricola)) {
                if (a.getMaterie().equals(materie) && a.getData().equals(data)) {
                    a.setMotivata(true);
                }
            }
        }
    }

    public List<Absenta> absenteNemotivate(Matricola matricola) {
        List<Absenta> rezultat = new ArrayList<>();
        if (absenteCatalog.containsKey(matricola)) {
            for (Absenta a : absenteCatalog.get(matricola)) {
                if (!a.isMotivata()) {
                    rezultat.add(a);
                }
            }
        }
        return rezultat;
    }
}