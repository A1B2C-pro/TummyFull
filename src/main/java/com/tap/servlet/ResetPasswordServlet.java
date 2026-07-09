package com.tap.servlet;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.dao.UserDAO;
import com.tap.daoimpl.DeliveryAgentDAOImpl;
import com.tap.daoimpl.UserDAOImpl;
import com.tap.util.BCryptUtil;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ResetPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();
    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Boolean otpVerified = (Boolean) session.getAttribute("otpVerifiedForReset");
        String email = (String) session.getAttribute(SessionConstants.PENDING_OTP_EMAIL);
        String role = (String) session.getAttribute(SessionConstants.ROLE);

        if (otpVerified == null || !otpVerified || email == null) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Session expired. Please restart the forgot-password process.");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!ValidationUtil.isValidPassword(newPassword) || !newPassword.equals(confirmPassword)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Passwords must match and be at least 6 characters.");
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }

        String hashed = BCryptUtil.hashPassword(newPassword);
        boolean updated;
        if ("DELIVERY".equals(role)) {
            updated = agentDAO.updatePassword(email, hashed);
        } else {
            updated = userDAO.updatePassword(email, hashed);
        }

        session.removeAttribute(SessionConstants.PENDING_OTP_EMAIL);
        session.removeAttribute(SessionConstants.PENDING_OTP_PURPOSE);
        session.removeAttribute("otpVerifiedForReset");

        if (updated) {
            request.setAttribute(SessionConstants.SUCCESS_MSG, "Password reset successful. Please login with your new password.");
        } else {
            request.setAttribute(SessionConstants.ERROR_MSG, "Failed to reset password. Please try again.");
        }
        String loginPage = "DELIVERY".equals(role) ? "/agent-login.jsp" : "/login.jsp";
        request.getRequestDispatcher(loginPage).forward(request, response);
    }
}
