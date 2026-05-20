package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useLocale(Locale.US);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (in.hasNext()) {
            String cmd = in.next();
            switch (cmd) {
                case "ENQUEUE" -> {
                    Tranzactie t = readTx(in);
                    coada.addLast(t);
                }
                case "PUSH" -> {
                    Tranzactie t = readTx(in);
                    coada.addFirst(t);
                }
                case "DEQUEUE" -> {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        System.out.println("Procesat: " + coada.removeFirst());
                    }
                }
                case "POP" -> {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        System.out.println("Extras: " + coada.removeFirst());
                    }
                }
                case "REMOVE_DEBIT" -> {
                    int n = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        if (itr.next().getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            n++;
                        }
                    }
                    System.out.println("Eliminat " + n + " tranzactii DEBIT.");
                }
                case "REMOVE_BELOW" -> {
                    double threshold = in.nextDouble();
                    int n = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        if (itr.next().getSuma() < threshold) {
                            itr.remove();
                            n++;
                        }
                    }
                    System.out.println("Eliminat " + n + " tranzactii sub " + String.format(Locale.US, "%.2f", threshold) + " RON.");
                }
                case "PRINT" -> {
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                }
                case "SIZE" -> System.out.println("Dimensiune coada: " + coada.size());
                default -> {
                }
            }
        }
    }

    private static Tranzactie readTx(Scanner in) {
        int id = in.nextInt();
        double suma = in.nextDouble();
        String data = in.next();
        TipTranzactie tip = TipTranzactie.valueOf(in.next());
        return new Tranzactie(id, suma, data, tip);
    }
}
