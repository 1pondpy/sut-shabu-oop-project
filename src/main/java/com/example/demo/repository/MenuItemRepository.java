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

import com.example.demo.entity.MenuItem;

public class MenuItemRepository {
    
    private static final String DEFAULT_FILE_PATH = "data/menu_items.txt";
    private final String filePath;

    public MenuItemRepository() {
        this(DEFAULT_FILE_PATH);
    }

    public MenuItemRepository(String filePath) {
        this.filePath = filePath;
    }


    public List<MenuItem> findAll() {
        List<MenuItem> items = new ArrayList<>();
        File f = new File(filePath);
        
       
        if (!f.exists()) return items;

        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                MenuItem it = parseLine(line);
                if (it != null) items.add(it);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    
    public List<MenuItem> findByIsActiveTrue() {
        List<MenuItem> out = new ArrayList<>();
        for (MenuItem it : findAll()) {
            if (it.isActive()) out.add(it);
        }
        return out;
    }
    public List<MenuItem> searchAndSort(String keyword, String sortMode, String categoryFilter) {
        List<MenuItem> list = findAll();

        // Filter by Category
        if (categoryFilter != null && !categoryFilter.isEmpty()) {
            list = list.stream()
                .filter(m -> m.getCategory() != null && m.getCategory().equalsIgnoreCase(categoryFilter))
                .collect(Collectors.toList());
        }

        // Search by Keyword (Name)
        if (keyword != null && !keyword.trim().isEmpty()) {
            String key = keyword.toLowerCase().trim();
            list = list.stream()
                .filter(m -> m.getName() != null && m.getName().toLowerCase().contains(key))
                .collect(Collectors.toList());
        }

        // Sort
        if (sortMode != null) {
            switch (sortMode) {
                case "name_asc":
                    list.sort(Comparator.comparing(MenuItem::getName));
                    break;
                case "name_desc":
                    list.sort(Comparator.comparing(MenuItem::getName).reversed());
                    break;
                case "category_asc":
                    list.sort(Comparator.comparing(MenuItem::getCategory));
                    break;
                default:
                    list.sort(Comparator.comparingInt(MenuItem::getId)); // Default sort by ID
            }
        }

        return list;
    }

    public MenuItem findById(int id) {
        return findAll().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

   
    public void save(MenuItem item) {
        List<MenuItem> all = findAll();
        
        if (item.getId() == null) {
           
            int maxId = all.stream().mapToInt(MenuItem::getId).max().orElse(0);
            item.setId(maxId + 1);
            all.add(item);
        } else {
            
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId().equals(item.getId())) {
                    all.set(i, item);
                    break;
                }
            }
        }
        writeAll(all); 
    }

    
    public void delete(int id) {
        List<MenuItem> all = findAll();
        boolean removed = all.removeIf(m -> m.getId().equals(id));
        if (removed) {
            writeAll(all);
        }
    }

    
    private void writeAll(List<MenuItem> list) {
        try {
            File f = new File(filePath);
            if (f.getParentFile() != null) f.getParentFile().mkdirs(); 

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
                for (MenuItem m : list) {
                    // id|imageFile|name|isActive|category|premium
                    String line = String.format("%d|%s|%s|%b|%s|%b",
                            m.getId(),
                            (m.getImageUrl() == null ? "" : m.getImageUrl()),
                            m.getName(),
                            m.isActive(),
                            (m.getCategory() == null ? "General" : m.getCategory()),
                            m.isPremium()
                    );
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private MenuItem parseLine(String line) {
        try {
            // id|imageFile|name|isActive|category|premium (premium is optional for legacy rows)
            String[] p = line.split("\\|", -1);
            if (p.length < 5) return null;

            MenuItem it = new MenuItem();
            it.setId(Integer.parseInt(p[0].trim()));
            it.setImageUrl(p[1].trim());
            it.setName(p[2].trim());
            it.setActive(Boolean.parseBoolean(p[3].trim()));
            it.setCategory(p[4].trim());
            it.setPremium(p.length > 5 ? Boolean.parseBoolean(p[5].trim()) : false);
            return it;
        } catch (Exception e) {
            return null;
        }
    }
    public List<MenuItem> searchActiveAndSort(String keyword, String sortMode) {
        List<MenuItem> all = findAll();
        List<MenuItem> result = new ArrayList<>();
        
        String key = (keyword == null) ? "" : keyword.toLowerCase().trim();
        
   
        for (MenuItem item : all) {
            if (item.isActive()) {
                if (key.isEmpty() || item.getName().toLowerCase().contains(key)) {
                    result.add(item);
                }
            }
        }

        //Sort: เรียงตามชื่อ
        if ("name_desc".equals(sortMode)) {
            result.sort((m1, m2) -> m2.getName().compareToIgnoreCase(m1.getName())); // Z-A
        } else {
            result.sort((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName())); // A-Z (Default)
        }
        
        return result;
    }

    
    public List<MenuItem> searchActiveAndSort(String keyword, String sortMode, String packageType) {
        List<MenuItem> all = findAll();
        List<MenuItem> result = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.toLowerCase().trim();
        boolean premiumAllowed = "PREMIUM".equalsIgnoreCase(packageType);

        for (MenuItem item : all) {
            if (item.isActive()) {
                if ((key.isEmpty() || item.getName().toLowerCase().contains(key)) && (premiumAllowed || !item.isPremium())) {
                    result.add(item);
                }
            }
        }

        if ("name_desc".equals(sortMode)) {
            result.sort((m1, m2) -> m2.getName().compareToIgnoreCase(m1.getName()));
        } else {
            result.sort((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
        }

        return result;
    }
}