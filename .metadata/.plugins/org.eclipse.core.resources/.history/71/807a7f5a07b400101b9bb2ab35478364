<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Duyệt Tin tức"/>
</jsp:include>

<div class="page-wrap container admin-layout">
    
    <jsp:include page="/manage/admin_nav.jsp" />

    <main class="admin-content">
        <h2><i class="fa-solid fa-file-shield"></i> Duyệt Tin tức (Tất cả)</h2>
        
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session" />
        </c:if>

        <h3>Danh sách bài viết</h3>
        <table class="crud-table">
            <thead>
                <tr>
                    <th>Ảnh</th>
                    <th>Tiêu đề</th>
                    <th>Tác giả</th>
                    <th>Ngày đăng</th>
                    <th>Trạng thái</th>
                    <th style="width: 250px;">Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="n" items="${newsList}">
                    <tr>
                        <td><img src="${n.image}" alt="Ảnh"></td>
                        <td>${n.title}</td>
                        <td>${n.author}</td>
                        <td><fmt:formatDate value="${n.postedDate}" pattern="dd-MM-yyyy" /></td>
                        <td>
                            <c:if test="${n.approved}">
                                <span class="status status-approved">
                                    <i class="fa-solid fa-check"></i> Đã duyệt
                                </span>
                            </c:if>
                            <c:if test="${!n.approved}">
                                <span class="status status-pending">
                                    <i class="fa-solid fa-clock"></i> Chờ duyệt
                                </span>
                            </c:if>
                        </td>
                        <td class="action-buttons">
                            <c:if test="${!n.approved}">
                                <form action="AdminNewsServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="approve">
                                    <input type="hidden" name="id" value="${n.id}">
                                    <button type="submit" class="btn btn-approve">
                                        <i class="fa-solid fa-check"></i> Duyệt
                                    </button>
                                </form>
                            </c:if>
                            
                            <c:if test="${n.approved}">
                                <form action="AdminNewsServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="reject">
                                    <input type="hidden" name="id" value="${n.id}">
                                    <button type="submit" class="btn" style="background-color: #ffc107; color: #333;">
                                        <i class="fa-solid fa-times"></i> Bỏ duyệt
                                    </button>
                                </form>
                            </c:if>
                            
                            <form action="AdminNewsServlet" method="post" onsubmit="return confirmDelete(event);" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${n.id}">
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

<script>
    // Có thể thêm JS để filter bảng nếu muốn
</script>