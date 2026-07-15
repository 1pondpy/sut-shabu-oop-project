package com.example.demo.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;

public class KitchenFinishServlet extends HttpServlet {
    private OrderRepository orderRepo = new OrderRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        long orderId = -1;
        try {
            Pattern p = Pattern.compile("\\\"orderId\\\"\\s*:\\s*(\\d+)");
            Matcher m = p.matcher(body);
            if (m.find()) orderId = Long.parseLong(m.group(1));
        } catch (Exception ignored) {}

        if (orderId > 0) {
            try {
                java.util.Optional<Order> opt = orderRepo.findById(orderId);
                if (opt.isPresent()) {
                    Order o = opt.get();
                    o.setStatus("FINISHED");
                    orderRepo.save(o);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("OK");
                    return;
                }
            } catch (Exception ignored) {}
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("ERROR");
    }
}
