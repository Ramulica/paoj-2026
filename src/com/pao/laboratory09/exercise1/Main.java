package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in).useLocale(Locale.US);
        int n = in.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Tranzactie t = new Tranzactie();
            t.setId(in.nextInt());
            t.setSuma(in.nextDouble());
            t.setData(in.next());
            t.setContSursa(in.next());
            t.setContDestinatie(in.next());
            t.setTip(TipTranzactie.valueOf(in.next()));
            t.setNote("procesat");
            tranzactii.add(t);
        }

        new File(OUTPUT_FILE).getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(tranzactii);
        }

        List<Tranzactie> restored;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            @SuppressWarnings("unchecked")
            List<Tranzactie> list = (List<Tranzactie>) ois.readObject();
            restored = list;
        }

        while (in.hasNext()) {
            String cmd = in.next();
            if ("LIST".equals(cmd)) {
                for (Tranzactie t : restored) {
                    System.out.println(formatTransaction(t));
                }
            } else if ("FILTER".equals(cmd)) {
                String prefix = in.next();
                boolean any = false;
                for (Tranzactie t : restored) {
                    if (t.getData().startsWith(prefix)) {
                        System.out.println(formatTransaction(t));
                        any = true;
                    }
                }
                if (!any) {
                    System.out.println("Niciun rezultat.");
                }
            } else if ("NOTE".equals(cmd)) {
                int id = in.nextInt();
                boolean found = false;
                for (Tranzactie t : restored) {
                    if (t.getId() == id) {
                        System.out.println("NOTE[" + id + "]: " + t.getNote());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("NOTE[" + id + "]: not found");
                }
            }
        }
    }

    private static String formatTransaction(Tranzactie t) {
        return String.format(Locale.US, "[%d] %s %s: %.2f RON | %s -> %s",
                t.getId(), t.getData(), t.getTip().name(), t.getSuma(), t.getContSursa(), t.getContDestinatie());
    }
}
