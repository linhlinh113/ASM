<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách tin tức | ABCNews</title>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css"/>
    <link rel="icon" type="image/png" href="${ctx}/assets/img/newsicon.png"/>
</head>
<body>
<jsp:include page="/layout/header.jsp"/>

<div class="page-wrap">
    <div class="layout">
        <div id="left-sidebar" class="sidebar left-sidebar sticky">
            <jsp:include page="/layout/leftSidebar.jsp"/>
        </div>

        <div class="main">
            <div class="container">
                <h2 class="main-title"><img src="${ctx}/assets/img/newsicon.png" width="32" alt="News"/> Danh sách tin</h2>

                <div class="category-menu">
                    <c:forEach var="cat" items="${categoryList}">
                        <a href="${ctx}/NewsListServlet?cat=${cat.id}" class="cat-btn">${cat.name}</a>
                    </c:forEach>
                </div>

                <div class="news-grid">
                    <c:forEach var="news" items="${newsList}">
                        <div class="news-card">
                            <a href="${ctx}/NewsDetailServlet?id=${news.id}">
                                <div class="thumb"><img src="${news.image}" alt="${news.title}"/></div>
                                <div class="meta">
                                    <div class="news-title">${news.title}</div>
                                    <div class="info">
                                        <span class="date"><c:out value="${news.postedDate}"/></span>
                                        <span class="view-count">${news.viewCount} lượt xem</span>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${empty newsList}">
                    <div class="alert error">Không có bản tin nào thuộc loại này!</div>
                </c:if>
            </div>
        </div>

        <aside id="right-sidebar" class="sidebar right-sidebar sticky">
            <jsp:include page="/layout/rightSidebar.jsp"/>
        </aside>
    </div>
</div>

<jsp:include page="/layout/footer.jsp"/>
<script src="${ctx}/assets/js/sidebar.js"></script>
</body>
</html>