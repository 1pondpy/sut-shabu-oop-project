package com.example.demo.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderSubmitServlet extends HttpServlet {
    private static final String ORDERS_PATH = "data/orders.txt";
    private static final String ORDER_ITEMS_PATH = "data/order_items.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableNo = req.getParameter("tableNo");
        if (tableNo == null || tableNo.isEmpty()) tableNo = "A1";
        com.example.demo.repository.TableSessionRepository sessionRepo = new com.example.demo.repository.TableSessionRepository();
        com.example.demo.entity.TableSession activeSession = sessionRepo.findTopByTableNoAndStatus(tableNo, "ACTIVE");
        if (activeSession == null) {
            com.example.demo.entity.TableSession newSession = new com.example.demo.entity.TableSession();
            newSession.setTableNo(tableNo);
            newSession.setStartTime(LocalDateTime.now());
            newSession.setStatus("ACTIVE");
            sessionRepo.save(newSession);
            activeSession = newSession;
        }
        try {
            if (activeSession.getStartTime() != null) {
                long elapsed = Duration.between(activeSession.getStartTime(), LocalDateTime.now()).getSeconds();
                if (elapsed >= 2 * 60 * 60) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().write("โต๊ะนี้ครบเวลา 2 ชั่วโมงแล้ว กรุณาเรียกพนักงาน");
                    return;
                }
            }
        } catch (Exception e) { /* ignore */ }

        String body;
        try (BufferedReader r = req.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
            body = sb.toString();
        }

        List<Item> items = parseCartJson(body);
        if (items.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Empty cart");
            return;
        }

        File ordersFile = new File(ORDERS_PATH);
        ordersFile.getParentFile().mkdirs();
        if (!ordersFile.exists()) ordersFile.createNewFile();

        File itemsFile = new File(ORDER_ITEMS_PATH);
        itemsFile.getParentFile().mkdirs();
        if (!itemsFile.exists()) itemsFile.createNewFile();

        long nextOrderId = nextId(ordersFile);
        String now = LocalDateTime.now().format(FMT);

        // id|session_id|order_time|status
        String sessionIdStr = activeSession != null && activeSession.getId() != null ? String.valueOf(activeSession.getId()) : "";
        try (FileWriter fw = new FileWriter(ordersFile, true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(nextOrderId + "|" + sessionIdStr + "|" + now + "|PENDING");
            bw.newLine();
        }

        long nextOrderItemId = nextId(itemsFile);
        try (FileWriter fw = new FileWriter(itemsFile, true); BufferedWriter bw = new BufferedWriter(fw)) {
            for (Item it : items) {
                bw.write(nextOrderItemId + "|" + nextOrderId + "|" + it.id + "|" + it.qty);
                bw.newLine();
                nextOrderItemId++;
            }
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Order " + nextOrderId + " saved");
    }

    private long nextId(File f) {
        long max = 0;
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

    private static class Item {
        int id;
        int qty;

        Item() {
            this.id = 0;
            this.qty = 0;
        }
    }

 
    private List<Item> parseCartJson(String body) {
        List<Item> out = new ArrayList<>();
        if (body == null || body.trim().isEmpty()) return out;
        Pattern p = Pattern.compile("\\{[^}]*\\\"id\\\"\\s*:\\s*(\\d+)[^}]*\\\"qty\\\"\\s*:\\s*(\\d+)[^}]*\\}");
        Matcher m = p.matcher(body);
        while (m.find()) {
            try {
                Item it = new Item();
                it.id = Integer.parseInt(m.group(1));
                it.qty = Integer.parseInt(m.group(2));
                out.add(it);
            } catch (Exception e) { /* ignore malformed */ }
        }
        return out;
    }
}
