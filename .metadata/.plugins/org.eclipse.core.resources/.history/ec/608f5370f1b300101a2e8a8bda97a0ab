package com.abcnews.filter;

import com.abcnews.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {
    "/AdminCategoryServlet", 
    "/AdminNewsServlet", 
    "/AdminUserServlet", 
    "/AdminNewsletterServlet", 
    "/ReporterNewsServlet",
    "/manage/*" // Bảo vệ tất cả file JSP trong thư mục manage
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); // false = không tạo session mới nếu chưa có

        String contextPath = req.getContextPath();
        String loginURI = contextPath + "/LoginServlet";
        String loginPageURI = contextPath + "/login.jsp";
        
        // Lấy URI hiện tại
        String uri = req.getRequestURI();

        // 1. Kiểm tra xem người dùng đã đăng nhập chưa
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang login
            // (Tránh vòng lặp vô tận bằng cách kiểm tra có phải đang truy cập trang login không)
            if (!uri.equals(loginURI) && !uri.equals(loginPageURI)) {
                System.out.println("AuthFilter: Chưa đăng nhập. Chuyển đến " + loginPageURI);
                resp.sendRedirect(loginPageURI + "?error=Vui lòng đăng nhập để tiếp tục!");
                return;
            }
        
        } else {
            // 2. Đã đăng nhập -> Kiểm tra vai trò (Phân quyền)
            boolean isAdmin = user.isRole();
            
            // Nếu là Reporter (false)
            if (!isAdmin) {
                // Reporter cố truy cập vào các trang của Admin
                if (uri.contains("AdminUserServlet") || 
                    uri.contains("AdminCategoryServlet") || 
                    uri.contains("AdminNewsletterServlet") ||
                    uri.contains("manage/user_crud.jsp") ||
                    uri.contains("manage/category_crud.jsp") ||
                    uri.contains("manage/newsletter_crud.jsp")) {
                    
                    System.out.println("AuthFilter: Reporter bị từ chối truy cập " + uri);
                    // Chuyển về trang chủ của Reporter với thông báo lỗi
                    req.setAttribute("error", "Bạn không có quyền truy cập chức năng này!");
                    // Chúng ta sẽ tạo trang reporter_home.jsp sau
                    req.getRequestDispatcher("/manage/reporter_home.jsp").forward(req, resp); 
                    return;
                }
            }
            // Nếu là Admin (true) -> không làm gì, cho qua
            // Nếu là Reporter truy cập đúng trang của Reporter -> không làm gì, cho qua
        }
        
        // Cho phép request tiếp tục
        chain.doFilter(request, response);
    }
}