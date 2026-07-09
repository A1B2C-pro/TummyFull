package com.tap.servlet;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.dao.OrderDAO;
import com.tap.daoimpl.DeliveryAgentDAOImpl;
import com.tap.daoimpl.OrderDAOImpl;
import com.tap.model.Order;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Admin: view all orders (optionally filtered by status), change status,
 * assign a delivery agent to an order.
 * action param: list (default) | changeStatus | assignAgent
 */
public class AdminOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        List<Order> orders = ValidationUtil.isNullOrEmpty(status)
                ? orderDAO.getAllOrders()
                : orderDAO.getOrdersByStatus(status);

        request.setAttribute("orders", orders);
        request.setAttribute("statusFilter", status);
        request.setAttribute("availableAgents", agentDAO.getAvailableAgents());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int orderId = ValidationUtil.parseIntOrZero(request.getParameter("orderId"));

        switch (action == null ? "" : action) {
            case "changeStatus": {
                String status = request.getParameter("status");
                orderDAO.updateOrderStatus(orderId, status);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Order status updated.");
                break;
            }
            case "assignAgent": {
                int agentId = ValidationUtil.parseIntOrZero(request.getParameter("agentId"));
                orderDAO.assignAgent(orderId, agentId);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Delivery agent assigned.");
                break;
            }
            case "cancel": {
                orderDAO.cancelOrder(orderId);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Order cancelled.");
                break;
            }
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminOrderServlet");
    }
}
