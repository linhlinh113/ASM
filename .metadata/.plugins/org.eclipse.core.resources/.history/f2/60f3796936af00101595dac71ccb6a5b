package com.abcnews.servlet;

import com.abcnews.dao.UserDAO;
import com.abcnews.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60; // 30 phút

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy params và trim
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        if (id != null) id = id.trim();
        if (password != null) password = password.trim();

        // Kiểm tra cơ bản
        if (id == null || id.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập cả mã đăng nhập và mật khẩu.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        UserDAO dao = new UserDAO();
        try {
            User user = dao.login(id, password);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);

                // redirect dùng context path để tránh lỗi đường dẫn
                String ctx = req.getContextPath();
                if (user.isRole()) { // true = admin
                    resp.sendRedirect(ctx + "/manage/admin_home.jsp");
                } else {
                    resp.sendRedirect(ctx + "/manage/reporter_home.jsp");
                }
                return;
            } else {
                req.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
        } catch (Exception e) {
            // Log chi tiết để debug, không phơi bày chi tiết cho người dùng
            logger.log(Level.SEVERE, "Lỗi khi đăng nhập user=" + id, e);
            req.setAttribute("error", "Lỗi hệ thống, vui lòng thử lại sau.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward tới login.jsp (đường dẫn bắt đầu bằng / để relative tới context root)
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}