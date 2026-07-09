package com.tap.servlet;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.daoimpl.DeliveryAgentDAOImpl;
import com.tap.model.DeliveryAgent;
import com.tap.util.BCryptUtil;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AgentLoginServlet extends HttpServlet {

    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please enter both email and password.");
            request.getRequestDispatcher("/agent-login.jsp").forward(request, response);
            return;
        }

        DeliveryAgent agent = agentDAO.getAgentByEmail(email);

        if (agent == null || !BCryptUtil.checkPassword(password, agent.getPasswordHash())) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Invalid email or password.");
            request.getRequestDispatcher("/agent-login.jsp").forward(request, response);
            return;
        }

        if (!agent.isVerified()) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please verify your email via OTP before logging in.");
            request.getRequestDispatcher("/agent-login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.AGENT_USER, agent);
        session.setAttribute(SessionConstants.ROLE, "DELIVERY");

        response.sendRedirect(request.getContextPath() + "/AgentDashboardServlet");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/agent-login.jsp").forward(request, response);
    }
}
