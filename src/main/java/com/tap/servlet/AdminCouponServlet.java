package com.tap.servlet;

import com.tap.dao.CouponDAO;
import com.tap.dao.OfferDAO;
import com.tap.dao.RestaurantDAO;
import com.tap.daoimpl.CouponDAOImpl;
import com.tap.daoimpl.OfferDAOImpl;
import com.tap.daoimpl.RestaurantDAOImpl;
import com.tap.model.Coupon;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

/**
 * Admin: coupon codes. action param: list (default) | add | update | delete
 */
public class AdminCouponServlet extends HttpServlet {

    private final CouponDAO couponDAO = new CouponDAOImpl();
    private final OfferDAO offerDAO = new OfferDAOImpl();
    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("coupons", couponDAO.getAllCoupons());
        request.setAttribute("offers", offerDAO.getAllOffers());
        request.setAttribute("restaurants", restaurantDAO.getAllRestaurants());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-offers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "" : action) {
            case "add": {
                Coupon c = buildFromRequest(request, new Coupon());
                couponDAO.addCoupon(c);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Coupon added.");
                break;
            }
            case "update": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("couponId"));
                Coupon c = buildFromRequest(request, new Coupon());
                c.setCouponId(id);
                c.setActive(true);
                couponDAO.updateCoupon(c);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Coupon updated.");
                break;
            }
            case "delete": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("couponId"));
                couponDAO.deleteCoupon(id);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Coupon deleted.");
                break;
            }
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminCouponServlet");
    }

    private Coupon buildFromRequest(HttpServletRequest request, Coupon c) {
        c.setCode(request.getParameter("code").trim().toUpperCase());
        c.setDescription(request.getParameter("description"));
        c.setDiscountPercent(ValidationUtil.parseDoubleOrZero(request.getParameter("discountPercent")));
        c.setMaxDiscount(ValidationUtil.parseDoubleOrZero(request.getParameter("maxDiscount")));
        c.setMinOrderValue(ValidationUtil.parseDoubleOrZero(request.getParameter("minOrderValue")));
        String validTillParam = request.getParameter("validTill");
        if (!ValidationUtil.isNullOrEmpty(validTillParam)) {
            c.setValidTill(Date.valueOf(validTillParam));
        }
        return c;
    }
}
