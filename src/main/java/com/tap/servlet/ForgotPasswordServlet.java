package com.tap.servlet;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.dao.OTPDAO;
import com.tap.dao.UserDAO;
import com.tap.daoimpl.DeliveryAgentDAOImpl;
import com.tap.daoimpl.OTPDAOImpl;
import com.tap.daoimpl.UserDAOImpl;
import com.tap.model.OTP;
import com.tap.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Handles "Forgot Password" for both CUSTOMER and DELIVERY roles.
 * Expects a "role" parameter of CUSTOMER or DELIVERY along with email.
 */
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();
    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();
    private final OTPDAO otpDAO = new OTPDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String role = request.getParameter("role");
        if (role == null) role = "CUSTOMER";

        boolean exists = "DELIVERY".equals(role) ? agentDAO.emailExists(email) : userDAO.emailExists(email);

        if (!ValidationUtil.isValidEmail(email) || !exists) {
            request.setAttribute(SessionConstants.ERROR_MSG, "No account found with this email.");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }

        otpDAO.invalidateOldOTPs(email, "FORGOT_PASSWORD");
        String otpCode = OTPUtil.generateOTP();
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + OTPUtil.OTP_VALIDITY_MINUTES * 60 * 1000L);
        otpDAO.saveOTP(new OTP(email, otpCode, "FORGOT_PASSWORD", expiresAt));
        MailUtil.sendOTPEmail(email, otpCode, "Password Reset");

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.PENDING_OTP_EMAIL, email);
        session.setAttribute(SessionConstants.PENDING_OTP_PURPOSE, "FORGOT_PASSWORD");
        session.setAttribute(SessionConstants.ROLE, role);

        response.sendRedirect(request.getContextPath() + "/otp-verify.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
    }
}
