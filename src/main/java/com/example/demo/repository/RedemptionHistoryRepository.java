package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.RedemptionHistory;

public class RedemptionHistoryRepository {
    
  
    private static final String FILE_PATH = "data/redemption_history.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void save(RedemptionHistory h) {
        try {
            File f = new File(FILE_PATH);
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
            if (!f.exists()) f.createNewFile();
            long nextId = getNextId();
            h.setId(nextId);

            String dateStr = h.getRedeemDate() != null ? h.getRedeemDate().format(FMT) : "";


            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true), "UTF-8"))) {
                //ID|เบอร์โทร|ชื่อรางวัล|แต้มที่ใช้|วันที่แลก
                String line = String.format("%d|%s|%s|%d|%s",
                        h.getId(),
                        h.getMemberPhone(),
                        h.getRewardName(),
                        h.getPointsUsed(),
                        dateStr
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    public List<RedemptionHistory> findAll() {
        List<RedemptionHistory> list = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return list;

        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 5) {
                    RedemptionHistory h = new RedemptionHistory();
                    h.setId(Long.parseLong(p[0]));
                    h.setMemberPhone(p[1]);
                    h.setRewardName(p[2]);
                    h.setPointsUsed(Integer.parseInt(p[3]));
                    if (!p[4].isEmpty()) {
                        h.setRedeemDate(LocalDateTime.parse(p[4], FMT));
                    }
                    list.add(h);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

   
    private long getNextId() {
        long max = 0;
        for (RedemptionHistory h : findAll()) {
            if (h.getId() > max) max = h.getId();
        }
        return max + 1;
    }
}