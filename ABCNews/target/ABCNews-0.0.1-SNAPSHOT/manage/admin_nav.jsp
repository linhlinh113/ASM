<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> <%-- THÊM DÒNG NÀY --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.user}" />

<aside class="admin-nav">
    <h3>Xin chào, ${user.fullname}!</h3>
    <ul class="admin-nav-list">
        <li>
            <a href="${ctx}/" target="_blank">
                <i class="fa-solid fa-globe"></i> Xem trang Public
            </a>
        </li>
    </ul>

    <h3>Chức năng Phóng viên</h3>
    <ul class="admin-nav-list">
        <li>
            <a href="${ctx}/ReporterNewsServlet" 
               class="${fn:contains(pageContext.request.servletPath, 'ReporterNewsServlet') ? 'active' : ''}">
                <i class="fa-solid fa-newspaper"></i> Quản lý Tin tức (Cá nhân)
            </a>
        </li>
    </ul>

    <c:if test="${user.role}">
        <h3>Chức năng Quản trị</h3>
        <ul class="admin-nav-list">
            <li>
                <a href="${ctx}/AdminUserServlet"
                   class="${fn:contains(pageContext.request.servletPath, 'AdminUserServlet') ? 'active' : ''}">
                    <i class="fa-solid fa-users"></i> Quản lý Người dùng
                </a>
            </li>
            <li>
                <a href="${ctx}/AdminCategoryServlet"
                   class="${fn:contains(pageContext.request.servletPath, 'AdminCategoryServlet') ? 'active' : ''}">
                    <i class="fa-solid fa-list-ul"></i> Quản lý Danh mục
                </a>
            </li>
            <li>
                <a href="${ctx}/AdminNewsServlet"
                   class="${fn:contains(pageContext.request.servletPath, 'AdminNewsServlet') ? 'active' : ''}">
                    <i class="fa-solid fa-file-shield"></i> Duyệt Tin tức (Tất cả)
                </a>
            </li>
            <li>
                <a href="${ctx}/AdminNewsletterServlet"
                   class="${fn:contains(pageContext.request.servletPath, 'AdminNewsletterServlet') ? 'active' : ''}">
                    <i class="fa-solid fa-envelope"></i> Quản lý Newsletter
                </a>
            </li>
        </ul>
    </c:if>
</aside>