package com.tap.servlet;

import com.tap.dao.ReviewDAO;
import com.tap.daoimpl.ReviewDAOImpl;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin: view/delete/highlight reviews.
 * action param: list (default) | delete | highlight | unhighlight
 */
public class AdminReviewServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("reviews", reviewDAO.getAllReviews());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-reviews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int reviewId = ValidationUtil.parseIntOrZero(request.getParameter("reviewId"));

        switch (action == null ? "" : action) {
            case "delete":
                reviewDAO.deleteReview(reviewId);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Review deleted.");
                break;
            case "highlight":
                reviewDAO.highlightReview(reviewId, true);
                break;
            case "unhighlight":
                reviewDAO.highlightReview(reviewId, false);
                break;
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminReviewServlet");
    }
}
