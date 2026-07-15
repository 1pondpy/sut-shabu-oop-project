package com.example.demo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Member;
import com.example.demo.entity.TableSession;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.TableSessionRepository;

public class OpenTableServlet extends HttpServlet {
    private TableSessionRepository sessionRepo = new TableSessionRepository();
    private MemberRepository memberRepo = new MemberRepository();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = req.getReader()) {
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
        }
        String body = sb.toString();
        String tableNo = extract(body, "tableNo");
        String customerName = extract(body, "customerName");
        String customerPhone = extract(body, "customerPhone");
        String paxStr = extract(body, "pax");
        String soup = extract(body, "soup");
        String packageType = extract(body, "packageType");

        if (tableNo == null || tableNo.isEmpty()) {
            resp.setStatus(400);
            resp.getWriter().write("missing tableNo");
            return;
        }

        TableSession s = new TableSession();
        s.setTableNo(tableNo);
        s.setCustomerName(customerName != null && !customerName.isEmpty() ? customerName : "ลูกค้าทั่วไป");
        s.setCustomerPhone(customerPhone != null ? customerPhone : "");
        try { s.setPax(paxStr != null && !paxStr.isEmpty() ? Integer.parseInt(paxStr) : 1); } catch (Exception e) { s.setPax(1); }
        s.setSoup(soup != null ? soup : "");
        s.setPackageType(packageType != null && !packageType.isEmpty() ? packageType : "STANDARD");
        try {
            com.example.demo.buffet.BuffetPackage pkg = com.example.demo.buffet.BuffetFactory.of(s.getPackageType());
            int unit = pkg.getPricePerPerson();
            int pax = s.getPax() == null ? 1 : s.getPax();
            s.setTotalAmount((double) (unit * pax));
        } catch (Exception e) {
            int pax = s.getPax() == null ? 1 : s.getPax();
            com.example.demo.buffet.BuffetConfigRepository cfgRepo = new com.example.demo.buffet.BuffetConfigRepository();
            com.example.demo.buffet.BuffetConfig cfg = cfgRepo.read();
            int unit = cfg.getStandardPrice();
            if (s.getPackageType() != null && s.getPackageType().equalsIgnoreCase("PREMIUM")) unit = cfg.getPremiumPrice();
            s.setTotalAmount((double) unit * pax);
        }
        s.setStartTime(LocalDateTime.now());
        s.setStatus("ACTIVE");

        sessionRepo.save(s);
        try {
            String phone = s.getCustomerPhone() != null ? s.getCustomerPhone().trim() : null;
            if (phone != null && !phone.isEmpty()) {
                Member m = memberRepo.findByPhoneNumber(phone);
                if (m != null) {
                    Integer gc = m.getGameChances() != null ? m.getGameChances() : 0;
                    m.setGameChances(gc + 1);
                    memberRepo.save(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            com.example.demo.buffet.BuffetPackage pkg = com.example.demo.buffet.BuffetFactory.of(s.getPackageType());
            int unit = pkg.getPricePerPerson();
            double total = s.getTotalAmount() != null ? s.getTotalAmount() : (double) (unit * (s.getPax() == null ? 1 : s.getPax()));

            resp.setStatus(200);
            resp.setContentType("application/json; charset=UTF-8");
            String json = String.format("{\"ok\":true,\"packageType\":\"%s\",\"unitPrice\":%d,\"totalAmount\":%.2f}",
                    s.getPackageType(), unit, total);
            resp.getWriter().write(json);
        } catch (Exception ex) {
            resp.setStatus(200);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write("{\"ok\":true,\"packageType\":\"" + s.getPackageType() + "\"}");
        }
    }

    private String extract(String json, String key) {
        if (json == null) return null;
        String q = '"' + key + '"';
        int i = json.indexOf(q);
        if (i < 0) return null;
        int colon = json.indexOf(':', i + q.length());
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        if (start >= json.length()) return null;
        char c = json.charAt(start);
        if (c == '"') {
            int end = json.indexOf('"', start + 1);
            if (end < 0) return null;
            return json.substring(start + 1, end);
        } else {
          
            int end = start;
            while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
            return json.substring(start, end);
        }
    }
}
