<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Quản lý Tin tức (Cá nhân)"/>
</jsp:include>

<div class="page-wrap container admin-layout">

    <jsp:include page="/manage/admin_nav.jsp" />

    <%-- Sửa lỗi class. thành class --%>
    <main class="admin-content">
        <%-- Sửa lỗi fa. thành fa- --%>
        <h2><i class="fa-solid fa-newspaper"></i> Quản lý Tin tức (Cá nhân)</h2>

        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session" />
        </c:if>

        <div class="crud-form">
            <h3 id="formTitle">Thêm mới Bài viết</h3>
            <%-- ===>>> THÊM enctype VÀO FORM <<<=== --%>
            <form id="newsForm" action="ReporterNewsServlet" method="post" enctype="multipart/form-data">
            <%-- ===>>> ======================== <<<=== --%>
                <input type="hidden" name="action" id="formAction" value="add">

                <div class="form-grid">
                    <div class="form-group">
                        <label for="id">Mã Bài viết (Id)</label>
                        <input type="text" id="id" name="id" required>
                    </div>

                    <div class="form-group">
                        <label for="categoryId">Danh mục</label>
                        <select id="categoryId" name="categoryId" required>
                            <option value="">-- Chọn danh mục --</option>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}">${cat.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group form-group-full">
                    <label for="title">Tiêu đề</label>
                    <input type="text" id="title" name="title" required>
                </div>

                <%-- ===>>> THÊM Ô UPLOAD FILE <<<=== --%>
                <div class="form-group form-group-full">
                    <label for="imageFile">Tải ảnh lên (Ưu tiên)</label>
                    <input type="file" id="imageFile" name="imageFile" accept="image/png, image/jpeg, image/gif">
                    <small style="color: var(--muted-color); font-size: 0.9em;">Nếu bạn tải ảnh lên, link ảnh bên dưới sẽ bị bỏ qua.</small>
                </div>
                <%-- ===>>> ==================== <<<=== --%>

                <div class="form-group form-group-full">
                    <label for="image">Hoặc nhập Link Hình ảnh (URL)</label>
                    <input type="text" id="image" name="image" placeholder="https://...">
                </div>

                <%-- Sửa lỗi class. thành class --%>
                <div class="form-group form-group-full">
                    <label for="content">Nội dung</label>
                    <textarea id="content" name="content" rows="5"></textarea>
                </div>

                <%-- Sửa lỗi class. thành class --%>
                <div class="form-group-checkbox form-group-full">
                    <input type="checkbox" id="home" name="home" value="on">
                    <label for="home">Ghim lên Trang chủ?</label>
                </div>

                <button type="submit" class="btn btn-primary">
                    <i class="fa-solid fa-save"></i> Gửi bài (Chờ duyệt)
                </button>
                <button type="button" class="btn" onclick="resetForm()" style="background-color: var(--muted-color); color: white;">
                    <i class="fa-solid fa-times"></i> Hủy
                </button>
            </form>
        </div>

        <h3>Danh sách bài viết của bạn</h3>
        <div class="crud-table-wrapper"> <%-- Thêm wrapper cho bảng --%>
            <table class="crud-table">
                <thead>
                    <tr>
                        <th>Ảnh</th>
                        <th>Tiêu đề</th>
                        <th>Ngày đăng</th>
                        <th>Trạng thái</th>
                        <th style="width: 180px;">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="n" items="${newsList}">
                        <tr>
                            <%-- Thêm class table-img --%>
                            <td><img src="${n.image}" alt="Ảnh" class="table-img"></td>
                            <td>${n.title}</td>
                            <td><fmt:formatDate value="${n.postedDate}" pattern="dd/MM/yyyy" /></td>
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
                                <button type="button" class="btn btn-edit"
                                        onclick="fillEditForm(
                                            '${n.id}',
                                            '${n.title}',
                                            '${n.image}',
                                            '${n.content}',
                                            '${n.categoryId}',
                                            '${n.home}'
                                        )">
                                    <i class="fa-solid fa-pen"></i> Sửa
                                </button>

                                <form action="ReporterNewsServlet" method="post" onsubmit="return confirmDelete(event);">
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
        </div> <%-- Đóng wrapper --%>

    </main>
</div>

<jsp:include page="/layout/footer.jsp" />

<script>
    function fillEditForm(id, title, image, content, categoryId, home) {
        // Điền dữ liệu vào form
        document.getElementById('id').value = id;
        document.getElementById('title').value = title;
        // KHÔNG set giá trị cho input[type=text] image nếu muốn ưu tiên file
        // document.getElementById('image').value = image; // Có thể bỏ dòng này
        document.getElementById('content').value = content;
        document.getElementById('categoryId').value = categoryId;
        document.getElementById('home').checked = (home === 'true');

        // Xóa giá trị cũ của ô upload file (trình duyệt không cho phép set giá trị)
        document.getElementById('imageFile').value = '';

        // Đổi action của form thành 'edit'
        document.getElementById('formAction').value = 'edit';

        // Đổi tiêu đề form
        document.getElementById('formTitle').innerText = 'Cập nhật Bài viết';

        // Không cho sửa ID khi edit
        document.getElementById('id').readOnly = true;

        // Cuộn lên đầu trang
        window.scrollTo(0, 0);
    }

    function resetForm() {
        document.getElementById('newsForm').reset();
        document.getElementById('formAction').value = 'add';
        document.getElementById('formTitle').innerText = 'Thêm mới Bài viết';
        document.getElementById('id').readOnly = false;
        document.getElementById('imageFile').value = ''; // Đảm bảo ô file được xóa
    }
</script>