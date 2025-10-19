<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <title>Đăng nhập hệ thống ABCNews</title>
    <link rel="icon" type="image/png" href="${ctx}/assets/img/newsicon.png"/>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css"/>
    <!-- Nếu muốn styles riêng cho login, có thể tạo file login.css và import ở đây -->
</head>
<body>
    <!-- include header để hiển thị navbar/menu -->
    <jsp:include page="/layout/header.jsp"/>

    <div class="page-wrap" style="min-height:70vh; display:flex; align-items:center; justify-content:center;">
        <div class="login-box" role="main" aria-labelledby="login-title" style="max-width:420px; width:100%;">
            <h2 id="login-title" style="margin-top:0;"><span class="icon-login"></span> Đăng nhập hệ thống</h2>

            <form action="${ctx}/LoginServlet" method="post" style="display:flex;flex-direction:column;gap:12px;">
                <label for="id" style="font-weight:600;">Mã đăng nhập</label>
                <input id="id" type="text" name="id" placeholder="Mã đăng nhập" required
                       style="padding:10px;border-radius:8px;border:1px solid #e6eef9;font-size:1rem;"/>

                <label for="password" style="font-weight:600;">Mật khẩu</label>
                <input id="password" type="password" name="password" placeholder="Mật khẩu" required
                       style="padding:10px;border-radius:8px;border:1px solid #e6eef9;font-size:1rem;"/>

                <div style="display:flex;justify-content:space-between;align-items:center;gap:12px;">
                    <button type="submit" class="login-btn btn" style="flex:0 0 auto;padding:10px 18px;">
                        <span class="icon-login" aria-hidden="true"></span> Đăng nhập
                    </button>
                    <a href="${ctx}/forgot-password.jsp" style="color:var(--primary); font-size:0.95rem; text-decoration:none;">Quên mật khẩu?</a>
                </div>
            </form>

            <c:if test="${not empty error}">
                <div class="alert error" role="alert" style="margin-top:12px;">${error}</div>
            </c:if>
        </div>
    </div>

    <jsp:include page="/layout/footer.jsp"/>
    <script src="${ctx}/assets/js/sidebar.js"></script>
</body>
</html>