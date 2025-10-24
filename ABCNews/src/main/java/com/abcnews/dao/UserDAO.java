package com.abcnews.dao;

import com.abcnews.entity.User;
import com.abcnews.util.DBUtil;
// Không cần import PasswordUtil nữa nếu muốn, hoặc giữ lại cũng không sao

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Kiểm tra đăng nhập bằng mật khẩu thường
     */
    public User login(String id, String password) throws Exception {
        System.out.println("--- Bắt đầu UserDAO.login() [KHÔNG MÃ HÓA] ---");
        System.out.println("ID: " + id + " | Pass nhập: [" + password + "]");

        // Dùng câu lệnh SQL để kiểm tra cả ID và Password trực tiếp
        String sql = "SELECT * FROM USERS WHERE Id = ? AND Password = ?"; // So sánh trực tiếp
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, password); // Truyền mật khẩu gốc

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("--- Đăng nhập THÀNH CÔNG [KHÔNG MÃ HÓA] ---");
                    return mapUser(rs); // Tìm thấy user với pass đúng
                }
            }
        } catch (Exception e) {
             System.err.println("UserDAO.login(): Lỗi khi kiểm tra đăng nhập: " + e.getMessage());
             throw e;
        }

        System.out.println("--- Đăng nhập THẤT BẠI [KHÔNG MÃ HÓA] ---");
        return null; // Không tìm thấy hoặc sai mật khẩu
    }

    // Các hàm getAll(), getById() giữ nguyên

    public List<User> getAll() throws Exception {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM USERS";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        }
        return list;
    }

    public User getById(String id) throws Exception {
        String sql = "SELECT * FROM USERS WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             System.out.println("UserDAO.getById(): Kết nối CSDL ok cho ID: " + id);
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) {
             System.err.println("UserDAO.getById(): LỖI: " + e.getMessage());
             throw e;
        }
        return null;
    }


    /**
     * Thêm người dùng mới (Lưu mật khẩu thường)
     */
    public boolean insert(User user) throws Exception {
        String sql = "INSERT INTO USERS (Id, Password, Fullname, Birthday, Gender, Mobile, Email, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // KHÔNG CẦN HASH NỮA
            // String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

            ps.setString(1, user.getId());
            ps.setString(2, user.getPassword()); // Lưu mật khẩu gốc
            ps.setString(3, user.getFullname());
            if (user.getBirthday() != null) {
                ps.setDate(4, new java.sql.Date(user.getBirthday().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setBoolean(5, user.isGender());
            ps.setString(6, user.getMobile());
            ps.setString(7, user.getEmail());
            ps.setBoolean(8, user.isRole());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật người dùng (Lưu mật khẩu thường nếu có thay đổi)
     */
    public boolean update(User user) throws Exception {
        String sql = "UPDATE USERS SET Password=?, Fullname=?, Birthday=?, Gender=?, Mobile=?, Email=?, Role=? WHERE Id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Lấy mật khẩu cũ nếu người dùng không nhập mới
            String passwordToSave = user.getPassword();
            if (passwordToSave == null || passwordToSave.isEmpty()) {
                User oldUser = getById(user.getId());
                if (oldUser != null) {
                    passwordToSave = oldUser.getPassword(); // Giữ mật khẩu cũ (không hash)
                } else {
                     throw new Exception("Không tìm thấy user để giữ mật khẩu cũ!");
                }
            }

            ps.setString(1, passwordToSave); // Lưu mật khẩu gốc (mới hoặc cũ)
            ps.setString(2, user.getFullname());
            if (user.getBirthday() != null) {
                ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setBoolean(4, user.isGender());
            ps.setString(5, user.getMobile());
            ps.setString(6, user.getEmail());
            ps.setBoolean(7, user.isRole());
            ps.setString(8, user.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Hàm delete() giữ nguyên

    public boolean delete(String id) throws Exception {
        NewsDAO newsDao = new NewsDAO();
        if (newsDao != null && !newsDao.getByAuthor(id).isEmpty()) {
            throw new Exception("Không thể xóa người dùng này vì họ vẫn còn bài viết.");
        }

        String sql = "DELETE FROM USERS WHERE Id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }


    // Hàm mapUser() giữ nguyên
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString("Id"));
        user.setPassword(rs.getString("Password")); // Lấy pass thường
        user.setFullname(rs.getString("Fullname"));
        user.setBirthday(rs.getDate("Birthday"));
        user.setGender(rs.getBoolean("Gender"));
        user.setMobile(rs.getString("Mobile"));
        user.setEmail(rs.getString("Email"));
        user.setRole(rs.getBoolean("Role"));
        return user;
    }
}