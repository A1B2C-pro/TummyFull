package com.tap.servlet;

import com.tap.dao.OrderDAO;
import com.tap.daoimpl.OrderDAOImpl;
import com.tap.model.Order;
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
 * Shows live order status for a single order (?orderId=). Prevents a
 * customer from viewing someone else's order (basic ownership check).
 */
public class OrderTrackServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionConstants.CUSTOMER_USER);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int orderId = ValidationUtil.parseIntOrZero(request.getParameter("orderId"));
        Order order = orderDAO.getOrderById(orderId);

        if (order == null || order.getUserId() != user.getUserId()) {
            response.sendRedirect(request.getContextPath() + "/OrderHistoryServlet");
            return;
        }

        request.setAttribute("order", order);
        request.setAttribute("justPlaced", "1".equals(request.getParameter("placed")));
        request.getRequestDispatcher("/WEB-INF/jsp/user/order-track.jsp").forward(request, response);
    }
}
