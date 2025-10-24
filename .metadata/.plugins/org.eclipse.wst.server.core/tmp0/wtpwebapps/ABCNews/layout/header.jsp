<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.user}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>ABCNews - Tin tức hàng đầu Việt Nam</title>
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
    
    <script defer src="${ctx}/assets/js/main.js"></script>
</head>
<body>
    <header class="header-bar">
        <div class="header-container">
            <a href="${ctx}/" class="brand" style="text-decoration: none;">
                <span class="brand-logo"><i class="fa-solid fa-newspaper"></i></span>
                <span class="brand-title">ABCNews</span>
            </a>
            
            <nav class="header-nav">
                <a href="${ctx}/" class="nav-link">
                    <i class="fa-solid fa-house"></i> Trang chủ
                </a>
                
                <div class="user-menu">
                    <c:if test="${empty user}">
                        <a href="${ctx}/LoginServlet" class="nav-link btn-login">
                            <i class="fa-solid fa-right-to-bracket"></i> Đăng nhập
                        </a>
                    </c:if>
                    
                    <c:if test="${not empty user}">
                        <span class="user-info">
                            <i class="fa-solid fa-user-check"></i> Xin chào, ${user.fullname}
                        </span>
                        
                        <c:if test="${user.role}">
                            <a href="${ctx}/manage/admin_home.jsp" class="nav-link">
                                <i class="fa-solid fa-shield-halved"></i> Quản trị
                            </a>
                        </c:if>
                        <c:if test="${not user.role}">
                            <a href="${ctx}/manage/reporter_home.jsp" class="nav-link">
                                <i class="fa-solid fa-pen-to-square"></i> Quản lý tin
                            </a>
                        </c:if>
                        
                        <a href="${ctx}/LogoutServlet" class="nav-link">
                            <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
                        </a>
                    </c:if>
                </div>
            </nav>
        </div>
    </header>