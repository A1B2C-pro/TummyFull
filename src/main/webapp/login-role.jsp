<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | TummyFull</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body>
<div class="role-select-page">
    <div class="page-heading">
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand" style="justify-content:center;margin-bottom:24px;">
            <span class="logo-badge">🍔</span> TummyFull
        </a>
        <h1>Welcome to TummyFull</h1>
        <p>Please select how you'd like to continue</p>
    </div>

    <div class="role-select-grid">
        <div class="role-card">
            <div class="role-icon">🧑</div>
            <h3>Customer</h3>
            <p>Order delicious food from your favorite Bangalore restaurants.</p>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-dark btn-block">Continue as Customer</a>
        </div>
        <div class="role-card">
            <div class="role-icon">🛡️</div>
            <h3>Admin</h3>
            <p>Manage restaurants, menus, orders and delivery operations.</p>
            <a href="${pageContext.request.contextPath}/admin-login.jsp" class="btn btn-dark btn-block">Continue as Admin</a>
        </div>
        <div class="role-card">
            <div class="role-icon">🛵</div>
            <h3>Delivery Agent</h3>
            <p>Accept deliveries and manage your assigned orders on the go.</p>
            <a href="${pageContext.request.contextPath}/agent-login.jsp" class="btn btn-dark btn-block">Continue as Agent</a>
        </div>
    </div>
</div>
</body>
</html>
