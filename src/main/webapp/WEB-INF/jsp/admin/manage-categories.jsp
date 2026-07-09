<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories | TummyFull Admin</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="categories" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar">
            <h1>Manage Categories</h1>
            <button class="btn btn-primary" data-modal-target="#categoryModal" onclick="resetCategoryForm()">+ Add Category</button>
        </div>
        <jsp:include page="/includes/alerts.jsp"/>

        <div class="panel">
            <table class="data-table">
                <thead><tr><th>Image</th><th>Category Name</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="cat" items="${categories}">
                    <tr>
                        <td><img class="table-img" src="${cat.imageUrl}" alt=""></td>
                        <td>${cat.categoryName}</td>
                        <td class="action-icons">
                            <button class="action-icon-btn" title="Edit" onclick='openEditCategory(${cat.categoryId}, "${cat.categoryName}", "${cat.imageUrl}")'>✏️</button>
                            <form action="${pageContext.request.contextPath}/AdminCategoryServlet" method="post" data-confirm="Delete this category?">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="categoryId" value="${cat.categoryId}">
                                <button type="submit" class="action-icon-btn danger" title="Delete">🗑️</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>

<div class="modal-overlay" id="categoryModal">
    <div class="modal-box">
        <span class="modal-close" data-modal-close>&times;</span>
        <h3 id="categoryModalTitle">Add Category</h3>
        <form action="${pageContext.request.contextPath}/AdminCategoryServlet" method="post">
            <input type="hidden" name="action" id="categoryFormAction" value="add">
            <input type="hidden" name="categoryId" id="categoryId">
            <div class="form-group">
                <label>Category Name</label>
                <input type="text" name="categoryName" id="catName" class="form-control" placeholder="Pizza" required>
            </div>
            <div class="form-group">
                <label>Image URL</label>
                <input type="url" name="imageUrl" id="catImageUrl" class="form-control" placeholder="https://..." required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Save Category</button>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
function resetCategoryForm() {
    document.getElementById('categoryModalTitle').textContent = 'Add Category';
    document.getElementById('categoryFormAction').value = 'add';
    document.getElementById('categoryId').value = '';
    document.getElementById('catName').value = '';
    document.getElementById('catImageUrl').value = '';
}
function openEditCategory(id, name, imageUrl) {
    document.getElementById('categoryModalTitle').textContent = 'Edit Category';
    document.getElementById('categoryFormAction').value = 'edit';
    document.getElementById('categoryId').value = id;
    document.getElementById('catName').value = name;
    document.getElementById('catImageUrl').value = imageUrl;
    document.getElementById('categoryModal').classList.add('open');
}
</script>
</body>
</html>
