package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.StaffRequest;

public class StaffRequestRepository {
    private static final String FILE_PATH = "data/staff_requests.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private List<StaffRequest> readAll() {
        List<StaffRequest> out = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return out;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                StaffRequest s = parseLine(line);
                if (s != null) out.add(s);
            }
        } catch (IOException ignored) {}
        return out;
    }

    private StaffRequest parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 5) return null;
            StaffRequest s = new StaffRequest();
            s.setId(Long.parseLong(p[0].trim()));
            s.setTableNo(p[1].trim());
            s.setRequestType(p[2].trim());
            s.setRequestTime(LocalDateTime.parse(p[3].trim(), FMT));
            s.setStatus(p[4].trim());
            return s;
        } catch (Exception e) {
            return null;
        }
    }

    public List<StaffRequest> findByStatusOrderByRequestTimeAsc(String status) {
        if (status == null) return new ArrayList<>();
        return readAll().stream()
                .filter(s -> status.equalsIgnoreCase(s.getStatus()))
                .sorted(Comparator.comparing(StaffRequest::getRequestTime))
                .collect(Collectors.toList());
    }
}
