package com.tap.servlet;

import com.tap.dao.ReviewDAO;
import com.tap.daoimpl.ReviewDAOImpl;
import com.tap.model.Review;
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
 * Lets a logged-in customer rate a restaurant and/or a specific food item,
 * with an optional comment and image URL. Redirects back to the referring
 * page (menu or order-history) after submission.
 */
public class ReviewServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionConstants.CUSTOMER_USER);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String restaurantIdParam = request.getParameter("restaurantId");
        String menuIdParam = request.getParameter("menuId");
        String ratingParam = request.getParameter("rating");
        String comment = request.getParameter("comment");
        String imageUrl = request.getParameter("imageUrl");
        String redirectTo = request.getParameter("redirectTo");

        double rating = 0;
        try { rating = Double.parseDouble(ratingParam); } catch (Exception ignored) {}

        if (rating < 1 || rating > 5) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please provide a rating between 1 and 5.");
        } else {
            Integer restaurantId = ValidationUtil.isNullOrEmpty(restaurantIdParam) ? null : Integer.parseInt(restaurantIdParam);
            Integer menuId = ValidationUtil.isNullOrEmpty(menuIdParam) ? null : Integer.parseInt(menuIdParam);

            Review review = new Review(user.getUserId(), restaurantId, menuId, rating, comment);
            review.setImageUrl(imageUrl);
            reviewDAO.addReview(review);
        }

        if (ValidationUtil.isNullOrEmpty(redirectTo)) {
            redirectTo = request.getContextPath() + "/RestaurantServlet";
        }
        response.sendRedirect(redirectTo);
    }
}
