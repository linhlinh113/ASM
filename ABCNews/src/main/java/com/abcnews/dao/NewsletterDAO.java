package com.abcnews.dao;

import com.abcnews.entity.Newsletter;
import com.abcnews.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsletterDAO {

    /**
     * Lấy tất cả email
     */
    public List<Newsletter> getAll() throws Exception {
        List<Newsletter> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWSLETTERS";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Newsletter n = new Newsletter();
                n.setEmail(rs.getString("Email"));
                n.setEnabled(rs.getBoolean("Enabled"));
                list.add(n);
            }
        }
        return list;
    }

    /**
     * Lấy email theo địa chỉ
     */
    public Newsletter getByEmail(String email) throws Exception {
        String sql = "SELECT * FROM NEWSLETTERS WHERE Email = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Newsletter n = new Newsletter();
                    n.setEmail(rs.getString("Email"));
                    n.setEnabled(rs.getBoolean("Enabled"));
                    return n;
                }
            }
        }
        return null;
    }

    /**
     * Thêm email mới
     */
    public boolean insert(Newsletter n) throws Exception {
        String sql = "INSERT INTO NEWSLETTERS (Email, Enabled) VALUES (?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, n.getEmail());
            ps.setBoolean(2, n.isEnabled());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("PRIMARY KEY constraint")) {
                throw new Exception("Email này đã được đăng ký từ trước.");
            }
            throw new Exception("Lỗi khi đăng ký nhận tin: " + e.getMessage());
        }
    }

    /**
     * Cập nhật trạng thái email
     */
    public boolean update(Newsletter n) throws Exception {
        String sql = "UPDATE NEWSLETTERS SET Enabled = ? WHERE Email = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setBoolean(1, n.isEnabled());
            ps.setString(2, n.getEmail());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa email
     */
    public boolean delete(String email) throws Exception {
        String sql = "DELETE FROM NEWSLETTERS WHERE Email = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        }
    }
}