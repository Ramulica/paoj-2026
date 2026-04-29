package com.pao.laboratory05.angajati;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine(); // consumă newline-ul

            if (optiune == 0) {
                System.out.println("La revedere!");
                break;
            } else if (optiune == 1) {
                System.out.print("Nume: ");
                String nume = scanner.nextLine();
                System.out.print("Departament (nume): ");
                String numeDept = scanner.nextLine();
                System.out.print("Departament (locatie): ");
                String locatieDept = scanner.nextLine();
                System.out.print("Salariu: ");
                double salariu = scanner.nextDouble();
                scanner.nextLine();

                Departament dept = new Departament(numeDept, locatieDept);
                service.addAngajat(new Angajat(nume, dept, salariu));
            } else if (optiune == 2) {
                service.listBySalary();
            } else if (optiune == 3) {
                System.out.print("Departament: ");
                String numeDept = scanner.nextLine();
                service.findByDepartament(numeDept);
            } else {
                System.out.println("Opțiune invalidă.");
            }
        }
        scanner.close();
    }
}