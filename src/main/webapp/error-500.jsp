<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Something Went Wrong | TummyFull</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="empty-state" style="min-height:100vh;display:flex;flex-direction:column;align-items:center;justify-content:center;">
    <div class="icon">😕</div>
    <h1 style="font-size:60px;color:var(--color-primary);">500</h1>
    <p style="margin-bottom:24px;">Something went wrong on our end. Our kitchen is fixing it right now!</p>
    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary">Back to Home</a>
</div>
</body>
</html>
