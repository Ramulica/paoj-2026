package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in).useLocale(Locale.US);
        int n = in.nextInt();

        new File(OUTPUT_FILE).getParentFile().mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                int id = in.nextInt();
                double suma = in.nextDouble();
                String data = in.next();
                TipTranzactie tip = TipTranzactie.valueOf(in.next());

                dos.write(leInt(id));
                dos.write(leDouble(suma));

                byte[] dateBytes = pad10(data);
                dos.write(dateBytes);

                dos.writeByte(tip == TipTranzactie.CREDIT ? 0 : 1);
                dos.writeByte(0); // PENDING
                dos.write(new byte[8]); // padding
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (in.hasNext()) {
                String cmd = in.next();
                if ("READ".equals(cmd)) {
                    int idx = in.nextInt();
                    printRecord(raf, idx);
                } else if ("UPDATE".equals(cmd)) {
                    int idx = in.nextInt();
                    String st = in.next();
                    int statusByte = switch (st) {
                        case "PENDING" -> 0;
                        case "PROCESSED" -> 1;
                        case "REJECTED" -> 2;
                        default -> 0;
                    };
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.writeByte(statusByte);
                    System.out.println("Updated [" + idx + "]: " + st);
                } else if ("PRINT_ALL".equals(cmd)) {
                    for (int idx = 0; idx < n; idx++) {
                        printRecord(raf, idx);
                    }
                }
            }
        }
    }

    private static byte[] leInt(int v) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(v).array();
    }

    private static byte[] leDouble(double v) {
        return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(v).array();
    }

    private static byte[] pad10(String data) {
        byte[] out = new byte[10];
        byte[] raw = data.getBytes();
        int len = Math.min(raw.length, 10);
        System.arraycopy(raw, 0, out, 0, len);
        for (int i = len; i < 10; i++) {
            out[i] = (byte) ' ';
        }
        return out;
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] rec = new byte[RECORD_SIZE];
        raf.readFully(rec);
        ByteBuffer bb = ByteBuffer.wrap(rec).order(ByteOrder.LITTLE_ENDIAN);
        int id = bb.getInt();
        double suma = bb.getDouble();
        byte[] dateRaw = new byte[10];
        bb.get(dateRaw);
        String data = new String(dateRaw).trim();
        int tipB = bb.get() & 0xFF;
        int statusB = bb.get() & 0xFF;
        TipTranzactie tip = tipB == 0 ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        String status = switch (statusB) {
            case 1 -> "PROCESSED";
            case 2 -> "REJECTED";
            default -> "PENDING";
        };
        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, data, tip.name(), suma, status);
    }
}
