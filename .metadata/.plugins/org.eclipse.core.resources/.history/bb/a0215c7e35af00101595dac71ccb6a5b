package com.abcnews.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=abcnews;encrypt=false;sendStringParametersAsUnicode=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456"; 

    public static Connection getConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}