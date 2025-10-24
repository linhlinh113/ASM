package com.abcnews.dao;

import com.abcnews.entity.Category;
import com.abcnews.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    /**
     * Lấy tất cả danh mục
     */
    public List<Category> getAll() throws Exception {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM CATEGORIES ORDER BY Name";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Category cat = new Category();
                cat.setId(rs.getString("Id"));
                cat.setName(rs.getString("Name"));
                list.add(cat);
            }
        } catch (SQLException e) {
            throw new Exception("Lỗi khi lấy danh sách danh mục: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy danh mục theo ID
     */
    public Category getById(String id) throws Exception {
        String sql = "SELECT * FROM CATEGORIES WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category cat = new Category();
                    cat.setId(rs.getString("Id"));
                    cat.setName(rs.getString("Name"));
                    return cat;
                }
            }
        } catch (SQLException e) {
            throw new Exception("Lỗi khi lấy danh mục theo ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Thêm danh mục mới
     */
    public boolean insert(Category cat) throws Exception {
        String sql = "INSERT INTO CATEGORIES (Id, Name) VALUES (?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cat.getId());
            ps.setString(2, cat.getName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("Lỗi khi thêm danh mục: " + e.getMessage());
        }
    }

    /**
     * Cập nhật danh mục
     */
    public boolean update(Category cat) throws Exception {
        String sql = "UPDATE CATEGORIES SET Name = ? WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cat.getName());
            ps.setString(2, cat.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("Lỗi khi cập nhật danh mục: " + e.getMessage());
        }
    }

    /**
     * Xóa danh mục
     */
    public boolean delete(String id) throws Exception {
        String sql = "DELETE FROM CATEGORIES WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Xử lý lỗi khóa ngoại (nếu có tin tức thuộc danh mục này)
            if (e.getMessage().contains("FOREIGN KEY constraint")) {
                throw new Exception("Không thể xóa danh mục này vì vẫn còn tin tức thuộc về nó.");
            }
            throw new Exception("Lỗi khi xóa danh mục: " + e.getMessage());
        }
    }
}