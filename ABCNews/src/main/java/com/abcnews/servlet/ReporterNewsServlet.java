package com.abcnews.servlet;

import com.abcnews.dao.NewsDAO;
import com.abcnews.entity.News;
import com.abcnews.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ReporterNewsServlet")
public class ReporterNewsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.isRole()) {
            resp.sendRedirect("login.jsp");
            return;
        }
        NewsDAO dao = new NewsDAO();
        try {
            List<News> newsList = dao.getByAuthor(user.getId());
            req.setAttribute("newsList", newsList);
            req.getRequestDispatcher("manage/news_crud_reporter.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("manage/news_crud_reporter.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.isRole()) {
            resp.sendRedirect("login.jsp");
            return;
        }
        String action = req.getParameter("action");
        NewsDAO dao = new NewsDAO();
        try {
            if ("add".equals(action)) {
                News n = new News();
                n.setId(req.getParameter("id"));
                n.setTitle(req.getParameter("title"));
                n.setContent(req.getParameter("content"));
                n.setImage(req.getParameter("image"));
                n.setPostedDate(java.sql.Date.valueOf(req.getParameter("postedDate")));
                n.setAuthor(user.getId());
                n.setViewCount(Integer.parseInt(req.getParameter("viewCount")));
                n.setCategoryId(req.getParameter("categoryId"));
                n.setHome("on".equals(req.getParameter("home")));
                dao.insert(n);
            } else if ("edit".equals(action)) {
                News n = new News();
                n.setId(req.getParameter("id"));
                n.setTitle(req.getParameter("title"));
                n.setContent(req.getParameter("content"));
                n.setImage(req.getParameter("image"));
                n.setPostedDate(java.sql.Date.valueOf(req.getParameter("postedDate")));
                n.setAuthor(user.getId());
                n.setViewCount(Integer.parseInt(req.getParameter("viewCount")));
                n.setCategoryId(req.getParameter("categoryId"));
                n.setHome("on".equals(req.getParameter("home")));
                dao.update(n);
            } else if ("delete".equals(action)) {
                String id = req.getParameter("id");
                dao.delete(id);
            }
            resp.sendRedirect("ReporterNewsServlet");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("manage/news_crud_reporter.jsp").forward(req, resp);
        }
    }
}