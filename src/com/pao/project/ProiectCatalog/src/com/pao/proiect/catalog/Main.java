package com.pao.proiect.catalog;

import com.pao.proiect.catalog.exception.ElevNotFoundException;
import com.pao.proiect.catalog.exception.NotaInvalidaException;
import com.pao.proiect.catalog.model.*;
import com.pao.proiect.catalog.service.CatalogService;
import com.pao.proiect.catalog.service.ElevService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ElevService elevService = ElevService.getInstance();
        CatalogService catalogService = CatalogService.getInstance();

        Profesor profMate = new Profesor("Ionescu", "Marian", "Matematica");
        Profesor profRomana = new Profesor("Popescu", "Elena", "Limba si Literatura Romana");

        Clasa clasa10A = new Clasa("10A", profMate);

        Materie mate = new Materie("Matematica");
        Materie romana = new Materie("Romana");

        Matricola m1 = new Matricola("M100", 2024);
        Matricola m2 = new Matricola("M101", 2024);
        Matricola m3 = new Matricola("M102", 2024);

        Elev elev1 = new Elev("Zaharia", "Andrei", m1, clasa10A);
        Elev elev2 = new Elev("Avram", "Mihai", m2, clasa10A);
        Elev elev3 = new Elev("Radu", "Cristina", m3, clasa10A);


        System.out.println("--- Actiunea 1: Adauga Elevi ---");
        elevService.adaugaElev(elev1);
        elevService.adaugaElev(elev2);
        elevService.adaugaElev(elev3);
        System.out.println("Au fost adaugati 3 elevi in sistem.\n");

        System.out.println("--- Actiunea 2: Listeaza Elevii Alfabetic ---");
        for (Elev e : elevService.listeazaEleviOrdonat()) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("--- Actiunea 3: Gaseste Elev dupa Matricola ---");
        try {
            Elev gasit = elevService.gasesteDupaMatricola(m1);
            System.out.println("Elev gasit cu succes: " + gasit);

            elevService.gasesteDupaMatricola(new Matricola("INEXISTENT", 2000));
        } catch (ElevNotFoundException e) {
            System.out.println("Exceptie prinsa corect: " + e.getMessage());
        }
        System.out.println();

        System.out.println("--- Actiunea 4: Adauga Note ---");
        try {
            catalogService.adaugaNota(m1, new Nota(9, LocalDate.of(2024, 3, 15), mate));
            catalogService.adaugaNota(m1, new Nota(10, LocalDate.of(2024, 4, 10), mate));
            System.out.println("S-au adaugat cu succes note valabile pentru elevul M100.");

            catalogService.adaugaNota(m1, new Nota(15, LocalDate.now(), mate));
        } catch (ElevNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (NotaInvalidaException e) {
            System.out.println("Exceptie prinsa corect: " + e.getMessage());
        }
        System.out.println();

        System.out.println("--- Actiunea 5: Listeaza Notele Elevului M100 la Matematica ---");
        System.out.println(catalogService.listeazaNote(m1, mate));
        System.out.println();

        System.out.println("--- Actiunea 6: Calculeaza Media la Matematica pentru M100 ---");
        double medie = catalogService.calculeazaMedie(m1, mate);
        System.out.println("Media: " + medie);
        System.out.println();

        System.out.println("--- Actiunea 7: Adauga Absente ---");
        try {
            catalogService.adaugaAbsenta(m2, new Absenta(LocalDate.of(2024, 4, 5), romana));
            catalogService.adaugaAbsenta(m2, new Absenta(LocalDate.of(2024, 4, 12), romana));
            System.out.println("S-au adaugat 2 absente pentru M101 la Romana.");
        } catch (ElevNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("--- Actiunea 8: Motiveaza Absenta din data de 05.04.2024 ---");
        catalogService.motiveazaAbsenta(m2, romana, LocalDate.of(2024, 4, 5));
        System.out.println("Absenta motivata cu succes.");
        System.out.println();

        System.out.println("--- Actiunea 9: Afiseaza Absente Nemotivate pentru M101 ---");
        for (Absenta a : catalogService.absenteNemotivate(m2)) {
            System.out.println(a);
        }
        System.out.println();

        System.out.println("--- Actiunea 10: Sterge Elev (M102) ---");
        try {
            elevService.stergeElev(m3);
            System.out.println("Elevul a fost sters cu succes. Elevi ramasi in sistem:");
            for (Elev e : elevService.listeazaEleviOrdonat()) {
                System.out.println(e.getNume() + " " + e.getPrenume());
            }
        } catch (ElevNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}