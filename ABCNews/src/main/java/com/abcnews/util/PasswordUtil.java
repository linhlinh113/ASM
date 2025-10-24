package com.abcnews.util;

// KHÔNG CẦN IMPORT THƯ VIỆN MÃ HÓA NÀO CẢ

public class PasswordUtil {

    /**
     * KHÔNG mã hóa mật khẩu, trả về y nguyên.
     * @param plainPassword Mật khẩu gốc.
     * @return Mật khẩu gốc.
     */
    public static String hashPassword(String plainPassword) {
        // Trả về mật khẩu gốc, không làm gì cả
        System.out.println("PasswordUtil (No Hash): hashPassword called, returning plain password."); // Log để biết đang dùng bản nào
        return plainPassword;
    }

    /**
     * So sánh mật khẩu gốc trực tiếp.
     * @param plainPassword Mật khẩu người dùng nhập.
     * @param storedPassword Mật khẩu lưu trong DB (cũng là gốc).
     * @return true nếu khớp, false nếu không.
     */
    public static boolean checkPassword(String plainPassword, String storedPassword) {
        System.out.println("PasswordUtil (No Hash): checkPassword called. Comparing [" + plainPassword + "] with [" + storedPassword + "]"); // Log để biết đang dùng bản nào

        if (plainPassword == null || storedPassword == null) {
            return false;
        }
        // So sánh chuỗi trực tiếp
        boolean result = plainPassword.equals(storedPassword);
        System.out.println("PasswordUtil (No Hash): Comparison result: " + result); // Log kết quả
        return result;
    }
}