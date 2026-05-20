package com.pao.laboratory11.exercise2;

import com.pao.laboratory11.exercise1.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            run();
        } catch (IOException e) {
            // checker: silent
        }
    }

    private static void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String first = nextNonEmpty(br);
        if (first == null) {
            return;
        }

        int n = Integer.parseInt(first);
        List<Tx> txs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String line = nextNonEmpty(br);
            if (line == null) {
                return;
            }

            String[] p = line.split("\\s+");
            txs.add(new Tx(
                    Integer.parseInt(p[0]),
                    Double.parseDouble(p[1]),
                    p[2],
                    p[3],
                    p[4],
                    p[5]));
        }

        int q = Integer.parseInt(nextNonEmpty(br));
        for (int i = 0; i < q; i++) {
            String line = nextNonEmpty(br);
            if (line == null) {
                return;
            }

            String[] p = line.split("\\s+");
            String op = p[0];

            switch (op) {
                case "REPORT_MONTH" -> {
                    String month = p[1];
                    List<Tx> inMonth = txs.stream()
                            .filter(tx -> tx.getDate().startsWith(month))
                            .toList();
                    double total = inMonth.stream().mapToDouble(Tx::getAmount).sum();
                    long count = inMonth.size();
                    System.out.printf(Locale.US, "MONTH %s total=%.2f count=%d%n", month, total, count);
                }
                case "REPORT_ACCOUNT" -> {
                    String account = p[1];
                    List<Tx> forAcc = txs.stream()
                            .filter(tx -> tx.getAccountId().equals(account))
                            .toList();
                    double total = forAcc.stream().mapToDouble(Tx::getAmount).sum();
                    long count = forAcc.size();
                    System.out.printf(Locale.US, "ACCOUNT %s total=%.2f count=%d%n", account, total, count);
                }
                case "TOP_CHANNELS" -> {
                    int k = Integer.parseInt(p[1]);
                    if (txs.isEmpty()) {
                        System.out.println("NONE");
                        break;
                    }
                    Map<String, Long> counts = txs.stream()
                            .collect(Collectors.groupingBy(Tx::getChannel, Collectors.counting()));

                    List<Map.Entry<String, Long>> entries = new ArrayList<>(counts.entrySet());
                    entries.sort(Comparator
                            .comparingLong((Map.Entry<String, Long> e) -> e.getValue()).reversed()
                            .thenComparing(Map.Entry.comparingByKey()));

                    int limit = Math.min(k, entries.size());
                    for (int idx = 0; idx < limit; idx++) {
                        Map.Entry<String, Long> e = entries.get(idx);
                        System.out.println(e.getKey() + " " + e.getValue());
                    }
                }
                default -> {
                    // ignore unknown
                }
            }
        }
    }

    private static String nextNonEmpty(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return null;
    }

    private static final class Tx extends Transaction {
        private final String accountId;

        private Tx(int id, double amount, String date, String country, String channel, String accountId) {
            super(id, amount, date, country, channel);
            this.accountId = accountId;
        }

        private String getAccountId() {
            return accountId;
        }
    }
}
