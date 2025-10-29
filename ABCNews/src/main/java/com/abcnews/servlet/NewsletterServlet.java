package com.abcnews.servlet;

import com.abcnews.dao.NewsletterDAO;
import com.abcnews.entity.Newsletter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Thêm import HttpSession

import java.io.IOException;

@WebServlet("/subscribe-action")
public class NewsletterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(">>> NewsletterServlet (subscribe-action) doPost CALLED! <<<");
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        HttpSession session = req.getSession(); // Lấy session để lưu thông báo

        // Kiểm tra tính hợp lệ của email
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            System.out.println("NewsletterServlet: Email không hợp lệ: [" + email + "]");
            // Gửi lỗi qua Session
            session.setAttribute("error", "Email không hợp lệ. Vui lòng thử lại!");
            // Redirect về trang chủ
            resp.sendRedirect(req.getContextPath() + "/HomeServlet");
            return;
        }

        NewsletterDAO dao = new NewsletterDAO();
        try {
            String trimmedEmail = email.trim();
            System.out.println("NewsletterServlet: Đang kiểm tra email: " + trimmedEmail);
            Newsletter exist = dao.getByEmail(trimmedEmail);

            if (exist != null) {
                System.out.println("NewsletterServlet: Email đã tồn tại.");
                if (!exist.isEnabled()) {
                    exist.setEnabled(true);
                    dao.update(exist);
                    // Gửi success qua Session
                    session.setAttribute("success", "Đăng ký nhận bản tin đã được kích hoạt lại!");
                } else {
                    // Gửi lỗi qua Session
                    session.setAttribute("error", "Email này đã được đăng ký từ trước!");
                }
            } else {
                System.out.println("NewsletterServlet: Email chưa tồn tại, đang thêm mới...");
                Newsletter n = new Newsletter(trimmedEmail, true);
                boolean inserted = dao.insert(n);
                if (inserted) {
                    System.out.println("NewsletterServlet: Thêm email thành công.");
                    // Gửi success qua Session
                    session.setAttribute("success", "Đăng ký nhận bản tin thành công!");
                } else {
                     System.out.println("NewsletterServlet: Lỗi insert DAO (không có exception).");
                     // Gửi lỗi qua Session
                     session.setAttribute("error", "Không thể thêm email vào CSDL.");
                }
            }
        } catch (Exception e) {
            System.err.println("NewsletterServlet: Lỗi Exception: " + e.getMessage());
            e.printStackTrace();
             // Gửi lỗi qua Session
            session.setAttribute("error", "Lỗi hệ thống khi đăng ký: " + e.getMessage());
        }

        // ===>>> THAY ĐỔI: Dùng sendRedirect <<<===
        System.out.println("NewsletterServlet: Redirecting về HomeServlet...");
        resp.sendRedirect(req.getContextPath() + "/HomeServlet");
        // ===>>> =========================== <<<===
    }

    // doGet giữ nguyên
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(">>> NewsletterServlet (subscribe-action) doGet WAS CALLED! <<<");
        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().println("NewsletterServlet (subscribe-action) GET is working!");
    }
}