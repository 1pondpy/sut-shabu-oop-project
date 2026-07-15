package com.example.demo.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.KitchenStaff;
import com.example.demo.entity.Manager;
import com.example.demo.entity.ServiceStaff;
import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;

@WebServlet("/manager-staff")
public class ManagerStaffServlet extends HttpServlet {

    private StaffRepository staffRepo = new StaffRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    staffRepo.deleteById(Long.parseLong(idStr));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            resp.sendRedirect("manager-staff");
            return;
        }
        String keyword = req.getParameter("search");
        String sort = req.getParameter("sort");
        String roleFilter = req.getParameter("roleFilter"); 

        List<Staff> staffList = staffRepo.searchAndSort(keyword, sort, roleFilter);
        
        req.setAttribute("staffList", staffList);
        req.setAttribute("currentSearch", keyword);
        req.setAttribute("currentSort", sort);
        req.setAttribute("currentRole", roleFilter);
        
        req.getRequestDispatcher("/manager-staff.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            String idStr = req.getParameter("id");
            String name = req.getParameter("name");
            String username = req.getParameter("username");
            String role = req.getParameter("role");
            String password = req.getParameter("password"); 
            
          
            Long id = null;
            if (idStr != null && !idStr.isEmpty()) {
                id = Long.parseLong(idStr);
            }

            if (id != null && (password == null || password.isEmpty())) {
                 Staff old = staffRepo.findById(id);
                 if (old != null) {
                     password = old.getPassword();
                 }
            }
            Staff staff = null;
            
            if ("Manager".equalsIgnoreCase(role)) {
                staff = new Manager(id, username, password, name);
            } else if ("Kitchen Staff".equalsIgnoreCase(role)) {
                staff = new KitchenStaff(id, username, password, name);
            } else {
                
                staff = new ServiceStaff(id, username, password, name);
            }
            staffRepo.save(staff);

        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect("manager-staff");
    }
}