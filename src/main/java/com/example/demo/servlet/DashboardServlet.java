package com.example.demo.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Member;
import com.example.demo.entity.MenuItem;
import com.example.demo.entity.RedemptionHistory;
import com.example.demo.entity.Tables;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.RedemptionHistoryRepository;
import com.example.demo.repository.StaffRequestRepository;
import com.example.demo.repository.TablesRepository;

public class DashboardServlet extends HttpServlet {

    private OrderRepository orderRepo = new OrderRepository();
    private OrderItemRepository orderItemRepo = new OrderItemRepository();
    private TablesRepository tablesRepo = new TablesRepository();
    private MemberRepository memberRepo = new MemberRepository();
    private MenuItemRepository menuItemRepo = new MenuItemRepository();
    private RedemptionHistoryRepository redemptionRepo = new RedemptionHistoryRepository();
    private StaffRequestRepository staffRequestRepo = new StaffRequestRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // สถิติรายได้ 
        double todayRevenue = calculateTodayRevenue();
        double monthRevenue = calculateMonthRevenue();
        double lastMonthRevenue = calculateLastMonthRevenue();
        double revenueGrowth = lastMonthRevenue > 0 ? ((monthRevenue - lastMonthRevenue) / lastMonthRevenue * 100) : 0;

        req.setAttribute("todayRevenue", String.format("%.2f", todayRevenue));
        req.setAttribute("monthRevenue", String.format("%.2f", monthRevenue));
        req.setAttribute("revenueGrowth", revenueGrowth); 
        req.setAttribute("revenueGrowthText", String.format("%.1f", revenueGrowth)); // สำหรับแสดงผล

        //สถิติออเดอร์ 
        long todayOrders = countTodayOrders();
        long pendingOrders = orderRepo.countByStatus("PENDING");
        long completedOrders = orderRepo.countByStatus("COMPLETED");
        double avgOrderValue = todayOrders > 0 ? todayRevenue / todayOrders : 0;

        req.setAttribute("todayOrders", todayOrders);
        req.setAttribute("pendingOrders", pendingOrders);
        req.setAttribute("completedOrders", completedOrders);
        req.setAttribute("avgOrderValue", String.format("%.2f", avgOrderValue));

        // สถานะโต๊ะ=
        List<Tables> allTables = tablesRepo.findByZone(null);
        int totalTables = allTables.size();
        int occupiedTables = countOccupiedTables();
        int availableTables = totalTables - occupiedTables;
        double occupancyRate = totalTables > 0 ? (occupiedTables * 100.0 / totalTables) : 0;

        req.setAttribute("totalTables", totalTables);
        req.setAttribute("occupiedTables", occupiedTables);
        req.setAttribute("availableTables", availableTables);
        req.setAttribute("occupancyRate", String.format("%.1f", occupancyRate));

        // สมาชิก 
        List<Member> allMembers = memberRepo.findAll();
        int totalMembers = allMembers.size();

        req.setAttribute("totalMembers", totalMembers);
        req.setAttribute("newMembers", 0); // ไม่มีข้อมูล joinDate ใน Member entity

        //เมนูยอดนิยม 
        List<Map<String, Object>> topMenus = getTopSellingMenus(5);
        req.setAttribute("topMenus", topMenus);

        //พนักงาน 
        int totalStaff = countTotalStaff();
        long pendingRequests = staffRequestRepo.findByStatusOrderByRequestTimeAsc("PENDING").size();

        req.setAttribute("totalStaff", totalStaff);
        req.setAttribute("pendingRequests", pendingRequests);

        // Rewards 
        int todayRedemptions = countTodayRedemptions();
        List<Map<String, Object>> topRewards = getTopRewards(3);
        req.setAttribute("todayRedemptions", todayRedemptions);
        req.setAttribute("topRewards", topRewards);

        
        Map<String, Double> paymentBreakdown = getPaymentMethodBreakdown();
        req.setAttribute("paymentBreakdown", paymentBreakdown);

