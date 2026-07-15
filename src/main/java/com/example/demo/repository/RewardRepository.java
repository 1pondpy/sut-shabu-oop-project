package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.Reward;

public class RewardRepository {
    private static final String FILE_PATH = "data/rewards.txt";

    public List<Reward> findAll() {
        List<Reward> out = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return out;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                Reward rw = parseLine(line);
                if (rw != null) out.add(rw);
            }
        } catch (IOException ignored) {}
        return out;
    }

    public Reward findById(Integer id) {
        if (id == null) return null;
        for (Reward r : findAll()) if (id.equals(r.getId())) return r;
        return null;
    }

    private Reward parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 5) return null;
            Reward r = new Reward();
            r.setId(Integer.parseInt(p[0].trim()));
            r.setName(p[1].trim());
            r.setPointsRequired(Integer.parseInt(p[2].trim()));
            r.setDiscountType(p[3].trim());
            r.setDiscountValue(Double.parseDouble(p[4].trim()));
            return r;
        } catch (Exception e) {
            return null;
        }
    }
    public void save(Reward reward) {
        List<Reward> allRewards = findAll();
        
        if (reward.getId() == null) {          
            int maxId = allRewards.stream().mapToInt(Reward::getId).max().orElse(0);
            reward.setId(maxId + 1);
            allRewards.add(reward);
        } else {
            for (int i = 0; i < allRewards.size(); i++) {
                if (allRewards.get(i).getId().equals(reward.getId())) {
                    allRewards.set(i, reward);
                    break;
                }
            }
        }
        rewriteFile(allRewards);
    }
       public void deleteById(Integer id) {
        List<Reward> allRewards = findAll();
        allRewards.removeIf(r -> r.getId().equals(id));
        rewriteFile(allRewards);
    }

    private void rewriteFile(List<Reward> rewards) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
                new java.io.OutputStreamWriter(new java.io.FileOutputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            for (Reward r : rewards) {
                //ID|Name|Points|Type|Value
                String line = String.format("%d|%s|%d|%s|%.2f",
                        r.getId(),
                        r.getName(),
                        r.getPointsRequired(),
                        r.getDiscountType(),
                        r.getDiscountValue());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 


    private static final String CONFIG_FILE = "data/point_config.txt";


    public int[] getPointConfig() {
        File f = new File(CONFIG_FILE);
        if (!f.exists()) return new int[]{100, 10}; 

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    return new int[]{ Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{100, 10}; // กรณี Error คืนค่า Default
    }

  
    public void savePointConfig(int baht, int points) {
        File f = new File(CONFIG_FILE);
        f.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
            bw.write(baht + "|" + points);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Reward> searchAndSort(String keyword, String sortMode, String discountTypeFilter) {
        List<Reward> allRewards = findAll(); 
        
        // Search
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKey = keyword.toLowerCase().trim();
            allRewards = allRewards.stream()
                .filter(r -> r.getName().toLowerCase().contains(lowerKey))
                .collect(Collectors.toList());
        }

        // Filter by discount type if provided (PERCENT or BAHT)
        if (discountTypeFilter != null && !discountTypeFilter.isEmpty()) {
            final String filter = discountTypeFilter.trim().toUpperCase();
            allRewards = allRewards.stream()
                .filter(r -> r.getDiscountType() != null)
                .filter(r -> filter.equals(r.getDiscountType().trim().toUpperCase()))
                .collect(Collectors.toList());
        }

        // Sort
        if (sortMode != null) {
            switch (sortMode) {
                case "name_asc": 
                    allRewards.sort(Comparator.comparing(Reward::getName));
                    break;
                case "name_desc": 
                    allRewards.sort(Comparator.comparing(Reward::getName).reversed());
                    break;
                case "points_asc": 
                    allRewards.sort(Comparator.comparingInt(Reward::getPointsRequired));
                    break;
                case "points_desc": 
                    allRewards.sort((r1, r2) -> r2.getPointsRequired() - r1.getPointsRequired());
                    break;
                case "id_desc": 
                    allRewards.sort((r1, r2) -> r2.getId().compareTo(r1.getId()));
                    break;
                default: 
                    allRewards.sort(Comparator.comparingInt(Reward::getId));
            }
        }
        
        return allRewards;
    }
}
