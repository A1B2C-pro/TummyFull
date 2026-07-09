<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Become a Delivery Partner | TummyFull</title>
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
            <h1>Earn on Your Own Schedule</h1>
            <p>Join TummyFull's delivery fleet in Bangalore. Flexible hours, weekly payouts, and a simple app to manage deliveries.</p>
        </div>
        <div class="auth-brand-footer">&copy; 2026 TummyFull. All rights reserved.</div>
    </div>

    <div class="auth-form-panel">
        <div class="auth-form-box">
            <h2>Delivery Partner Sign Up</h2>
            <p class="auth-subtitle">Fill in your details to get started</p>

            <jsp:include page="/includes/alerts.jsp"/>

            <form action="${pageContext.request.contextPath}/AgentRegisterServlet" method="post">
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="fullName" class="form-control" placeholder="John Doe" required>
                </div>
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" class="form-control" placeholder="you@example.com" required>
                </div>
                <div class="form-group">
                    <label>Phone Number</label>
                    <input type="tel" name="phone" class="form-control" placeholder="9876543210" pattern="[6-9][0-9]{9}" required>
                </div>
                <div class="form-group">
                    <label>Vehicle Number</label>
                    <input type="text" name="vehicleNumber" class="form-control" placeholder="KA01AB1234" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" placeholder="Minimum 6 characters" minlength="6" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Sign Up Now</button>
            </form>

            <p class="auth-switch">Already a delivery partner? <a href="${pageContext.request.contextPath}/agent-login.jsp" class="auth-link">Login</a></p>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
