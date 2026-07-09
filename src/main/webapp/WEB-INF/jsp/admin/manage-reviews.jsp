<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Reviews | TummyFull Admin</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="reviews" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar"><h1>Manage Reviews</h1></div>
        <jsp:include page="/includes/alerts.jsp"/>

        <div class="panel">
            <table class="data-table">
                <thead><tr><th>User</th><th>Rating</th><th>Comment</th><th>Highlighted</th><th>Date</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="r" items="${reviews}">
                    <tr>
                        <td>${r.userName}</td>
                        <td class="review-stars">
                            <c:forEach begin="1" end="5" var="i">${i <= r.rating ? '★' : '☆'}</c:forEach>
                        </td>
                        <td>${r.comment}</td>
                        <td>
                            <c:choose>
                                <c:when test="${r.highlighted}"><span class="status-tag status-DELIVERED">Highlighted</span></c:when>
                                <c:otherwise><span class="status-tag status-PLACED">Normal</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>${r.createdAt}</td>
                        <td class="action-icons">
                            <c:choose>
                                <c:when test="${r.highlighted}">
                                    <form action="${pageContext.request.contextPath}/AdminReviewServlet" method="post">
                                        <input type="hidden" name="action" value="unhighlight">
                                        <input type="hidden" name="reviewId" value="${r.reviewId}">
                                        <button type="submit" class="action-icon-btn" title="Unhighlight">☆</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/AdminReviewServlet" method="post">
                                        <input type="hidden" name="action" value="highlight">
                                        <input type="hidden" name="reviewId" value="${r.reviewId}">
                                        <button type="submit" class="action-icon-btn" title="Highlight">⭐</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                            <form action="${pageContext.request.contextPath}/AdminReviewServlet" method="post" data-confirm="Delete this review?">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="reviewId" value="${r.reviewId}">
                                <button type="submit" class="action-icon-btn danger" title="Delete">🗑️</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
