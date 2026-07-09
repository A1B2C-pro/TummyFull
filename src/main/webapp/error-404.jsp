<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Not Found | TummyFull</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="empty-state" style="min-height:100vh;display:flex;flex-direction:column;align-items:center;justify-content:center;">
    <div class="icon">🍔</div>
    <h1 style="font-size:60px;color:var(--color-primary);">404</h1>
    <p style="margin-bottom:24px;">Oops! The page you're looking for has been delivered elsewhere.</p>
    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary">Back to Home</a>
</div>
</body>
</html>
