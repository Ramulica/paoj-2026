package com.pao.laboratory08.lab8Rezolvare;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului paoj-2026
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) {
        List<Student> studenti = new ArrayList<>();

        // 1. Citește studenții din fișier
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
        } catch (IOException e) {
            System.out.println("Eroare la citirea fișierului: " + e.getMessage());
            return;
        }

        // 2. Citește comanda din stdin
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String comanda = scanner.nextLine().trim();

        // 3. Execută comanda
        if (comanda.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }
        } else if (comanda.startsWith("SHALLOW ") || comanda.startsWith("DEEP ")) {
            String[] comandaParts = comanda.split(" ", 2);
            String tipClonare = comandaParts[0];
            String numeCautat = comandaParts[1];

            Student studentGasit = null;
            for (Student s : studenti) {
                if (s.getNume().equals(numeCautat)) {
                    studentGasit = s;
                    break;
                }
            }

            if (studentGasit != null) {
                try {
                    Student clona;
                    if (tipClonare.equals("SHALLOW")) {
                        clona = studentGasit.shallowClone();
                    } else {
                        clona = studentGasit.deepClone();
                    }

                    clona.getAdresa().setOras("MODIFICAT");

                    System.out.println("Original: " + studentGasit);
                    System.out.println("Clona: " + clona);

                } catch (CloneNotSupportedException e) {
                    System.out.println("Eroare la clonare: " + e.getMessage());
                }
            }
        }

        scanner.close();
    }
}