package com.example.demo.servlet;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.example.demo.buffet.BuffetFactory;
import com.example.demo.buffet.BuffetPackage;
import com.example.demo.entity.MenuItem;
import com.example.demo.entity.TableSession;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.TableSessionRepository;
import com.example.demo.util.FileUploadUtil;

@MultipartConfig(
    maxFileSize = 1024 * 1024 * 20,
    maxRequestSize = 1024 * 1024 * 50
)
public class MenuServlet extends HttpServlet {

    private MenuItemRepository menuRepo = new MenuItemRepository();
    private TableSessionRepository sessionRepo = new TableSessionRepository();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String tableNo = req.getParameter("tableNo");
        if (tableNo == null || tableNo.isEmpty()) tableNo = "A1";
        req.setAttribute("tableNo", tableNo);

        TableSession activeSession = sessionRepo.findTopByTableNoAndStatus(tableNo, "ACTIVE");

        boolean canOrder = false;
        String warningMsg = "";

        if (activeSession != null) {
            if (activeSession.getCustomerName() != null && activeSession.getPax() != null && activeSession.getPax() > 0) {
                canOrder = true;
            } else {
                warningMsg = "ข้อมูลโต๊ะไม่สมบูรณ์ กรุณาติดต่อพนักงาน";
            }
        } else {
            warningMsg = "โต๊ะนี้ยังไม่ได้เปิดให้บริการ";
        }

        req.setAttribute("canOrder", canOrder);
        req.setAttribute("warningMsg", warningMsg);
        long remainingSeconds = 0;
        int currentPax = 0;

        if (activeSession != null && activeSession.getStartTime() != null) {
            long limitSeconds = 2 * 60 * 60;
            long elapsed = Duration.between(activeSession.getStartTime(), LocalDateTime.now()).getSeconds();
            remainingSeconds = Math.max(0, limitSeconds - elapsed);
        }

        if (activeSession != null && activeSession.getPax() != null) {
            currentPax = activeSession.getPax();
        }

        req.setAttribute("remainingSeconds", remainingSeconds);
        req.setAttribute("pax", currentPax);
        if (activeSession != null && remainingSeconds <= 0) {
            canOrder = false;
            warningMsg = "หมดเวลาทานแล้ว";
        }
        req.setAttribute("canOrder", canOrder);
        req.setAttribute("warningMsg", warningMsg);

        String search = req.getParameter("search");
        String sort = req.getParameter("sort"); 

    
        String packageType = "STANDARD";
        if (activeSession != null && activeSession.getPackageType() != null && !activeSession.getPackageType().isEmpty()) {
            packageType = activeSession.getPackageType();
        }

        BuffetPackage pkg = BuffetFactory.of(packageType);
        List<MenuItem> items = menuRepo.searchActiveAndSort(search, sort, packageType)
            .stream().filter(m -> pkg.allows(m)).collect(java.util.stream.Collectors.toList());

        req.setAttribute("menuItems", items);
        req.setAttribute("packageName", pkg.getName());
        req.setAttribute("packagePrice", pkg.getPricePerPerson());
        req.setAttribute("currentSearch", search);
        req.setAttribute("currentSort", sort);

        req.getRequestDispatcher("/menu.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
         String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String category = req.getParameter("category");
        String oldImageUrl = req.getParameter("oldImageUrl");

        String imageFileName = oldImageUrl;

        try {
            Part filePart = req.getPart("imageFile");
            String uploaded = FileUploadUtil.uploadImage(filePart, getServletContext());
            if (uploaded != null) {
                imageFileName = uploaded;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MenuItem item = new MenuItem();
        item.setName(name);
        item.setCategory(category);
        item.setImageUrl(imageFileName);
        item.setActive(true);

        if (idStr != null && !idStr.isEmpty()) {
            item.setId(Integer.parseInt(idStr));
        }

        menuRepo.save(item);
        resp.sendRedirect("menu");
    }
}