<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | TummyFull</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>
<jsp:include page="/includes/navbar-customer.jsp"/>

<div class="page-header-strip">
    <div class="container"><h1>My Profile</h1><p>Manage your account details and delivery address</p></div>
</div>

<div class="container mt-3 mb-4">
    <jsp:include page="/includes/alerts.jsp"/>

    <div class="profile-layout">
        <div class="profile-side">
            <div class="profile-avatar">${fn:substring(user.fullName, 0, 1)}</div>
            <h3>${user.fullName}</h3>
            <p>${user.email}</p>
            <div class="profile-side-links">
                <a href="#" class="active">👤 Profile &amp; Address</a>
                <a href="${pageContext.request.contextPath}/OrderHistoryServlet">📦 Order History</a>
                <a href="${pageContext.request.contextPath}/CartServlet">🛒 My Cart</a>
                <a href="${pageContext.request.contextPath}/LogoutServlet">🚪 Logout</a>
            </div>
        </div>

        <div class="panel">
            <div class="panel-head"><h3>Edit Profile &amp; Address</h3></div>
            <form action="${pageContext.request.contextPath}/ProfileServlet" method="post">
                <div class="form-grid-2">
                    <div class="form-group">
                        <label>Full Name</label>
                        <input type="text" name="fullName" class="form-control" value="${user.fullName}" required>
                    </div>
                    <div class="form-group">
                        <label>Phone Number</label>
                        <input type="tel" name="phone" class="form-control" value="${user.phone}" pattern="[6-9][0-9]{9}" required>
                    </div>
                </div>
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" class="form-control" value="${user.email}" disabled>
                </div>
                <div class="form-group">
                    <label>Delivery Address</label>
                    <textarea name="address" class="form-control" rows="2">${user.address}</textarea>
                </div>
                <div class="form-group">
                    <label>City</label>
                    <input type="text" name="city" class="form-control" value="${user.city}">
                </div>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
