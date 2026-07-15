package com.example.demo.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Member;
import com.example.demo.entity.TableSession;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RewardRepository;
import com.example.demo.repository.TableSessionRepository;

public class RewardServlet extends HttpServlet {
    private TableSessionRepository sessionRepo = new TableSessionRepository();
    private MemberRepository memberRepo = new MemberRepository();
    private RewardRepository rewardRepo = new RewardRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableNo = req.getParameter("tableNo");
        if (tableNo == null || tableNo.isEmpty()) tableNo = "A1";
        req.setAttribute("tableNo", tableNo);

        TableSession session = sessionRepo.findTopByTableNoAndStatus(tableNo, "ACTIVE");
        if (session != null && session.getCustomerPhone() != null && !session.getCustomerPhone().trim().isEmpty()) {
            Member m = memberRepo.findByPhoneNumber(session.getCustomerPhone().trim());
            req.setAttribute("member", m);
        }

        req.setAttribute("rewards", rewardRepo.findAll());

        RequestDispatcher rd = req.getRequestDispatcher("/reward.jsp");
        rd.forward(req, resp);
    }
}
