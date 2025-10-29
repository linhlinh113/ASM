package com.abcnews.dao;

import com.abcnews.entity.News;
import com.abcnews.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    // === CÁC PHƯƠNG THỨC CHO ADMIN VÀ REPORTER ===

    /**
     * Lấy TẤT CẢ tin tức (bao gồm cả tin chưa duyệt)
     * Dùng cho trang Admin quản lý
     */
    public List<News> getAllForAdmin() throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapNews(rs));
            }
        }
        return list;
    }

    /**
     * Lấy tin tức theo tác giả (cho trang của Reporter)
     * Hiển thị cả tin đã duyệt và chưa duyệt
     */
    public List<News> getByAuthor(String authorId) throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS WHERE Author = ? ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNews(rs));
                }
            }
        }
        return list;
    }

    /**
     * Thêm tin mới (do Admin hoặc Reporter)
     */
    public boolean insert(News n) throws Exception {
        String sql = "INSERT INTO NEWS (Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home, Approved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, n.getId());
            ps.setString(2, n.getTitle());
            ps.setString(3, n.getContent());
            ps.setString(4, n.getImage());
            if (n.getPostedDate() != null) {
                ps.setDate(5, new java.sql.Date(n.getPostedDate().getTime()));
            } else {
                ps.setDate(5, new java.sql.Date(System.currentTimeMillis())); // Mặc định ngày hiện tại
            }
            ps.setString(6, n.getAuthor());
            ps.setInt(7, n.getViewCount());
            ps.setString(8, n.getCategoryId());
            ps.setBoolean(9, n.isHome());
            // Approved: Nếu là null thì mặc định là false (chờ duyệt)
            ps.setBoolean(10, n.getApproved() != null ? n.getApproved() : false); 
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật tin tức
     */
    public boolean update(News n) throws Exception {
        String sql = "UPDATE NEWS SET Title=?, Content=?, Image=?, PostedDate=?, Author=?, ViewCount=?, CategoryId=?, Home=?, Approved=? WHERE Id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, n.getTitle());
            ps.setString(2, n.getContent());
            ps.setString(3, n.getImage());
            if (n.getPostedDate() != null) {
                ps.setDate(4, new java.sql.Date(n.getPostedDate().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, n.getAuthor());
            ps.setInt(6, n.getViewCount());
            ps.setString(7, n.getCategoryId());
            ps.setBoolean(8, n.isHome());
            ps.setBoolean(9, n.getApproved() != null ? n.getApproved() : false);
            ps.setString(10, n.getId());
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa tin tức
     */
    public boolean delete(String id) throws Exception {
        String sql = "DELETE FROM NEWS WHERE Id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * Duyệt / Bỏ duyệt tin (dùng cho Admin)
     */
    public boolean setApprovalStatus(String id, boolean status) throws Exception {
         String sql = "UPDATE NEWS SET Approved = ? WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setBoolean(1, status);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        }
    }


    // === CÁC PHƯƠNG THỨC CHO ĐỌC GIẢ (PUBLIC) ===
    // Tất cả các phương thức public đều phải lọc "AND Approved = 1"

    /**
     * Lấy tin theo ID (phải được duyệt)
     */
    public News getById(String id) throws Exception {
        String sql = "SELECT * FROM NEWS WHERE Id = ? AND Approved = 1";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapNews(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Tăng lượt xem
     */
    public void incrementViewCount(String id) throws Exception {
        String sql = "UPDATE NEWS SET ViewCount = ViewCount + 1 WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Lấy tin theo danh mục (đã duyệt)
     */
    public List<News> getByCategory(String categoryId) throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS WHERE CategoryId = ? AND Approved = 1 ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNews(rs));
                }
            }
        }
        return list;
    }

    /**
     * Lấy tin hot (TOP 5 lượt xem, đã duyệt)
     */
    public List<News> getHotNews(int limit) throws Exception {
        List<News> list = new ArrayList<>();
        // "SELECT TOP (?)" là cú pháp của SQL Server
        String sql = "SELECT TOP (?) * FROM NEWS WHERE Approved = 1 ORDER BY ViewCount DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNews(rs));
                }
            }
        }
        return list;
    }

    /**
     * Lấy tin mới nhất (TOP 5 ngày đăng, đã duyệt)
     */
    public List<News> getNewestNews(int limit) throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM NEWS WHERE Approved = 1 ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNews(rs));
                }
            }
        }
        return list;
    }

    /**
     * Lấy tin hiển thị trang chủ (Home = 1, đã duyệt)
     */
    public List<News> getHomeNews() throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS WHERE Home = 1 AND Approved = 1 ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapNews(rs));
            }
        }
        return list;
    }
    
    /**
     * Tìm kiếm tin tức (theo Tiêu đề hoặc Nội dung, đã duyệt)
     */
    public List<News> search(String keyword) throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS WHERE (Title LIKE ? OR Content LIKE ?) AND Approved = 1 ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNews(rs));
                }
            }
        }
        return list;
    }


    /**
     * Hàm tiện ích private để map ResultSet sang đối tượng News
     */
    private News mapNews(ResultSet rs) throws SQLException {
        News n = new News();
        n.setId(rs.getString("Id"));
        n.setTitle(rs.getString("Title"));
        n.setContent(rs.getString("Content"));
        n.setImage(rs.getString("Image"));
        n.setPostedDate(rs.getDate("PostedDate"));
        n.setAuthor(rs.getString("Author"));
        n.setViewCount(rs.getInt("ViewCount"));
        n.setCategoryId(rs.getString("CategoryId"));
        n.setHome(rs.getBoolean("Home"));
        n.setApproved(rs.getBoolean("Approved"));
        return n;
    }
}