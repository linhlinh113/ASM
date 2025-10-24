package com.abcnews.servlet;

import com.abcnews.dao.CategoryDAO;
import com.abcnews.entity.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/AdminCategoryServlet")
public class AdminCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Tải danh sách và hiển thị
        CategoryDAO dao = new CategoryDAO();
        try {
            req.setAttribute("catList", dao.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi tải danh sách: " + e.getMessage());
        }
        req.getRequestDispatcher("/manage/category_crud.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // Đảm bảo Tiếng Việt
        String action = req.getParameter("action");
        CategoryDAO dao = new CategoryDAO();
        
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        
        try {
            if ("add".equals(action)) {
                Category cat = new Category(id, name);
                dao.insert(cat);
                req.getSession().setAttribute("success", "Thêm danh mục thành công!");
                
            } else if ("edit".equals(action)) {
                Category cat = new Category(id, name);
                dao.update(cat);
                req.getSession().setAttribute("success", "Cập nhật danh mục thành công!");
                
            } else if ("delete".equals(action)) {
                dao.delete(id);
                req.getSession().setAttribute("success", "Xóa danh mục thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Gửi lỗi qua Session để hiển thị sau khi redirect
            req.getSession().setAttribute("error", "Thao tác thất bại: " + e.getMessage());
        }
        
        // Dùng Post-Redirect-Get (PRG) Pattern
        // Luôn redirect về doGet để tránh lỗi "resubmit form"
        resp.sendRedirect("AdminCategoryServlet");
    }
}