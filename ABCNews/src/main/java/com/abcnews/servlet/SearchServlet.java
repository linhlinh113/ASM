package com.abcnews.servlet;

import com.abcnews.dao.CategoryDAO;
import com.abcnews.dao.NewsDAO;
import com.abcnews.entity.News;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String keyword = req.getParameter("keyword");
        
        if (keyword == null || keyword.trim().isEmpty()) {
            resp.sendRedirect("HomeServlet");
            return;
        }
        
        NewsDAO newsDao = new NewsDAO();
        
        try {
            // 1. Gọi hàm search DAO
            List<News> resultList = newsDao.search(keyword.trim());
            
            // 2. Nạp dữ liệu 2 sidebar (luôn cần)
            loadSidebarData(req);
            
            // 3. Đẩy dữ liệu sang JSP
            req.setAttribute("resultList", resultList);
            req.setAttribute("keyword", keyword.trim()); // Gửi lại từ khóa để hiển thị

            req.getRequestDispatcher("/search_result.jsp").forward(req, resp);
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi khi tìm kiếm: " + e.getMessage());
            req.getRequestDispatcher("/search_result.jsp").forward(req, resp);
        }
    }
    
    /**
     * Hàm tiện ích để nạp dữ liệu cho 2 sidebar
     */
    private void loadSidebarData(HttpServletRequest req) {
        try {
            req.setAttribute("categoryList", new CategoryDAO().getAll());
            req.setAttribute("hotList", new NewsDAO().getHotNews(5));
            req.setAttribute("newestList", new NewsDAO().getNewestNews(5));
        } catch (Exception e) {
            System.err.println("Lỗi nạp sidebar data: " + e.getMessage());
        }
    }
}