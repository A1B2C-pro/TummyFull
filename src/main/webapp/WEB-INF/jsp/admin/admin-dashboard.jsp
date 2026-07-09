<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | TummyFull</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="dashboard" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar">
            <h1>Dashboard Overview</h1>
            <div class="admin-chip">🛡️ ${sessionScope.loggedInAdmin.fullName}</div>
        </div>

        <jsp:include page="/includes/alerts.jsp"/>

        <div class="stat-grid">
            <div class="stat-card">
                <div class="stat-icon blue">📦</div>
                <div><div class="stat-value">${totalOrders}</div><div class="stat-label">Total Orders</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon green">💰</div>
                <div><div class="stat-value">₹${totalRevenue}</div><div class="stat-label">Total Revenue</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon gold">👥</div>
                <div><div class="stat-value">${totalUsers}</div><div class="stat-label">Registered Users</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon coral">🏬</div>
                <div><div class="stat-value">${totalRestaurants}</div><div class="stat-label">Restaurants</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon blue">🛵</div>
                <div><div class="stat-value">${activeDeliveries}</div><div class="stat-label">Active Deliveries</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon gold">⭐</div>
                <div><div class="stat-value" style="font-size:16px;">${bestSellingItem}</div><div class="stat-label">Best Selling Item</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon coral">⏳</div>
                <div><div class="stat-value">${pendingOrders}</div><div class="stat-label">Pending Orders</div></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon green">🚴</div>
                <div><div class="stat-value">${totalAgents}</div><div class="stat-label">Delivery Agents</div></div>
            </div>
        </div>

        <div class="panel">
            <div class="panel-head"><h3>Monthly Sales (Delivered Orders)</h3></div>
            <c:choose>
                <c:when test="${empty monthlySales}">
                    <p class="text-muted">No delivered orders yet to chart monthly sales.</p>
                </c:when>
                <c:otherwise>
                    <table class="data-table">
                        <thead><tr><th>Month</th><th>Revenue</th></tr></thead>
                        <tbody>
                        <c:forEach var="entry" items="${monthlySales}">
                            <tr><td>${entry.key}</td><td>₹${entry.value}</td></tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
