<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="ABCNews - Trang chủ"/>
</jsp:include>

<div class="container" style="margin-top: 15px;">
    <c:if test="${not empty error}">
        <div class="alert alert-error">
            <i class="fa-solid fa-triangle-exclamation"></i> ${error}
        </div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success">
            <i class="fa-solid fa-check-circle"></i> ${success}
        </div>
    </c:if>
</div>

<div class="page-wrap container">
    <div class="main-layout">
        
        <jsp:include page="/layout/left_sidebar.jsp" />

        <main class="main-content">
            
            <section>
                <h2 class="section-title">
                    <i class="fa-solid fa-star" style="color: #ffc107;"></i>
                    Tin Trang chủ
                </h2>
                
                <c:if test="${empty homeNews}">
                    <p class="alert alert-info">Chưa có tin nào được ghim lên trang chủ.</p>
                </c:if>
                
                <div class="news-grid">
                    <c:forEach var="news" items="${homeNews}">
                        <div class="news-card">
                            <a href="${ctx}/NewsDetailServlet?id=${news.id}">
                                <img src="${news.image}" alt="${news.title}" class="news-card-image">
                            </a>
                            <div class="news-card-content">
                                <h3 class="news-card-title">
                                    <a href="${ctx}/NewsDetailServlet?id=${news.id}">${news.title}</a>
                                </h3>
                                <div class="news-card-meta">
                                    <span><i class="fa-solid fa-calendar-days"></i> ${news.postedDate}</span>
                                    <span><i class="fa-solid fa-eye"></i> ${news.viewCount}</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
            
            <section>
                <form action="${ctx}/NewsletterServlet" method="post" class="newsletter-form">
                    <h4><i class="fa-solid fa-envelope-open-text"></i> Nhận bản tin</h4>
                    <input type="email" name="email" placeholder="Nhập email của bạn..." required>
                    <button type="submit" class="btn-submit">
                        <i class="fa-solid fa-paper-plane"></i> Đăng ký
                    </button>
                </form>
            </section>
            
        </main>

        <jsp:include page="/layout/right_sidebar.jsp" />
        
    </div>
</div>

<jsp:include page="/layout/footer.jsp" />