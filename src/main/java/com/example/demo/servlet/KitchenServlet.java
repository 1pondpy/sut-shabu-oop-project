package com.example.demo.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;

public class KitchenServlet extends HttpServlet {
    private OrderRepository orderRepo = new OrderRepository();
    private OrderItemRepository orderItemRepo = new OrderItemRepository();
    private MenuItemRepository menuRepo = new MenuItemRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tab = req.getParameter("tab");
        if (tab == null || tab.isEmpty()) tab = "PENDING";

       
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay().minusNanos(1);

        long waitingCount = orderRepo.countByStatusAndOrderTimeBetween("PENDING", start, end);
        long finishedCount = orderRepo.countByStatusAndOrderTimeBetween("FINISHED", start, end);

        List<Order> orders = orderRepo.findByStatusAndOrderTimeBetweenOrderByOrderTimeAsc(tab, start, end);

    
        Map<Integer, MenuItem> menuMap = new HashMap<>();
        for (MenuItem m : menuRepo.findAll()) menuMap.put(m.getId(), m);

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

        req.setAttribute("orders", orders);
        req.setAttribute("currentTab", tab);
        req.setAttribute("waitingCount", waitingCount);
        req.setAttribute("finishedCount", finishedCount);

        RequestDispatcher rd = req.getRequestDispatcher("/kitchen.jsp");
        rd.forward(req, resp);
    }
}
