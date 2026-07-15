package com.example.demo.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/call-staff/ack")
public class AckStaffRequestServlet extends HttpServlet {
    private static final File DATA_FILE = new File("data/staff_requests.txt");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        
       
        StringBuilder body = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) { body.append(line); }
        }
        String b = body.toString();

        Integer id = null;
        try {
          
            int idx = b.indexOf("\"id\":");
            if (idx >= 0) {
                int start = idx + 5; 

                while (start < b.length() && (b.charAt(start) == ' ' || b.charAt(start) == '\t')) {
                    start++;
                }

                int end = start;
                while (end < b.length() && Character.isDigit(b.charAt(end))) {
                    end++;
                }
                
                if (end > start) {
                    id = Integer.parseInt(b.substring(start, end));
                }
            }
        } catch (Exception ex) { 
            ex.printStackTrace();
        }

        if (id == null) {
            resp.setStatus(400);
            try (PrintWriter out = resp.getWriter()) { out.println("{\"ok\":false,\"error\":\"missing or invalid id\"}"); }
            return;
        }

      
        List<String> lines = new ArrayList<>();
    
        if (DATA_FILE.exists()) {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(DATA_FILE), StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) lines.add(line);
            }
        }

        boolean changed = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split("\\|", -1);
            try {
                if (parts.length >= 5) {
                    long lid = Long.parseLong(parts[0].trim());
                    
                    if (lid == id) {
                        parts[4] = "ACKNOWLEDGED"; 
                        lines.set(i, String.join("|", parts));
                        changed = true;
                        break;
                    }
                }
            } catch (Exception ex) { }
        }

        
        if (changed) {
            try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(DATA_FILE, false), StandardCharsets.UTF_8))) {
                for (String L : lines) w.println(L);
            }
        }

        try (PrintWriter out = resp.getWriter()) { out.println("{\"ok\":" + changed + "}"); }
    }
}