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

import com.example.demo.entity.TableSession;

public class TableSessionRepository {
    
    private static final String FILE_PATH = "data/table_sessions.txt";
    private static final DateTimeFormatter FMT_ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter FMT_MINUTES = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter FMT_SECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

 
    public void save(TableSession s) {
        try {
            File f = new File(FILE_PATH);
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
            if (!f.exists()) f.createNewFile();

       
            if (s.getId() == null) {
                s.setId(getNextId());
            }

      
            String startStr = s.getStartTime() != null ? s.getStartTime().format(FMT_ISO) : "";
            String endStr = s.getEndTime() != null ? s.getEndTime().format(FMT_ISO) : "";
            
           
            String appliedRewards = s.getAppliedRewardIds() != null ? s.getAppliedRewardIds() : "";

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true), "UTF-8"))) {
                // ID|Table|Name|Phone|Pax|Start|Status|Package|Soup|End|Total|Rewards
                    String packageType = s.getPackageType() != null ? s.getPackageType() : "STANDARD";
                    String line = String.format("%d|%s|%s|%s|%d|%s|%s|%s|%s|%s|%.2f|%s",
                            s.getId(),
                            s.getTableNo(),
                            s.getCustomerName(),
                            s.getCustomerPhone(),
                            s.getPax(),
                            startStr,
                            s.getStatus(),
                            packageType,
                            s.getSoup(),
                            endStr,
                            s.getTotalAmount() != null ? s.getTotalAmount() : 0.0,
                            appliedRewards
                    );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 
    public List<TableSession> findAll() {
        List<TableSession> list = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return list;

        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String line;
            while ((line = r.readLine()) != null) {
                TableSession s = parseLine(line);
                if (s != null) list.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

   
    public TableSession findTopByTableNoAndStatus(String tableNo, String status) {
        TableSession latestSession = null;
        
    
        for (TableSession s : findAll()) {
            if (s.getTableNo().equalsIgnoreCase(tableNo)) {
                latestSession = s;
            }
        }

        if (latestSession != null && latestSession.getStatus().equalsIgnoreCase(status)) {
            return latestSession;
        }
        return null;
    }


    private TableSession parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1); 
            if (p.length < 8) return null;

            TableSession s = new TableSession();
            s.setId(Long.parseLong(p[0]));
            s.setTableNo(p[1]);
            s.setCustomerName(p[2]);
            s.setCustomerPhone(p[3]);
            s.setPax(p[4].isEmpty() ? 0 : Integer.parseInt(p[4]));
            if (!p[5].isEmpty()) s.setStartTime(parseDateLenient(p[5]));
            s.setStatus(p[6]);
            if (p.length > 7) s.setPackageType(p[7]);
            if (p.length > 8) s.setSoup(p[8]);
            if (p.length > 9 && !p[9].isEmpty()) s.setEndTime(parseDateLenient(p[9]));
            if (p.length > 10 && !p[10].isEmpty()) s.setTotalAmount(Double.parseDouble(p[10]));
            if (p.length > 11) s.setAppliedRewardIds(p[11]);

            return s;
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime parseDateLenient(String raw) {
        if (raw == null || raw.isEmpty()) return null;
        Exception lastEx = null;
        try {
            return LocalDateTime.parse(raw, FMT_ISO);
        } catch (Exception e) { lastEx = e; }
        try {
            return LocalDateTime.parse(raw, FMT_SECONDS);
        } catch (Exception e) { lastEx = e; }
        try {
            return LocalDateTime.parse(raw, FMT_MINUTES);
        } catch (Exception e) { lastEx = e; }
        try {
            return LocalDateTime.parse(raw);
        } catch (Exception e) { lastEx = e; }
        return null;
    }

    private long getNextId() {
        long max = 0;
        for (TableSession s : findAll()) {
            if (s.getId() > max) max = s.getId();
        }
        return max + 1;
    }
}