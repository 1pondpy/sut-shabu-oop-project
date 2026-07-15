package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.entity.Order;
import com.example.demo.entity.TableSession;

public class OrderRepository {
    private static final String FILE_PATH = "data/orders.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private List<Order> readAll() {
        List<Order> out = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return out;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                Order o = parseLine(line);
                if (o != null) out.add(o);
            }
        } catch (IOException e) {
            
        }
        return out;
    }

    private Order parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 4) return null;
            Order o = new Order();
            o.setId(Long.parseLong(p[0].trim()));
            String sessionIdStr = p[1].trim();
            if (!sessionIdStr.isEmpty()) {
                try {
                    TableSession s = new TableSession();
                    s.setId(Long.parseLong(sessionIdStr));
                    o.setSession(s);
                } catch (Exception ignored) {}
            }
            o.setOrderTime(LocalDateTime.parse(p[2].trim(), FMT));
            o.setStatus(p[3].trim());
            return o;
        } catch (Exception e) {
            return null;
        }
    }

   
    public List<Order> findBySessionOrderByOrderTimeDesc(TableSession session) {
        if (session == null || session.getId() == null) return new ArrayList<>();
        Long sessionId = session.getId();
        return readAll().stream()
                .filter(o -> o.getSession() != null && o.getSession().getId() != null && sessionId.equals(o.getSession().getId()))
                .sorted(Comparator.comparing(Order::getOrderTime).reversed())
                .collect(Collectors.toList());
    }

   
    public List<Order> findByStatus(String status) {
        if (status == null) return new ArrayList<>();
        return readAll().stream()
                .filter(o -> status.equalsIgnoreCase(o.getStatus()))
                .collect(Collectors.toList());
    }

   
    public long countByStatus(String status) {
        if (status == null) return 0;
        return readAll().stream().filter(o -> status.equalsIgnoreCase(o.getStatus())).count();
    }

    public List<Order> findByStatusAndOrderTimeBetweenOrderByOrderTimeAsc(String status, LocalDateTime start, LocalDateTime end) {
        if (status == null || start == null || end == null) return new ArrayList<>();
        return readAll().stream()
                .filter(o -> status.equalsIgnoreCase(o.getStatus()) && (o.getOrderTime() != null)
                        && (o.getOrderTime().isEqual(start) || o.getOrderTime().isAfter(start))
                        && (o.getOrderTime().isEqual(end) || o.getOrderTime().isBefore(end)))
                .sorted(Comparator.comparing(Order::getOrderTime))
                .collect(Collectors.toList());
    }

    public long countByStatusAndOrderTimeBetween(String status, LocalDateTime start, LocalDateTime end) {
        return findByStatusAndOrderTimeBetweenOrderByOrderTimeAsc(status, start, end).size();
    }


    public Order save(Order order) {
        File f = new File(FILE_PATH);
        f.getParentFile().mkdirs();
        try {
            if (!f.exists()) f.createNewFile();
        } catch (IOException ignored) {}

        if (order.getId() == null) {
            long id = nextId(f);
            order.setId(id);
            String now = order.getOrderTime() != null ? order.getOrderTime().format(FMT) : "";
            String sessionId = "";
            if (order.getSession() != null && order.getSession().getId() != null) sessionId = String.valueOf(order.getSession().getId());
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))) {
                bw.write(id + "|" + sessionId + "|" + now + "|" + (order.getStatus() != null ? order.getStatus() : ""));
                bw.newLine();
            } catch (IOException ignored) {}
            return order;
        } else {
  
            List<String> lines = new ArrayList<>();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) lines.add(line);
            } catch (IOException ignored) {}

                String newLine = order.getId() + "|" + (order.getSession() != null && order.getSession().getId() != null ? order.getSession().getId() : "")
                    + "|" + (order.getOrderTime() != null ? order.getOrderTime().format(FMT) : "")
                    + "|" + (order.getStatus() != null ? order.getStatus() : "");

            boolean changed = false;
            for (int i = 0; i < lines.size(); i++) {
                String[] p = lines.get(i).split("\\|", -1);
                if (p.length > 0) {
                    try {
                        long id = Long.parseLong(p[0].trim());
                        if (id == order.getId()) { lines.set(i, newLine); changed = true; break; }
                    } catch (Exception ignored) {}
                }
            }

            if (changed) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))) {
                    for (String L : lines) { bw.write(L); bw.newLine(); }
                } catch (IOException ignored) {}
            }
            return order;
        }
    }

    public Optional<Order> findById(Long id) {
        if (id == null) return Optional.empty();
        for (Order o : readAll()) if (id.equals(o.getId())) return Optional.of(o);
        return Optional.empty();
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
