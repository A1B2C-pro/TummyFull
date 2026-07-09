package com.tap.filter;

import com.tap.util.SessionConstants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** Prevents direct URL access to delivery agent pages without an agent session. */
public class AgentAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        boolean loggedIn = session != null && session.getAttribute(SessionConstants.AGENT_USER) != null;

        if (!loggedIn) {
            request.setAttribute("errorMsg", "Please login as delivery agent to continue.");
            request.getRequestDispatcher("/agent-login.jsp").forward(request, response);
            return;
        }
        chain.doFilter(req, res);
    }
}
