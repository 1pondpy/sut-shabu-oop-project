package com.example.demo.servlet;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.MenuItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.TableSession;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.TableSessionRepository;

public class MyOrdersServlet extends HttpServlet {
    private OrderRepository orderRepo = new OrderRepository();
    private TableSessionRepository sessionRepo = new TableSessionRepository();
    private OrderItemRepository orderItemRepo = new OrderItemRepository();
    private MenuItemRepository menuRepo = new MenuItemRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableNo = req.getParameter("tableNo");
        if (tableNo == null || tableNo.isEmpty()) tableNo = "A1";
        req.setAttribute("tableNo", tableNo);

        TableSession activeSession = sessionRepo.findTopByTableNoAndStatus(tableNo, "ACTIVE");
        boolean isValidSession = false;
        String warningMsg = "";

        if (activeSession != null) {
            String name = activeSession.getCustomerName();
            Integer pax = activeSession.getPax();
            if (name != null && !name.trim().equalsIgnoreCase("null") && !name.isEmpty() && 
                pax != null && pax > 0) {
                isValidSession = true;
            } else {
                warningMsg = "ข้อมูลโต๊ะไม่สมบูรณ์";
            }
        } else {
            warningMsg = "โต๊ะนี้ยังไม่ได้เปิดให้บริการ";
        }

        req.setAttribute("canView", isValidSession); 
        req.setAttribute("canOrder", isValidSession);
        req.setAttribute("warningMsg", warningMsg);


        long remainingSeconds = 0;
        int pax = 0;
        String packageName = "";
        List<Order> orders = null; 

        if (isValidSession) {
            if (activeSession.getStartTime() != null) {
                long limitSeconds = 2 * 60 * 60;
                long elapsed = Duration.between(activeSession.getStartTime(), LocalDateTime.now()).getSeconds();
                remainingSeconds = limitSeconds - elapsed;
                if (remainingSeconds < 0) remainingSeconds = 0;
            }
            if (activeSession.getPax() != null) pax = activeSession.getPax();
            packageName = activeSession.getPackageType() != null ? activeSession.getPackageType() : "Standard";
            orders = orderRepo.findBySessionOrderByOrderTimeDesc(activeSession);
            Map<Integer, MenuItem> menuMap = new HashMap<>();
            for (MenuItem m : menuRepo.findAll()) menuMap.put(m.getId(), m);

            if (orders != null) {
                for (Order o : orders) {
                    if (o.getId() == null) continue;
                    List<OrderItem> items = orderItemRepo.findByOrderId(o.getId());
                    for (OrderItem it : items) {
                        if (it.getMenuItem() != null && it.getMenuItem().getId() != null) {
                            MenuItem full = menuMap.get(it.getMenuItem().getId());
                            if (full != null) it.setMenuItem(full);
                        }
                    }
                    o.setOrderItems(items);
                }
            }
        } else {
            orders = new ArrayList<>();
        }

        req.setAttribute("orders", orders);
        req.setAttribute("remainingSeconds", remainingSeconds);
        req.setAttribute("pax", pax);
        req.setAttribute("packageName", packageName);
        req.setAttribute("totalOrders", orders != null ? orders.size() : 0);

        RequestDispatcher rd = req.getRequestDispatcher("/my-orders.jsp");
        rd.forward(req, resp);
    }
}