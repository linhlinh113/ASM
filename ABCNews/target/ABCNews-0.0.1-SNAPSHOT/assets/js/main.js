/**
 * 
 */// File này để dành cho các hiệu ứng JavaScript phức tạp sau này.
// Ví dụ: Mobile Menu Toggle, Modal, ...
console.log("ABCNews main.js loaded.");

// Hàm confirm xóa (để dùng chung)
function confirmDelete(event) {
    if (!confirm('Bạn có chắc chắn muốn xóa mục này?')) {
        event.preventDefault();
        return false;
    }
    return true;
}