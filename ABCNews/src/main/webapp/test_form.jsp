<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <title>Test POST Form</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css"> <%-- Nạp CSS cho đẹp --%>
</head>
<body>
    <jsp:include page="/layout/header.jsp"/> <%-- Nạp Header --%>

    <div class="page-wrap container" style="min-height: 50vh;">
        <main class="main-content">
            <h2>Kiểm tra Servlet POST</h2>
            <p>Form này sẽ gửi yêu cầu POST đến <code>${ctx}/test-servlet-post</code></p>

            <%-- Form gọi TestServlet --%>
            <form action="${ctx}/test-servlet-post" method="post">
                 <button type="submit" class="btn btn-primary">
                    <i class="fa-solid fa-paper-plane"></i> Gửi Test POST
                 </button>
            </form>

            <hr style="margin: 20px 0;">

             <p>Link này sẽ gửi yêu cầu GET đến <code>${ctx}/test-servlet-post</code></p>
             <a href="${ctx}/test-servlet-post" class="btn btn-edit">
                <i class="fa-solid fa-link"></i> Thử Test GET
             </a>
        </main>
    </div>

    <jsp:include page="/layout/footer.jsp"/> <%-- Nạp Footer --%>
</body>
</html>