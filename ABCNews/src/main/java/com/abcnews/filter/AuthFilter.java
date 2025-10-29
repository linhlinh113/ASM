package com.abcnews.filter;

import com.abcnews.entity.User;
import jakarta.servlet.*;
// import jakarta.servlet.annotation.WebFilter; // Giữ import cũng được
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// ===>>> ĐÃ COMMENT LẠI @WebFilter ĐỂ TẠM TẮT FILTER NÀY <<<===
/*
@WebFilter(urlPatterns = {
    "/AdminCategoryServlet",
    "/AdminNewsServlet",
    "/AdminUserServlet",
    "/AdminNewsletterServlet",
    "/ReporterNewsServlet",
    "/manage/*" // Bảo vệ tất cả file JSP trong thư mục manage
})
*/
// ===>>> ================================================= <<<===
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

        // **LOGIC KIỂM TRA ĐĂNG NHẬP VÀ PHÂN QUYỀN SẼ TẠM THỜI KHÔNG CHẠY**
        // **VÌ @WebFilter ĐÃ BỊ COMMENT**

        // Tuy nhiên, để code không lỗi, chúng ta vẫn giữ logic kiểm tra user
        // nhưng sẽ bỏ qua phần redirect/forward nếu filter không được kích hoạt
        boolean shouldFilter = true; // Giả sử filter đang hoạt động để kiểm tra logic
        // Trong thực tế, nếu @WebFilter bị comment, hàm doFilter sẽ không bao giờ được gọi
        // cho các URL đó nữa, nên phần code bên dưới sẽ không chạy cho URL admin/manage.

        if (shouldFilter) { // Chỉ là để giữ cấu trúc code
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            if (user == null) {
                // Tạm thời không làm gì nếu chưa đăng nhập khi filter bị tắt
                // if (!uri.equals(loginURI) && !uri.equals(loginPageURI)) {
                //     System.out.println("AuthFilter (DISABLED): Would redirect to login.");
                //     // resp.sendRedirect(loginPageURI + "?error=Vui lòng đăng nhập để tiếp tục!");
                //     // return; // Không return
                // }
            } else {
                boolean isAdmin = user.isRole();
                if (!isAdmin) {
                    if (uri.contains("AdminUserServlet") ||
                        uri.contains("AdminCategoryServlet") ||
                        uri.contains("AdminNewsletterServlet") ||
                        uri.contains("manage/user_crud.jsp") ||
                        uri.contains("manage/category_crud.jsp") ||
                        uri.contains("manage/newsletter_crud.jsp")) {
                        System.out.println("AuthFilter (DISABLED): Would deny reporter access to " + uri);
                        // req.setAttribute("error", "Bạn không có quyền truy cập chức năng này!");
                        // req.getRequestDispatcher("/manage/reporter_home.jsp").forward(req, resp);
                        // return; // Không return
                    }
                }
            }
        }


        // Luôn cho phép request tiếp tục khi filter bị tắt
        System.out.println("AuthFilter (DISABLED): Passing through request for " + uri); // Log để biết filter bị tắt
        chain.doFilter(request, response);
    }
}