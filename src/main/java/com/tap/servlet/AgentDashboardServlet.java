package com.tap.servlet;

import com.tap.dao.OrderDAO;
import com.tap.daoimpl.OrderDAOImpl;
import com.tap.model.DeliveryAgent;
import com.tap.util.SessionConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Delivery agent dashboard: shows all orders currently assigned to this
 * agent with pickup/delivery details and available status actions.
 */
public class AgentDashboardServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute(SessionConstants.AGENT_USER);
        if (agent == null) {
            response.sendRedirect(request.getContextPath() + "/agent-login.jsp");
            return;
        }

        request.setAttribute("agent", agent);
        request.setAttribute("assignedOrders", orderDAO.getOrdersByAgent(agent.getAgentId()));
        request.getRequestDispatcher("/WEB-INF/jsp/delivery/agent-dashboard.jsp").forward(request, response);
    }
}
