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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60; // 30 phút

    /**
     * Xử lý POST request: Thực hiện đăng nhập
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        
        // Trim và kiểm tra null
        if (id != null) id = id.trim();
        if (password != null) password = password.trim();

        if (id == null || id.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        UserDAO dao = new UserDAO();
        try {
            // 1. Gọi hàm login đã tích hợp BCrypt
            User user = dao.login(id, password);

            if (user != null) {
                // 2. Đăng nhập thành công -> Tạo Session
                HttpSession session = req.getSession();
                session.setAttribute("user", user); // Lưu đối tượng User vào session
                session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);

                // 3. Phân quyền và chuyển hướng
                String ctx = req.getContextPath();
                if (user.isRole()) {
                    // Là Admin
                    resp.sendRedirect(ctx + "/manage/admin_home.jsp"); // Sẽ tạo trang này sau
                } else {
                    // Là Reporter
                    resp.sendRedirect(ctx + "/manage/reporter_home.jsp"); // Sẽ tạo trang này sau
                }
            } else {
                // 4. Đăng nhập thất bại
                req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    /**
     * Xử lý GET request: Hiển thị trang đăng nhập
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Bất cứ khi nào người dùng truy cập /LoginServlet,
        // chỉ cần hiển thị trang login.jsp
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}