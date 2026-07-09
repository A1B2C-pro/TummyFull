package com.tap.servlet;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.dao.OrderDAO;
import com.tap.daoimpl.DeliveryAgentDAOImpl;
import com.tap.daoimpl.OrderDAOImpl;
import com.tap.model.DeliveryAgent;
import com.tap.util.BCryptUtil;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin: add/update/delete delivery agents, view their assigned orders.
 * action param: list (default) | add | update | delete
 */
public class AdminDeliveryServlet extends HttpServlet {

    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String viewOrdersFor = request.getParameter("viewOrdersFor");
        if (!ValidationUtil.isNullOrEmpty(viewOrdersFor)) {
            int agentId = ValidationUtil.parseIntOrZero(viewOrdersFor);
            request.setAttribute("targetAgent", agentDAO.getAgentById(agentId));
            request.setAttribute("agentOrders", orderDAO.getOrdersByAgent(agentId));
        }
        request.setAttribute("agents", agentDAO.getAllAgents());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-delivery.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "" : action) {
            case "add": {
                String email = request.getParameter("email");
                if (agentDAO.emailExists(email)) {
                    request.getSession().setAttribute(SessionConstants.ERROR_MSG, "Email already registered.");
                    break;
                }
                DeliveryAgent agent = new DeliveryAgent(
                        request.getParameter("fullName"), email, request.getParameter("phone"),
                        BCryptUtil.hashPassword(request.getParameter("password")),
                        request.getParameter("vehicleNumber"));
                agentDAO.registerAgent(agent);
                agentDAO.markVerified(email); // admin-added agents are auto-verified
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Delivery agent added.");
                break;
            }
            case "update": {
                int agentId = ValidationUtil.parseIntOrZero(request.getParameter("agentId"));
                DeliveryAgent agent = agentDAO.getAgentById(agentId);
                if (agent != null) {
                    agent.setFullName(request.getParameter("fullName"));
                    agent.setPhone(request.getParameter("phone"));
                    agent.setVehicleNumber(request.getParameter("vehicleNumber"));
                    agentDAO.updateAgent(agent);
                    request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Delivery agent updated.");
                }
                break;
            }
            case "delete": {
                int agentId = ValidationUtil.parseIntOrZero(request.getParameter("agentId"));
                agentDAO.deleteAgent(agentId);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Delivery agent removed.");
                break;
            }
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminDeliveryServlet");
    }
}
