<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password | TummyFull</title>
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
            <h1>Forgot Your Password?</h1>
            <p>No worries — enter your registered email and we'll send you a One-Time Password to reset it.</p>
        </div>
        <div class="auth-brand-footer">&copy; 2026 TummyFull. All rights reserved.</div>
    </div>

    <div class="auth-form-panel">
        <div class="auth-form-box">
            <h2>Reset Password</h2>
            <p class="auth-subtitle">We'll email you a 6-digit OTP to verify it's you</p>

            <jsp:include page="/includes/alerts.jsp"/>

            <form action="${pageContext.request.contextPath}/ForgotPasswordServlet" method="post">
                <div class="form-group">
                    <label>I am a</label>
                    <select name="role" class="form-control">
                        <option value="CUSTOMER" ${param.role != 'DELIVERY' ? 'selected' : ''}>Customer</option>
                        <option value="DELIVERY" ${param.role == 'DELIVERY' ? 'selected' : ''}>Delivery Agent</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Registered Email Address</label>
                    <input type="email" name="email" class="form-control" placeholder="you@example.com" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Send OTP</button>
            </form>

            <p class="auth-switch">Remembered your password? <a href="${pageContext.request.contextPath}/login-role.jsp" class="auth-link">Login</a></p>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
