package com.example.demo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Member;
import com.example.demo.entity.TableSession;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.TableSessionRepository;

public class GameStartServlet extends HttpServlet {

    private final TableSessionRepository sessionRepo = new TableSessionRepository();
    private final MemberRepository memberRepo = new MemberRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String body = readBody(req);
        String phone = extractJson(body, "phone");

        String tableNo = req.getParameter("tableNo");
        if (tableNo == null || tableNo.isEmpty()) tableNo = req.getParameter("table");
        if (tableNo == null || tableNo.isEmpty()) tableNo = "A1";

        TableSession session = sessionRepo.findTopByTableNoAndStatus(tableNo, "ACTIVE");
        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try (PrintWriter out = resp.getWriter()) { out.write("{\"error\":\"no_active_table\"}"); }
            return;
        }

        String phoneFromTable = session.getCustomerPhone();
        if (phoneFromTable == null || phoneFromTable.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try (PrintWriter out = resp.getWriter()) { out.write("{\"error\":\"member_required\"}"); }
            return;
        }

        
        if (phone != null && !phone.trim().isEmpty()) {
            String a = phone.replaceAll("\\D", "");
            String b = phoneFromTable.replaceAll("\\D", "");
            if (!(a.equals(b) || a.endsWith(b) || b.endsWith(a))) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                try (PrintWriter out = resp.getWriter()) { out.write("{\"error\":\"mismatch_phone\"}"); }
                return;
            }
        }

        Member m = memberRepo.findByPhoneNumber(phoneFromTable);
        if (m == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try (PrintWriter out = resp.getWriter()) { out.write("{\"error\":\"member_not_found\"}"); }
            return;
        }

        int chances = m.getGameChances() == null ? 0 : m.getGameChances();
        if (chances <= 0) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try (PrintWriter out = resp.getWriter()) { out.write("{\"error\":\"no_chance\"}"); }
            return;
        }

     
        m.setGameChances(chances - 1);
        memberRepo.save(m);

        long seed = new Random().nextInt(1_000_000); 
        GameSessionStore.Entry e = GameSessionStore.create(m.getPhoneNumber(), seed, 5 * 60); // 5 minutes

        try (PrintWriter out = resp.getWriter()) {
            out.write("{" +
                "\"token\":\"" + e.token + "\"," +
                "\"seed\":" + e.seed + "," +
                "\"expiresAt\":\"" + Instant.now().plusSeconds(5*60).toString() + "\"}" );
        }
    }

    private static String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = req.getReader()) {
            String line; while ((line = r.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }

    private static String extractJson(String json, String key) {
        if (json == null) return null;
        try {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\\"" + java.util.regex.Pattern.quote(key) + "\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"");
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception ignored) {}
        return null;
    }
}