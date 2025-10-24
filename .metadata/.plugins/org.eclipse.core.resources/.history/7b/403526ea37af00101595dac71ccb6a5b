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

@WebServlet("/NewsListServlet")
public class NewsListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String catId = req.getParameter("cat");
        CategoryDAO catDao = new CategoryDAO();
        NewsDAO newsDao = new NewsDAO();
        try {
            List<Category> categoryList = catDao.getAll();
            List<News> newsList = newsDao.getByCategory(catId);

            req.setAttribute("categoryList", categoryList);
            req.setAttribute("newsList", newsList);

            req.getRequestDispatcher("news_list.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("news_list.jsp").forward(req, resp);
        }
    }
}