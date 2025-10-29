package com.abcnews.servlet;

import com.abcnews.dao.NewsletterDAO;
import com.abcnews.entity.Newsletter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/NewsletterServlet")
public class NewsletterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");

        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            req.setAttribute("error", "Email không hợp lệ. Vui lòng thử lại!");
            // Chuyển tiếp (forward) sang HomeServlet để nó tải lại trang chủ
            req.getRequestDispatcher("/HomeServlet").forward(req, resp);
            return;
        }

        NewsletterDAO dao = new NewsletterDAO();
        try {
            // Kiểm tra email đã tồn tại chưa
            Newsletter exist = dao.getByEmail(email);
            if (exist != null) {
                req.setAttribute("error", "Email này đã được đăng ký từ trước!");
            } else {
                // Thêm email mới
                Newsletter n = new Newsletter(email.trim(), true);
                dao.insert(n);
                req.setAttribute("success", "Đăng ký nhận bản tin thành công!");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        
        // Luôn chuyển tiếp sang HomeServlet để nó tải lại toàn bộ dữ liệu trang chủ
        // (bao gồm cả tin tức, danh mục VÀ thông báo success/error)
        req.getRequestDispatcher("/HomeServlet").forward(req, resp);
    }
}