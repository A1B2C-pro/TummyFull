<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Login | TummyFull</title>
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
            <h1>Welcome Back</h1>
            <p>Login to order your favorite food from the best restaurants in Bangalore, delivered fresh and fast.</p>
        </div>
        <div class="auth-brand-footer">&copy; 2026 TummyFull. All rights reserved.</div>
    </div>

    <div class="auth-form-panel">
        <div class="auth-form-box">
            <h2>Login</h2>
            <p class="auth-subtitle">Enter your details to access your account</p>

            <jsp:include page="/includes/alerts.jsp"/>

            <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" class="form-control" placeholder="you@example.com" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                </div>
                <div class="auth-row-between">
                    <label class="auth-checkbox">
                        <input type="checkbox"> Remember Me
                    </label>
                    <a href="${pageContext.request.contextPath}/forgot-password.jsp" class="auth-link">Forgot Password?</a>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Login Now</button>
            </form>

            <p class="auth-terms">By clicking "Login Now" you agree to our <a href="#" class="auth-link">Terms of Service</a> &amp; <a href="#" class="auth-link">Privacy Policy</a></p>
            <p class="auth-switch">Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp" class="auth-link">Sign Up</a></p>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
