package com.example.demo.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/call-staff/send")
public class SendStaffRequestServlet extends HttpServlet {
    private static final String FILE = "data/staff_requests.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String body = new java.io.BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8)).lines().reduce("", (a,b)->a+b);

        
        String tableNo = extract(body, "tableNo");
        String type = extract(body, "type");
        if (tableNo == null || type == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = resp.getWriter()) { out.println("{\"error\":\"missing fields\"}"); }
            return;
        }

        File f = new File(FILE);
        f.getParentFile().mkdirs();
        if (!f.exists()) f.createNewFile();

        long nextId = 1;
        try (java.io.BufferedReader r = new java.io.BufferedReader(new InputStreamReader(new java.io.FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length > 0) {
                    try { long id = Long.parseLong(p[0].trim()); if (id >= nextId) nextId = id + 1; } catch(Exception ignored) {}
                }
            }
        } catch (IOException ignored) {}

        String ts = LocalDateTime.now().format(FMT);
        String safeType = type.replaceAll("[\\r\\n|]", " ");

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true), StandardCharsets.UTF_8))) {
            bw.write(nextId + "|" + tableNo + "|" + safeType + "|" + ts + "|PENDING");
            bw.newLine();
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        try (PrintWriter out = resp.getWriter()) { out.println("{\"ok\":true}"); }
    }

    private String extract(String body, String key) {
        if (body == null) return null;
        String quoteKey = "\"" + key + "\"";
        int idx = body.indexOf(quoteKey);
        if (idx < 0) return null;
        int colon = body.indexOf(':', idx + quoteKey.length());
        if (colon < 0) return null;
        int firstQuote = body.indexOf('"', colon);
        if (firstQuote < 0) return null;
        int end = body.indexOf('"', firstQuote + 1);
        if (end < 0) return null;
        return body.substring(firstQuote + 1, end);
    }
}
