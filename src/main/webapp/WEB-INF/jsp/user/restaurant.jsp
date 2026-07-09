<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restaurants in Bangalore | TummyFull</title>
    <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>
<jsp:include page="/includes/navbar-customer.jsp"/>

<div class="page-header-strip with-bg" id="heroStrip">
    <div class="container">
        <h1>Restaurants near you in Bangalore</h1>
        <p>Handpicked favorites, delivered hot and fresh</p>
    </div>
</div>

<div class="container restaurant-container mt-3">
    <jsp:include page="/includes/alerts.jsp"/>

    <div class="restaurant-page-layout">
        <aside class="filter-sidebar">
            <h3>Categories</h3>
            <div class="chip-col">
                <a href="${pageContext.request.contextPath}/RestaurantServlet" class="chip ${empty searchQuery ? 'active' : ''}">🍽️ All</a>
                <c:forEach var="cat" items="${categories}">
                    <a href="${pageContext.request.contextPath}/RestaurantServlet?q=${cat.categoryName}" class="chip">
                        <img src="${cat.imageUrl}" alt="${cat.categoryName}"> ${cat.categoryName}
                    </a>
                </c:forEach>
            </div>

            <h3>Filters</h3>
            <div class="filter-col">
                <a href="${pageContext.request.contextPath}/RestaurantServlet?sort=rating${not empty searchQuery ? '&q='.concat(searchQuery) : ''}" class="filter-pill">⭐ Top Rated</a>
                <a href="${pageContext.request.contextPath}/RestaurantServlet?sort=fast${not empty searchQuery ? '&q='.concat(searchQuery) : ''}" class="filter-pill">⚡ Fast Delivery</a>
                <a href="${pageContext.request.contextPath}/RestaurantServlet" class="filter-pill">✕ Clear Filters</a>
            </div>
        </aside>

        <div>
            <c:choose>
                <c:when test="${empty restaurants}">
                    <div class="empty-state">
                        <div class="icon">🍽️</div>
                        <h3>No restaurants found</h3>
                        <p>Try a different search term or clear your filters.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="grid mb-3">
                        <c:forEach var="r" items="${restaurants}">
                            <a href="${pageContext.request.contextPath}/MenuServlet?restaurantId=${r.restaurantId}" class="card restaurant-card">
                                <div class="card-image-wrap">
                                    <img src="${r.imageUrl}" alt="${r.name}">
                                    <c:if test="${not empty r.offerText}"><span class="badge badge-offer">${r.offerText}</span></c:if>
                                    <c:if test="${!r.open}"><span class="badge badge-status-closed">Closed</span></c:if>
                                </div>
                                <div class="card-body">
                                    <div class="card-title">${r.name}</div>
                                    <div class="card-sub">${r.cuisine}</div>
                                    <div class="card-meta-row">
                                        <span class="rating-pill">★ ${r.rating}</span>
                                        <span>🕒 ${r.deliveryTime}</span>
                                    </div>
                                    <div class="card-meta-row card-meta-row" style="margin-top:8px;">
                                        <span class="text-muted">📍 ${r.address}</span>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
(function () {
    var heroImages = [
        <c:forEach var="r" items="${restaurants}" varStatus="vs">'${r.imageUrl}'<c:if test="${!vs.last}">,</c:if></c:forEach>
    ];
    var strip = document.getElementById('heroStrip');
    if (!strip || heroImages.length === 0) return;
    var i = 0;
    function showSlide(index) {
        strip.style.backgroundImage = "url('" + heroImages[index] + "')";
    }
    showSlide(0);
    if (heroImages.length > 1) {
        setInterval(function () {
            i = (i + 1) % heroImages.length;
            showSlide(i);
        }, 4000);
    }
})();
</script>
</body>
</html>
