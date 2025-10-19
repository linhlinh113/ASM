package com.abcnews.servlet;

import com.abcnews.dao.CategoryDAO;
import com.abcnews.dao.NewsDAO;
import com.abcnews.entity.Category;
import com.abcnews.entity.News;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        NewsDAO newsDao = new NewsDAO();
        CategoryDAO catDao = new CategoryDAO();

        try {
            List<Category> categoryList = catDao.getAll();
            List<News> hotList = newsDao.getHotNews(5);
            List<News> newestList = newsDao.getNewestNews(5);
            List<News> homeNews = newsDao.getHomeNews();

            req.setAttribute("categoryList", categoryList);
            req.setAttribute("hotList", hotList);
            req.setAttribute("newestList", newestList);
            req.setAttribute("homeNews", homeNews);

            System.out.println("[" + new java.util.Date() + "] HotList size: " + (hotList != null ? hotList.size() : 0));
            System.out.println("[" + new java.util.Date() + "] NewestList size: " + (newestList != null ? newestList.size() : 0));
            System.out.println("[" + new java.util.Date() + "] HomeNews size: " + (homeNews != null ? homeNews.size() : 0));

            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } catch (Exception e) {
            System.out.println("[" + new java.util.Date() + "] Error in HomeServlet: " + e.getMessage());
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            e.printStackTrace();
        }
    }
}