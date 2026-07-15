package com.example.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.buffet.BuffetConfig;
import com.example.demo.buffet.BuffetConfigRepository;

@WebServlet("/manager-buffet-config")
public class BuffetConfigServlet extends HttpServlet {
    private BuffetConfigRepository repo = new BuffetConfigRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String sStandard = req.getParameter("standardPrice");
        String sPremium = req.getParameter("premiumPrice");

        BuffetConfig cfg = repo.read();
        try {
            if (sStandard != null && !sStandard.isEmpty()) cfg.setStandardPrice(Integer.parseInt(sStandard));
        } catch (Exception e) { /* ignore */ }
        try {
            if (sPremium != null && !sPremium.isEmpty()) cfg.setPremiumPrice(Integer.parseInt(sPremium));
        } catch (Exception e) { /* ignore */ }

        repo.save(cfg);

        resp.sendRedirect("manager-menu");
    }
}
