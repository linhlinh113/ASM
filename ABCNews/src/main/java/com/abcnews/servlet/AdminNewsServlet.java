package com.abcnews.servlet;

import com.abcnews.dao.CategoryDAO;
import com.abcnews.dao.NewsDAO;
import com.abcnews.dao.NewsletterDAO;
import com.abcnews.entity.News;
import com.abcnews.entity.Newsletter;
import com.abcnews.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig; // Thêm import
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part; // Thêm import
import java.io.File; // Thêm import
import java.io.IOException;
import java.nio.file.Files; // Thêm import
import java.nio.file.Paths; // Thêm import
import java.nio.file.StandardCopyOption; // Thêm import
import java.sql.Date; // Thêm import (nếu chưa có)
import java.util.List; // Thêm import (nếu chưa có)
import java.util.UUID; // Thêm import

@WebServlet("/AdminNewsServlet")
// ===>>> THÊM ANNOTATION @MultipartConfig <<<===
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
    maxFileSize = 1024 * 1024 * 10, // 10 MB
    maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
// ===>>> ============================== <<<===
public class AdminNewsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ===>>> ĐỊNH NGHĨA THƯ MỤC UPLOAD <<<===
    private static final String UPLOAD_DIR_RELATIVE = "uploads" + File.separator + "images";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NewsDAO newsDao = new NewsDAO();
        CategoryDAO catDao = new CategoryDAO();

        try {
            // Lấy TẤT CẢ bài viết
            req.setAttribute("newsList", newsDao.getAllForAdmin());
            // Lấy danh sách chuyên mục cho form Sửa
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
        String id = req.getParameter("id");

        NewsDAO newsDao = new NewsDAO();
        NewsletterDAO newsletterDao = new NewsletterDAO();

        try {
            if ("approve".equals(action)) {
                // *** DUYỆT BÀI VIẾT ***
                boolean success = newsDao.setApprovalStatus(id, true);
                if (success) {
                    req.getSession().setAttribute("success", "Duyệt bài viết thành công!");
                    News approvedNews = newsDao.getById(id); // Chỉ cần lấy tin đã duyệt để gửi mail
                    List<Newsletter> subscribers = newsletterDao.getAll();
                    if (approvedNews != null && !subscribers.isEmpty()) {
                        new Thread(() -> EmailUtil.sendNewPostNotification(approvedNews, subscribers)).start();
                        System.out.println("Đã kích hoạt gửi email thông báo cho tin: " + id);
                    }
                } else {
                    throw new Exception("Không tìm thấy bài viết để duyệt.");
                }

            } else if ("reject".equals(action)) {
                // *** TỪ CHỐI/BỎ DUYỆT BÀI VIẾT ***
                newsDao.setApprovalStatus(id, false);
                req.getSession().setAttribute("success", "Đã bỏ duyệt/từ chối bài viết!");

            } else if ("delete".equals(action)) {
                // *** XÓA BÀI VIẾT ***
                newsDao.delete(id);
                req.getSession().setAttribute("success", "Xóa bài viết thành công!");

            } else if ("edit".equals(action)) {
                // ===>>> THÊM LOGIC SỬA BÀI VIẾT CHO ADMIN <<<===
                News n = new News();
                n.setId(id);
                n.setTitle(req.getParameter("title"));
                n.setContent(req.getParameter("content"));
                n.setCategoryId(req.getParameter("categoryId"));
                n.setHome("on".equals(req.getParameter("home")));
                n.setAuthor(req.getParameter("author")); // Admin có thể sửa tác giả

                // === XỬ LÝ UPLOAD ẢNH (Giống ReporterNewsServlet) ===
                String imageUrl = null;
                Part filePart = req.getPart("imageFile");
                String submittedFileName = filePart.getSubmittedFileName();
                String fileName = (submittedFileName != null) ? Paths.get(submittedFileName).getFileName().toString() : "";

                if (!fileName.isEmpty()) {
                    System.out.println("AdminNewsServlet: Processing uploaded file: " + fileName);
                    String fileExtension = getFileExtension(fileName);
                    if (!List.of("jpg", "jpeg", "png", "gif").contains(fileExtension)) {
                        throw new ServletException("Chỉ cho phép tải lên file ảnh JPG, PNG, GIF.");
                    }

                    String applicationPath = req.getServletContext().getRealPath("");
                    String uploadDirAbsolutePath = applicationPath + File.separator + UPLOAD_DIR_RELATIVE;
                    File uploadDir = new File(uploadDirAbsolutePath);
                    if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                        throw new IOException("Không thể tạo thư mục upload tại: " + uploadDirAbsolutePath);
                    }

                    String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
                    String destinationFilePath = uploadDirAbsolutePath + File.separator + uniqueFileName;
                    File destinationFile = new File(destinationFilePath);

                    try (var inputStream = filePart.getInputStream()) {
                        Files.copy(inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("AdminNewsServlet: File saved successfully to: " + destinationFilePath);
                    }
                    imageUrl = req.getContextPath() + "/" + UPLOAD_DIR_RELATIVE.replace(File.separator, "/") + "/" + uniqueFileName;

                } else {
                    System.out.println("AdminNewsServlet: No file uploaded, using text input URL.");
                    imageUrl = req.getParameter("image");
                    if (imageUrl != null) imageUrl = imageUrl.trim();

                    // Giữ ảnh cũ nếu không có ảnh/link mới
                    if (imageUrl == null || imageUrl.isEmpty()) {
                         try {
                            News oldNews = newsDao.getByIdForAdmin(id); // Dùng hàm getByIdForAdmin
                            if (oldNews != null) {
                                imageUrl = oldNews.getImage();
                                System.out.println("AdminNewsServlet (Edit): Keeping old image URL: " + imageUrl);
                            }
                         } catch (Exception e) {
                             System.err.println("AdminNewsServlet: Error getting old news for image: " + e.getMessage());
                         }
                    }
                }
                n.setImage(imageUrl);
                // === KẾT THÚC XỬ LÝ ẢNH ===

                // --- Logic riêng cho Admin Edit ---
                n.setApproved(false); // Reset về "Chờ duyệt" sau khi Admin sửa

                // Giữ ngày đăng và lượt xem cũ
                Date postedDate = null;
                int viewCount = 0;
                 try {
                    News oldNews = newsDao.getByIdForAdmin(id); // Dùng hàm getByIdForAdmin
                    if (oldNews != null) {
                        postedDate = (Date) oldNews.getPostedDate();
                        viewCount = oldNews.getViewCount();
                    }
                 } catch (Exception e) {
                      System.err.println("AdminNewsServlet: Error getting old news for date/viewcount: " + e.getMessage());
                 }
                n.setPostedDate(postedDate != null ? postedDate : new Date(System.currentTimeMillis())); // Giữ ngày cũ hoặc dùng ngày mới nếu lỗi
                n.setViewCount(viewCount); // Giữ view count cũ

                newsDao.update(n); // Gọi hàm update của DAO
                req.getSession().setAttribute("success", "Cập nhật bài viết thành công! Bài viết cần được duyệt lại.");
                // ===>>> KẾT THÚC LOGIC SỬA <<<===

            } else {
                // Hành động không xác định
                 req.getSession().setAttribute("error", "Hành động không hợp lệ: " + action);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Thao tác thất bại: " + e.getMessage());
        }

        // Chuyển hướng về doGet để tải lại danh sách
        resp.sendRedirect("AdminNewsServlet");
    }

    // Hàm tiện ích lấy đuôi file (giữ nguyên)
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
}