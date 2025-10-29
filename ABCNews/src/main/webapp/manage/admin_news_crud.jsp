<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Quản lý & Duyệt Tin tức"/>
</jsp:include>

<div class="page-wrap container admin-layout">

    <jsp:include page="/manage/admin_nav.jsp" />

    <main class="admin-content">
        <h2><i class="fa-solid fa-file-shield"></i> Quản lý & Duyệt Tin tức</h2>

        <%-- Hiển thị thông báo thành công/lỗi từ Session --%>
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session" />
        </c:if>

        <%-- === FORM SỬA BÀI VIẾT (Ẩn ban đầu) === --%>
        <div id="editFormContainer" class="crud-form" style="display: none; border-color: var(--secondary-color);"> <%-- Ẩn và thêm viền màu khác --%>
            <h3 id="formTitle">Cập nhật Bài viết</h3>
            <%-- Form để Sửa, bao gồm cả upload ảnh --%>
            <form id="newsEditForm" action="AdminNewsServlet" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="edit"> <%-- Action luôn là edit --%>
                <input type="hidden" name="id" id="editId"> <%-- ID bài viết cần sửa --%>

                <div class="form-grid">
                    <div class="form-group">
                        <label for="editCategoryId">Danh mục</label>
                        <select id="editCategoryId" name="categoryId" required>
                            <option value="">-- Chọn danh mục --</option>
                            <%-- Lấy categoryList từ request (doGet đã nạp) --%>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}">${cat.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                     <div class="form-group">
                        <label for="editAuthor">Tác giả</label>
                        <%-- Admin có thể sửa tác giả nếu muốn, bỏ readonly --%>
                        <input type="text" id="editAuthor" name="author" required>
                    </div>
                </div>

                <div class="form-group form-group-full">
                    <label for="editTitle">Tiêu đề</label>
                    <input type="text" id="editTitle" name="title" required>
                </div>

                <div class="form-group form-group-full">
                    <label for="editImageFile">Tải ảnh mới lên (Ưu tiên)</label>
                    <input type="file" id="editImageFile" name="imageFile" accept="image/png, image/jpeg, image/gif">
                     <%-- Hiển thị ảnh cũ --%>
                     <img id="currentImagePreview" src="" alt="Ảnh hiện tại" style="max-width: 100px; max-height: 60px; margin-top: 5px; display: none; border: 1px solid var(--border-color); border-radius: var(--radius-sm);">
                    <small style="color: var(--muted-color); font-size: 0.9em;">Nếu tải ảnh mới, ảnh cũ/link bên dưới sẽ bị thay thế.</small>
                </div>

                <div class="form-group form-group-full">
                    <label for="editImage">Hoặc giữ Link Ảnh hiện tại/Nhập Link mới</label>
                    <input type="text" id="editImage" name="image" placeholder="https://...">
                </div>

                <div class="form-group form-group-full">
                    <label for="editContent">Nội dung</label>
                    <textarea id="editContent" name="content" rows="5"></textarea>
                </div>

                <div class="form-group-checkbox form-group-full">
                    <input type="checkbox" id="editHome" name="home" value="on">
                    <label for="editHome">Ghim lên Trang chủ?</label>
                </div>

                <button type="submit" class="btn btn-primary">
                    <i class="fa-solid fa-save"></i> Lưu thay đổi (Cần duyệt lại)
                </button>
                <button type="button" class="btn" onclick="hideEditForm()" style="background-color: var(--muted-color); color: white;">
                    <i class="fa-solid fa-times"></i> Hủy
                </button>
            </form>
        </div>
        <%-- === KẾT THÚC FORM SỬA === --%>


        <h3>Danh sách bài viết</h3>
        <div class="crud-table-wrapper">
            <table class="crud-table">
                <thead>
                    <tr>
                        <th>Ảnh</th>
                        <th>Tiêu đề</th>
                        <th>Tác giả</th>
                        <th>Ngày đăng</th>
                        <th>Trạng thái</th>
                        <th style="width: 300px;">Hành động</th> <%-- Tăng chiều rộng cột Action --%>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="n" items="${newsList}">
                        <tr>
                            <td><img src="${n.image}" alt="Ảnh" class="table-img"></td>
                            <td>${n.title}</td>
                            <td>${n.author}</td>
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
                                <%-- Nút Duyệt --%>
                                <c:if test="${!n.approved}">
                                    <form action="AdminNewsServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="approve">
                                        <input type="hidden" name="id" value="${n.id}">
                                        <button type="submit" class="btn btn-approve">
                                            <i class="fa-solid fa-check"></i> Duyệt
                                        </button>
                                    </form>
                                </c:if>

                                <%-- Nút Bỏ duyệt --%>
                                 <c:if test="${n.approved}">
                                    <form action="AdminNewsServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="reject">
                                        <input type="hidden" name="id" value="${n.id}">
                                        <button type="submit" class="btn btn-reject"> <%-- Dùng class chuẩn --%>
                                            <i class="fa-solid fa-times"></i> Bỏ duyệt
                                        </button>
                                    </form>
                                </c:if>

                                <%-- Nút Sửa (Mới) --%>
                                <button type="button" class="btn btn-edit"
                                        onclick="showEditForm(
                                            '${n.id}',
                                            '${n.title}',
                                            '${n.image}',
                                            '${n.content}',
                                            '${n.categoryId}',
                                            '${n.author}',
                                            '${n.home}'
                                        )">
                                    <i class="fa-solid fa-pen"></i> Sửa
                                </button>

                                <%-- Nút Xóa --%>
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
        </div>
    </main>
