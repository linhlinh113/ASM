package com.abcnews.servlet;

import com.abcnews.dao.NewsDAO;
import com.abcnews.entity.News;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/NewsDetailServlet")
public class NewsDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newsId = req.getParameter("id");
        NewsDAO dao = new NewsDAO();
        try {
            dao.incrementViewCount(newsId); // tăng view
            News news = dao.getById(newsId);
            List<News> relatedList = dao.getByCategory(news.getCategoryId());

            req.setAttribute("news", news);
            req.setAttribute("relatedList", relatedList);

            req.getRequestDispatcher("news_detail.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("news_detail.jsp").forward(req, resp);
        }
    }
}