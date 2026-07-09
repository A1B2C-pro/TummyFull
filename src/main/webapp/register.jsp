<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | TummyFull</title>
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
            <h1>Join TummyFull Today</h1>
            <p>Create your account and start ordering from Bangalore's best restaurants in minutes.</p>
        </div>
        <div class="auth-brand-footer">&copy; 2026 TummyFull. All rights reserved.</div>
    </div>

    <div class="auth-form-panel">
        <div class="auth-form-box">
            <h2>Create New Account</h2>
            <p class="auth-subtitle">Sign up to get started</p>

            <jsp:include page="/includes/alerts.jsp"/>

            <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
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
                    <label>Delivery Address</label>
                    <input type="text" name="address" class="form-control" placeholder="Flat / Street / Area, Bangalore" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" placeholder="Minimum 6 characters" minlength="6" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Sign Up Now</button>
            </form>

            <p class="auth-terms">By clicking "Sign Up Now" you agree to our <a href="#" class="auth-link">Terms of Service</a> &amp; <a href="#" class="auth-link">Privacy Policy</a></p>
            <p class="auth-switch">Already have an account? <a href="${pageContext.request.contextPath}/login.jsp" class="auth-link">Login</a></p>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
