package com.abcnews.servlet;

import com.abcnews.dao.NewsletterDAO;
import com.abcnews.entity.Newsletter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/AdminNewsletterServlet")
public class AdminNewsletterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NewsletterDAO dao = new NewsletterDAO();
        try {
            List<Newsletter> newsletterList = dao.getAll();
            req.setAttribute("newsletterList", newsletterList);
            req.getRequestDispatcher("manage/newsletter_crud.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("manage/newsletter_crud.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        NewsletterDAO dao = new NewsletterDAO();
        try {
            if ("add".equals(action)) {
                Newsletter nl = new Newsletter();
                nl.setEmail(req.getParameter("email"));
                nl.setEnabled("on".equals(req.getParameter("enabled")));
                dao.insert(nl);
            } else if ("edit".equals(action)) {
                Newsletter nl = new Newsletter();
                nl.setEmail(req.getParameter("email"));
                nl.setEnabled("on".equals(req.getParameter("enabled")));
                dao.update(nl);
            } else if ("delete".equals(action)) {
                String email = req.getParameter("email");
                dao.delete(email);
            }
            resp.sendRedirect("AdminNewsletterServlet");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("manage/newsletter_crud.jsp").forward(req, resp);
        }
    }
}