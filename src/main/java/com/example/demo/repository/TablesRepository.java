package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.Tables;

public class TablesRepository {
    private static final String FILE_PATH = "data/tables.txt";

    public List<Tables> findByZone(String zone) {
        List<Tables> out = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return out;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length < 4) continue;
                String z = p[2].trim();
                if (zone == null || zone.equalsIgnoreCase(z)) {
                    Tables t = new Tables();
                    t.setId(Integer.parseInt(p[0].trim()));
                    t.setTableNo(p[1].trim());
                    t.setZone(z);
                    t.setSeatCount(Integer.parseInt(p[3].trim()));
                    out.add(t);
                }
            }
        } catch (IOException ignored) {}
        return out;
    }
}
