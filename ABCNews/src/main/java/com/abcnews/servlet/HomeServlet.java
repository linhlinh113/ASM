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

@WebServlet(urlPatterns = {"", "/HomeServlet"}) // Chạy khi truy cập root ("/") hoặc /HomeServlet
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set UTF-8 để hiển thị Tiếng Việt
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Khởi tạo các DAO cần thiết
        NewsDAO newsDao = new NewsDAO();
        CategoryDAO catDao = new CategoryDAO();

        try {
            // 1. Lấy Danh mục (cho left_sidebar.jsp)
            List<Category> categoryList = catDao.getAll();

            // 2. Lấy 5 Tin Hot (cho right_sidebar.jsp)
            List<News> hotList = newsDao.getHotNews(5);

            // 3. Lấy 5 Tin Mới nhất (cho right_sidebar.jsp)
            List<News> newestList = newsDao.getNewestNews(5);

            // 4. Lấy Tin Trang chủ (cho index.jsp)
            List<News> homeNews = newsDao.getHomeNews();

            // 5. Đẩy tất cả dữ liệu vào request để JSP sử dụng
            req.setAttribute("categoryList", categoryList);
            req.setAttribute("hotList", hotList);
            req.setAttribute("newestList", newestList);
            req.setAttribute("homeNews", homeNews);

            // 6. Chuyển sang index.jsp để hiển thị giao diện
            req.getRequestDispatcher("/index.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Lỗi nghiêm trọng khi tải HomeServlet: " + e.getMessage()); // Log lỗi ra console server
            e.printStackTrace(); // In chi tiết lỗi
            // Hiển thị thông báo lỗi thân thiện cho người dùng trên trang chủ
            req.setAttribute("error", "Không thể tải dữ liệu trang chủ. Vui lòng thử lại sau.");
            // Vẫn forward đến index.jsp để hiển thị layout và thông báo lỗi
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}