
package com.example.demo.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Reward;
import com.example.demo.entity.StaffRequest;
import com.example.demo.entity.TableSession;
import com.example.demo.entity.Tables; 
import com.example.demo.repository.RewardRepository;
import com.example.demo.repository.StaffRequestRepository;
import com.example.demo.repository.TableSessionRepository;
import com.example.demo.repository.TablesRepository;

public class StaffTableServlet extends HttpServlet {
    private TablesRepository tablesRepo = new TablesRepository();
    private TableSessionRepository sessionRepo = new TableSessionRepository();
    private StaffRequestRepository staffRepo = new StaffRequestRepository();
    private RewardRepository rewardRepo = new RewardRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Map<String,Object>> zoneA = buildZone("A");
        List<Map<String,Object>> zoneB = buildZone("B");

        req.setAttribute("zoneA", zoneA);
        req.setAttribute("zoneB", zoneB);
        
        List<StaffRequest> pending = staffRepo.findByStatusOrderByRequestTimeAsc("PENDING");
        req.setAttribute("notifications", pending != null ? pending : new ArrayList<>());
        req.setAttribute("rewards", rewardRepo.findAll());

        com.example.demo.buffet.BuffetConfigRepository cfgRepo = new com.example.demo.buffet.BuffetConfigRepository();
        com.example.demo.buffet.BuffetConfig cfg = cfgRepo.read();
        req.setAttribute("standardPrice", cfg.getStandardPrice());
        req.setAttribute("premiumPrice", cfg.getPremiumPrice());

        RequestDispatcher rd = req.getRequestDispatcher("/staff-tables.jsp");
        rd.forward(req, resp);
    }

    private List<Map<String,Object>> buildZone(String zone) {
        List<Map<String,Object>> out = new ArrayList<>();
        List<Tables> tables = tablesRepo.findByZone(zone);
        for (Tables t : tables) {
            Map<String,Object> m = new HashMap<>();
            m.put("number", t.getTableNo());
            m.put("zone", t.getZone());
            m.put("seatCount", t.getSeatCount());
            m.put("status", "FREE");
            
            TableSession s = sessionRepo.findTopByTableNoAndStatus(t.getTableNo(), "ACTIVE");
            if (s != null) {
                m.put("status", "BUSY");
                int pax = s.getPax() != null ? s.getPax() : 1;
                m.put("pax", pax);
                m.put("startTime", s.getStartTime() != null ? s.getStartTime().toString() : null);
               
                try {
                    long limitSeconds = 2 * 60 * 60;
                    long elapsed = 0;
                    if (s.getStartTime() != null) {
                        elapsed = java.time.Duration.between(s.getStartTime(), java.time.LocalDateTime.now()).getSeconds();
                    }
                    long remaining = Math.max(0, limitSeconds - elapsed);
                    m.put("remainingSeconds", remaining);
                } catch (Exception e) {
                    m.put("remainingSeconds", 0);
                }
                m.put("soup", s.getSoup());
                m.put("alertMsg", s.getCustomerName());
                m.put("phone", s.getCustomerPhone());


                int unitPrice = 299;
                if (s.getPackageType() != null && s.getPackageType().equalsIgnoreCase("PREMIUM")) {
                    com.example.demo.buffet.BuffetConfigRepository cfgRepo = new com.example.demo.buffet.BuffetConfigRepository();
                    com.example.demo.buffet.BuffetConfig cfg = cfgRepo.read();
                    unitPrice = cfg.getPremiumPrice();
                } else {
                    com.example.demo.buffet.BuffetConfigRepository cfgRepo = new com.example.demo.buffet.BuffetConfigRepository();
                    com.example.demo.buffet.BuffetConfig cfg = cfgRepo.read();
                    unitPrice = cfg.getStandardPrice();
                }

                double basePrice = pax * (double) unitPrice;
                double currentPrice = basePrice; 
                
                List<Reward> usedRewards = new ArrayList<>();
                String rIds = s.getAppliedRewardIds();
                if (rIds != null && !rIds.isEmpty()) {
                    String[] ids = rIds.split(",");
                    for (String idStr : ids) {
                        try {
                            Integer rId = Integer.parseInt(idStr.trim());
                            Reward r = rewardRepo.findById(rId);
                            if (r != null) usedRewards.add(r);
                        } catch (Exception e) {}
                    }
                }

                for (Reward r : usedRewards) {
                    String type = r.getDiscountType();
                    if ("BAHT".equalsIgnoreCase(type) || "AMOUNT".equalsIgnoreCase(type)) {
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
                double totalDiscount = basePrice - roundedNet;

                m.put("totalPrice", basePrice);
                m.put("discount", totalDiscount);
                m.put("netPrice", roundedNet);
                m.put("packageType", s.getPackageType());
            } else {
                m.put("pax", 0);
                m.put("startTime", null);
                m.put("soup", "");
                m.put("alertMsg", "");
                m.put("phone", "");
                m.put("packageType", "STANDARD");
                m.put("remainingSeconds", 0);
            }
            out.add(m);
        }
        return out;
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