<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Delivery Agents | TummyFull Admin</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="delivery" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar">
            <h1>Manage Delivery Agents</h1>
            <button class="btn btn-primary" data-modal-target="#agentModal" onclick="resetAgentForm()">+ Add Agent</button>
        </div>
        <jsp:include page="/includes/alerts.jsp"/>

        <c:if test="${not empty targetAgent}">
            <div class="panel">
                <div class="panel-head"><h3>Orders — ${targetAgent.fullName}</h3></div>
                <table class="data-table">
                    <thead><tr><th>Order ID</th><th>Customer</th><th>Restaurant</th><th>Status</th></tr></thead>
                    <tbody>
                    <c:forEach var="o" items="${agentOrders}">
                        <tr>
                            <td>#${o.orderId}</td><td>${o.customerName}</td><td>${o.restaurantName}</td>
                            <td><span class="status-tag status-${o.orderStatus}">${fn:replace(o.orderStatus,'_',' ')}</span></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <div class="panel">
            <table class="data-table">
                <thead><tr><th>Name</th><th>Email</th><th>Phone</th><th>Vehicle</th><th>Available</th><th>Verified</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="a" items="${agents}">
                    <tr>
                        <td>${a.fullName}</td>
                        <td>${a.email}</td>
                        <td>${a.phone}</td>
                        <td>${a.vehicleNumber}</td>
                        <td><span class="status-tag ${a.available ? 'status-DELIVERED' : 'status-CANCELLED'}">${a.available ? 'Available' : 'Offline'}</span></td>
                        <td><span class="status-tag ${a.verified ? 'status-DELIVERED' : 'status-ACCEPTED'}">${a.verified ? 'Verified' : 'Pending'}</span></td>
                        <td class="action-icons">
                            <a href="${pageContext.request.contextPath}/AdminDeliveryServlet?viewOrdersFor=${a.agentId}" class="action-icon-btn" title="View Orders">📦</a>
                            <button class="action-icon-btn" title="Edit"
                                onclick='openEditAgent(${a.agentId}, "${a.fullName}", "${a.phone}", "${a.vehicleNumber}")'>✏️</button>
                            <form action="${pageContext.request.contextPath}/AdminDeliveryServlet" method="post" data-confirm="Remove this delivery agent?">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="agentId" value="${a.agentId}">
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

<div class="modal-overlay" id="agentModal">
    <div class="modal-box">
        <span class="modal-close" data-modal-close>&times;</span>
        <h3 id="agentModalTitle">Add Delivery Agent</h3>
        <form action="${pageContext.request.contextPath}/AdminDeliveryServlet" method="post">
            <input type="hidden" name="action" id="agentFormAction" value="add">
            <input type="hidden" name="agentId" id="agentId">
            <div class="form-group">
                <label>Full Name</label>
                <input type="text" name="fullName" id="aName" class="form-control" required>
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" id="aEmail" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="tel" name="phone" id="aPhone" class="form-control" pattern="[6-9][0-9]{9}" required>
                </div>
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Vehicle Number</label>
                    <input type="text" name="vehicleNumber" id="aVehicle" class="form-control" required>
                </div>
                <div class="form-group" id="aPasswordGroup">
                    <label>Password</label>
                    <input type="password" name="password" id="aPassword" class="form-control" minlength="6">
                </div>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Save Agent</button>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
function resetAgentForm() {
    document.getElementById('agentModalTitle').textContent = 'Add Delivery Agent';
    document.getElementById('agentFormAction').value = 'add';
    document.getElementById('agentId').value = '';
    document.getElementById('aName').value = '';
    document.getElementById('aEmail').value = '';
    document.getElementById('aEmail').disabled = false;
    document.getElementById('aPhone').value = '';
    document.getElementById('aVehicle').value = '';
    document.getElementById('aPassword').required = true;
    document.getElementById('aPasswordGroup').style.display = 'block';
}
function openEditAgent(id, fullName, phone, vehicleNumber) {
    document.getElementById('agentModalTitle').textContent = 'Edit Delivery Agent';
    document.getElementById('agentFormAction').value = 'update';
    document.getElementById('agentId').value = id;
    document.getElementById('aName').value = fullName;
    document.getElementById('aPhone').value = phone;
    document.getElementById('aVehicle').value = vehicleNumber;
    document.getElementById('aPassword').required = false;
    document.getElementById('aPasswordGroup').style.display = 'none';
    document.getElementById('agentModal').classList.add('open');
}
</script>
</body>
</html>
