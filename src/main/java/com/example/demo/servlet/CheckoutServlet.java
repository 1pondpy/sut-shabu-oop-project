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
import com.example.demo.entity.PaymentRecord;
import com.example.demo.entity.Reward;
import com.example.demo.entity.TableSession;
import com.example.demo.payment.CashPayment;
import com.example.demo.payment.CreditCardPayment;
import com.example.demo.payment.PaymentMethod;
import com.example.demo.payment.PromptPayPayment;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.RewardRepository;
import com.example.demo.repository.TableSessionRepository;

@WebServlet("/api/orders/checkout")
public class CheckoutServlet extends HttpServlet {

    private TableSessionRepository sessionRepo = new TableSessionRepository();
    private RewardRepository rewardRepo = new RewardRepository();
    private MemberRepository memberRepo = new MemberRepository();
    private PaymentRepository paymentRepo = new PaymentRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
          
            StringBuilder sb = new StringBuilder();
            String line;
            try (java.io.BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) sb.append(line);
            }
            String json = sb.toString(); 
            
           
            String tableNo = extractJson(json, "tableNo");
            String method = extractJson(json, "method"); // "CASH", "CREDIT", "SCAN"
            String paidStr = extractJson(json, "paid");
            
            double paidAmount = 0.0;
            try { paidAmount = Double.parseDouble(paidStr); } catch (Exception e) {}

            
            TableSession session = sessionRepo.findTopByTableNoAndStatus(tableNo, "ACTIVE");
            if (session == null) { error(resp, "ไม่พบข้อมูลโต๊ะ"); return; }

        
            int pax = session.getPax() != null ? session.getPax() : 1;
            double totalPrice;
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
            String rIds = session.getAppliedRewardIds();
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

            // หักส่วนลดแบบจำนวนเงิน
            for (Reward r : usedRewards) { 
                String type = r.getDiscountType();
                if ("AMOUNT".equalsIgnoreCase(type) || "BAHT".equalsIgnoreCase(type)) {
                    double val = r.getDiscountValue();
                    if (val > currentPrice) val = currentPrice;
                    currentPrice -= val;
                }
            }
            // หักส่วนลดแบบเปอร์เซ็นต์
            for (Reward r : usedRewards) { 
                if ("PERCENT".equalsIgnoreCase(r.getDiscountType())) {
                    double val = currentPrice * (r.getDiscountValue() / 100.0);
                    val = roundPercent(val);
                    currentPrice -= val;
                }
            }
            
            if (currentPrice < 0) currentPrice = 0;
            double netAmount = roundNet(currentPrice); // ราคาสุทธิที่ต้องจ่าย
            double discountTotal = totalPrice - netAmount;
            
            
            //  Payment (Polymorphism) 
            PaymentMethod payment = null; 

            if ("CASH".equalsIgnoreCase(method)) {
                payment = new CashPayment(netAmount, paidAmount);
            } else if ("CREDIT".equalsIgnoreCase(method)) {
                String cardNo = extractJson(json, "cardNo");
                if (cardNo.isEmpty()) cardNo = "1234567812345678"; 
                payment = new CreditCardPayment(netAmount, cardNo);
            } else if ("SCAN".equalsIgnoreCase(method)) {
                payment = new PromptPayPayment(netAmount);
            } else {
                error(resp, "รูปแบบการชำระเงินไม่ถูกต้อง");
                return;
            }

            try {
                payment.process(); 
            } catch (Exception e) {
                error(resp, e.getMessage()); 
                return;
            }


            double finalReceived = payment.getAmountReceived(); 
            double change = payment.getChange();                

            // คำนวณแต้มสะสม 
            int pointsEarned = 0;
            String phone = session.getCustomerPhone();
            if (phone != null && !phone.isEmpty()) {
                Member member = memberRepo.findByPhoneNumber(phone);
                if (member != null) {
                    int[] pointConfig = rewardRepo.getPointConfig();
                    int configBaht = pointConfig[0]; 
                    int configPoints = pointConfig[1]; 

                    if (configBaht <= 0) configBaht = 100;

                    // สูตรคำนวณแต้ม
                    pointsEarned = (int) ((netAmount / configBaht) * configPoints);
                    
                    member.setPoints(member.getPoints() + pointsEarned);
                    memberRepo.save(member);
                }
            }

    
            PaymentRecord bill = new PaymentRecord();
            bill.setTableNo(tableNo);
            bill.setCustomerName(session.getCustomerName());
            bill.setMemberPhone(session.getCustomerPhone());
            bill.setTotalAmount(totalPrice);
            bill.setDiscount(discountTotal);
            bill.setNetAmount(netAmount);
            bill.setPaymentMethod(method); 
            
            
            bill.setAmountReceived(finalReceived);
            bill.setChangeAmount(change);
            
            bill.setPaymentTime(LocalDateTime.now());
            paymentRepo.save(bill);

           
            session.setTotalAmount(netAmount);
            session.setEndTime(LocalDateTime.now());
            session.setStatus("COMPLETED"); 
            sessionRepo.save(session);


            try (PrintWriter out = resp.getWriter()) {
                out.print("{");
                out.print("\"ok\":true,");
                out.print("\"pointsEarned\":" + pointsEarned);
                out.print("}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            error(resp, "Server Error: " + e.getMessage());
        }
    }

  
    private String extractJson(String json, String key) {
        try {
            String[] parts = json.split("\"" + key + "\":");
            if (parts.length > 1) {
                String val = parts[1].split(",")[0].split("}")[0];
                return val.replace("\"", "").trim();
            }
        } catch (Exception e) {}
        return "";
    }

    private void error(HttpServletResponse resp, String msg) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.print("{\"ok\":false, \"error\":\"" + msg + "\"}");
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