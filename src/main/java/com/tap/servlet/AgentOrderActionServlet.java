package com.tap.servlet;

import com.tap.dao.OrderDAO;
import com.tap.daoimpl.OrderDAOImpl;
import com.tap.model.DeliveryAgent;
import com.tap.model.Order;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Drives the delivery agent side of the order lifecycle:
 *   ACCEPTED -> (Pick Up Order) -> READY_FOR_PICKUP
 *   READY_FOR_PICKUP -> (Start Delivery) -> OUT_FOR_DELIVERY
 *   OUT_FOR_DELIVERY -> (Mark Delivered) -> DELIVERED
 * "Accept Delivery" is used when an order is assigned to the agent but
 * still sitting at PLACED (self-service acceptance).
 * action param: accept | pickup | startDelivery | delivered
 */
public class AgentOrderActionServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute(SessionConstants.AGENT_USER);
        if (agent == null) {
            response.sendRedirect(request.getContextPath() + "/agent-login.jsp");
            return;
        }

        int orderId = ValidationUtil.parseIntOrZero(request.getParameter("orderId"));
        String action = request.getParameter("action");
        Order order = orderDAO.getOrderById(orderId);

        if (order == null || order.getAgentId() == null || order.getAgentId() != agent.getAgentId()) {
            request.getSession().setAttribute(SessionConstants.ERROR_MSG, "This order is not assigned to you.");
            response.sendRedirect(request.getContextPath() + "/AgentDashboardServlet");
            return;
        }

        String newStatus = null;
        switch (action == null ? "" : action) {
            case "accept":
                newStatus = "ACCEPTED";
                break;
            case "pickup":
                newStatus = "READY_FOR_PICKUP";
                break;
            case "startDelivery":
                newStatus = "OUT_FOR_DELIVERY";
                break;
            case "delivered":
                newStatus = "DELIVERED";
                break;
            default:
                break;
        }

        if (newStatus != null) {
            orderDAO.updateOrderStatus(orderId, newStatus);
            session.setAttribute(SessionConstants.SUCCESS_MSG, "Order #" + orderId + " updated to " + newStatus + ".");
        }

        response.sendRedirect(request.getContextPath() + "/AgentDashboardServlet");
    }
}
