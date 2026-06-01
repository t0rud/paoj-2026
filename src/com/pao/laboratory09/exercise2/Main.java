package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs();

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array());

                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array());

                String paddedData = String.format("%-10s", data);
                dos.write(paddedData.getBytes(StandardCharsets.US_ASCII), 0, 10);

                dos.writeByte(tip == TipTranzactie.CREDIT ? 0 : 1);

                dos.writeByte(0);

                dos.write(new byte[8]);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNext()) {
                String cmd = scanner.next();

                switch (cmd) {
                    case "READ":
                        int readIdx = scanner.nextInt();
                        printRecord(raf, readIdx);
                        break;

                    case "UPDATE":
                        int updateIdx = scanner.nextInt();
                        String newStatusStr = scanner.next();

                        byte statusByte = 0;
                        if (newStatusStr.equals("PROCESSED")) statusByte = 1;
                        else if (newStatusStr.equals("REJECTED")) statusByte = 2;

                        long statusOffset = (long) updateIdx * RECORD_SIZE + 23;
                        raf.seek(statusOffset);
                        raf.writeByte(statusByte);

                        System.out.println("Updated [" + updateIdx + "]: " + newStatusStr);
                        break;

                    case "PRINT_ALL":
                        long numRecords = raf.length() / RECORD_SIZE;
                        for (int i = 0; i < (int) numRecords; i++) {
                            printRecord(raf, i);
                        }
                        break;
                }
            }
        }
        scanner.close();
    }

    /**
     * Metodă ajutătoare care se poziționează la indexul dorit,
     * citește fix 32 de octeți, îi decodează și afișează linia formatată.
     */
    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        ByteBuffer bb = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);

        int id = bb.getInt();
        double suma = bb.getDouble();

        String data = new String(record, 12, 10, StandardCharsets.US_ASCII).trim();

        String tip = (record[22] == 0) ? "CREDIT" : "DEBIT";

        String status = "PENDING";
        if (record[23] == 1) status = "PROCESSED";
        else if (record[23] == 2) status = "REJECTED";

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n",
                idx, id, data, tip, suma, status);
    }
}