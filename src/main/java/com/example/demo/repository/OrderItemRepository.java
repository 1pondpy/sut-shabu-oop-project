package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.OrderItem;

public class OrderItemRepository {
    private static final String FILE_PATH = "data/order_items.txt";

   
    public void saveAllForOrder(long orderId, List<OrderItem> items) {
        File f = new File(FILE_PATH);
        f.getParentFile().mkdirs();
        try {
            if (!f.exists()) f.createNewFile();
        } catch (IOException ignored) {}

        long nextId = nextId(f);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))) {
            for (OrderItem it : items) {
                Integer menuId = it.getMenuItem() != null ? it.getMenuItem().getId() : null;
                int qty = it.getQuantity() != null ? it.getQuantity() : 0;
                bw.write(nextId + "|" + orderId + "|" + (menuId != null ? menuId : "") + "|" + qty);
                bw.newLine();
                nextId++;
            }
        } catch (IOException ignored) {}
    }

   
    public List<com.example.demo.entity.OrderItem> findByOrderId(long orderId) {
        List<com.example.demo.entity.OrderItem> out = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return out;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length < 4) continue;
                try {
                    long id = Long.parseLong(p[0].trim());
                    long oId = Long.parseLong(p[1].trim());
                    if (oId != orderId) continue;
                    String menuIdStr = p[2].trim();
                    String qtyStr = p[3].trim();
                    com.example.demo.entity.OrderItem it = new com.example.demo.entity.OrderItem();
                    it.setId(id);
                    int qty = 0;
                    try { qty = Integer.parseInt(qtyStr); } catch (Exception ignored) {}
                    it.setQuantity(qty);
                    if (!menuIdStr.isEmpty()) {
                        try {
                            int mid = Integer.parseInt(menuIdStr);
                            com.example.demo.entity.MenuItem m = new com.example.demo.entity.MenuItem();
                            m.setId(mid);
                            it.setMenuItem(m);
                        } catch (Exception ignored) {}
                    }
                    out.add(it);
                } catch (Exception ignored) {}
            }
        } catch (IOException ignored) {}
        return out;
    }

    
    public void saveAll(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return;
        File f = new File(FILE_PATH);
        f.getParentFile().mkdirs();
        try {
            if (!f.exists()) f.createNewFile();
        } catch (IOException ignored) {}

        long nextId = nextId(f);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))) {
            for (OrderItem it : items) {
                Long orderId = it.getOrder() != null ? it.getOrder().getId() : null;
                if (orderId == null) continue;
                Integer menuId = it.getMenuItem() != null ? it.getMenuItem().getId() : null;
                int qty = it.getQuantity() != null ? it.getQuantity() : 0;
                bw.write(nextId + "|" + orderId + "|" + (menuId != null ? menuId : "") + "|" + qty);
                bw.newLine();
                nextId++;
            }
        } catch (IOException ignored) {}
    }

    private long nextId(File f) {
        long max = 0;
        if (!f.exists()) return 1;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length > 0) {
                    try { long id = Long.parseLong(p[0].trim()); if (id > max) max = id; } catch (Exception ignored) {}
                }
            }
        } catch (IOException ignored) {}
        return max + 1;
    }
}
