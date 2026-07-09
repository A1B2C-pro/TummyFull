<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout | TummyFull</title>
     <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>
<jsp:include page="/includes/navbar-customer.jsp"/>

<div class="page-header-strip">
    <div class="container"><h1>Checkout</h1><p>Review your order and complete payment</p></div>
</div>

<div class="container mt-3 mb-4">
    <jsp:include page="/includes/alerts.jsp"/>

    <form action="${pageContext.request.contextPath}/PlaceOrderServlet" method="post" id="placeOrderForm">
        <input type="hidden" name="razorpayOrderId" id="razorpayOrderId">
        <input type="hidden" name="razorpayPaymentId" id="razorpayPaymentId">
        <input type="hidden" name="razorpaySignature" id="razorpaySignature">

        <div class="checkout-layout">
            <div>
                <div class="panel">
                    <div class="panel-head"><h3>Delivery Details</h3></div>
                    <div class="form-group">
                        <label>Full Name</label>
                        <input type="text" name="customerName" class="form-control" value="${user.fullName}" required>
                    </div>
                    <div class="form-grid-2">
                        <div class="form-group">
                            <label>Phone Number</label>
                            <input type="tel" name="phone" class="form-control" value="${user.phone}" pattern="[6-9][0-9]{9}" required>
                        </div>
                        <div class="form-group">
                            <label>City</label>
                            <input type="text" class="form-control" value="Bangalore" disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Delivery Address</label>
                        <textarea name="address" class="form-control" rows="2" required>${user.address}</textarea>
                    </div>
                    <div class="form-group">
                        <label>Delivery Instructions (optional)</label>
                        <input type="text" name="instructions" class="form-control" placeholder="e.g. Ring the bell twice">
                    </div>
                </div>

                <div class="panel">
                    <div class="panel-head"><h3>Payment Method</h3></div>
                    <label class="payment-option selected">
                        <input type="radio" name="paymentMethod" value="COD" checked>
                        <span class="pay-icon">💵</span> Cash on Delivery
                    </label>
                    <label class="payment-option">
                        <input type="radio" name="paymentMethod" value="UPI">
                        <span class="pay-icon">📱</span> UPI (Razorpay)
                    </label>
                    <label class="payment-option">
                        <input type="radio" name="paymentMethod" value="CREDIT_CARD">
                        <span class="pay-icon">💳</span> Credit Card (Razorpay)
                    </label>
                    <label class="payment-option">
                        <input type="radio" name="paymentMethod" value="DEBIT_CARD">
                        <span class="pay-icon">💳</span> Debit Card (Razorpay)
                    </label>
                    <p class="text-muted mt-1" style="font-size:12.5px;">UPI/Card payments are securely processed by Razorpay. You'll see a payment window after clicking "Place Order".</p>
                </div>
            </div>

            <div class="summary-box">
                <h3 class="mb-2">Order Summary</h3>
                <c:forEach var="entry" items="${cart.items}">
                    <div class="summary-row"><span>${entry.value.menuName} x${entry.value.quantity}</span><span>₹${entry.value.subtotal}</span></div>
                </c:forEach>
                <hr style="border:none;border-top:1px dashed var(--color-border);margin:14px 0;">
                <div class="summary-row"><span>Item Total</span><span>₹${itemTotal}</span></div>
                <div class="summary-row"><span>Platform Fee</span><span>₹${platformFee}</span></div>
                <div class="summary-row"><span>Delivery Fee</span><span>₹${deliveryFee}</span></div>
                <div class="summary-row"><span>GST (2%)</span><span>₹${gst}</span></div>
                <c:if test="${couponDiscount > 0}">
                    <div class="summary-row"><span>Coupon (${couponCode})</span><span>−₹${couponDiscount}</span></div>
                </c:if>
                <div class="summary-row total"><span>Final Total</span><span>₹${finalTotal}</span></div>
            </div>
        </div>
    </form>

    <div class="panel mt-3" style="max-width:460px;">
        <div class="panel-head"><h3>Have a Coupon?</h3></div>
        <form action="${pageContext.request.contextPath}/CheckoutServlet" method="post" class="coupon-row">
            <input type="text" name="couponCode" class="form-control" placeholder="Enter coupon code" value="${couponCode}">
            <button type="submit" class="btn btn-dark">Apply</button>
        </form>
        <p class="text-muted" style="font-size:12.5px;">Try: TUMMY20 or FREESHIP</p>
    </div>

    <button type="button" id="placeOrderBtn" class="btn btn-primary btn-block mt-3" style="max-width:460px;">Place Order &middot; ₹${finalTotal}</button>
    <p id="paymentStatusMsg" class="text-muted mt-1" style="max-width:460px;"></p>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
<script>
document.getElementById('placeOrderBtn').addEventListener('click', function () {
    var form = document.getElementById('placeOrderForm');
    if (!form.reportValidity()) return; // run native HTML5 validation first

    var paymentMethod = form.querySelector('input[name="paymentMethod"]:checked').value;
    var btn = document.getElementById('placeOrderBtn');
    var statusMsg = document.getElementById('paymentStatusMsg');

    if (paymentMethod === 'COD') {
        form.submit();
        return;
    }

    // Non-COD: create a Razorpay order server-side first, then open Checkout.js
    btn.disabled = true;
    btn.textContent = 'Preparing secure payment...';
    statusMsg.textContent = '';

    fetch('${pageContext.request.contextPath}/CreateRazorpayOrderServlet', { method: 'POST' })
        .then(function (res) { return res.json(); })
        .then(function (data) {
            if (data.error) {
                statusMsg.textContent = data.error;
                btn.disabled = false;
                btn.textContent = 'Place Order · ₹${finalTotal}';
                return;
            }
            var razorpayMethodMap = { 'UPI': 'upi', 'CREDIT_CARD': 'card', 'DEBIT_CARD': 'card' };
            var options = {
                key: data.keyId,
                amount: data.amountInPaise,
                currency: 'INR',
                name: 'TummyFull',
                description: 'Food order payment',
                order_id: data.razorpayOrderId,
                prefill: {
                    name: data.customerName,
                    email: data.customerEmail,
                    contact: data.customerPhone,
                    method: razorpayMethodMap[paymentMethod] || ''
                },
                theme: { color: '#1b3a2b' },
                handler: function (response) {
                    document.getElementById('razorpayOrderId').value = response.razorpay_order_id;
                    document.getElementById('razorpayPaymentId').value = response.razorpay_payment_id;
                    document.getElementById('razorpaySignature').value = response.razorpay_signature;
                    form.submit();
                },
                modal: {
                    ondismiss: function () {
                        btn.disabled = false;
                        btn.textContent = 'Place Order · ₹${finalTotal}';
                        statusMsg.textContent = 'Payment window closed. You can try again.';
                    }
                }
            };

            var rzp = new Razorpay(options);
            rzp.on('payment.failed', function (response) {
                statusMsg.textContent = 'Payment failed: ' + response.error.description;
                btn.disabled = false;
                btn.textContent = 'Place Order · ₹${finalTotal}';
            });
            rzp.open();
        })
        .catch(function () {
            statusMsg.textContent = 'Could not connect for payment. Please try again.';
            btn.disabled = false;
            btn.textContent = 'Place Order · ₹${finalTotal}';
        });
});
</script>
</body>
</html>





