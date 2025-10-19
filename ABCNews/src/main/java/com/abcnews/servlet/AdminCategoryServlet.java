package com.abcnews.servlet;

import com.abcnews.dao.CategoryDAO;
import com.abcnews.entity.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/AdminCategoryServlet")
public class AdminCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CategoryDAO dao = new CategoryDAO();
        try {
            List<Category> catList = dao.getAll();
            req.setAttribute("catList", catList);
            req.getRequestDispatcher("manage/category_crud.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("manage/category_crud.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        CategoryDAO dao = new CategoryDAO();
        try {
            if ("add".equals(action)) {
                Category cat = new Category();
                cat.setId(req.getParameter("id"));
                cat.setName(req.getParameter("name"));
                dao.insert(cat);
            } else if ("edit".equals(action)) {
                Category cat = new Category();
                cat.setId(req.getParameter("id"));
                cat.setName(req.getParameter("name"));
                dao.update(cat);
            } else if ("delete".equals(action)) {
                String id = req.getParameter("id");
                dao.delete(id);
            }
            resp.sendRedirect("AdminCategoryServlet");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("manage/category_crud.jsp").forward(req, resp);
        }
    }
}