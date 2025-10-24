package com.abcnews.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    
    // Cấu hình thông tin kết nối CSDL
    // sendStringParametersAsUnicode=true: Để hỗ trợ Tiếng Việt (NVARCHAR)
    // encrypt=false: Tắt mã hóa SSL (nếu dùng SQL Server bản mới có thể cần bật)
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=abcnews;encrypt=false;sendStringParametersAsUnicode=true";
    private static final String USER = "sa"; // Tên đăng nhập SQL Server
    private static final String PASSWORD = "123456"; // Mật khẩu SQL Server
    private static final String DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    /**
     * Lấy kết nối đến CSDL
     * @return một đối tượng Connection
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        try {
            // 1. Nạp driver
            Class.forName(DRIVER_NAME);
            
            // 2. Lấy kết nối
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // Ném ra lỗi để các lớp DAO có thể xử lý
            throw new Exception("Lỗi kết nối CSDL: " + e.getMessage());
        }
    }
}