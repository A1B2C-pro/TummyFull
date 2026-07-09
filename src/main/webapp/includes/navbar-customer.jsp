<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    Object cartObj = session.getAttribute("cart");
    int cartCount = 0;
    if (cartObj instanceof com.tap.model.Cart) {
        cartCount = ((com.tap.model.Cart) cartObj).getTotalItemCount();
    }
    request.setAttribute("cartCount", cartCount);
    request.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));
%>
<nav class="navbar">
    <div class="container navbar-inner">
        <a href="${pageContext.request.contextPath}/RestaurantServlet" class="navbar-brand">
            <span class="logo-badge">🍔</span> TummyFull
        </a>

        <form action="${pageContext.request.contextPath}/RestaurantServlet" method="get" class="search-bar">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
            <input type="text" name="q" placeholder="Search restaurants, cuisines..." value="${searchQuery}">
        </form>

        <div class="navbar-links">
            <a href="${pageContext.request.contextPath}/RestaurantServlet">Restaurants</a>
            <a href="${pageContext.request.contextPath}/OrderHistoryServlet">Orders</a>
        </div>

        <div class="navbar-actions">
            <a href="${pageContext.request.contextPath}/CartServlet" class="icon-btn" title="Cart">
                🛒
                <c:if test="${cartCount > 0}"><span class="icon-badge">${cartCount}</span></c:if>
            </a>
            <a href="${pageContext.request.contextPath}/ProfileServlet" class="icon-btn" title="Profile">👤</a>
            <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn btn-outline btn-sm">Logout</a>
        </div>
    </div>
</nav>
