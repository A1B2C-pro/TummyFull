<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify OTP | TummyFull</title>
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
            <h1>Verify Your Email</h1>
            <p>We've sent a 6-digit One-Time Password to your registered email address via Gmail. Enter it below to continue.</p>
        </div>
        <div class="auth-brand-footer">&copy; 2026 TummyFull. All rights reserved.</div>
    </div>

    <div class="auth-form-panel">
        <div class="auth-form-box">
            <h2>Enter OTP</h2>
            <p class="auth-subtitle">Sent to <strong>${sessionScope.pendingOtpEmail}</strong></p>

            <jsp:include page="/includes/alerts.jsp"/>

            <form action="${pageContext.request.contextPath}/VerifyOTPServlet" method="post" id="otpForm">
                <div class="otp-input-row">
                    <input type="text" class="otp-box" maxlength="1" inputmode="numeric" autofocus>
                    <input type="text" class="otp-box" maxlength="1" inputmode="numeric">
                    <input type="text" class="otp-box" maxlength="1" inputmode="numeric">
                    <input type="text" class="otp-box" maxlength="1" inputmode="numeric">
                    <input type="text" class="otp-box" maxlength="1" inputmode="numeric">
                    <input type="text" class="otp-box" maxlength="1" inputmode="numeric">
                </div>
                <input type="hidden" name="otp" id="otpValue">
                <div class="otp-timer">Code expires in <span id="otpCountdown">5:00</span></div>
                <button type="submit" class="btn btn-primary btn-block">Verify OTP</button>
            </form>

            <form action="${pageContext.request.contextPath}/ResendOTPServlet" method="post" class="text-center mt-2">
                <button type="submit" class="resend-btn" id="resendBtn" disabled>Resend OTP</button>
            </form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/otp.js"></script>
</body>
</html>