</div>

<jsp:include page="/layout/footer.jsp" />

<%-- === JAVASCRIPT ĐỂ HIỆN/ẨN FORM SỬA === --%>
<script>
    const editFormContainer = document.getElementById('editFormContainer');
    const editForm = document.getElementById('newsEditForm');
    const formTitle = document.getElementById('formTitle');
    // Lấy các input trong form Sửa
    const editId = document.getElementById('editId');
    const editTitle = document.getElementById('editTitle');
    const editImageFile = document.getElementById('editImageFile');
    const editImage = document.getElementById('editImage');
    const editContent = document.getElementById('editContent');
    const editCategoryId = document.getElementById('editCategoryId');
    const editAuthor = document.getElementById('editAuthor');
    const editHome = document.getElementById('editHome');
    const currentImagePreview = document.getElementById('currentImagePreview');

    function showEditForm(id, title, image, content, categoryId, author, home) {
        // Điền dữ liệu vào form Sửa
        editId.value = id;
        editTitle.value = title;
        editImage.value = image; // Hiển thị link ảnh cũ
        editContent.value = content;
        editCategoryId.value = categoryId;
        editAuthor.value = author; // Điền tác giả hiện tại
        editHome.checked = (home === 'true' || home === true); // Xử lý boolean

        // Hiển thị ảnh preview nếu có
        if (image && image.trim() !== '') {
            currentImagePreview.src = image;
            currentImagePreview.style.display = 'inline-block';
        } else {
            currentImagePreview.style.display = 'none';
        }

        // Xóa giá trị ô file (trình duyệt không cho set)
        editImageFile.value = '';

        // Hiển thị form và cuộn lên
        formTitle.innerText = 'Cập nhật Bài viết (ID: ' + id + ')'; // Cập nhật tiêu đề form
        editFormContainer.style.display = 'block';
        // Cuộn mượt lên vị trí form
        window.scrollTo({ top: editFormContainer.offsetTop - 80, behavior: 'smooth' });
    }

    function hideEditForm() {
        editFormContainer.style.display = 'none'; // Ẩn form
        editForm.reset(); // Reset các trường input
        currentImagePreview.style.display = 'none'; // Ẩn ảnh preview
        editImageFile.value = ''; // Xóa ô file
    }

    // Hàm confirmDelete đã có trong main.js hoặc bạn có thể thêm vào đây
    // function confirmDelete(event) {
    //     if (!confirm('Bạn có chắc chắn muốn xóa mục này?')) {
    //         event.preventDefault();
    //         return false;
    //     }
    //     return true;
    // }
</script>
<%-- === KẾT THÚC JAVASCRIPT === --%>