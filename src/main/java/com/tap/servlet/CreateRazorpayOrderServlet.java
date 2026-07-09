package com.tap.servlet;

import com.tap.dao.CouponDAO;
import com.tap.daoimpl.CouponDAOImpl;
import com.tap.model.Cart;
import com.tap.model.Coupon;
import com.tap.model.User;
import com.tap.util.RazorpayUtil;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AJAX endpoint called from checkout.jsp right before opening the Razorpay
 * Checkout.js modal (only for non-COD payment methods). Recomputes the
 * final total server-side (never trusts a client-supplied amount) and asks
 * Razorpay to create an Order, returning its id + amount as small JSON for
 * the client-side Razorpay widget to use.
 */
public class CreateRazorpayOrderServlet extends HttpServlet {

    private final CouponDAO couponDAO = new CouponDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(SessionConstants.CART);
        User user = (User) session.getAttribute(SessionConstants.CUSTOMER_USER);

        response.setContentType("application/json");

        if (cart == null || cart.isEmpty() || user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Cart is empty or you are not logged in.\"}");
            return;
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

        String receipt = "tummyfull_" + user.getUserId() + "_" + System.currentTimeMillis();
        RazorpayUtil.RazorpayOrder rzOrder = RazorpayUtil.createOrder(finalTotal, receipt);

        if (rzOrder == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Could not initiate payment. Please try again.\"}");
            return;
        }

        String json = "{"
                + "\"razorpayOrderId\":\"" + rzOrder.orderId + "\","
                + "\"amountInPaise\":" + rzOrder.amountInPaise + ","
                + "\"keyId\":\"" + RazorpayUtil.getKeyId() + "\","
                + "\"customerName\":\"" + escapeJson(user.getFullName()) + "\","
                + "\"customerEmail\":\"" + escapeJson(user.getEmail()) + "\","
                + "\"customerPhone\":\"" + escapeJson(user.getPhone()) + "\""
                + "}";

        response.getWriter().write(json);
    }

    private double round2(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

