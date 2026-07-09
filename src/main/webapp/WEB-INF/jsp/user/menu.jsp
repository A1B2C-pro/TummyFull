<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${restaurant.name} Menu | TummyFull</title>
    <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>
<jsp:include page="/includes/navbar-customer.jsp"/>

<div class="page-header-strip with-bg" style="background-image:url('${restaurant.imageUrl}');">
    <div class="container flex items-center justify-between" style="flex-wrap:wrap;gap:14px;">
        <div>
            <h1>${restaurant.name}</h1>
            <p>${restaurant.cuisine} &middot; ${restaurant.address}</p>
        </div>
        <div class="flex gap-1 items-center">
            <span class="rating-pill">★ ${restaurant.rating}</span>
            <span class="filter-pill">🕒 ${restaurant.deliveryTime}</span>
        </div>
    </div>
</div>

<div class="container mt-3">
    <jsp:include page="/includes/alerts.jsp"/>

    <form action="${pageContext.request.contextPath}/MenuServlet" method="get" class="search-bar mb-3" style="max-width:460px;">
        <input type="hidden" name="restaurantId" value="${restaurant.restaurantId}">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <input type="text" name="q" placeholder="Search dishes..." value="${param.q}">
    </form>

    <div class="filter-row">
        <a href="${pageContext.request.contextPath}/MenuServlet?restaurantId=${restaurant.restaurantId}" class="filter-pill">All</a>
        <a href="${pageContext.request.contextPath}/MenuServlet?restaurantId=${restaurant.restaurantId}&veg=1" class="filter-pill">🟢 Veg Only</a>
        <a href="${pageContext.request.contextPath}/MenuServlet?restaurantId=${restaurant.restaurantId}&sort=priceAsc" class="filter-pill">Price: Low to High</a>
        <a href="${pageContext.request.contextPath}/MenuServlet?restaurantId=${restaurant.restaurantId}&sort=priceDesc" class="filter-pill">Price: High to Low</a>
    </div>

    <c:choose>
        <c:when test="${empty menuItems}">
            <div class="empty-state">
                <div class="icon">🍽️</div>
                <h3>No dishes found</h3>
                <p>Try a different search or filter.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="panel">
                <c:forEach var="m" items="${menuItems}">
                    <div class="menu-item-row">
                        <div class="menu-item-info">
                            <div class="flex items-center gap-1">
                                <span class="${m.veg ? 'badge-veg' : 'badge-nonveg'}"></span>
                                <h4>${m.name}</h4>
                            </div>
                            <p class="desc">${m.description}</p>
                            <div class="menu-item-price">
                                <c:if test="${not empty m.offerPrice}"><span class="strike">₹${m.price}</span></c:if>
                                ₹${m.effectivePrice}
                                <span class="rating-pill">★ ${m.rating}</span>
                            </div>
                        </div>
                        <div class="menu-item-image-wrap">
                            <img src="${m.imageUrl}" alt="${m.name}">
                            <c:set var="existingQty" value="${sessionScope.cart.items[m.menuId].quantity}"/>
                            <c:choose>
                                <c:when test="${not empty existingQty && existingQty > 0}">
                                    <form class="menu-add-form" action="${pageContext.request.contextPath}/CartServlet" method="post">
                                        <input type="hidden" name="menuId" value="${m.menuId}">
                                        <input type="hidden" name="action" value="increase">
                                        <input type="hidden" name="redirectTo" value="${pageContext.request.contextPath}/MenuServlet?restaurantId=${restaurant.restaurantId}">
                                        <div class="qty-stepper">
                                            <button type="button" class="qty-minus">−</button>
                                            <span class="qty-display">${existingQty}</span>
                                            <button type="button" class="qty-plus">+</button>
                                        </div>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form class="menu-add-form" action="${pageContext.request.contextPath}/CartServlet" method="post">
                                        <input type="hidden" name="menuId" value="${m.menuId}">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="redirectTo" value="${pageContext.request.contextPath}/MenuServlet?restaurantId=${restaurant.restaurantId}">
                                        <button type="submit" class="add-to-cart-btn" ${!m.available ? 'disabled' : ''}>
                                            ${m.available ? '+ ADD' : 'SOLD OUT'}
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <c:if test="${not empty sessionScope.cart && sessionScope.cart.restaurantId == restaurant.restaurantId && not empty sessionScope.cart.items}">
        <a href="${pageContext.request.contextPath}/CartServlet" class="btn btn-primary" style="position:fixed;bottom:30px;left:50%;transform:translateX(-50%);z-index:50;box-shadow:var(--shadow-lg);">
            🛒 View Cart (${sessionScope.cart.totalItemCount} items) &middot; ₹${sessionScope.cart.itemTotal}
        </a>
    </c:if>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
