package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) {
        List<Student> studenti = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String nume = parts[0].trim();
                    int varsta = Integer.parseInt(parts[1].trim());
                    String oras = parts[2].trim();
                    String strada = parts[3].trim();

                    Adresa adresa = new Adresa(oras, strada);
                    Student student = new Student(nume, varsta, adresa);

                    studenti.add(student);
                }
            }
        } catch (Exception e) {
            System.out.println("Eroare la citirea fisierului: " + e.getMessage());
            return;
        }


        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String input = scanner.nextLine();


        try {
            if (input.equals("PRINT")) {
                // Afisam toti studentii din lista
                for (Student s : studenti) {
                    System.out.println(s);
                }
            } else if (input.startsWith("SHALLOW") || input.startsWith("DEEP")) {

                String[] commandParts = input.split(" ", 2);
                String tipClonare = commandParts[0];
                String numeCautat = commandParts[1];

                // Cautam studentul cu numele dat in lista noastra
                Student original = null;
                for (Student s : studenti) {
                    if (s.getNume().equals(numeCautat)) {
                        original = s;
                        break;
                    }
                }

                if (original != null) {
                    Student clona = null;

                    // depinde ce vrem, shallow sau deep
                    if (tipClonare.equals("SHALLOW")) {
                        clona = original.shallowClone();
                    } else if (tipClonare.equals("DEEP")) {
                        clona = original.deepClone();
                    }

                    clona.getAdresa().setOras("MODIFICAT");

                    // Afisam atat originalul, cat si clona, pentru a observa diferenta
                    System.out.println("Original: " + original);
                    System.out.println("Clona: " + clona);
                }
            }
        } catch (CloneNotSupportedException e) {
            System.out.println("Eroare la clonare: " + e.getMessage());
        }
    }
}