package com.example.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.RedemptionHistory;
import com.example.demo.entity.TableSession;
import com.example.demo.repository.RedemptionHistoryRepository;
import com.example.demo.repository.TableSessionRepository;

public class RedemptionHistoryListServlet extends HttpServlet {

    private final TableSessionRepository sessionRepo = new TableSessionRepository();
    private final RedemptionHistoryRepository historyRepo = new RedemptionHistoryRepository();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String tableNo = req.getParameter("tableNo");
        if (tableNo == null || tableNo.isEmpty()) {
            tableNo = "A1"; 
        }

        TableSession session = sessionRepo.findTopByTableNoAndStatus(tableNo, "ACTIVE");
        if (session == null || session.getCustomerPhone() == null || session.getCustomerPhone().trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"error\":\"member_required\"}");
            }
            return;
        }

        final String phone = session.getCustomerPhone().trim();
        List<RedemptionHistory> all = historyRepo.findAll();
        List<RedemptionHistory> list = all.stream()
                .filter(h -> phone.equals(h.getMemberPhone()))
                .sorted(Comparator.comparing(RedemptionHistory::getRedeemDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());

        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            RedemptionHistory h = list.get(i);
            sb.append("{");
            long idVal = (h.getId() == null ? 0L : h.getId().longValue());
            sb.append("\"id\":").append(idVal).append(",");
            sb.append("\"rewardName\":\"").append(escape(h.getRewardName())).append("\",");
            int pt = (h.getPointsUsed() == null ? 0 : h.getPointsUsed().intValue());
            sb.append("\"pointsUsed\":").append(pt).append(",");
            String dateStr = h.getRedeemDate() == null ? "" : h.getRedeemDate().format(FMT);
            sb.append("\"redeemDate\":\"").append(escape(dateStr)).append("\"");
            sb.append("}");
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");

        try (PrintWriter out = resp.getWriter()) {
            out.write(sb.toString());
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
