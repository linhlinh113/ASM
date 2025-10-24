<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="sidebar right-sidebar sticky">
    <div>
        <h4 class="section-title"><span class="icon-fire"></span> Hot nhất</h4>
        <div class="list-block">
            <c:forEach var="n" items="${hotList}">
                <a class="list-item" href="${ctx}/NewsDetailServlet?id=${n.id}" style="text-decoration:none;color:inherit;">
                    <img src="${n.image}" alt="${n.title}"/>
                    <div>
                        <div class="title">${n.title}</div>
                        <div style="font-size:0.85rem;color:var(--muted);">${n.viewCount} lượt xem</div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>

    <hr style="margin:12px 0;border:none;border-top:1px solid rgba(15,23,42,0.04)"/>

    <div>
        <h4 class="section-title"><span class="icon-new"></span> Mới nhất</h4>
        <div class="list-block">
            <c:forEach var="n" items="${newestList}">
                <a class="list-item" href="${ctx}/NewsDetailServlet?id=${n.id}" style="text-decoration:none;color:inherit;">
                    <img src="${n.image}" alt="${n.title}"/>
                    <div>
                        <div class="title">${n.title}</div>
                        <div style="font-size:0.85rem;color:var(--muted);"><c:out value="${n.postedDate}"/></div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>

    <hr style="margin:12px 0;border:none;border-top:1px solid rgba(15,23,42,0.04)"/>

    <div>
        <h4 class="section-title">Đã xem gần đây</h4>
        <div class="recent-views">
            <c:if test="${not empty sessionScope.recentViews}">
                <c:forEach var="nid" items="${sessionScope.recentViews}">
                    <c:choose>
                        <c:when test="${not empty newsMap && newsMap[nid] != null}">
                            <c:set var="rn" value="${newsMap[nid]}"/>
                            <a href="${ctx}/NewsDetailServlet?id=${rn.id}" title="${rn.title}">
                                <img src="${rn.image}" alt="${rn.title}"/>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <!-- skip if not in map -->
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
        </div>
    </div>
</div>