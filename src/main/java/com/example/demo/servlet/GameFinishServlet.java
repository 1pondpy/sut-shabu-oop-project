package com.example.demo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;

public class GameFinishServlet extends HttpServlet {

    private final MemberRepository memberRepo = new MemberRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String body = readBody(req);
        String token = extractJson(body, "token");
        String scoreStr = extractNumber(body, "score");
        int score = 0;
        try { score = Integer.parseInt(scoreStr); } catch (Exception ignored) {}

        GameSessionStore.Entry e = GameSessionStore.get(token);
        if (e == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try (PrintWriter out = resp.getWriter()) { out.write("{\"error\":\"invalid_token\"}"); }
            return;
        }

       
        int pointsEarned = Math.max(0, score);

        Member m = memberRepo.findByPhoneNumber(e.phone);
        if (m != null && pointsEarned > 0) {
            int current = m.getPoints() == null ? 0 : m.getPoints();
            m.setPoints(current + pointsEarned);
            memberRepo.save(m);
        }

        GameSessionStore.remove(token); 

        try (PrintWriter out = resp.getWriter()) {
            out.write("{\"ok\":true,\"pointsEarned\":" + pointsEarned + "}");
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
            if (m.find()) return m.group(1);
        } catch (Exception ignored) {}
        return null;
    }
    private static String extractNumber(String json, String key) {
        if (json == null) return null;
        try {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\\"" + java.util.regex.Pattern.quote(key) + "\\\"\\s*:\\s*([0-9]+)");
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) return m.group(1);
        } catch (Exception ignored) {}
        return null;
    }
}