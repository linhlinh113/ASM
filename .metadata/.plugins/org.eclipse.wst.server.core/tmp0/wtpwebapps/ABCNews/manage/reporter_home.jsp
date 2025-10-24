<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Trang Phóng viên"/>
</jsp:include>

<div class="page-wrap container admin-layout">
    
    <jsp:include page="/manage/admin_nav.jsp" />

    <main class="admin-content">
        <h2><i class="fa-solid fa-pen-to-square"></i> Chào mừng Phóng viên</h2>
        <p>Chào mừng ${sessionScope.user.fullname} đến với trang quản lý tin tức cá nhân.</p>
        <p>Vui lòng chọn chức năng "Quản lý Tin tức" từ menu bên trái để bắt đầu.</p>
        
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
    </main>
    
</div>

<jsp:include page="/layout/footer.jsp" />