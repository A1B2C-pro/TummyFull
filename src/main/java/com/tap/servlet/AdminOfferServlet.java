package com.tap.servlet;

import com.tap.dao.CouponDAO;
import com.tap.dao.OfferDAO;
import com.tap.dao.RestaurantDAO;
import com.tap.daoimpl.CouponDAOImpl;
import com.tap.daoimpl.OfferDAOImpl;
import com.tap.daoimpl.RestaurantDAOImpl;
import com.tap.model.Offer;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

/**
 * Admin: festival offers, cashback, discounts.
 * action param: list (default) | add | update | delete | toggleActive
 */
public class AdminOfferServlet extends HttpServlet {

    private final OfferDAO offerDAO = new OfferDAOImpl();
    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
    private final CouponDAO couponDAO = new CouponDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("offers", offerDAO.getAllOffers());
        request.setAttribute("restaurants", restaurantDAO.getAllRestaurants());
        request.setAttribute("coupons", couponDAO.getAllCoupons());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-offers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "" : action) {
            case "add": {
                Offer o = buildFromRequest(request, new Offer());
                offerDAO.addOffer(o);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Offer added.");
                break;
            }
            case "update": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("offerId"));
                Offer o = buildFromRequest(request, new Offer());
                o.setOfferId(id);
                o.setActive(true);
                offerDAO.updateOffer(o);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Offer updated.");
                break;
            }
            case "delete": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("offerId"));
                offerDAO.deleteOffer(id);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Offer deleted.");
                break;
            }
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminOfferServlet");
    }

    private Offer buildFromRequest(HttpServletRequest request, Offer o) {
        o.setTitle(request.getParameter("title"));
        o.setDescription(request.getParameter("description"));
        String restIdParam = request.getParameter("restaurantId");
        o.setRestaurantId(ValidationUtil.isNullOrEmpty(restIdParam) ? null : Integer.parseInt(restIdParam));
        o.setDiscountPercent(ValidationUtil.parseDoubleOrZero(request.getParameter("discountPercent")));
        String validTillParam = request.getParameter("validTill");
        if (!ValidationUtil.isNullOrEmpty(validTillParam)) {
            o.setValidTill(Date.valueOf(validTillParam));
        }
        return o;
    }
}
