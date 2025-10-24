<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<aside class="sidebar">
    <h4 class="sidebar-title">
        <i class="fa-solid fa-magnifying-glass"></i> Tìm kiếm
    </h4>
    <form action="${ctx}/SearchServlet" method="get" class="search-form">
        <input type="text" name="keyword" placeholder="Nhập từ khóa..." required>
        <button type="submit"><i class="fa-solid fa-arrow-right"></i></button>
    </form>
    
    <h4 class="sidebar-title" style="margin-top: 20px;">
        <i class="fa-solid fa-list"></i> Danh mục
    </h4>
    <ul class="category-list">
        <c:forEach var="cat" items="${categoryList}">
            <li>
                <a href="${ctx}/NewsListServlet?catId=${cat.id}">
                    <i class="fa-solid fa-chevron-right"></i> ${cat.name}
                </a>
            </li>
        </c:forEach>
    </ul>
</aside>