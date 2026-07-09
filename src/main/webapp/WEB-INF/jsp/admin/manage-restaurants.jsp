<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Restaurants | TummyFull Admin</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="restaurants" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar">
            <h1>Manage Restaurants</h1>
            <button class="btn btn-primary" data-modal-target="#restaurantModal" onclick="resetRestaurantForm()">+ Add Restaurant</button>
        </div>

        <jsp:include page="/includes/alerts.jsp"/>

        <div class="panel">
            <table class="data-table">
                <thead><tr><th>Image</th><th>Name</th><th>Cuisine</th><th>Rating</th><th>Delivery</th><th>Offer</th><th>Open</th><th>Active</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="r" items="${restaurants}">
                    <tr>
                        <td><img class="table-img" src="${r.imageUrl}" alt=""></td>
                        <td>${r.name}</td>
                        <td>${r.cuisine}</td>
                        <td>★ ${r.rating}</td>
                        <td>${r.deliveryTime}</td>
                        <td>${r.offerText}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/AdminRestaurantServlet" method="post">
                                <input type="hidden" name="action" value="toggleOpen">
                                <input type="hidden" name="restaurantId" value="${r.restaurantId}">
                                <input type="hidden" name="value" value="${r.open ? 0 : 1}">
                                <label class="toggle-switch">
                                    <input type="checkbox" ${r.open ? 'checked' : ''} onchange="this.form.submit()">
                                    <span class="toggle-slider"></span>
                                </label>
                            </form>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/AdminRestaurantServlet" method="post">
                                <input type="hidden" name="action" value="toggleActive">
                                <input type="hidden" name="restaurantId" value="${r.restaurantId}">
                                <input type="hidden" name="value" value="${r.active ? 0 : 1}">
                                <label class="toggle-switch">
                                    <input type="checkbox" ${r.active ? 'checked' : ''} onchange="this.form.submit()">
                                    <span class="toggle-slider"></span>
                                </label>
                            </form>
                        </td>
                        <td class="action-icons">
                            <button class="action-icon-btn" title="Edit"
                                onclick='openEditRestaurant(${r.restaurantId}, "${r.name}", "${r.cuisine}", ${r.categoryId}, "${r.imageUrl}", "${r.deliveryTime}", "${r.address}", "${r.offerText}", ${r.open})'>✏️</button>
                            <form action="${pageContext.request.contextPath}/AdminRestaurantServlet" method="post" data-confirm="Delete this restaurant and all its menu items?">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="restaurantId" value="${r.restaurantId}">
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

<!-- Add / Edit Restaurant Modal -->
<div class="modal-overlay" id="restaurantModal">
    <div class="modal-box">
        <span class="modal-close" data-modal-close>&times;</span>
        <h3 id="restaurantModalTitle">Add Restaurant</h3>
        <form action="${pageContext.request.contextPath}/AdminRestaurantServlet" method="post" id="restaurantForm">
            <input type="hidden" name="action" id="restaurantFormAction" value="add">
            <input type="hidden" name="restaurantId" id="restaurantId">
            <div class="form-group">
                <label>Restaurant Name</label>
                <input type="text" name="name" id="rName" class="form-control" required>
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Cuisine</label>
                    <input type="text" name="cuisine" id="rCuisine" class="form-control" placeholder="North Indian, Chinese" required>
                </div>
                <div class="form-group">
                    <label>Category</label>
                    <select name="categoryId" id="rCategoryId" class="form-control">
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.categoryId}">${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label>Image URL</label>
                <input type="url" name="imageUrl" id="rImageUrl" class="form-control" placeholder="https://..." required>
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Delivery Time</label>
                    <input type="text" name="deliveryTime" id="rDeliveryTime" class="form-control" placeholder="25-30 mins" required>
                </div>
                <div class="form-group">
                    <label>Offer Text</label>
                    <input type="text" name="offerText" id="rOfferText" class="form-control" placeholder="Flat 50% OFF">
                </div>
            </div>
            <div class="form-group">
                <label>Address</label>
                <input type="text" name="address" id="rAddress" class="form-control" placeholder="Area, Bangalore" required>
            </div>
            <div class="form-group">
                <label class="auth-checkbox"><input type="checkbox" name="isOpen" id="rIsOpen" value="1" checked> Currently Open</label>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Save Restaurant</button>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
function resetRestaurantForm() {
    document.getElementById('restaurantModalTitle').textContent = 'Add Restaurant';
    document.getElementById('restaurantFormAction').value = 'add';
    document.getElementById('restaurantForm').reset();
}
function openEditRestaurant(id, name, cuisine, categoryId, imageUrl, deliveryTime, address, offerText, isOpen) {
    document.getElementById('restaurantModalTitle').textContent = 'Edit Restaurant';
    document.getElementById('restaurantFormAction').value = 'edit';
    document.getElementById('restaurantId').value = id;
    document.getElementById('rName').value = name;
    document.getElementById('rCuisine').value = cuisine;
    document.getElementById('rCategoryId').value = categoryId;
    document.getElementById('rImageUrl').value = imageUrl;
    document.getElementById('rDeliveryTime').value = deliveryTime;
    document.getElementById('rAddress').value = address;
    document.getElementById('rOfferText').value = offerText;
    document.getElementById('rIsOpen').checked = isOpen;
    document.getElementById('restaurantModal').classList.add('open');
}
</script>
</body>
</html>
