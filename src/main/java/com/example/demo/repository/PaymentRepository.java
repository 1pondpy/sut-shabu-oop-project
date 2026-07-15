package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.format.DateTimeFormatter;

import com.example.demo.entity.PaymentRecord;

public class PaymentRepository {
    private static final String FILE_PATH = "data/payments.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void save(PaymentRecord p) {
        try {
            File f = new File(FILE_PATH);
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
            if (!f.exists()) f.createNewFile();

            p.setId(getNextId());
            String timeStr = p.getPaymentTime() != null ? p.getPaymentTime().format(FMT) : "";

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true), "UTF-8"))) {
                //ID|Table|Name|Phone|Total|Discount|Net|Method|Received|Change|Time
                String line = String.format("%d|%s|%s|%s|%.2f|%.2f|%.2f|%s|%.2f|%.2f|%s",
                        p.getId(), p.getTableNo(), p.getCustomerName(), p.getMemberPhone(),
                        p.getTotalAmount(), p.getDiscount(), p.getNetAmount(),
                        p.getPaymentMethod(), p.getAmountReceived(), p.getChangeAmount(), timeStr);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private long getNextId() {
        long max = 0;
        File f = new File(FILE_PATH);
        if (!f.exists()) return 1;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    try { long id = Long.parseLong(parts[0]); if(id > max) max = id; } catch(Exception e){}
                }
            }
        } catch (Exception e) {}
        return max + 1;
    }
}