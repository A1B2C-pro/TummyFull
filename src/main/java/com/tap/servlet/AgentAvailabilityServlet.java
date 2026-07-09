package com.tap.servlet;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.daoimpl.DeliveryAgentDAOImpl;
import com.tap.model.DeliveryAgent;
import com.tap.util.SessionConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AgentAvailabilityServlet extends HttpServlet {

    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute(SessionConstants.AGENT_USER);
        if (agent == null) {
            response.sendRedirect(request.getContextPath() + "/agent-login.jsp");
            return;
        }

        boolean newAvailability = !agent.isAvailable();
        agentDAO.updateAvailability(agent.getAgentId(), newAvailability);
        agent.setAvailable(newAvailability);
        session.setAttribute(SessionConstants.AGENT_USER, agent);

        response.sendRedirect(request.getContextPath() + "/AgentDashboardServlet");
    }
}
