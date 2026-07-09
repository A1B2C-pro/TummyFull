<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Orders | TummyFull Admin</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="orders" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar"><h1>Manage Orders</h1></div>
        <jsp:include page="/includes/alerts.jsp"/>

        <div class="filter-row">
            <a href="${pageContext.request.contextPath}/AdminOrderServlet" class="filter-pill ${empty statusFilter ? 'active' : ''}">All</a>
            <a href="${pageContext.request.contextPath}/AdminOrderServlet?status=PLACED" class="filter-pill">Pending</a>
            <a href="${pageContext.request.contextPath}/AdminOrderServlet?status=PREPARING" class="filter-pill">Processing</a>
            <a href="${pageContext.request.contextPath}/AdminOrderServlet?status=DELIVERED" class="filter-pill">Delivered</a>
            <a href="${pageContext.request.contextPath}/AdminOrderServlet?status=CANCELLED" class="filter-pill">Cancelled</a>
        </div>

        <div class="panel">
            <table class="data-table">
                <thead><tr><th>ID</th><th>Customer</th><th>Restaurant</th><th>Total</th><th>Status</th><th>Agent</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td>#${o.orderId}</td>
                        <td>${o.customerName}</td>
                        <td>${o.restaurantName}</td>
                        <td>₹${o.finalTotal}</td>
                        <td><span class="status-tag status-${o.orderStatus}">${fn:replace(o.orderStatus,'_',' ')}</span></td>
                        <td>${not empty o.agentName ? o.agentName : '—'}</td>
                        <td class="action-icons">
                            <form action="${pageContext.request.contextPath}/AdminOrderServlet" method="post" class="flex gap-1">
                                <input type="hidden" name="action" value="changeStatus">
                                <input type="hidden" name="orderId" value="${o.orderId}">
                                <select name="status" class="form-control" style="padding:6px 10px;font-size:12.5px;" onchange="this.form.submit()">
                                    <option value="PLACED" ${o.orderStatus == 'PLACED' ? 'selected' : ''}>Placed</option>
                                    <option value="ACCEPTED" ${o.orderStatus == 'ACCEPTED' ? 'selected' : ''}>Accepted</option>
                                    <option value="PREPARING" ${o.orderStatus == 'PREPARING' ? 'selected' : ''}>Preparing</option>
                                    <option value="READY_FOR_PICKUP" ${o.orderStatus == 'READY_FOR_PICKUP' ? 'selected' : ''}>Ready for Pickup</option>
                                    <option value="OUT_FOR_DELIVERY" ${o.orderStatus == 'OUT_FOR_DELIVERY' ? 'selected' : ''}>Out for Delivery</option>
                                    <option value="DELIVERED" ${o.orderStatus == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                    <option value="CANCELLED" ${o.orderStatus == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                </select>
                            </form>
                            <form action="${pageContext.request.contextPath}/AdminOrderServlet" method="post" class="flex gap-1">
                                <input type="hidden" name="action" value="assignAgent">
                                <input type="hidden" name="orderId" value="${o.orderId}">
                                <select name="agentId" class="form-control" style="padding:6px 10px;font-size:12.5px;" onchange="this.form.submit()">
                                    <option value="0">Assign Agent</option>
                                    <c:forEach var="a" items="${availableAgents}">
                                        <option value="${a.agentId}">${a.fullName}</option>
                                    </c:forEach>
                                </select>
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
