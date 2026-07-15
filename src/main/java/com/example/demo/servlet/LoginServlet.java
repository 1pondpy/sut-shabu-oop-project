package com.example.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.demo.entity.Staff;
import com.example.demo.service.LoginService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private LoginService loginService = new LoginService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("username");
        String pass = req.getParameter("password");

        Staff staff = loginService.login(user, pass);

        if (staff != null) {
           
            HttpSession session = req.getSession();
            session.setAttribute("user", staff);

     
            resp.sendRedirect(staff.getRedirectUrl());
            
        } else {
            req.setAttribute("error", "Username หรือ Password ไม่ถูกต้อง");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}