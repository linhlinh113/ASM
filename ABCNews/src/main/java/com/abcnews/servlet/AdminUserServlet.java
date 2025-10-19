package com.abcnews.servlet;

import com.abcnews.dao.UserDAO;
import com.abcnews.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/AdminUserServlet")
public class AdminUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDAO dao = new UserDAO();
        try {
            List<User> userList = dao.getAll();
            req.setAttribute("userList", userList);
            req.getRequestDispatcher("manage/user_crud.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("manage/user_crud.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        UserDAO dao = new UserDAO();
        try {
            if ("add".equals(action)) {
                User user = new User();
                user.setId(req.getParameter("id"));
                user.setPassword(req.getParameter("password"));
                user.setFullname(req.getParameter("fullname"));
                if (req.getParameter("birthday") != null && !req.getParameter("birthday").isEmpty()) {
                    user.setBirthday(java.sql.Date.valueOf(req.getParameter("birthday")));
                }
                user.setGender("on".equals(req.getParameter("gender")));
                user.setMobile(req.getParameter("mobile"));
                user.setEmail(req.getParameter("email"));
                user.setRole("on".equals(req.getParameter("role")));
                dao.insert(user);
            } else if ("edit".equals(action)) {
                User user = new User();
                user.setId(req.getParameter("id"));
                user.setPassword(req.getParameter("password"));
                user.setFullname(req.getParameter("fullname"));
                if (req.getParameter("birthday") != null && !req.getParameter("birthday").isEmpty()) {
                    user.setBirthday(java.sql.Date.valueOf(req.getParameter("birthday")));
                }
                user.setGender("on".equals(req.getParameter("gender")));
                user.setMobile(req.getParameter("mobile"));
                user.setEmail(req.getParameter("email"));
                user.setRole("on".equals(req.getParameter("role")));
                dao.update(user);
            } else if ("delete".equals(action)) {
                String id = req.getParameter("id");
                dao.delete(id);
            }
            resp.sendRedirect("AdminUserServlet");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("manage/user_crud.jsp").forward(req, resp);
        }
    }
}