<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<aside class="sidebar">
    <h4 class="sidebar-title">
        <i class="fa-solid fa-fire-flame-curved"></i> Tin hot nhất
    </h4>
    <div class="list-block">
        <c:forEach var="news" items="${hotList}">
            <a href="${ctx}/NewsDetailServlet?id=${news.id}" class="list-item">
                <img src="${news.image}" alt="${news.title}">
                <div class="list-item-content">
                    <h5 class="list-item-title">${news.title}</h5>
                    <div class="list-item-meta">
                        <i class="fa-solid fa-eye"></i> ${news.viewCount}
                    </div>
                </div>
            </a>
        </c:forEach>
    </div>
    
    <h4 class="sidebar-title" style="margin-top: 25px;">
        <i class="fa-solid fa-clock"></i> Mới nhất
    </h4>
    <div class="list-block">
        <c:forEach var="news" items="${newestList}">
            <a href="${ctx}/NewsDetailServlet?id=${news.id}" class="list-item">
                <img src="${news.image}" alt="${news.title}">
                <div class="list-item-content">
                    <h5 class="list-item-title">${news.title}</h5>
                    <div class="list-item-meta">
                        <i class="fa-solid fa-calendar-days"></i> ${news.postedDate}
                    </div>
                </div>
            </a>
        </c:forEach>
    </div>
    
    <h4 class="sidebar-title" style="margin-top: 25px;">
        <i class="fa-solid fa-history"></i> Đã xem gần đây
    </h4>
    <div class="recent-view-list">
        <c:if test="${empty sessionScope.recentViewMap}">
            <p style="color: var(--muted-color); font-size: 0.9rem;">Chưa có tin nào được xem gần đây.</p>
        </c:if>
        
        <c:forEach var="entry" items="${sessionScope.recentViewMap}">
            <c:set var="news" value="${entry.value}" />
            <a href="${ctx}/NewsDetailServlet?id=${news.id}" title="${news.title}">
                <img src="${news.image}" alt="${news.title}">
            </a>
        </c:forEach>
    </div>
</aside>