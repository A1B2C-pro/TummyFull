<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<aside class="dash-sidebar">
    <a href="${pageContext.request.contextPath}/AdminDashboardServlet" class="navbar-brand">
        <span class="logo-badge">🍔</span> TummyFull
    </a>
    <nav class="dash-nav">
        <a href="${pageContext.request.contextPath}/AdminDashboardServlet" class="${activePage == 'dashboard' ? 'active' : ''}"><span class="nav-icon">📊</span> Dashboard</a>
        <a href="${pageContext.request.contextPath}/AdminRestaurantServlet" class="${activePage == 'restaurants' ? 'active' : ''}"><span class="nav-icon">🏬</span> Restaurants</a>
        <a href="${pageContext.request.contextPath}/AdminMenuServlet" class="${activePage == 'menu' ? 'active' : ''}"><span class="nav-icon">🍽️</span> Menu</a>
        <a href="${pageContext.request.contextPath}/AdminUserServlet" class="${activePage == 'users' ? 'active' : ''}"><span class="nav-icon">👥</span> Users</a>
        <a href="${pageContext.request.contextPath}/AdminOrderServlet" class="${activePage == 'orders' ? 'active' : ''}"><span class="nav-icon">📦</span> Orders</a>
        <a href="${pageContext.request.contextPath}/AdminDeliveryServlet" class="${activePage == 'delivery' ? 'active' : ''}"><span class="nav-icon">🛵</span> Delivery Agents</a>
        <a href="${pageContext.request.contextPath}/AdminOfferServlet" class="${activePage == 'offers' ? 'active' : ''}"><span class="nav-icon">🏷️</span> Offers &amp; Coupons</a>
        <a href="${pageContext.request.contextPath}/AdminCategoryServlet" class="${activePage == 'categories' ? 'active' : ''}"><span class="nav-icon">🗂️</span> Categories</a>
        <a href="${pageContext.request.contextPath}/AdminReviewServlet" class="${activePage == 'reviews' ? 'active' : ''}"><span class="nav-icon">⭐</span> Reviews</a>
    </nav>
    <div class="dash-sidebar-footer">
        <a href="${pageContext.request.contextPath}/LogoutServlet">🚪 Logout</a>
    </div>
</aside>
