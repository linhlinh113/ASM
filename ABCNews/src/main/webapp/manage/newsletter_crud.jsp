<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Quản lý Newsletter"/>
</jsp:include>

<div class="page-wrap container admin-layout">
    
    <jsp:include page="/manage/admin_nav.jsp" />

    <main class="admin-content">
        <h2><i class="fa-solid fa-envelope"></i> Quản lý Newsletter</h2>
        
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session" />
        </c:if>

        <h3>Danh sách Email Đăng ký</h3>
        <table class="crud-table">
            <thead>
                <tr>
                    <th>Email</th>
                    <th>Trạng thái</th>
                    <th style="width: 220px;">Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="nl" items="${newsletterList}">
                    <tr>
                        <td>${nl.email}</td>
                        <td>
                            <c:if test="${nl.enabled}">
                                <span class="status status-approved">
                                    <i class="fa-solid fa-check"></i> Đang hoạt động
                                </span>
                            </c:if>
                            <c:if test="${!nl.enabled}">
                                <span class="status status-pending">
                                    <i class="fa-solid fa-times"></i> Đã hủy
                                </span>
                            </c:if>
                        </td>
                        <td class="action-buttons">
                            <form action="AdminNewsletterServlet" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="toggle">
                                <input type="hidden" name="email" value="${nl.email}">
                                <button type="submit" class="btn btn-edit">
                                    <i class="fa-solid fa-toggle-${nl.enabled ? 'on' : 'off'}"></i> ${nl.enabled ? 'Tắt' : 'Bật'}
                                </button>
                            </form>
                            
                            <form action="AdminNewsletterServlet" method="post" onsubmit="return confirmDelete(event);" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="email" value="${nl.email}">
                                <button type="submit" class="btn btn-delete">
                                    <i class="fa-solid fa-trash"></i> Xóa
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
    </main>
</div>

<jsp:include page="/layout/footer.jsp" />