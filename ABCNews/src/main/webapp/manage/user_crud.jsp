<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Nạp thư viện format ngày --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Quản lý Người dùng"/>
</jsp:include>

<div class="page-wrap container admin-layout">
    
    <jsp:include page="/manage/admin_nav.jsp" />

    <main class="admin-content">
        <h2><i class="fa-solid fa-users"></i> Quản lý Người dùng</h2>
        
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session" />
        </c:if>
        
        <div class="crud-form">
            <h3 id="formTitle">Thêm mới Người dùng</h3>
            <form id="userForm" action="AdminUserServlet" method="post">
                <input type="hidden" name="action" id="formAction" value="add">
                
                <div class="form-grid">
                    <div class="form-group">
                        <label for="id">Mã đăng nhập (Id)</label>
                        <input type="text" id="id" name="id" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Mật khẩu</label>
                        <input type="password" id="password" name="password" placeholder="Bỏ trống để giữ mật khẩu cũ">
                    </div>
                    <div class="form-group">
                        <label for="fullname">Họ và tên</label>
                        <input type="text" id="fullname" name="fullname" required>
                    </div>
                    <div class="form-group">
                        <label for="birthday">Ngày sinh</label>
                        <input type="date" id="birthday" name="birthday">
                    </div>
                    <div class="form-group">
                        <label>Giới tính</label>
                        <div>
                            <input type="radio" id="genderMale" name="gender" value="1" checked> <label for="genderMale">Nam</label>
                            <input type="radio" id="genderFemale" name="gender" value="0"> <label for="genderFemale">Nữ</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Vai trò</label>
                        <div>
                            <input type="radio" id="roleReporter" name="role" value="0" checked> <label for="roleReporter">Phóng viên</label>
                            <input type="radio" id="roleAdmin" name="role" value="1"> <label for="roleAdmin">Quản trị</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="mobile">Điện thoại</label>
                        <input type="tel" id="mobile" name="mobile">
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email">
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

        <h3>Danh sách Người dùng</h3>
        <table class="crud-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Họ tên</th>
                    <th>Email</th>
                    <th>Vai trò</th>
                    <th style="width: 180px;">Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${userList}">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.fullname}</td>
                        <td>${u.email}</td>
                        <td>
                            <c:if test="${u.role}">
                                <span class="status status-approved" style="background-color: var(--red); color: white;">Admin</span>
                            </c:if>
                            <c:if test="${!u.role}">
                                <span class="status status-pending" style="background-color: var(--secondary-color); color: white;">Reporter</span>
                            </c:if>
                        </td>
                        <td class="action-buttons">
                            <button type="button" class="btn btn-edit" 
                                    onclick="fillEditForm(
                                        '${u.id}', 
                                        '${u.fullname}', 
                                        '<fmt:formatDate value="${u.birthday}" pattern="yyyy-MM-dd" />',
                                        '${u.gender ? '1' : '0'}', 
                                        '${u.mobile}', 
                                        '${u.email}', 
                                        '${u.role ? '1' : '0'}'
                                    )">
                                <i class="fa-solid fa-pen"></i> Sửa
                            </button>
                            
                            <form action="AdminUserServlet" method="post" onsubmit="return confirmDelete(event);">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${u.id}">
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
    function fillEditForm(id, fullname, birthday, gender, mobile, email, role) {
        // Điền dữ liệu vào form
        document.getElementById('id').value = id;
        document.getElementById('fullname').value = fullname;
        document.getElementById('birthday').value = birthday;
        document.getElementById('mobile').value = mobile;
        document.getElementById('email').value = email;
        
        // Xử lý radio buttons
        document.querySelector('input[name="gender"][value="' + gender + '"]').checked = true;
        document.querySelector('input[name="role"][value="' + role + '"]').checked = true;

        // Xử lý Mật khẩu
        document.getElementById('password').value = ''; // Luôn xóa trống
        document.getElementById('password').placeholder = 'Bỏ trống để giữ mật khẩu cũ';
        
        // Đổi action của form thành 'edit'
        document.getElementById('formAction').value = 'edit';
        
        // Đổi tiêu đề form
        document.getElementById('formTitle').innerText = 'Cập nhật Người dùng';
        
        // Không cho sửa ID khi edit
        document.getElementById('id').readOnly = true;
        
        // Cuộn lên đầu trang
        window.scrollTo(0, 0);
    }

    function resetForm() {
        document.getElementById('userForm').reset(); 
        document.getElementById('formAction').value = 'add';
        document.getElementById('formTitle').innerText = 'Thêm mới Người dùng';
        document.getElementById('id').readOnly = false;
        document.getElementById('password').placeholder = 'Bỏ trống để giữ mật khẩu cũ';
    }
</script>