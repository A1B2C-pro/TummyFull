<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Orders | TummyFull</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<jsp:include page="/includes/navbar-customer.jsp"/>

<div class="page-header-strip">
    <div class="container"><h1>Your Orders</h1><p>Track current orders and revisit past ones</p></div>
</div>

<div class="container mt-3 mb-4">
    <jsp:include page="/includes/alerts.jsp"/>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="empty-state">
                <div class="icon">📦</div>
                <h3>No orders yet</h3>
                <p>When you place an order, it will show up here.</p>
                <a href="${pageContext.request.contextPath}/RestaurantServlet" class="btn btn-primary mt-2">Order Now</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="panel">
                <table class="data-table">
                    <thead>
                        <tr><th>Order ID</th><th>Restaurant</th><th>Items</th><th>Total</th><th>Status</th><th>Placed At</th><th></th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="o" items="${orders}">
                        <tr>
                            <td>#${o.orderId}</td>
                            <td>${o.restaurantName}</td>
                            <td>${fn:length(o.orderItems)} item(s)</td>
                            <td>₹${o.finalTotal}</td>
                            <td><span class="status-tag status-${o.orderStatus}">${fn:replace(o.orderStatus, '_', ' ')}</span></td>
                            <td>${o.placedAt}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/OrderTrackServlet?orderId=${o.orderId}" class="action-icon-btn" title="Track">view</a>
                                <a href="${pageContext.request.contextPath}/InvoiceServlet?orderId=${o.orderId}" class="action-icon-btn" title="Invoice">download invoice</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
