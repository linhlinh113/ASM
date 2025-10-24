package com.abcnews.util;

import com.abcnews.entity.News;
import com.abcnews.entity.Newsletter;

import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

    // === CẤU HÌNH (THAY ĐỔI 2 DÒNG NÀY) ===
    // 1. Email của bạn (dùng để gửi)
    private static final String APP_EMAIL = "your-email@gmail.com"; 
    
    // 2. Mật khẩu Ứng dụng (16 ký tự) - XEM HƯỚNG DẪN BÊN DƯỚI
    private static final String APP_PASSWORD = "your-16-digit-app-password"; 
    
    // === CẤU HÌNH MÁY CHỦ (Giữ nguyên) ===
    private static final String HOST_NAME = "smtp.gmail.com";
    private static final int SSL_PORT = 465; // Port for SSL
    private static final int TSL_PORT = 587; // Port for TSL
    

    /**
     * Gửi một email cơ bản
     * @param toEmail Email người nhận
     * @param subject Tiêu đề
     * @param content Nội dung (Hỗ trợ HTML)
     * @throws Exception
     */
    public static void sendEmail(String toEmail, String subject, String content) throws Exception {
        System.out.println("Bắt đầu gửi email tới: " + toEmail);

        // 1. Cài đặt thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST_NAME);
        props.put("mail.smtp.port", TSL_PORT); // Sử dụng TSL

        // 2. Tạo phiên (Session) với Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(APP_EMAIL, APP_PASSWORD);
            }
        };
        Session session = Session.getInstance(props, auth);

        // 3. Tạo tin nhắn
        MimeMessage msg = new MimeMessage(session);
        
        // Kiểu "ABCNews <your-email@gmail.com>"
        msg.setFrom(new InternetAddress(APP_EMAIL, "ABCNews"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject, "UTF-8");
        
        // Đặt nội dung là HTML
        msg.setContent(content, "text/html; charset=UTF-8");

        // 4. Gửi mail
        Transport.send(msg);
        
        System.out.println("Gửi email thành công!");
    }

    /**
     * Hàm tiện ích để gửi thông báo tin mới cho tất cả subscriber
     * @param news Bài báo vừa được duyệt
     * @param subscribers Danh sách email
     */
    public static void sendNewPostNotification(News news, List<Newsletter> subscribers) {
        String subject = "[ABCNews] Tin mới: " + news.getTitle();
        
        // Tạo nội dung email HTML
        String content = "<html><body style='font-family: Arial, sans-serif;'>"
            + "<h2 style='color: #0056b3;'>Tin tức mới từ ABCNews</h2>"
            + "<h3>" + news.getTitle() + "</h3>"
            + "<img src='" + news.getImage() + "' alt='Ảnh minh họa' style='max-width: 400px;'>"
            + "<p>" + news.getContent().substring(0, Math.min(news.getContent().length(), 150)) + "...</p>" // Lấy 150 ký tự đầu
            + "<a href='#' style='padding: 10px 15px; background-color: #0056b3; color: white; text-decoration: none; border-radius: 5px;'>"
            + "Đọc chi tiết</a>" // Chú ý: Cần thay # bằng link thật, ví dụ: "http://your-domain.com/NewsDetailServlet?id=" + news.getId()
            + "</body></html>";

        // Gửi cho từng người
        for (Newsletter sub : subscribers) {
            if (sub.isEnabled()) {
                try {
                    sendEmail(sub.getEmail(), subject, content);
                } catch (Exception e) {
                    System.err.println("Lỗi gửi mail tới " + sub.getEmail() + ": " + e.getMessage());
                    // Tiếp tục gửi cho người khác
                }
            }
        }
    }
}