<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users | TummyFull Admin</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="users" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar"><h1>Manage Users</h1></div>
        <jsp:include page="/includes/alerts.jsp"/>

        <c:if test="${not empty targetUser}">
            <div class="panel">
                <div class="panel-head"><h3>Order History — ${targetUser.fullName}</h3></div>
                <table class="data-table">
                    <thead><tr><th>Order ID</th><th>Restaurant</th><th>Total</th><th>Status</th><th>Placed At</th></tr></thead>
                    <tbody>
                    <c:forEach var="o" items="${userOrders}">
                        <tr>
                            <td>#${o.orderId}</td><td>${o.restaurantName}</td><td>₹${o.finalTotal}</td>
                            <td><span class="status-tag status-${o.orderStatus}">${fn:replace(o.orderStatus,'_',' ')}</span></td>
                            <td>${o.placedAt}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <div class="panel">
            <table class="data-table">
                <thead><tr><th>Name</th><th>Email</th><th>Phone</th><th>City</th><th>Status</th><th>Joined</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>${u.fullName}</td>
                        <td>${u.email}</td>
                        <td>${u.phone}</td>
                        <td>${u.city}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.blocked}"><span class="status-tag status-CANCELLED">Blocked</span></c:when>
                                <c:when test="${u.verified}"><span class="status-tag status-DELIVERED">Verified</span></c:when>
                                <c:otherwise><span class="status-tag status-ACCEPTED">Unverified</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>${u.createdAt}</td>
                        <td class="action-icons">
                            <a href="${pageContext.request.contextPath}/AdminUserServlet?viewOrdersFor=${u.userId}" class="action-icon-btn" title="View Orders">📦</a>
                            <c:choose>
                                <c:when test="${u.blocked}">
                                    <form action="${pageContext.request.contextPath}/AdminUserServlet" method="post">
                                        <input type="hidden" name="action" value="unblock">
                                        <input type="hidden" name="userId" value="${u.userId}">
                                        <button type="submit" class="action-icon-btn" title="Unblock">🔓</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/AdminUserServlet" method="post">
                                        <input type="hidden" name="action" value="block">
                                        <input type="hidden" name="userId" value="${u.userId}">
                                        <button type="submit" class="action-icon-btn danger" title="Block">🔒</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                            <form action="${pageContext.request.contextPath}/AdminUserServlet" method="post" data-confirm="Permanently delete this user?">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="userId" value="${u.userId}">
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
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
