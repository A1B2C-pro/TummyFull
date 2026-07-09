<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Offers &amp; Coupons | TummyFull Admin</title>
        <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<div class="dash-layout">
    <c:set var="activePage" value="offers" scope="request"/>
    <jsp:include page="/includes/admin-sidebar.jsp"/>

    <main class="dash-main">
        <div class="dash-topbar"><h1>Offers &amp; Coupons</h1></div>
        <jsp:include page="/includes/alerts.jsp"/>

        <div class="panel">
            <div class="panel-head">
                <h3>Festival Offers &amp; Discounts</h3>
                <button class="btn btn-primary btn-sm" data-modal-target="#offerModal" onclick="resetOfferForm()">+ Add Offer</button>
            </div>
            <table class="data-table">
                <thead><tr><th>Title</th><th>Description</th><th>Restaurant</th><th>Discount</th><th>Valid Till</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="o" items="${offers}">
                    <tr>
                        <td>${o.title}</td>
                        <td>${o.description}</td>
                        <td>
                            <c:forEach var="r" items="${restaurants}">
                                <c:if test="${r.restaurantId == o.restaurantId}">${r.name}</c:if>
                            </c:forEach>
                            <c:if test="${empty o.restaurantId}">All Restaurants</c:if>
                        </td>
                        <td>${o.discountPercent}%</td>
                        <td>${o.validTill}</td>
                        <td class="action-icons">
                            <form action="${pageContext.request.contextPath}/AdminOfferServlet" method="post" data-confirm="Delete this offer?">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="offerId" value="${o.offerId}">
                                <button type="submit" class="action-icon-btn danger" title="Delete">🗑️</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="panel">
            <div class="panel-head">
                <h3>Coupon Codes</h3>
                <button class="btn btn-primary btn-sm" data-modal-target="#couponModal" onclick="resetCouponForm()">+ Add Coupon</button>
            </div>
            <table class="data-table">
                <thead><tr><th>Code</th><th>Description</th><th>Discount</th><th>Max Discount</th><th>Min Order</th><th>Valid Till</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="c" items="${coupons}">
                    <tr>
                        <td><strong>${c.code}</strong></td>
                        <td>${c.description}</td>
                        <td>${c.discountPercent}%</td>
                        <td>₹${c.maxDiscount}</td>
                        <td>₹${c.minOrderValue}</td>
                        <td>${c.validTill}</td>
                        <td class="action-icons">
                            <form action="${pageContext.request.contextPath}/AdminCouponServlet" method="post" data-confirm="Delete this coupon?">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="couponId" value="${c.couponId}">
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

<div class="modal-overlay" id="offerModal">
    <div class="modal-box">
        <span class="modal-close" data-modal-close>&times;</span>
        <h3>Add Offer</h3>
        <form action="${pageContext.request.contextPath}/AdminOfferServlet" method="post">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label>Title</label>
                <input type="text" name="title" class="form-control" placeholder="Diwali Festival Special" required>
            </div>
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" class="form-control" rows="2"></textarea>
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Restaurant (optional)</label>
                    <select name="restaurantId" class="form-control">
                        <option value="">All Restaurants</option>
                        <c:forEach var="r" items="${restaurants}">
                            <option value="${r.restaurantId}">${r.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Discount %</label>
                    <input type="number" step="0.01" name="discountPercent" class="form-control" required>
                </div>
            </div>
            <div class="form-group">
                <label>Valid Till</label>
                <input type="date" name="validTill" class="form-control">
            </div>
            <button type="submit" class="btn btn-primary btn-block">Save Offer</button>
        </form>
    </div>
</div>

<div class="modal-overlay" id="couponModal">
    <div class="modal-box">
        <span class="modal-close" data-modal-close>&times;</span>
        <h3>Add Coupon</h3>
        <form action="${pageContext.request.contextPath}/AdminCouponServlet" method="post">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label>Coupon Code</label>
                <input type="text" name="code" class="form-control" placeholder="TUMMY50" required style="text-transform:uppercase;">
            </div>
            <div class="form-group">
                <label>Description</label>
                <input type="text" name="description" class="form-control" placeholder="Flat 50% off for new users">
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Discount %</label>
                    <input type="number" step="0.01" name="discountPercent" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Max Discount (₹)</label>
                    <input type="number" step="0.01" name="maxDiscount" class="form-control" required>
                </div>
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label>Min Order Value (₹)</label>
                    <input type="number" step="0.01" name="minOrderValue" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Valid Till</label>
                    <input type="date" name="validTill" class="form-control">
                </div>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Save Coupon</button>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
function resetOfferForm() {}
function resetCouponForm() {}
</script>
</body>
</html>