        req.getRequestDispatcher("/manager-dashboard.jsp").forward(req, resp);
    }



    private double calculateTodayRevenue() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        return calculateRevenueFromPayments(startOfDay, endOfDay);
    }

    private double calculateMonthRevenue() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.plusMonths(1).withDayOfMonth(1).atStartOfDay();
        return calculateRevenueFromPayments(startOfMonth, endOfMonth);
    }

    private double calculateLastMonthRevenue() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfLastMonth = today.minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfLastMonth = today.withDayOfMonth(1).atStartOfDay();
        return calculateRevenueFromPayments(startOfLastMonth, endOfLastMonth);
    }

    private double calculateRevenueFromPayments(LocalDateTime start, LocalDateTime end) {
        double total = 0.0;
        File f = new File("data/payments.txt");
        if (!f.exists()) return total;
        
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 11) {
                    try {
                        LocalDateTime paymentTime = LocalDateTime.parse(p[10].trim(), fmt);
                        if ((paymentTime.isEqual(start) || paymentTime.isAfter(start)) && paymentTime.isBefore(end)) {
                            double netAmount = Double.parseDouble(p[6].trim());
                            total += netAmount;
                        }
                    } catch (Exception ignored) {}
                }
            }
        } catch (IOException ignored) {}
        return total;
    }

    private long countTodayOrders() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        
        return orderRepo.countByStatusAndOrderTimeBetween("COMPLETED", start, end);
    }

    private int countOccupiedTables() {
        File f = new File("data/table_sessions.txt");
        if (!f.exists()) return 0;

       
        Map<String, String[]> latestByTable = new HashMap<>(); 
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                //ID|Table|Name|Phone|Pax|Start|Status|Soup|End|Total|Rewards
                if (p.length >= 7) {
                    String tableNo = p[1].trim();
                    String status = p[6].trim();
                    String endStr = (p.length > 8) ? p[8].trim() : "";
                    latestByTable.put(tableNo, new String[]{status, endStr});
                }
            }
        } catch (IOException ignored) {}

        int count = 0;
        for (Map.Entry<String, String[]> e : latestByTable.entrySet()) {
            String status = e.getValue()[0];
            if ("ACTIVE".equalsIgnoreCase(status)) {
                count++;
            }
        }
        return count;
    }

    private List<Map<String, Object>> getTopSellingMenus(int limit) {
        
        Map<Integer, Integer> menuCount = new HashMap<>();
        
        File f = new File("data/order_items.txt");
        if (!f.exists()) return new ArrayList<>();
        
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 4) {
                    try {
                        int menuId = Integer.parseInt(p[2].trim());
                        int qty = Integer.parseInt(p[3].trim());
                        menuCount.put(menuId, menuCount.getOrDefault(menuId, 0) + qty);
                    } catch (Exception ignored) {}
                }
            }
        } catch (IOException ignored) {}
        
       
        return menuCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    int menuId = entry.getKey();
                    int count = entry.getValue();
                    
                 
                    MenuItem menuItem = menuItemRepo.findById(menuId);
                    String menuName = (menuItem != null) ? menuItem.getName() : "Unknown";
                    
                    item.put("name", menuName);
                    item.put("count", count);
                    return item;
                })
                .collect(Collectors.toList());
    }

    private int countTotalStaff() {
        File f = new File("data/staff.txt");
        if (!f.exists()) return 0;
        
        int count = 0;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (!line.trim().isEmpty()) count++;
            }
        } catch (IOException ignored) {}
        return count;
    }

    private int countTodayRedemptions() {
        LocalDate today = LocalDate.now();
        List<RedemptionHistory> all = redemptionRepo.findAll();
        
        int count = 0;
        for (RedemptionHistory h : all) {
            if (h.getRedeemDate() != null && h.getRedeemDate().toLocalDate().equals(today)) {
                count++;
            }
        }
        return count;
    }

    private List<Map<String, Object>> getTopRewards(int limit) {
        List<RedemptionHistory> all = redemptionRepo.findAll();
        Map<String, Integer> rewardCount = new HashMap<>();
        
        for (RedemptionHistory h : all) {
            String name = h.getRewardName();
            rewardCount.put(name, rewardCount.getOrDefault(name, 0) + 1);
        }
        
        return rewardCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", entry.getKey());
                    item.put("count", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }

    private Map<String, Double> getPaymentMethodBreakdown() {
        Map<String, Double> breakdown = new HashMap<>();
        breakdown.put("เงินสด", 0.0);
        breakdown.put("บัตร", 0.0);
        breakdown.put("QR Code", 0.0);
        
        File f = new File("data/payments.txt");
        if (!f.exists()) return breakdown;
        
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 8) {
                    try {
                        String method = p[7].trim();
                        double amount = Double.parseDouble(p[6].trim());
                        breakdown.put(method, breakdown.getOrDefault(method, 0.0) + amount);
                    } catch (Exception ignored) {}
                }
            }
        } catch (IOException ignored) {}
        
        return breakdown;
    }
}