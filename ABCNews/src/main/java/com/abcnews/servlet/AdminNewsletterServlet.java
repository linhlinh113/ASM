package com.abcnews.servlet;

import com.abcnews.dao.NewsletterDAO;
import com.abcnews.entity.Newsletter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/AdminNewsletterServlet")
public class AdminNewsletterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NewsletterDAO dao = new NewsletterDAO();
        try {
            req.setAttribute("newsletterList", dao.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi tải danh sách newsletter: " + e.getMessage());
        }
        req.getRequestDispatcher("/manage/newsletter_crud.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String email = req.getParameter("email");
        NewsletterDAO dao = new NewsletterDAO();

        try {
            if ("toggle".equals(action)) {
                // Đảo ngược trạng thái Enabled
                Newsletter current = dao.getByEmail(email);
                if (current != null) {
                    current.setEnabled(!current.isEnabled());
                    dao.update(current);
                    req.getSession().setAttribute("success", "Cập nhật trạng thái email thành công!");
                } else {
                     throw new Exception("Không tìm thấy email.");
                }
            } else if ("delete".equals(action)) {
                dao.delete(email);
                req.getSession().setAttribute("success", "Xóa email thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Thao tác thất bại: " + e.getMessage());
        }

        resp.sendRedirect("AdminNewsletterServlet");
    }
}