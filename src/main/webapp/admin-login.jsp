<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login | TummyFull</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body>
<div class="auth-page">
    <div class="auth-brand-panel">
        <a href="${pageContext.request.contextPath}/login-role.jsp" class="auth-back-link">←</a>
        <div class="auth-brand-content">
            <div class="navbar-brand"><span class="logo-badge">🍔</span> TummyFull</div>
            <h1>Admin Control Panel</h1>
            <p>Manage restaurants, menus, orders, delivery agents and offers across all of Bangalore from one dashboard.</p>
        </div>
        <div class="auth-brand-footer">&copy; 2026 TummyFull. All rights reserved.</div>
    </div>

    <div class="auth-form-panel">
        <div class="auth-form-box">
            <h2>Admin Login</h2>
            <p class="auth-subtitle">Authorized personnel only</p>

            <jsp:include page="/includes/alerts.jsp"/>

            <form action="${pageContext.request.contextPath}/AdminLoginServlet" method="post">
                <div class="form-group">
                    <label>Admin Email</label>
                    <input type="email" name="email" class="form-control" placeholder="admin@tummyfull.com" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Login to Dashboard</button>
            </form>

            <p class="auth-switch">Not an admin? <a href="${pageContext.request.contextPath}/login-role.jsp" class="auth-link">Choose a different role</a></p>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
