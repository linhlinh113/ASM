<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Đăng nhập hệ thống"/>
</jsp:include>

<div class="login-wrap">
    <div class="login-box">
        <h2>
            <i class="fa-solid fa-right-to-bracket" style="color: var(--primary-color);"></i>
            Đăng nhập hệ thống
        </h2>
        
        <c:if test="${not empty param.error}">
             <div class="alert alert-error">
                <i class="fa-solid fa-triangle-exclamation"></i>
                ${param.error}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <i class="fa-solid fa-triangle-exclamation"></i>
                ${error}
            </div>
        </c:if>

        <form action="${ctx}/LoginServlet" method="post">
            <div class="form-group">
                <label for="id">Tên đăng nhập (ID)</label>
                <input type="text" id="id" name="id" required>
            </div>
            
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <button type="submit" class="btn-login-submit">
                <i class="fa-solid fa-key"></i> Đăng nhập
            </button>
        </form>
    </div>
</div>

<jsp:include page="/layout/footer.jsp" />