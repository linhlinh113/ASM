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
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String categoryId = req.getParameter("catId");
        
        NewsDAO newsDao = new NewsDAO();
        CategoryDAO catDao = new CategoryDAO();
        
        try {
            if (categoryId == null || categoryId.isEmpty()) {
                // Nếu không có catId, có thể chuyển về trang chủ
                resp.sendRedirect("HomeServlet");
                return;
            }
            
            // 1. Lấy danh sách tin theo danh mục
            List<News> newsList = newsDao.getByCategory(categoryId);
            
            // 2. Lấy thông tin của danh mục hiện tại (để hiển thị tên)
            Category currentCategory = catDao.getById(categoryId);
            
            // 3. Nạp dữ liệu cho 2 sidebar (luôn cần)
            loadSidebarData(req);
            
            // 4. Đẩy dữ liệu sang JSP
            req.setAttribute("newsList", newsList);
            req.setAttribute("currentCategory", currentCategory);

            req.getRequestDispatcher("/news_list.jsp").forward(req, resp);
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi tải danh sách tin: " + e.getMessage());
            req.getRequestDispatcher("/news_list.jsp").forward(req, resp);
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