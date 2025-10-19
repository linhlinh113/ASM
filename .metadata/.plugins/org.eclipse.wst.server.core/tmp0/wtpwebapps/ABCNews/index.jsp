<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <title>ABCNews - Trang chủ</title>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css"/>
    <link rel="icon" type="image/png" href="${ctx}/assets/img/newsicon.png"/>
    <style>
        .news-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 24px;
        }
        .news-card {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .news-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 24px 48px rgba(16, 24, 40, 0.15);
        }
        .thumb {
            height: 200px;
            overflow: hidden;
        }
        .thumb img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.4s ease;
        }
        .news-card:hover .thumb img {
            transform: scale(1.08);
        }
        .news-title {
            min-height: 64px;
            font-size: 1.15rem;
            line-height: 1.4;
            padding: 12px;
        }
        .info {
            padding: 0 12px 12px 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-top: 1px solid rgba(16, 24, 40, 0.05);
        }
        .layout {
            gap: 24px;
        }
        .newsletter-form {
            background: linear-gradient(135deg, #f7fafc, #e6f0fa);
            padding: 20px;
            border-radius: 12px;
            box-shadow: var(--shadow);
            display: flex;
            gap: 12px;
            align-items: center;
        }
        .newsletter-form input {
            flex: 1;
            padding: 14px;
            border-radius: 10px;
            border: 1px solid #e6eef9;
            font-size: 1rem;
        }
        .newsletter-form .btn {
            padding: 14px 20px;
            background: var(--primary);
            color: #fff;
            border-radius: 8px;
            border: none;
            cursor: pointer;
            transition: background 0.3s ease;
        }
        .newsletter-form .btn:hover {
            background: #1e5389;
        }
        .empty-message {
            text-align: center;
            padding: 60px 20px;
            color: var(--muted);
            font-style: italic;
            background: #fff;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
        }
        @media (max-width: 980px) {
            .layout {
                grid-template-columns: 1fr;
                gap: 20px;
            }
            .news-grid {
                grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
            }
            .newsletter-form {
                flex-direction: column;
                gap: 10px;
            }
            .newsletter-form .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<jsp:include page="/layout/header.jsp"/>

<div class="page-wrap">
    <div class="mobile-toggle" style="display:none; margin-bottom:12px;">
        <button class="btn" onclick="ABC.toggleLeft()">☰ Danh mục</button>
        <button class="btn" onclick="ABC.toggleRight()">☰ Khác</button>
    </div>

    <div class="layout">
        <!-- Left sidebar -->
        <div id="left-sidebar" class="sidebar left-sidebar sticky">
            <jsp:include page="/layout/leftSidebar.jsp"/>
        </div>

        <!-- Main content -->
        <main class="main">
            <div class="container">
                <h2 class="main-title"><img src="${ctx}/assets/img/newsicon.png" width="28" alt="Logo"/> Tin nổi bật hôm nay</h2>

                <!-- Menu loại tin -->
                <div class="category-menu" style="margin-bottom:28px;">
                    <c:forEach var="cat" items="${categoryList}">
                        <a href="${ctx}/NewsListServlet?cat=${cat.id}" class="cat-btn">${cat.name}</a>
                    </c:forEach>
                </div>

                <!-- Hot nhất -->
                <h3 class="main-title" style="font-size:1.3rem;margin-bottom:16px;"><span class="icon-fire"></span> Bản tin hot nhất</h3>
                <div class="news-grid">
                    <c:choose>
                        <c:when test="${not empty hotList}">
                            <c:forEach var="news" items="${hotList}">
                                <div class="news-card">
                                    <a href="${ctx}/NewsDetailServlet?id=${news.id}" style="text-decoration:none;color:inherit;">
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
                        </c:when>
                        <c:otherwise>
                            <% System.out.println("[" + new java.util.Date() + "] hotList is empty in JSP"); %>
                            <div class="empty-message">Chưa có bản tin hot nào. Kiểm tra lại sau!</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Mới nhất -->
                <h3 class="main-title" style="font-size:1.3rem;margin-top:28px;"><span class="icon-new"></span> Bản tin mới nhất</h3>
                <div class="news-grid">
                    <c:choose>
                        <c:when test="${not empty newestList}">
                            <c:forEach var="news" items="${newestList}">
                                <div class="news-card">
                                    <a href="${ctx}/NewsDetailServlet?id=${news.id}" style="text-decoration:none;color:inherit;">
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
                        </c:when>
                        <c:otherwise>
                            <% System.out.println("[" + new java.util.Date() + "] newestList is empty in JSP"); %>
                            <div class="empty-message">Chưa có bản tin mới nào. Kiểm tra lại sau!</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Tin trang chủ (nếu muốn hiển thị) -->
                <h3 class="main-title" style="font-size:1.3rem;margin-top:28px;"><span class="icon-home"></span> Tin trang chủ</h3>
                <div class="news-grid">
                    <c:choose>
                        <c:when test="${not empty homeNews}">
                            <c:forEach var="news" items="${homeNews}">
                                <div class="news-card">
                                    <a href="${ctx}/NewsDetailServlet?id=${news.id}" style="text-decoration:none;color:inherit;">
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
                        </c:when>
                        <c:otherwise>
                            <% System.out.println("[" + new java.util.Date() + "] homeNews is empty in JSP"); %>
                            <div class="empty-message">Chưa có tin trang chủ nào. Kiểm tra lại sau!</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Newsletter form -->
                <section>
                    <form action="${ctx}/NewsletterServlet" method="post" class="newsletter-form">
                        <input type="email" name="email" placeholder="Nhập email để nhận bản tin mới" required />
                        <button type="submit" class="btn">Đăng ký nhận tin</button>
                    </form>
                    <c:if test="${not empty success}">
                        <div class="alert success">${success}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert error">${error}</div>
                    </c:if>
                </section>
            </div>
        </main>

        <!-- Right sidebar -->
        <aside id="right-sidebar" class="sidebar right-sidebar sticky">
            <jsp:include page="/layout/rightSidebar.jsp"/>
        </aside>
    </div>
</div>

<jsp:include page="/layout/footer.jsp"/>
<script src="${ctx}/assets/js/sidebar.js"></script>
</body>
</html>