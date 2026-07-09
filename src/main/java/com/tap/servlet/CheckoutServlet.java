package com.tap.servlet;

import com.tap.dao.CouponDAO;
import com.tap.daoimpl.CouponDAOImpl;
import com.tap.model.Cart;
import com.tap.model.Coupon;
import com.tap.model.User;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Shows checkout page: item total, platform fee, delivery fee, GST,
 * optional coupon discount, and collects delivery details before
 * PlaceOrderServlet actually creates the order.
 */
public class CheckoutServlet extends HttpServlet {

    public static final double PLATFORM_FEE = 3.00;
    public static final double DELIVERY_FEE = 10.00;
    public static final double GST_PERCENT = 2.00;

    private final CouponDAO couponDAO = new CouponDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(SessionConstants.CART);
        User user = (User) session.getAttribute(SessionConstants.CUSTOMER_USER);

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/CartServlet");
            return;
        }

        double itemTotal = cart.getItemTotal();
        double gst = round2(itemTotal * GST_PERCENT / 100.0);
        double couponDiscount = 0;
        String couponCode = (String) session.getAttribute("checkoutCouponCode");

        if (!ValidationUtil.isNullOrEmpty(couponCode)) {
            Coupon coupon = couponDAO.getCouponByCode(couponCode);
            if (coupon != null) {
                couponDiscount = round2(coupon.calculateDiscount(itemTotal));
            }
        }

        double finalTotal = round2(itemTotal + PLATFORM_FEE + DELIVERY_FEE + gst - couponDiscount);

        request.setAttribute("cart", cart);
        request.setAttribute("user", user);
        request.setAttribute("itemTotal", itemTotal);
        request.setAttribute("platformFee", PLATFORM_FEE);
        request.setAttribute("deliveryFee", DELIVERY_FEE);
        request.setAttribute("gst", gst);
        request.setAttribute("couponDiscount", couponDiscount);
        request.setAttribute("couponCode", couponCode);
        request.setAttribute("finalTotal", finalTotal);

        request.getRequestDispatcher("/WEB-INF/jsp/user/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Applying/removing a coupon code re-renders the checkout page with new totals
        String couponCode = request.getParameter("couponCode");
        HttpSession session = request.getSession();

        if (ValidationUtil.isNullOrEmpty(couponCode)) {
            session.removeAttribute("checkoutCouponCode");
        } else {
            Coupon coupon = couponDAO.getCouponByCode(couponCode.trim().toUpperCase());
            if (coupon == null || !coupon.isActive()) {
                request.setAttribute(SessionConstants.ERROR_MSG, "Invalid or expired coupon code.");
                session.removeAttribute("checkoutCouponCode");
            } else {
                session.setAttribute("checkoutCouponCode", coupon.getCode());
            }
        }
        doGet(request, response);
    }

    private double round2(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}
