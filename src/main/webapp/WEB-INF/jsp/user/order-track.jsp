<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Track Order #${order.orderId} | TummyFull</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>
<jsp:include page="/includes/navbar-customer.jsp"/>

<div class="page-header-strip">
    <div class="container">
        <h1>Order #${order.orderId}</h1>
        <p>${order.restaurantName}</p>
    </div>
</div>

<div class="container mt-3 mb-4">
    <c:if test="${justPlaced}">
        <div class="alert alert-success">✓ Your order has been placed successfully! We'll notify you as it progresses.</div>
    </c:if>

    <%
        String status = ((com.tap.model.Order) request.getAttribute("order")).getOrderStatus();
        java.util.List<String> steps = java.util.Arrays.asList(
                "PLACED", "ACCEPTED", "PREPARING", "READY_FOR_PICKUP", "OUT_FOR_DELIVERY", "DELIVERED");
        int currentIdx = steps.indexOf(status);
        request.setAttribute("statusSteps", steps);
        request.setAttribute("currentIdx", currentIdx);
        request.setAttribute("isCancelled", "CANCELLED".equals(status));
    %>

    <div class="panel">
        <c:choose>
            <c:when test="${isCancelled}">
                <div class="empty-state">
                    <div class="icon">✕</div>
                    <h3>This order was cancelled</h3>
                </div>
            </c:when>
            <c:otherwise>
                <div class="order-status-tracker">
                    <c:forEach var="step" items="${statusSteps}" varStatus="vs">
                        <div class="tracker-step ${vs.index < currentIdx ? 'done' : (vs.index == currentIdx ? 'current' : '')}">
                            <div class="tracker-dot">${vs.index <= currentIdx ? '✓' : vs.index + 1}</div>
                            <div class="tracker-label">${fn:replace(step, '_', ' ')}</div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="text-center mt-2">
            <span class="status-tag status-${order.orderStatus}">${fn:replace(order.orderStatus, '_', ' ')}</span>
        </div>
    </div>

    <div class="cart-layout mt-3">
        <div class="panel">
            <div class="panel-head"><h3>Items</h3></div>
            <c:forEach var="item" items="${order.orderItems}">
                <div class="invoice-line">
                    <span>${item.itemName} x${item.quantity}</span>
                    <span>₹${item.subtotal}</span>
                </div>
            </c:forEach>
        </div>
        <div class="summary-box">
            <h3 class="mb-2">Order Info</h3>
            <div class="summary-row"><span>Customer</span><span>${order.customerName}</span></div>
            <div class="summary-row"><span>Phone</span><span>${order.customerPhone}</span></div>
            <div class="summary-row"><span>Address</span><span style="text-align:right;max-width:220px;">${order.deliveryAddress}</span></div>
            <div class="summary-row"><span>Payment</span><span>${order.paymentMethod}</span></div>
            <div class="summary-row total"><span>Final Total</span><span>₹${order.finalTotal}</span></div>
            <a href="${pageContext.request.contextPath}/InvoiceServlet?orderId=${order.orderId}" class="btn btn-outline btn-block mt-2">⬇ Download Invoice</a>
            <a href="${pageContext.request.contextPath}/OrderHistoryServlet" class="btn btn-dark btn-block mt-2">Back to Orders</a>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
