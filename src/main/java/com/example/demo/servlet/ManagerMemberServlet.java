package com.example.demo.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;

@WebServlet("/manager-member")
public class ManagerMemberServlet extends HttpServlet {
    
    private MemberRepository memberRepo = new MemberRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        if ("delete".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                try { memberRepo.deleteById(Long.parseLong(idStr)); } 
                catch (Exception e) {}
            }
            resp.sendRedirect("manager-member");
            return;
        }

        
        String keyword = req.getParameter("search");
        String sort = req.getParameter("sort");

    
        List<Member> members = memberRepo.searchAndSort(keyword, sort);
        
        req.setAttribute("members", members);
        req.setAttribute("currentSearch", keyword); 
        req.setAttribute("currentSort", sort);      
        
        req.getRequestDispatcher("/manager-member.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        try {
            String idStr = req.getParameter("id"); 
            String name = req.getParameter("name");
            String phone = req.getParameter("phoneNumber");
            String pointsStr = req.getParameter("points");

            Member m = new Member();
            
          
            if (idStr != null && !idStr.isEmpty()) {
                m.setId(Long.parseLong(idStr));
                Member oldMember = memberRepo.findById(m.getId());
                if(oldMember != null) {
                    m.setGameChances(oldMember.getGameChances());
                } else {
                    m.setGameChances(0);
                }
            } else {
                m.setGameChances(0); 
            }

            m.setName(name);
            m.setPhoneNumber(phone);
            m.setPoints(pointsStr != null && !pointsStr.isEmpty() ? Integer.parseInt(pointsStr) : 0);
            memberRepo.save(m);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("manager-member");
    }
}