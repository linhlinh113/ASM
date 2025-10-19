<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <title>${news.title} - Chi tiết tin tức | ABCNews</title>
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
                <div class="news-detail">
                    <h2>${news.title}</h2>
                    <img src="${news.image}" alt="${news.title}" class="news-detail-img"/>
                    <p>${news.content}</p>
                    <div class="meta">
                        <span class="icon-user"></span> Tác giả: ${news.author}
                        <span class="icon-calendar"></span> Ngày đăng: <c:out value="${news.postedDate}"/>
                        <span class="icon-eye"></span> Lượt xem: ${news.viewCount}
                    </div>
                </div>

                <div class="related-news" style="margin-top:18px;">
                    <h3><span class="icon-link"></span> Bản tin cùng loại</h3>
                    <ul>
                        <c:forEach var="relNews" items="${relatedList}">
                            <li>
                                <a href="${ctx}/NewsDetailServlet?id=${relNews.id}">${relNews.title}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
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