package com.abcnews.servlet;

import com.abcnews.dao.UserDAO;
import com.abcnews.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/AdminUserServlet")
public class AdminUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDAO dao = new UserDAO();
        try {
            // Lấy danh sách tất cả user
            req.setAttribute("userList", dao.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi tải danh sách người dùng: " + e.getMessage());
        }
        req.getRequestDispatcher("/manage/user_crud.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        UserDAO dao = new UserDAO();

        try {
            if ("add".equals(action)) {
                // THÊM MỚI USER
                User user = new User();
                user.setId(req.getParameter("id"));
                user.setPassword(req.getParameter("password")); // DAO sẽ hash
                user.setFullname(req.getParameter("fullname"));
                user.setMobile(req.getParameter("mobile"));
                user.setEmail(req.getParameter("email"));
                
                // Lấy ngày sinh (cần kiểm tra null/empty)
                String birthdayStr = req.getParameter("birthday");
                if (birthdayStr != null && !birthdayStr.isEmpty()) {
                    user.setBirthday(Date.valueOf(birthdayStr));
                }
                
                // Lấy giá trị boolean từ radio button
                user.setGender("1".equals(req.getParameter("gender"))); // 1=Nam, 0=Nữ
                user.setRole("1".equals(req.getParameter("role"))); // 1=Admin, 0=Reporter
                
                dao.insert(user);
                req.getSession().setAttribute("success", "Thêm người dùng thành công!");

            } else if ("edit".equals(action)) {
                // CẬP NHẬT USER
                User user = new User();
                String id = req.getParameter("id");
                user.setId(id);
                user.setFullname(req.getParameter("fullname"));
                user.setMobile(req.getParameter("mobile"));
                user.setEmail(req.getParameter("email"));
                
                String birthdayStr = req.getParameter("birthday");
                if (birthdayStr != null && !birthdayStr.isEmpty()) {
                    user.setBirthday(Date.valueOf(birthdayStr));
                }
                
                user.setGender("1".equals(req.getParameter("gender")));
                user.setRole("1".equals(req.getParameter("role")));

                // *** XỬ LÝ MẬT KHẨU ***
                String password = req.getParameter("password");
                if (password == null || password.isEmpty()) {
                    // Người dùng không muốn đổi pass
                    // Lấy pass cũ (đã hash) từ CSDL
                    User oldUser = dao.getById(id);
                    user.setPassword(oldUser.getPassword()); 
                } else {
                    // Người dùng muốn đổi pass -> Gửi pass mới (plain text)
                    // DAO.update() sẽ tự động hash mật khẩu mới này
                    user.setPassword(password);
                }
                
                dao.update(user);
                req.getSession().setAttribute("success", "Cập nhật người dùng thành công!");

            } else if ("delete".equals(action)) {
                // XÓA USER
                String id = req.getParameter("id");
                
                // (Tùy chọn) Ngăn admin tự xóa mình
                User currentUser = (User) req.getSession().getAttribute("user");
                if (currentUser.getId().equals(id)) {
                    throw new Exception("Bạn không thể tự xóa tài khoản của mình!");
                }
                
                dao.delete(id);
                req.getSession().setAttribute("success", "Xóa người dùng thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Thao tác thất bại: " + e.getMessage());
        }

        // Chuyển hướng về doGet
        resp.sendRedirect("AdminUserServlet");
    }
}