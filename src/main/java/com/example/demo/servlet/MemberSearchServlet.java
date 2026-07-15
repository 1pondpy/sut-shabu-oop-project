package com.example.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;

@WebServlet("/api/members/search")
public class MemberSearchServlet extends HttpServlet {
    
    private MemberService memberService = new MemberService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String phone = request.getParameter("phone");
        if (phone != null) phone = phone.trim();
        
        if (phone == null || phone.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"error\": \"ต้องส่ง phone parameter\"}");
            return;
        }
        
        System.out.println("MemberSearchServlet: search phone=[" + phone + "]");
        java.util.List<com.example.demo.entity.Member> all = memberService.getAllMembers();
        System.out.println("MemberSearchServlet: loaded members count=" + all.size());
        for (com.example.demo.entity.Member m : all) {
            System.out.println(" - member id=" + m.getId() + " phone=[" + m.getPhoneNumber() + "] name=[" + m.getName() + "]");
        }

        Member member = memberService.searchByPhoneNumber(phone);
        
        if (member != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            String json = String.format(
                "{\"id\": %d, \"phoneNumber\": \"%s\", \"name\": \"%s\", \"points\": %d, \"gameChances\": %d}",
                member.getId(), member.getPhoneNumber(), member.getName(), member.getPoints(), member.getGameChances()
            );
            out.println(json);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("{\"error\": \"ไม่เจอเบอร์นี้\"}");
        }
    }
}
