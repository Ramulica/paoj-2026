package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useLocale(Locale.US);
        int n = in.nextInt();
        List<Tranzactie> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lista.add(new Tranzactie(in.nextInt(), in.nextDouble(), in.next(), TipTranzactie.valueOf(in.next())));
        }

        while (in.hasNext()) {
            String op = in.next();
            switch (op) {
                case "UNIQUE_IDS" -> {
                    LinkedHashSet<Integer> ids = lista.stream()
                            .map(Tranzactie::getId)
                            .collect(Collectors.toCollection(LinkedHashSet::new));
                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                }
                case "MONTHLY_REPORT" -> {
                    TreeMap<String, double[]> byMonth = new TreeMap<>();
                    for (Tranzactie t : lista) {
                        String m = t.getData().substring(0, 7);
                        double[] sums = byMonth.computeIfAbsent(m, k -> new double[]{0.0, 0.0});
                        if (t.getTip() == TipTranzactie.CREDIT) {
                            sums[0] += t.getSuma();
                        } else {
                            sums[1] += t.getSuma();
                        }
                    }
                    for (Map.Entry<String, double[]> e : byMonth.entrySet()) {
                        double c = e.getValue()[0];
                        double d = e.getValue()[1];
                        System.out.printf(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                e.getKey(), c, d);
                    }
                }
                case "TOP" -> {
                    int k = in.nextInt();
                    List<Tranzactie> copy = new ArrayList<>(lista);
                    copy.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    System.out.println("Top " + k + ":");
                    int limit = Math.min(k, copy.size());
                    for (int i = 0; i < limit; i++) {
                        System.out.println(copy.get(i));
                    }
                }
                case "SORT_ASC" -> {
                    lista.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                }
                case "SORT_DESC" -> {
                    lista.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                }
                case "REVERSE" -> {
                    Collections.reverse(lista);
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                }
                case "MIN_MAX" -> {
                    Tranzactie min = Collections.min(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                }
                case "CME_DEMO" -> {
                    try {
                        for (Tranzactie t : lista) {
                            lista.remove(t);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                }
                default -> {
                }
            }
        }
    }
}
