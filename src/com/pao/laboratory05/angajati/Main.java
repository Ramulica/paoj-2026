package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Cerințele se află în Readme.md — secțiunea Exercise 3.");

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");
            // citește opțiunea și execută acțiunea

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            
            AngajatService job = AngajatService.getInstance();
            
            switch (option) {
                case 1:
                    System.out.println("Nume: ");
                    String nume = scanner.next();
                    System.out.println("Salraiu: ");
                    double salariu = scanner.nextDouble();
                    System.out.println("Nume departament: ");
                    String numeDept = scanner.next();
                    System.out.println("Locatie departament: ");
                    String locatieDept = scanner.next();
                    Departament dept = new Departament(numeDept, locatieDept);
                    Angajat ang = new Angajat(nume, dept, salariu);
                    job.addAngajat(ang);
                    break;
                case 2:
                    job.listBySalary();
                    break;
                case 3:
                    System.out.println("Nume departament: ");
                    String numeDept1 = scanner.next();
                    job.findByDepartament(numeDept1);
                    break;
                case 0:
                    System.out.println("La revedere!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opțiune invalidă. Încearcă din nou.");
            }
        }
    }
}
