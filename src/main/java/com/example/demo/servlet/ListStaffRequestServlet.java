package com.example.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.StaffRequest;
import com.example.demo.repository.StaffRequestRepository;

@WebServlet("/api/call-staff/list")
public class ListStaffRequestServlet extends HttpServlet {
    private StaffRequestRepository repo = new StaffRequestRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        List<StaffRequest> pending = repo.findByStatusOrderByRequestTimeAsc("PENDING");
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (StaffRequest s : pending) {
            if (!first) sb.append(",");
            first = false;
            sb.append("{");
            sb.append("\"id\":").append(s.getId()).append(",");
            sb.append("\"tableNo\":\"").append(escape(s.getTableNo())).append("\",");
            sb.append("\"requestType\":\"").append(escape(s.getRequestType())).append("\",");
            sb.append("\"requestTime\":\"").append(s.getRequestTime().toString()).append("\"");
            sb.append("}");
        }
        sb.append("]");
        try (PrintWriter out = resp.getWriter()) { out.println(sb.toString()); }
    }

    private String escape(String s) { if (s == null) return ""; return s.replace("\"", "\\\""); }
}
