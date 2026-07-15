package com.example.demo.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.example.demo.entity.MenuItem;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.util.FileUploadUtil;

@WebServlet("/manager-menu")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 20,   
    maxRequestSize = 1024 * 1024 * 50
)
public class ManagerMenuServlet extends HttpServlet {

    private MenuItemRepository menuRepo = new MenuItemRepository();

   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                menuRepo.delete(Integer.parseInt(idStr));
            }
            resp.sendRedirect("manager-menu");

        } else if ("toggle".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                MenuItem m = menuRepo.findById(Integer.parseInt(idStr));
                if (m != null) {
                    m.setActive(!m.isActive());
                    menuRepo.save(m);
                }
            }
            resp.sendRedirect("manager-menu");

        } else {
            String keyword = req.getParameter("search");
            String sort = req.getParameter("sort");
            String categoryFilter = req.getParameter("categoryFilter");
            String premiumFilter = req.getParameter("premiumFilter"); 

          
            List<MenuItem> items = menuRepo.findAll();

            // category filter
            if (categoryFilter != null && !categoryFilter.isEmpty()) {
                items.removeIf(m -> m.getCategory() == null || !m.getCategory().equalsIgnoreCase(categoryFilter));
            }

            // keyword search
            if (keyword != null && !keyword.trim().isEmpty()) {
                String key = keyword.toLowerCase().trim();
                items.removeIf(m -> m.getName() == null || !m.getName().toLowerCase().contains(key));
            }

            // premium filter
            if (premiumFilter != null) {
                switch (premiumFilter) {
                    case "only":
                        items.removeIf(m -> !m.isPremium());
                        break;
                    case "non":
                        items.removeIf(m -> m.isPremium());
                        break;
                    default:
                       
                }
            }

            // sort
            if (sort != null) {
                switch (sort) {
                    case "name_asc":
                        items.sort(java.util.Comparator.comparing(MenuItem::getName, String.CASE_INSENSITIVE_ORDER));
                        break;
                    case "name_desc":
                        items.sort(java.util.Comparator.comparing(MenuItem::getName, String.CASE_INSENSITIVE_ORDER).reversed());
                        break;
                    case "category_asc":
                        items.sort(java.util.Comparator.comparing(MenuItem::getCategory, String.CASE_INSENSITIVE_ORDER));
                        break;
                    default:
                        items.sort(java.util.Comparator.comparingInt(MenuItem::getId));
                }
            } else {
                items.sort(java.util.Comparator.comparingInt(MenuItem::getId));
            }

            req.setAttribute("menuItems", items);
            req.setAttribute("currentSearch", keyword);
            req.setAttribute("currentSort", sort);
            req.setAttribute("currentCategory", categoryFilter);
            req.setAttribute("currentPremiumFilter", premiumFilter == null ? "all" : premiumFilter);

               
                com.example.demo.buffet.BuffetConfigRepository cfgRepo = new com.example.demo.buffet.BuffetConfigRepository();
                com.example.demo.buffet.BuffetConfig cfg = cfgRepo.read();
                req.setAttribute("standardPrice", cfg.getStandardPrice());
                req.setAttribute("premiumPrice", cfg.getPremiumPrice());

                req.getRequestDispatcher("/manager-menu.jsp").forward(req, resp);
        }
    }

   
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String category = req.getParameter("category");

        String oldImageUrl = req.getParameter("oldImageUrl");
        String finalImageName = oldImageUrl;

        try {
            Part filePart = req.getPart("imageFile");
            String uploaded =
                    FileUploadUtil.uploadImage(filePart, getServletContext());

            if (uploaded != null) {
                finalImageName = uploaded;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (finalImageName == null) finalImageName = "";

        MenuItem item = new MenuItem();

        if (idStr != null && !idStr.isEmpty()) {
            item.setId(Integer.parseInt(idStr));
            MenuItem old = menuRepo.findById(item.getId());
            if (old != null) item.setActive(old.isActive());
        } else {
            item.setActive(true);
        }

        item.setName(name);
        item.setCategory(category);
        item.setImageUrl(finalImageName);
        String isPremium = req.getParameter("isPremium");
        boolean premium = "on".equalsIgnoreCase(isPremium) || "true".equalsIgnoreCase(isPremium);
        item.setPremium(premium);

        menuRepo.save(item);

        resp.sendRedirect("manager-menu");
    }
}
