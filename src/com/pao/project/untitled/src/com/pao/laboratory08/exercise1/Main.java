package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/exercise1/studenti.txt";

    public static void main(String[] args) {
        List<Student> studenti = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Adresa adresa = new Adresa(parts[2].trim(), parts[3].trim());
                    Student student = new Student(parts[0].trim(), Integer.parseInt(parts[1].trim()), adresa);
                    studenti.add(student);
                }
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea fișierului: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;

        String comanda = scanner.nextLine().trim();

        if (comanda.equals("PRINT")) {
            for (Student s : studenti) System.out.println(s);
        } else if (comanda.startsWith("SHALLOW ") || comanda.startsWith("DEEP ")) {
            String[] parts = comanda.split(" ", 2);

            Student studentGasit = null;
            for (Student s : studenti) {
                if (s.getNume().equals(parts[1])) {
                    studentGasit = s;
                    break;
                }
            }

            if (studentGasit != null) {
                try {
                    Student clona = parts[0].equals("SHALLOW") ? studentGasit.shallowClone() : studentGasit.deepClone();
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