<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Menu | TummyFull Admin</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="menu" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar">
            <h1>Manage Menu</h1>
            <c:if test="${selectedRestaurantId > 0}">
                <button class="btn btn-primary" data-modal-target="#menuModal" onclick="resetMenuForm()">+ Add Food Item</button>
            </c:if>
        </div>

        <jsp:include page="/includes/alerts.jsp"/>

        <div class="panel">
            <div class="form-group" style="max-width:360px;">
                <label>Select Restaurant</label>
                <select class="form-control" onchange="window.location='${pageContext.request.contextPath}/AdminMenuServlet?restaurantId=' + this.value">
                    <option value="0">-- Choose a restaurant --</option>
                    <c:forEach var="r" items="${restaurants}">
                        <option value="${r.restaurantId}" ${r.restaurantId == selectedRestaurantId ? 'selected' : ''}>${r.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <c:if test="${selectedRestaurantId > 0}">
            <div class="panel">
                <table class="data-table">
                    <thead><tr><th>Image</th><th>Name</th><th>Price</th><th>Offer Price</th><th>Veg</th><th>Available</th><th>Actions</th></tr></thead>
                    <tbody>
                    <c:forEach var="m" items="${menuItems}">
                        <tr>
                            <td><img class="table-img" src="${m.imageUrl}" alt=""></td>
                            <td>${m.name}</td>
                            <td>₹${m.price}</td>
                            <td>${not empty m.offerPrice ? '₹'.concat(m.offerPrice) : '-'}</td>
                            <td><span class="${m.veg ? 'badge-veg' : 'badge-nonveg'}"></span></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/AdminMenuServlet" method="post">
                                    <input type="hidden" name="action" value="toggleAvailable">
                                    <input type="hidden" name="menuId" value="${m.menuId}">
                                    <input type="hidden" name="restaurantId" value="${selectedRestaurantId}">
                                    <input type="hidden" name="value" value="${m.available ? 0 : 1}">
                                    <label class="toggle-switch">
                                        <input type="checkbox" ${m.available ? 'checked' : ''} onchange="this.form.submit()">
                                        <span class="toggle-slider"></span>
                                    </label>
                                </form>
                            </td>
                            <td class="action-icons">
                                <button class="action-icon-btn" title="Edit"
                                    onclick='openEditMenu(${m.menuId}, "${m.name}", "${m.description}", ${m.price}, ${not empty m.offerPrice ? m.offerPrice : 0}, "${m.imageUrl}", ${m.categoryId}, ${m.veg})'>✏️</button>
                                <form action="${pageContext.request.contextPath}/AdminMenuServlet" method="post" data-confirm="Delete this food item?">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="menuId" value="${m.menuId}">
                                    <input type="hidden" name="restaurantId" value="${selectedRestaurantId}">
                                    <button type="submit" class="action-icon-btn danger" title="Delete">🗑️</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </main>
</div>

<div class="modal-overlay" id="menuModal">
    <div class="modal-box">
        <span class="modal-close" data-modal-close>&times;</span>
        <h3 id="menuModalTitle">Add Food Item</h3>
        <form action="${pageContext.request.contextPath}/AdminMenuServlet" method="post">
            <input type="hidden" name="action" id="menuFormAction" value="add">
            <input type="hidden" name="menuId" id="menuId">
            <input type="hidden" name="restaurantId" value="${selectedRestaurantId}">
            <div class="form-group">
                <label>Food Name</label>
                <input type="text" name="name" id="mName" class="form-control" required>
            </div>
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" id="mDescription" class="form-control" rows="2"></textarea>
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Price (₹)</label>
                    <input type="number" step="0.01" name="price" id="mPrice" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Offer Price (₹, optional)</label>
                    <input type="number" step="0.01" name="offerPrice" id="mOfferPrice" class="form-control">
                </div>
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Category</label>
                    <select name="categoryId" id="mCategoryId" class="form-control">
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.categoryId}">${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Type</label>
                    <select name="isVeg" id="mIsVeg" class="form-control">
                        <option value="1">Veg</option>
                        <option value="0">Non-Veg</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label>Image URL</label>
                <input type="url" name="imageUrl" id="mImageUrl" class="form-control" placeholder="https://..." required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Save Food Item</button>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
function resetMenuForm() {
    document.getElementById('menuModalTitle').textContent = 'Add Food Item';
    document.getElementById('menuFormAction').value = 'add';
    document.getElementById('mName').value = '';
    document.getElementById('mDescription').value = '';
    document.getElementById('mPrice').value = '';
    document.getElementById('mOfferPrice').value = '';
    document.getElementById('mImageUrl').value = '';
}
function openEditMenu(id, name, description, price, offerPrice, imageUrl, categoryId, isVeg) {
    document.getElementById('menuModalTitle').textContent = 'Edit Food Item';
    document.getElementById('menuFormAction').value = 'edit';
    document.getElementById('menuId').value = id;
    document.getElementById('mName').value = name;
    document.getElementById('mDescription').value = description;
    document.getElementById('mPrice').value = price;
    document.getElementById('mOfferPrice').value = offerPrice > 0 ? offerPrice : '';
    document.getElementById('mImageUrl').value = imageUrl;
    document.getElementById('mCategoryId').value = categoryId;
    document.getElementById('mIsVeg').value = isVeg ? '1' : '0';
    document.getElementById('menuModal').classList.add('open');
}
</script>
</body>
</html>
