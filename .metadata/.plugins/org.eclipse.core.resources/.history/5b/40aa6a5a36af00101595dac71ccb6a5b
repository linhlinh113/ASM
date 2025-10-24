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

    // Lấy tất cả tin tức (không lọc Approved, dùng cho admin)
    public List<News> getAll() throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
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
                list.add(n);
                System.out.println("[" + new java.util.Date() + "] getAll added: " + n.getId() + " - " + n.getTitle());
            }
            System.out.println("[" + new java.util.Date() + "] getAll returned " + list.size() + " items. SQL: " + sql);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in getAll: " + e.getMessage());
            throw new Exception("Lỗi khi lấy danh sách tin tức: " + e.getMessage());
        }
        return list;
    }

    // Lấy tin theo ID
    public News getById(String id) throws Exception {
        String sql = "SELECT * FROM NEWS WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
                    System.out.println("[" + new java.util.Date() + "] getById found: " + n.getId() + " - " + n.getTitle());
                    return n;
                } else {
                    System.out.println("[" + new java.util.Date() + "] getById not found for ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in getById: " + e.getMessage());
            throw new Exception("Lỗi khi lấy tin theo ID: " + e.getMessage());
        }
        return null;
    }

    // Thêm tin mới
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
                ps.setNull(5, Types.DATE);
            }
            ps.setString(6, n.getAuthor());
            ps.setInt(7, n.getViewCount());
            ps.setString(8, n.getCategoryId());
            ps.setBoolean(9, n.isHome());
            ps.setBoolean(10, n.isApproved() != null ? n.isApproved() : false);
            int rowsAffected = ps.executeUpdate();
            System.out.println("[" + new java.util.Date() + "] insert affected " + rowsAffected + " rows for ID: " + n.getId());
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in insert: " + e.getMessage());
            throw new Exception("Lỗi khi thêm tin tức: " + e.getMessage());
        }
    }

    // Cập nhật tin
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
            ps.setBoolean(9, n.isApproved() != null ? n.isApproved() : false);
            ps.setString(10, n.getId());
            int rowsAffected = ps.executeUpdate();
            System.out.println("[" + new java.util.Date() + "] update affected " + rowsAffected + " rows for ID: " + n.getId());
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in update: " + e.getMessage());
            throw new Exception("Lỗi khi cập nhật tin tức: " + e.getMessage());
        }
    }

    // Xóa tin
    public boolean delete(String id) throws Exception {
        String sql = "DELETE FROM NEWS WHERE Id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();
            System.out.println("[" + new java.util.Date() + "] delete affected " + rowsAffected + " rows for ID: " + id);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in delete: " + e.getMessage());
            throw new Exception("Lỗi khi xóa tin tức: " + e.getMessage());
        }
    }

    // Tăng lượt xem
    public void incrementViewCount(String id) throws Exception {
        String sql = "UPDATE NEWS SET ViewCount = ViewCount + 1 WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();
            System.out.println("[" + new java.util.Date() + "] incrementViewCount affected " + rowsAffected + " rows for ID: " + id);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in incrementViewCount: " + e.getMessage());
            throw new Exception("Lỗi khi tăng lượt xem: " + e.getMessage());
        }
    }

    // Lấy tin theo danh mục
    public List<News> getByCategory(String categoryId) throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS WHERE CategoryId = ? AND Approved = 1 ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
                    list.add(n);
                    System.out.println("[" + new java.util.Date() + "] getByCategory added: " + n.getId() + " - " + n.getTitle());
                }
            }
            System.out.println("[" + new java.util.Date() + "] getByCategory returned " + list.size() + " items for category: " + categoryId + ". SQL: " + sql);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in getByCategory: " + e.getMessage());
            throw new Exception("Lỗi khi lấy tin theo danh mục: " + e.getMessage());
        }
        return list;
    }

    // Lấy tin hot
    public List<News> getHotNews(int limit) throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM NEWS WHERE Approved = 1 ORDER BY ViewCount DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
                    list.add(n);
                    System.out.println("[" + new java.util.Date() + "] getHotNews added: " + n.getId() + " - " + n.getTitle() + " (ViewCount: " + n.getViewCount() + ")");
                }
            }
            System.out.println("[" + new java.util.Date() + "] getHotNews returned " + list.size() + " items. SQL: " + sql);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in getHotNews: " + e.getMessage());
            throw new Exception("Lỗi khi lấy tin hot: " + e.getMessage());
        }
        return list;
    }

    // Lấy tin mới nhất
    public List<News> getNewestNews(int limit) throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM NEWS WHERE Approved = 1 ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
                    list.add(n);
                    System.out.println("[" + new java.util.Date() + "] getNewestNews added: " + n.getId() + " - " + n.getTitle() + " (PostedDate: " + n.getPostedDate() + ")");
                }
            }
            System.out.println("[" + new java.util.Date() + "] getNewestNews returned " + list.size() + " items. SQL: " + sql);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in getNewestNews: " + e.getMessage());
            throw new Exception("Lỗi khi lấy tin mới: " + e.getMessage());
        }
        return list;
    }

    // Lấy tin trang chủ
    public List<News> getHomeNews() throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS WHERE Home = 1 AND Approved = 1 ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
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
                list.add(n);
                System.out.println("[" + new java.util.Date() + "] getHomeNews added: " + n.getId() + " - " + n.getTitle() + " (Home: " + n.isHome() + ")");
            }
            System.out.println("[" + new java.util.Date() + "] getHomeNews returned " + list.size() + " items. SQL: " + sql);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in getHomeNews: " + e.getMessage());
            throw new Exception("Lỗi khi lấy tin trang chủ: " + e.getMessage());
        }
        return list;
    }

    // Lấy tin theo tác giả
    public List<News> getByAuthor(String authorId) throws Exception {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWS WHERE Author = ? AND Approved = 1 ORDER BY PostedDate DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
                    list.add(n);
                    System.out.println("[" + new java.util.Date() + "] getByAuthor added: " + n.getId() + " - " + n.getTitle());
                }
            }
            System.out.println("[" + new java.util.Date() + "] getByAuthor returned " + list.size() + " items for author: " + authorId + ". SQL: " + sql);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in getByAuthor: " + e.getMessage());
            throw new Exception("Lỗi khi lấy tin theo tác giả: " + e.getMessage());
        }
        return list;
    }

    // Duyệt tin
    public void approve(String id) throws Exception {
        String sql = "UPDATE NEWS SET Approved = 1 WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();
            System.out.println("[" + new java.util.Date() + "] approve affected " + rowsAffected + " rows for ID: " + id);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in approve: " + e.getMessage());
            throw new Exception("Lỗi khi duyệt tin tức: " + e.getMessage());
        }
    }

    // Từ chối tin
    public void reject(String id) throws Exception {
        String sql = "UPDATE NEWS SET Approved = 0 WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();
            System.out.println("[" + new java.util.Date() + "] reject affected " + rowsAffected + " rows for ID: " + id);
        } catch (SQLException e) {
            System.out.println("[" + new java.util.Date() + "] Error in reject: " + e.getMessage());
            throw new Exception("Lỗi khi từ chối tin tức: " + e.getMessage());
        }
    }
}