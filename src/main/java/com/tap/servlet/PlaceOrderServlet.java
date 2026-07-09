package com.tap.servlet;

import com.tap.dao.CouponDAO;
import com.tap.dao.OrderDAO;
import com.tap.dao.PaymentDAO;
import com.tap.daoimpl.CouponDAOImpl;
import com.tap.daoimpl.OrderDAOImpl;
import com.tap.daoimpl.PaymentDAOImpl;
import com.tap.model.*;
import com.tap.util.RazorpayUtil;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final CouponDAO couponDAO = new CouponDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(SessionConstants.CART);
        User user = (User) session.getAttribute(SessionConstants.CUSTOMER_USER);

        if (cart == null || cart.isEmpty() || user == null) {
            response.sendRedirect(request.getContextPath() + "/CartServlet");
            return;
        }

        String customerName = request.getParameter("customerName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String instructions = request.getParameter("instructions");
        String paymentMethod = request.getParameter("paymentMethod");

        // Present only for non-COD payments, populated by Razorpay Checkout.js
        // on the client after a successful payment (see checkout.jsp).
        String razorpayOrderId = request.getParameter("razorpayOrderId");
        String razorpayPaymentId = request.getParameter("razorpayPaymentId");
        String razorpaySignature = request.getParameter("razorpaySignature");

        if (ValidationUtil.isNullOrEmpty(customerName) || !ValidationUtil.isValidPhone(phone)
                || ValidationUtil.isNullOrEmpty(address) || ValidationUtil.isNullOrEmpty(paymentMethod)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please fill all required checkout details.");
            response.sendRedirect(request.getContextPath() + "/CheckoutServlet");
            return;
        }

        boolean isCOD = "COD".equals(paymentMethod);
        String paymentStatus;
        String transactionRef = null;

        if (!isCOD) {
            // Online payment: the order was ALREADY charged via Razorpay Checkout.js
            // on the client (see CreateRazorpayOrderServlet + checkout.jsp). We must
            // verify the signature server-side before trusting it - never accept a
            // client's word that a payment succeeded.
            boolean verified = RazorpayUtil.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
            if (!verified) {
                request.setAttribute(SessionConstants.ERROR_MSG,
                        "Payment verification failed. If money was deducted, it will be auto-refunded by Razorpay within a few days. Please try again.");
                response.sendRedirect(request.getContextPath() + "/CheckoutServlet");
                return;
            }
            paymentStatus = "PAID";
            transactionRef = razorpayPaymentId;
        } else {
            paymentStatus = "PENDING";
        }

        double itemTotal = cart.getItemTotal();
        double gst = round2(itemTotal * CheckoutServlet.GST_PERCENT / 100.0);
        double couponDiscount = 0;
        String couponCode = (String) session.getAttribute("checkoutCouponCode");
        if (!ValidationUtil.isNullOrEmpty(couponCode)) {
            Coupon coupon = couponDAO.getCouponByCode(couponCode);
            if (coupon != null) couponDiscount = round2(coupon.calculateDiscount(itemTotal));
        }
        double finalTotal = round2(itemTotal + CheckoutServlet.PLATFORM_FEE + CheckoutServlet.DELIVERY_FEE + gst - couponDiscount);

        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setRestaurantId(cart.getRestaurantId());
        order.setCustomerName(customerName);
        order.setCustomerPhone(phone);
        order.setDeliveryAddress(address);
        order.setDeliveryInstructions(instructions);
        order.setItemTotal(itemTotal);
        order.setPlatformFee(CheckoutServlet.PLATFORM_FEE);
        order.setDeliveryFee(CheckoutServlet.DELIVERY_FEE);
        order.setGst(gst);
        order.setCouponDiscount(couponDiscount);
        order.setCouponCode(couponCode);
        order.setFinalTotal(finalTotal);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(paymentStatus);

        List<OrderItem> items = new ArrayList<>();
        for (CartItem ci : cart.getItems().values()) {
            items.add(new OrderItem(ci.getMenuId(), ci.getMenuName(), ci.getQuantity(), ci.getPrice()));
        }
        order.setOrderItems(items);

        int orderId = orderDAO.placeOrder(order);

        if (orderId > 0) {
            // Record the payment row regardless of method - COD gets a PENDING
            // record (settled at delivery), online payments get a SUCCESS record
            // with Razorpay's payment id as the traceable transaction reference.
            Payment payment = new Payment(
                    orderId,
                    finalTotal,
                    paymentMethod,
                    isCOD ? "PENDING" : "SUCCESS",
                    transactionRef
            );
            paymentDAO.recordPayment(payment);

            cart.clear();
            session.removeAttribute("checkoutCouponCode");
            response.sendRedirect(request.getContextPath() + "/OrderTrackServlet?orderId=" + orderId + "&placed=1");
        } else {
            request.setAttribute(SessionConstants.ERROR_MSG, "Failed to place order. Please try again.");
            response.sendRedirect(request.getContextPath() + "/CheckoutServlet");
        }
    }

    private double round2(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}

