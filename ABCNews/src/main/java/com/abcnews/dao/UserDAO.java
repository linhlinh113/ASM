package com.abcnews.dao;

import com.abcnews.entity.User;
import com.abcnews.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAO {

    public User login(String id, String password) throws Exception {
        String sql = "SELECT * FROM USERS WHERE Id = ? AND Password = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getString("Id"));
                    user.setPassword(rs.getString("Password"));
                    user.setFullname(rs.getString("Fullname"));
                    user.setBirthday(rs.getDate("Birthday"));
                    user.setGender(rs.getBoolean("Gender"));
                    user.setMobile(rs.getString("Mobile"));
                    user.setEmail(rs.getString("Email"));
                    user.setRole(rs.getBoolean("Role"));
                    return user;
                }
            }
        }
        return null;
    }

    public List<User> getAll() throws Exception {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM USERS";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("Id"));
                user.setPassword(rs.getString("Password"));
                user.setFullname(rs.getString("Fullname"));
                user.setBirthday(rs.getDate("Birthday"));
                user.setGender(rs.getBoolean("Gender"));
                user.setMobile(rs.getString("Mobile"));
                user.setEmail(rs.getString("Email"));
                user.setRole(rs.getBoolean("Role"));
                list.add(user);
            }
        }
        return list;
    }

    public User getById(String id) throws Exception {
        String sql = "SELECT * FROM USERS WHERE Id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getString("Id"));
                    user.setPassword(rs.getString("Password"));
                    user.setFullname(rs.getString("Fullname"));
                    user.setBirthday(rs.getDate("Birthday"));
                    user.setGender(rs.getBoolean("Gender"));
                    user.setMobile(rs.getString("Mobile"));
                    user.setEmail(rs.getString("Email"));
                    user.setRole(rs.getBoolean("Role"));
                    return user;
                }
            }
        }
        return null;
    }

    public boolean insert(User user) throws Exception {
        String sql = "INSERT INTO USERS (Id, Password, Fullname, Birthday, Gender, Mobile, Email, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getPassword());
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

    public boolean update(User user) throws Exception {
        String sql = "UPDATE USERS SET Password=?, Fullname=?, Birthday=?, Gender=?, Mobile=?, Email=?, Role=? WHERE Id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getPassword());
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

    public boolean delete(String id) throws Exception {
        String sql = "DELETE FROM USERS WHERE Id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}