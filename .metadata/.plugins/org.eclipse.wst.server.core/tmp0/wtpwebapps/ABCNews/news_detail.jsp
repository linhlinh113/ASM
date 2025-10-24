<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="${news.title}"/>
</jsp:include>

<div class="container" style="margin-top: 15px;">
    <c:if test="${not empty error}">
        <div class="alert alert-error">
            <i class="fa-solid fa-triangle-exclamation"></i> ${error}
        </div>
    </c:if>
</div>

<div class="page-wrap container">
    <div class="main-layout">
        
        <jsp:include page="/layout/left_sidebar.jsp" />

        <main class="main-content">
            <c:if test="${not empty news}">
                <article class="news-detail-content">
                    <h1 class="section-title" style="font-size: 2rem; line-height: 1.4;">${news.title}</h1>
                    
                    <div class="news-detail-meta" style="margin-bottom: 20px; color: var(--muted-color); border-bottom: 1px solid var(--border-color); padding-bottom: 15px;">
                        <span style="margin-right: 15px;"><i class="fa-solid fa-user"></i> ${news.author}</span>
                        <span style="margin-right: 15px;"><i class="fa-solid fa-calendar-days"></i> ${news.postedDate}</span>
                        <span><i class="fa-solid fa-eye"></i> ${news.viewCount} lượt xem</span>
                    </div>
                    
                    <img src="${news.image}" alt="${news.title}" style="width: 100%; border-radius: var(--radius-md); margin-bottom: 20px;">
                    
                    <div class="news-content-body" style="font-size: 1.1rem; line-height: 1.7;">
                        <p>${news.content}</p>
                        </div>
                </article>

                <section class="related-news" style="margin-top: 30px;">
                    <h2 class="section-title" style="font-size: 1.5rem;">
                        <i class="fa-solid fa-paperclip"></i> Tin liên quan
                    </h2>
                    <div class="list-block">
                        <c:forEach var="rel" items="${relatedList}">
                            <a href="${ctx}/NewsDetailServlet?id=${rel.id}" class="list-item">
                                <img src="${rel.image}" alt="${rel.title}">
                                <div class="list-item-content">
                                    <h5 class="list-item-title">${rel.title}</h5>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </section>
            </c:if>
        </main>

        <jsp:include page="/layout/right_sidebar.jsp" />
        
    </div>
</div>

<jsp:include page="/layout/footer.jsp" />