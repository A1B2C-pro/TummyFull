<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Cart | TummyFull</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>
<jsp:include page="/includes/navbar-customer.jsp"/>

<div class="page-header-strip">
    <div class="container">
        <h1>Your Cart</h1>
        <c:if test="${not empty cart && not empty cart.items}"><p>Ordering from <strong>${cart.restaurantName}</strong></p></c:if>
    </div>
</div>

<div class="container mt-3 mb-4">
    <jsp:include page="/includes/alerts.jsp"/>

    <c:choose>
    <c:when test="${empty cart || empty cart.items}">
            <div class="empty-state">
                <div class="icon">🛒</div>
                <h3>Your cart is empty</h3>
                <p>Looks like you haven't added anything yet.</p>
                <a href="${pageContext.request.contextPath}/RestaurantServlet" class="btn btn-primary mt-2">Browse Restaurants</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="cart-layout">
                <div class="panel">
                    <div class="panel-head">
                        <h3>Items in Cart</h3>
                        <form action="${pageContext.request.contextPath}/CartServlet" method="post" data-confirm="Clear your entire cart?">
                            <input type="hidden" name="action" value="clear">
                            <button type="submit" class="btn btn-outline btn-sm">Clear Cart</button>
                        </form>
                    </div>
                    <c:forEach var="entry" items="${cart.items}">
                        <c:set var="item" value="${entry.value}"/>
                        <div class="cart-item-row">
                            <img src="${item.imageUrl}" alt="${item.menuName}">
                            <div class="cart-item-info">
                                <h4>${item.menuName}</h4>
                                <span class="text-muted">₹${item.price} x ${item.quantity} = ₹${item.subtotal}</span>
                            </div>
                            <div class="cart-qty-control">
                                <form action="${pageContext.request.contextPath}/CartServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="menuId" value="${item.menuId}">
                                    <input type="hidden" name="action" value="decrease">
                                    <button type="submit">−</button>
                                </form>
                                <span>${item.quantity}</span>
                                <form action="${pageContext.request.contextPath}/CartServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="menuId" value="${item.menuId}">
                                    <input type="hidden" name="action" value="increase">
                                    <button type="submit">+</button>
                                </form>
                            </div>
                            <form action="${pageContext.request.contextPath}/CartServlet" method="post">
                                <input type="hidden" name="menuId" value="${item.menuId}">
                                <input type="hidden" name="action" value="remove">
                                <button type="submit" class="action-icon-btn danger" title="Remove">🗑️</button>
                            </form>
                        </div>
                    </c:forEach>
                </div>

                <div class="summary-box">
                    <h3 class="mb-2">Order Summary</h3>
                    <div class="summary-row"><span>Item Total</span><span>₹${cart.itemTotal}</span></div>
                    <div class="summary-row"><span>Platform Fee</span><span>₹3.00</span></div>
                    <div class="summary-row"><span>Delivery Fee</span><span>₹10.00</span></div>
                    <div class="summary-row"><span>GST (%)</span><span>Calculated at checkout</span></div>
                    <div class="summary-row total"><span>Estimated Total</span><span>₹${cart.itemTotal + 13}+</span></div>
                    <a href="${pageContext.request.contextPath}/CheckoutServlet" class="btn btn-primary btn-block mt-2">Proceed to Checkout</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
