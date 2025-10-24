<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Quản lý Danh mục"/>
</jsp:include>

<div class="page-wrap container admin-layout">
    
    <jsp:include page="/manage/admin_nav.jsp" />

    <main class="admin-content">
        <h2><i class="fa-solid fa-list-ul"></i> Quản lý Danh mục</h2>
        
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session" />
        </c:if>
        
        <div class="crud-form">
            <h3 id="formTitle">Thêm mới Danh mục</h3>
            <form id="catForm" action="AdminCategoryServlet" method="post">
                <input type="hidden" name="action" id="formAction" value="add">
                
                <div class="form-grid">
                    <div class="form-group">
                        <label for="id">Mã Danh mục (Id)</label>
                        <input type="text" id="id" name="id" required>
                    </div>
                    <div class="form-group">
                        <label for="name">Tên Danh mục</label>
                        <input type="text" id="name" name="name" required>
                    </div>
                </div>
                
                <button type="submit" class="btn btn-primary">
                    <i class="fa-solid fa-save"></i> Lưu
                </button>
                <button type="button" class="btn" onclick="resetForm()" style="background-color: var(--muted-color); color: white;">
                    <i class="fa-solid fa-times"></i> Hủy
                </button>
            </form>
        </div>

        <h3>Danh sách Danh mục</h3>
        <table class="crud-table">
            <thead>
                <tr>
                    <th>Mã (Id)</th>
                    <th>Tên Danh mục</th>
                    <th style="width: 180px;">Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cat" items="${catList}">
                    <tr>
                        <td>${cat.id}</td>
                        <td>${cat.name}</td>
                        <td class="action-buttons">
                            <button type="button" class="btn btn-edit" 
                                    onclick="fillEditForm('${cat.id}', '${cat.name}')">
                                <i class="fa-solid fa-pen"></i> Sửa
                            </button>
                            
                            <form action="AdminCategoryServlet" method="post" onsubmit="return confirmDelete(event);">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${cat.id}">
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
    function fillEditForm(id, name) {
        // Điền dữ liệu vào form
        document.getElementById('id').value = id;
        document.getElementById('name').value = name;
        
        // Đổi action của form thành 'edit'
        document.getElementById('formAction').value = 'edit';
        
        // Đổi tiêu đề form
        document.getElementById('formTitle').innerText = 'Cập nhật Danh mục';
        
        // Không cho sửa ID khi edit
        document.getElementById('id').readOnly = true;
        
        // Cuộn lên đầu trang để thấy form
        window.scrollTo(0, 0);
    }

    function resetForm() {
        document.getElementById('catForm').reset(); // Reset giá trị
        document.getElementById('formAction').value = 'add';
        document.getElementById('formTitle').innerText = 'Thêm mới Danh mục';
        document.getElementById('id').readOnly = false;
    }
</script>