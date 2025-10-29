package com.abcnews.util;

import com.abcnews.entity.News;
import com.abcnews.entity.Newsletter;

import java.util.List;
import java.util.Properties;

// ===>>> THAY ĐỔI IMPORT TỪ javax.mail SANG jakarta.mail <<<===
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
// ===>>> =============================================== <<<===

public class EmailUtil {

    // === CẤU HÌNH EMAIL VÀ MẬT KHẨU ỨNG DỤNG (Giữ nguyên) ===
    private static final String APP_EMAIL = "linh1172004@gmail.com";
    private static final String APP_PASSWORD = "zshiynrdblwtckon";

    // === CẤU HÌNH MÁY CHỦ (Giữ nguyên SSL/465) ===
    private static final String HOST_NAME = "smtp.gmail.com";
    private static final int SSL_PORT = 465;

    public static void sendEmail(String toEmail, String subject, String content) throws Exception {
        System.out.println("Chuẩn bị gửi email tới: " + toEmail + " | Chủ đề: " + subject);
        System.out.println("Sử dụng email gửi: " + APP_EMAIL);

        // === CẤU HÌNH SSL (Giữ nguyên) ===
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST_NAME);
        props.put("mail.smtp.port", String.valueOf(SSL_PORT)); // Chuyển port sang String
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", String.valueOf(SSL_PORT)); // Chuyển port sang String
        props.put("mail.smtp.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory"); // <<< THAY ĐỔI javax -> jakarta
        props.put("mail.smtp.ssl.enable", "true"); // Giữ nguyên
        // Thêm các thuộc tính TLS versions (đề phòng)
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");

        // === Authenticator và Session (Giữ nguyên, chỉ thay đổi import) ===
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(APP_EMAIL, APP_PASSWORD);
            }
        };
        Session session = Session.getInstance(props, auth);
        session.setDebug(true); // Vẫn bật Debug

        // === MimeMessage (Giữ nguyên, chỉ thay đổi import) ===
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(APP_EMAIL, "ABCNews"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject, "UTF-8");
        msg.setContent(content, "text/html; charset=UTF-8");
        msg.setSentDate(new java.util.Date());

        System.out.println("Đang gửi email (qua Jakarta Mail - SSL)..."); // Cập nhật log
        Transport.send(msg);
        System.out.println("Đã gửi email thành công tới: " + toEmail);
    }

    // Hàm sendNewPostNotification giữ nguyên logic, chỉ thay đổi import
    public static void sendNewPostNotification(News news, List<Newsletter> subscribers) {
         // ... (code không đổi) ...
         if (news == null || subscribers == null || subscribers.isEmpty()) {
            System.out.println("sendNewPostNotification: Không có tin tức hoặc không có người đăng ký.");
            return;
        }

        String subject = "[ABCNews] Tin mới: " + news.getTitle();
        String newsLink = "http://localhost:8080/ABCNews/NewsDetailServlet?id=" + news.getId();

        String content = String.format( /* ... code tạo content giữ nguyên ... */
             "<html><body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>" +
            "<div style='max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;'>" +
            "<h2 style='color: #0056b3; border-bottom: 2px solid #0056b3; padding-bottom: 10px;'>Tin tức mới từ ABCNews</h2>" +
            "<h3 style='color: #007bff;'>%s</h3>" +
            (news.getImage() != null && !news.getImage().isEmpty() ?
             "<p><img src='%s' alt='Ảnh minh họa' style='max-width: 100%%; height: auto; border-radius: 5px;'></p>" : "") +
            "<p>%s...</p>" +
            "<p style='margin-top: 20px;'>" +
            "<a href='%s' style='display: inline-block; padding: 12px 20px; background-color: #0056b3; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;'>" +
            "Đọc chi tiết</a>" +
            "</p>" +
            "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>" +
            "<p style='font-size: 0.9em; color: #777;'>Bạn nhận được email này vì đã đăng ký nhận tin tại ABCNews.</p>" +
            "</div></body></html>",
            news.getTitle(),
            news.getImage() != null ? news.getImage() : "",
            news.getContent() != null ? news.getContent().substring(0, Math.min(news.getContent().length(), 200)) : "",
            newsLink
        );

        int count = 0;
        for (Newsletter sub : subscribers) {
            if (sub.isEnabled()) {
                try {
                    sendEmail(sub.getEmail(), subject, content);
                    count++;
                } catch (Exception e) {
                    System.err.println("Lỗi gửi mail tới " + sub.getEmail() + ": " + e.getMessage());
                }
            } else {
                 System.out.println("Bỏ qua email bị tắt: " + sub.getEmail());
            }
        }
         System.out.println("Hoàn tất gửi thông báo. Đã gửi tới " + count + "/" + subscribers.size() + " email.");
    }
}