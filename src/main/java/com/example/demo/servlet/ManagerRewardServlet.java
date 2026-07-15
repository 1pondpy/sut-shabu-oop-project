package com.example.demo.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Reward;
import com.example.demo.repository.RewardRepository;



@WebServlet("/manager-reward")
public class ManagerRewardServlet extends HttpServlet {
    
    private RewardRepository rewardRepo = new RewardRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        if ("delete".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) rewardRepo.deleteById(Integer.parseInt(idStr));
            resp.sendRedirect("manager-reward");
            return;
        }

    
        int[] config = rewardRepo.getPointConfig();
        req.setAttribute("configBaht", config[0]);
        req.setAttribute("configPoints", config[1]);

    
        String keyword = req.getParameter("search"); 
        String sort = req.getParameter("sort");
        String discountTypeFilter = req.getParameter("discountTypeFilter");
        if (discountTypeFilter != null) {
            discountTypeFilter = discountTypeFilter.trim();
            if (discountTypeFilter.isEmpty()) {
                discountTypeFilter = null;
            }
        }
        

        List<Reward> rewards = rewardRepo.searchAndSort(keyword, sort, discountTypeFilter);
        
      
        req.setAttribute("rewards", rewards);
        req.setAttribute("currentSearch", keyword); 
        req.setAttribute("currentSort", sort);  
        req.setAttribute("currentDiscountType", discountTypeFilter);
        
        req.getRequestDispatcher("/manager-reward.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

     
        if ("update_config".equals(action)) {
            try {
                int baht = Integer.parseInt(req.getParameter("rateBaht"));
                int points = Integer.parseInt(req.getParameter("ratePoints"));
                rewardRepo.savePointConfig(baht, points);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resp.sendRedirect("manager-reward");
            return;
        }
   
        try {
            String idStr = req.getParameter("id");
             String name = req.getParameter("name");
             int points = Integer.parseInt(req.getParameter("pointsRequired"));
             String type = req.getParameter("discountType");
             double value = Double.parseDouble(req.getParameter("discountValue"));

             Reward r = new Reward();
             if (idStr != null && !idStr.isEmpty()) {
                 r.setId(Integer.parseInt(idStr));
             }
             r.setName(name);
             r.setPointsRequired(points);
             r.setDiscountType(type);
             r.setDiscountValue(value);

             rewardRepo.save(r);

        } catch (Exception e) {
             e.printStackTrace();
        }

        resp.sendRedirect("manager-reward");
    }
}