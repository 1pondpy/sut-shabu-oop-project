package com.example.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;      

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Member;
import com.example.demo.entity.RedemptionHistory;
import com.example.demo.entity.Reward;
import com.example.demo.entity.TableSession;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RedemptionHistoryRepository;
import com.example.demo.repository.RewardRepository;
import com.example.demo.repository.TableSessionRepository;

@WebServlet("/api/rewards/redeem")
public class RedeemRewardServlet extends HttpServlet {
    
    private MemberRepository memberRepo = new MemberRepository();
    private RewardRepository rewardRepo = new RewardRepository();
    private RedemptionHistoryRepository historyRepo = new RedemptionHistoryRepository();
    private TableSessionRepository sessionRepo = new TableSessionRepository(); 

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            String phone = req.getParameter("phone");
            String rIdStr = req.getParameter("rewardId");
            String tableNo = req.getParameter("tableNo"); 

            if (phone == null || rIdStr == null) {
                error(resp, "Missing params");
                return;
            }

            Integer rewardId = Integer.parseInt(rIdStr);
            Member member = memberRepo.findByPhoneNumber(phone);
            Reward reward = rewardRepo.findById(rewardId);

            if (member == null) { error(resp, "ไม่พบสมาชิก"); return; }
            if (reward == null) { error(resp, "ไม่พบของรางวัล"); return; }
            if (member.getPoints() < reward.getPointsRequired()) {
                error(resp, "แต้มไม่พอ");
                return;
            }

           
            member.setPoints(member.getPoints() - reward.getPointsRequired());
            memberRepo.save(member);

            RedemptionHistory history = new RedemptionHistory();
            history.setMemberPhone(member.getPhoneNumber());
            history.setRewardName(reward.getName());
            history.setPointsUsed(reward.getPointsRequired());
            history.setRedeemDate(LocalDateTime.now());
            historyRepo.save(history);

            
            double totalPrice = 0.0;
            double totalDiscount = 0.0;
            double netPrice = 0.0;

            if (tableNo != null && !tableNo.isEmpty()) {
                TableSession session = sessionRepo.findTopByTableNoAndStatus(tableNo, "ACTIVE");
                if (session != null) {
                 
                    String currentIds = session.getAppliedRewardIds();
                    if (currentIds == null || currentIds.trim().isEmpty()) {
                        currentIds = String.valueOf(reward.getId());
                    } else {
                        currentIds = currentIds + "," + reward.getId();
                    }
                    session.setAppliedRewardIds(currentIds);
                    sessionRepo.save(session);

            
                    int pax = session.getPax() != null ? session.getPax() : 1;
                    if (session.getTotalAmount() != null && session.getTotalAmount() > 0) {
                        totalPrice = session.getTotalAmount();
                    } else {
                        com.example.demo.buffet.BuffetConfigRepository cfgRepo = new com.example.demo.buffet.BuffetConfigRepository();
                        com.example.demo.buffet.BuffetConfig cfg = cfgRepo.read();
                        int unit = cfg.getStandardPrice();
                        if (session.getPackageType() != null && session.getPackageType().equalsIgnoreCase("PREMIUM")) {
                            unit = cfg.getPremiumPrice();
                        }
                        totalPrice = pax * (double) unit;
                    }

                    double currentPrice = totalPrice;
            
                    List<Reward> usedRewards = new ArrayList<>();
                    String[] ids = currentIds.split(",");
                    for (String idStr : ids) {
                        try {
                            Integer rId = Integer.parseInt(idStr.trim());
                            Reward r = rewardRepo.findById(rId);
                            if (r != null) usedRewards.add(r);
                        } catch (Exception e) {}
                    }

                    for (Reward r : usedRewards) {
                        if ("BAHT".equalsIgnoreCase(r.getDiscountType())) {
                            double val = r.getDiscountValue();
                            if (val > currentPrice) val = currentPrice;
                            currentPrice -= val;
                        }
                    }

                    for (Reward r : usedRewards) {
                        if ("PERCENT".equalsIgnoreCase(r.getDiscountType())) {
                            double val = currentPrice * (r.getDiscountValue() / 100.0);
                            val = roundPercent(val);
                            currentPrice -= val;
                        }
                    }

                    if (currentPrice < 0) currentPrice = 0;
                    double roundedNet = roundNet(currentPrice);
                    netPrice = roundedNet;
                    totalDiscount = totalPrice - roundedNet;
                }
            }

            try (PrintWriter out = resp.getWriter()) {
                out.print("{");
                out.print("\"ok\":true,");
                out.print("\"remainingPoints\":" + member.getPoints() + ",");
                out.print("\"newTotalPrice\":" + totalPrice + ",");
                out.print("\"newDiscount\":" + totalDiscount + ",");
                out.print("\"newNetPrice\":" + netPrice);
                out.print("}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            error(resp, "Server Error: " + e.getMessage());
        }
    }

    private void error(HttpServletResponse resp, String msg) throws IOException {
        try (PrintWriter out = resp.getWriter()) { 
            String safeMsg = msg.replace("\"", "'");
            out.println("{\"ok\":false, \"error\":\"" + safeMsg + "\"}"); 
        }
    }

    private double roundNet(double amount) {
        return Math.round(amount);
    }

    private double roundPercent(double value) {
        double floor = Math.floor(value);
        double frac = value - floor;
        if (frac == 0.0) return value;
        return (frac >= 0.5) ? (floor + 1.0) : floor;
    }
}