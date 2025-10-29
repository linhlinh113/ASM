package com.abcnews.servlet;

import com.abcnews.dao.CategoryDAO;
import com.abcnews.dao.NewsDAO;
import com.abcnews.entity.Category; // Cần import Category
import com.abcnews.entity.News;
import com.abcnews.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig; // Thêm import này
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part; // Thêm import Part

import java.io.File; // Thêm import File
import java.io.IOException;
import java.nio.file.Files; // Thêm import Files
import java.nio.file.Paths; // Thêm import Paths
import java.nio.file.StandardCopyOption; // Thêm import StandardCopyOption
import java.sql.Date;
import java.util.List; // Thêm import List
import java.util.UUID; // Thêm import UUID

@WebServlet("/ReporterNewsServlet")
// ===>>> THÊM ANNOTATION @MultipartConfig <<<===
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
    maxFileSize = 1024 * 1024 * 10, // 10 MB
    maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
// ===>>> ============================== <<<===
public class ReporterNewsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ===>>> ĐỊNH NGHĨA THƯ MỤC UPLOAD (Bên trong webapp) <<<===
    private static final String UPLOAD_DIR_RELATIVE = "uploads" + File.separator + "images";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Auth check (nên có ở cả doGet và doPost)
        if (user == null) {
             resp.sendRedirect(req.getContextPath() + "/LoginServlet?error=Session expired");
             return;
        }
        // Phân quyền (chỉ Reporter hoặc Admin mới vào được) - Filter đã làm việc này rồi

        NewsDAO newsDao = new NewsDAO();
        CategoryDAO catDao = new CategoryDAO();

        try {
            // Lấy danh sách bài viết CỦA Phóng viên này
            req.setAttribute("newsList", newsDao.getByAuthor(user.getId()));

            // Lấy danh sách chuyên mục để nạp vào form <select>
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

        // Auth check
        if (user == null) {
             resp.sendRedirect(req.getContextPath() + "/LoginServlet?error=Session expired");
             return;
        }

        String action = req.getParameter("action");
        NewsDAO dao = new NewsDAO();
        String id = req.getParameter("id"); // Lấy ID trước để dùng chung

        try {
            if ("add".equals(action) || "edit".equals(action)) {
                News n = new News();
                n.setId(id); // Set ID
                n.setTitle(req.getParameter("title"));
                n.setContent(req.getParameter("content"));
                n.setCategoryId(req.getParameter("categoryId"));
                n.setHome("on".equals(req.getParameter("home")));

                // ===>>> XỬ LÝ UPLOAD ẢNH <<<===
                String imageUrl = null;
                Part filePart = req.getPart("imageFile");
                String submittedFileName = filePart.getSubmittedFileName(); // Lấy tên file gốc từ client
                String fileName = (submittedFileName != null) ? Paths.get(submittedFileName).getFileName().toString() : ""; // Trích xuất tên file

                if (!fileName.isEmpty()) {
                    // 1. Người dùng đã chọn file để upload
                    System.out.println("ReporterNewsServlet: Processing uploaded file: " + fileName);

                    // 2. Kiểm tra đuôi file ảnh
                    String fileExtension = "";
                    int dotIndex = fileName.lastIndexOf('.');
                    if (dotIndex > 0) {
                        fileExtension = fileName.substring(dotIndex + 1).toLowerCase();
                    }
                    if (!List.of("jpg", "jpeg", "png", "gif").contains(fileExtension)) {
                        throw new ServletException("Chỉ cho phép tải lên file ảnh JPG, PNG, GIF.");
                    }

                    // 3. Lấy đường dẫn tuyệt đối của thư mục webapp
                    String applicationPath = req.getServletContext().getRealPath("");
                    // Tạo đường dẫn tuyệt đối đến thư mục upload (vd: .../wtpwebapps/ABCNews/uploads/images)
                    String uploadDirAbsolutePath = applicationPath + File.separator + UPLOAD_DIR_RELATIVE;

                    // 4. Tạo thư mục upload nếu chưa có
                    File uploadDir = new File(uploadDirAbsolutePath);
                    if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                        throw new IOException("Không thể tạo thư mục upload tại: " + uploadDirAbsolutePath);
                    }

                    // 5. Tạo tên file mới duy nhất
                    String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
                    String destinationFilePath = uploadDirAbsolutePath + File.separator + uniqueFileName;
                    File destinationFile = new File(destinationFilePath);

                    // 6. Lưu file vào thư mục upload
                    try (var inputStream = filePart.getInputStream()) {
                        Files.copy(inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("ReporterNewsServlet: File saved successfully to: " + destinationFilePath);
                    }

                    // 7. Tạo URL tương đối để lưu vào DB và hiển thị trên web
                    // (Ví dụ: /ABCNews/uploads/images/uuid.jpg)
                    imageUrl = req.getContextPath() + "/" + UPLOAD_DIR_RELATIVE.replace(File.separator, "/") + "/" + uniqueFileName;

                } else {
                    // 8. Người dùng không upload file -> Lấy link từ ô input text
                    System.out.println("ReporterNewsServlet: No file uploaded, using text input URL.");
                    imageUrl = req.getParameter("image");
                    if (imageUrl != null) imageUrl = imageUrl.trim();

                    // Nếu là edit và không có link mới, giữ lại ảnh cũ (Cần lấy news cũ từ DB)
                    if ("edit".equals(action) && (imageUrl == null || imageUrl.isEmpty())) {
                         try {
                            News oldNews = dao.getByIdForAdmin(id); // Cần hàm lấy tin không cần Approved
                            if (oldNews != null) {
                                imageUrl = oldNews.getImage();
                                System.out.println("ReporterNewsServlet (Edit): Keeping old image URL: " + imageUrl);
                            }
                         } catch (Exception e) {
                             System.err.println("ReporterNewsServlet: Error getting old news: " + e.getMessage());
                         }
                    }
                }

                n.setImage(imageUrl); // Set đường dẫn ảnh (upload hoặc link)
                // ===>>> KẾT THÚC XỬ LÝ ẢNH <<<===


                // === Logic còn lại ===
                n.setAuthor(user.getId()); // Gán tác giả
                n.setApproved(false);      // Luôn chờ duyệt

                if ("add".equals(action)) {
                    n.setPostedDate(new Date(System.currentTimeMillis())); // Ngày đăng
                    n.setViewCount(0); // View ban đầu
                    dao.insert(n);
                    session.setAttribute("success", "Thêm bài viết thành công! Bài viết đang chờ Admin duyệt.");
                } else { // edit
                    // Lấy ngày đăng và view count cũ từ DB để không bị mất khi update
                    Date postedDate = null;
                    int viewCount = 0;
                     try {
                        News oldNews = dao.getByIdForAdmin(id); // Cần hàm lấy tin không cần Approved
                        if (oldNews != null) {
                            postedDate = (Date) oldNews.getPostedDate(); // Giữ ngày đăng gốc
                            viewCount = oldNews.getViewCount();        // Giữ view count
                        }
                     } catch (Exception e) {
                         System.err.println("ReporterNewsServlet: Error getting old news for date/viewcount: " + e.getMessage());
                     }
                    n.setPostedDate(postedDate != null ? postedDate : new Date(System.currentTimeMillis())); // Giữ ngày cũ hoặc dùng ngày mới nếu lỗi
                    n.setViewCount(viewCount); // Giữ view count cũ

                    dao.update(n);
                    session.setAttribute("success", "Cập nhật bài viết thành công! Bài viết đang chờ Admin duyệt lại.");
                }

            } else if ("delete".equals(action)) {
                // Thêm kiểm tra tác giả trước khi xóa
                boolean canDelete = false;
                 try {
                     News newsToDelete = dao.getByIdForAdmin(id); // Cần hàm lấy tin không cần Approved
                     if (newsToDelete != null && newsToDelete.getAuthor().equals(user.getId())) {
                         canDelete = true;
                     }
                 } catch (Exception e) {
                      System.err.println("ReporterNewsServlet: Error checking author before delete: " + e.getMessage());
                 }

                if (canDelete) {
                    dao.delete(id);
                    session.setAttribute("success", "Xóa bài viết thành công!");
                } else {
                     session.setAttribute("error", "Bạn không có quyền xóa bài viết này hoặc bài viết không tồn tại!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Thao tác thất bại: " + e.getMessage());
        }

        resp.sendRedirect("ReporterNewsServlet");
    }

    // (Helper Method) Lấy đuôi file từ tên file
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

     // Lưu ý: Cần thêm hàm getByIdForAdmin(String id) vào NewsDAO.java
     // Hàm này giống getById nhưng không kiểm tra Approved = 1
}