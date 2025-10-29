package com.abcnews.servlet;

import com.abcnews.dao.CategoryDAO;
import com.abcnews.dao.NewsDAO;
import com.abcnews.entity.News;
import com.abcnews.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/ReporterNewsServlet")
public class ReporterNewsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        NewsDAO newsDao = new NewsDAO();
        CategoryDAO catDao = new CategoryDAO();

        try {
            // 1. Lấy danh sách bài viết CỦA Phóng viên này
            req.setAttribute("newsList", newsDao.getByAuthor(user.getId()));
            
            // 2. Lấy danh sách chuyên mục để nạp vào form <select>
            req.setAttribute("categoryList", catDao.getAll());
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi tải dữ liệu: " + e.getMessage());
        }
        
        req.getRequestDispatcher("/manage/reporter_news_crud.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        String action = req.getParameter("action");
        NewsDAO dao = new NewsDAO();

        try {
            if ("add".equals(action)) {
                News n = new News();
                n.setId(req.getParameter("id")); // Mã tin
                n.setTitle(req.getParameter("title")); // Tiêu đề
                n.setContent(req.getParameter("content")); // Nội dung
                n.setImage(req.getParameter("image")); // Link ảnh
                n.setCategoryId(req.getParameter("categoryId")); // Mã chuyên mục
                n.setHome("on".equals(req.getParameter("home"))); // Checkbox "Hiển thị trang chủ"
                
                // === LOGIC QUAN TRỌNG ===
                n.setAuthor(user.getId()); // 1. Tự động gán tác giả là Phóng viên đang đăng nhập
                n.setApproved(false);      // 2. Tự động set "Chờ duyệt"
                n.setPostedDate(new Date(System.currentTimeMillis())); // 3. Tự động set ngày đăng
                n.setViewCount(0);         // 4. Lượt xem ban đầu = 0
                
                dao.insert(n);
                req.getSession().setAttribute("success", "Thêm bài viết thành công! Bài viết đang chờ Admin duyệt.");

            } else if ("edit".equals(action)) {
                News n = new News();
                n.setId(req.getParameter("id"));
                n.setTitle(req.getParameter("title"));
                n.setContent(req.getParameter("content"));
                n.setImage(req.getParameter("image"));
                n.setCategoryId(req.getParameter("categoryId"));
                n.setHome("on".equals(req.getParameter("home")));
                
                // === LOGIC QUAN TRỌNG KHI SỬA ===
                n.setAuthor(user.getId()); // 1. Giữ nguyên tác giả
                n.setApproved(false);      // 2. TỰ ĐỘNG SET LẠI "CHỜ DUYỆT" SAU KHI SỬA
                
                // Lấy thông tin cũ (ngày đăng, lượt xem) để không bị mất
                // Tạm thời, chúng ta giả định là không thể sửa ngày đăng và lượt xem
                // (Một logic đầy đủ hơn sẽ lấy bản ghi cũ từ DB)
                n.setPostedDate(new Date(System.currentTimeMillis())); // Cập nhật ngày
                n.setViewCount(0); // Reset lượt xem (hoặc lấy từ DB)
                
                dao.update(n);
                req.getSession().setAttribute("success", "Cập nhật bài viết thành công! Bài viết đang chờ Admin duyệt lại.");

            } else if ("delete".equals(action)) {
                String id = req.getParameter("id");
                
                // TODO: Thêm bước kiểm tra xem bài viết này có đúng là của
                // user này không trước khi xóa (để tăng bảo mật)
                
                dao.delete(id);
                req.getSession().setAttribute("success", "Xóa bài viết thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Thao tác thất bại: " + e.getMessage());
        }

        // Chuyển hướng về doGet
        resp.sendRedirect("ReporterNewsServlet");
    }
}