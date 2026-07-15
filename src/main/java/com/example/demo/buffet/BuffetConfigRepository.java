package com.example.demo.buffet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class BuffetConfigRepository {
    private static final String DEFAULT = "data/buffet_config.txt";
    private final String filePath;

    public BuffetConfigRepository() { this(DEFAULT); }
    public BuffetConfigRepository(String filePath) { this.filePath = filePath; }

    public BuffetConfig read() {
        BuffetConfig cfg = new BuffetConfig();
        File f = new File(filePath);
        if (!f.exists()) return cfg;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] p = line.split("=", 2);
                if (p.length < 2) continue;
                String k = p[0].trim();
                String v = p[1].trim();
                try {
                    if ("STANDARD".equalsIgnoreCase(k)) cfg.setStandardPrice(Integer.parseInt(v));
                    if ("PREMIUM".equalsIgnoreCase(k)) cfg.setPremiumPrice(Integer.parseInt(v));
                } catch (NumberFormatException ex) {
                    // ignore bad values
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfg;
    }

    public void save(BuffetConfig cfg) {
        try {
            File f = new File(filePath);
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
                w.write("STANDARD=" + cfg.getStandardPrice()); w.newLine();
                w.write("PREMIUM=" + cfg.getPremiumPrice()); w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
