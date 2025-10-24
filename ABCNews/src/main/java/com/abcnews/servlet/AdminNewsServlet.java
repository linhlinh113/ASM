package com.abcnews.servlet;

import com.abcnews.dao.CategoryDAO;
import com.abcnews.dao.NewsDAO;
import com.abcnews.dao.NewsletterDAO;
import com.abcnews.entity.News;
import com.abcnews.entity.Newsletter;
import com.abcnews.util.EmailUtil; // Import EmailUtil
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/AdminNewsServlet")
public class AdminNewsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NewsDAO newsDao = new NewsDAO();
        CategoryDAO catDao = new CategoryDAO();

        try {
            // 1. Lấy TẤT CẢ bài viết (cả chờ duyệt và đã duyệt)
            req.setAttribute("newsList", newsDao.getAllForAdmin());

            // 2. Lấy danh sách chuyên mục (cho form filter/edit - nếu cần)
            req.setAttribute("categoryList", catDao.getAll());

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi tải danh sách tin tức: " + e.getMessage());
        }

        req.getRequestDispatcher("/manage/admin_news_crud.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id"); // Lấy ID bài viết

        NewsDAO newsDao = new NewsDAO(); // Khai báo newsDao ở đây
        NewsletterDAO newsletterDao = new NewsletterDAO();

        try {
            if ("approve".equals(action)) {
                // *** DUYỆT BÀI VIẾT ***
                boolean success = newsDao.setApprovalStatus(id, true); // Set Approved = 1

                if (success) {
                    req.getSession().setAttribute("success", "Duyệt bài viết thành công!");

                    // *** GỬI EMAIL THÔNG BÁO ***
                    News approvedNews = newsDao.getById(id);
                    List<Newsletter> subscribers = newsletterDao.getAll();
                    if (approvedNews != null && !subscribers.isEmpty()) {
                        new Thread(() -> EmailUtil.sendNewPostNotification(approvedNews, subscribers)).start();
                        System.out.println("Đã kích hoạt gửi email thông báo cho tin: " + id);
                    }
                } else {
                     throw new Exception("Không tìm thấy bài viết để duyệt.");
                }

            } else if ("reject".equals(action)) {
                // *** TỪ CHỐI BÀI VIẾT ***
                newsDao.setApprovalStatus(id, false); // Set Approved = 0
                req.getSession().setAttribute("success", "Đã từ chối bài viết!");

            } else if ("delete".equals(action)) {
                // *** XÓA BÀI VIẾT ***
                // SỬA Ở ĐÂY: Dùng newsDao thay vì dao
                newsDao.delete(id);
                req.getSession().setAttribute("success", "Xóa bài viết thành công!");
            }
            // LƯU Ý: Admin không có chức năng Thêm/Sửa trực tiếp ở đây.
            // Nếu muốn, bạn có thể thêm logic giống ReporterNewsServlet

        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Thao tác thất bại: " + e.getMessage());
        }

        // Chuyển hướng về doGet
        resp.sendRedirect("AdminNewsServlet");
    }
}