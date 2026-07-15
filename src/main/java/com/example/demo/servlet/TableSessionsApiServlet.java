package com.example.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.TableSession;
import com.example.demo.repository.TableSessionRepository;

public class TableSessionsApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final TableSessionRepository repo = new TableSessionRepository();
    private static final long LIMIT_SECONDS = 2 * 60 * 60; 

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        List<TableSession> sessions = repo.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (TableSession s : sessions) {
            if (!first) sb.append(','); first = false;
            sb.append('{');
            sb.append("\"tableNo\":\"").append(escape(s.getTableNo())).append("\"");
            sb.append(",\"status\":\"").append(escape(s.getStatus())).append("\"");
            sb.append(",\"pax\":").append(s.getPax());
            String start = s.getStartTime() == null ? "" : s.getStartTime().format(fmt);
            sb.append(",\"startTime\":\"").append(escape(start)).append("\"");

            long remaining = Long.MIN_VALUE;
            if (s.getStartTime() != null) {
                LocalDateTime now = LocalDateTime.now();
                Duration elapsed = Duration.between(s.getStartTime(), now);
                remaining = LIMIT_SECONDS - elapsed.getSeconds();
            }
            sb.append(",\"remainingSeconds\":").append(remaining == Long.MIN_VALUE ? "null" : String.valueOf(remaining));

            sb.append(",\"packageType\":\"").append(escape(s.getPackageType())).append("\"");
            sb.append(",\"totalAmount\":").append(s.getTotalAmount() == null ? 0 : String.format("%.2f", s.getTotalAmount()));
            sb.append('}');
        }
        sb.append("]");

        try (PrintWriter out = resp.getWriter()) {
            out.println(sb.toString());
        }
    }

    private String escape(String s) { if (s == null) return ""; return s.replace("\\", "\\\\").replace("\"", "\\\""); }
}
