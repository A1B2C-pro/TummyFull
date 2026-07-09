<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delivery Dashboard | TummyFull</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout" style="grid-template-columns: 1fr;">
    <main class="dash-main">
        <div class="dash-topbar">
            <div class="navbar-brand"><span class="logo-badge">🍔</span> TummyFull Delivery</div>
            <div class="flex items-center gap-2">
                <form action="${pageContext.request.contextPath}/AgentAvailabilityServlet" method="post">
                    <label class="toggle-switch">
                        <input type="checkbox" ${agent.available ? 'checked' : ''} onchange="this.form.submit()">
                        <span class="toggle-slider"></span>
                    </label>
                </form>
                <span>${agent.available ? '🟢 Available' : '🔴 Offline'}</span>
                <div class="admin-chip">🛵 ${agent.fullName}</div>
                <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn btn-outline btn-sm">Logout</a>
            </div>
        </div>

        <jsp:include page="/includes/alerts.jsp"/>

        <div class="stat-grid" style="grid-template-columns: repeat(3, 1fr);">
            <div class="stat-card">
                <div class="stat-icon blue">📦</div>
                <div><div class="stat-value">${fn:length(assignedOrders)}</div><div class="stat-label">Assigned Orders</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon gold">🚗</div>
                <div><div class="stat-value">${agent.vehicleNumber}</div><div class="stat-label">Vehicle</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon green">📞</div>
                <div><div class="stat-value" style="font-size:16px;">${agent.phone}</div><div class="stat-label">Contact</div></div>
            </div>
        </div>

        <div class="panel">
            <div class="panel-head"><h3>Your Assigned Orders</h3></div>
            <c:choose>
                <c:when test="${empty assignedOrders}">
                    <div class="empty-state">
                        <div class="icon">🛵</div>
                        <h3>No orders assigned yet</h3>
                        <p>Turn on availability and wait for the admin to assign you an order.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="o" items="${assignedOrders}">
                        <div class="panel" style="border:1.5px solid var(--color-border);">
                            <div class="panel-head">
                                <h3>Order #${o.orderId} &middot; <span class="status-tag status-${o.orderStatus}">${fn:replace(o.orderStatus,'_',' ')}</span></h3>
                                <span class="text-muted">₹${o.finalTotal}</span>
                            </div>
                            <div class="form-grid-2 mb-2">
                                <div><strong>Customer:</strong> ${o.customerName} (${o.customerPhone})</div>
                                <div><strong>Restaurant:</strong> ${o.restaurantName}</div>
                                <div><strong>Pickup Address:</strong> ${o.restaurantName}</div>
                                <div><strong>Delivery Address:</strong> ${o.deliveryAddress}</div>
                            </div>
                            <div class="flex gap-1">
                                <c:if test="${o.orderStatus == 'PLACED' || o.orderStatus == 'ACCEPTED'}">
                                    <form action="${pageContext.request.contextPath}/AgentOrderActionServlet" method="post">
                                        <input type="hidden" name="orderId" value="${o.orderId}">
                                        <input type="hidden" name="action" value="accept">
                                        <button type="submit" class="btn btn-dark btn-sm">Accept Delivery</button>
                                    </form>
                                </c:if>
                                <c:if test="${o.orderStatus == 'PREPARING' || o.orderStatus == 'ACCEPTED'}">
                                    <form action="${pageContext.request.contextPath}/AgentOrderActionServlet" method="post">
                                        <input type="hidden" name="orderId" value="${o.orderId}">
                                        <input type="hidden" name="action" value="pickup">
                                        <button type="submit" class="btn btn-dark btn-sm">Pick Up Order</button>
                                    </form>
                                </c:if>
                                <c:if test="${o.orderStatus == 'READY_FOR_PICKUP'}">
                                    <form action="${pageContext.request.contextPath}/AgentOrderActionServlet" method="post">
                                        <input type="hidden" name="orderId" value="${o.orderId}">
                                        <input type="hidden" name="action" value="startDelivery">
                                        <button type="submit" class="btn btn-primary btn-sm">Start Delivery</button>
                                    </form>
                                </c:if>
                                <c:if test="${o.orderStatus == 'OUT_FOR_DELIVERY'}">
                                    <form action="${pageContext.request.contextPath}/AgentOrderActionServlet" method="post">
                                        <input type="hidden" name="orderId" value="${o.orderId}">
                                        <input type="hidden" name="action" value="delivered">
                                        <button type="submit" class="btn btn-primary btn-sm">Mark Delivered</button>
                                    </form>
                                </c:if>
                                <c:if test="${o.orderStatus == 'DELIVERED'}">
                                    <span class="status-tag status-DELIVERED">✓ Completed</span>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
