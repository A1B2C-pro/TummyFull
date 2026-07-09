package com.tap.servlet;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.dao.OTPDAO;
import com.tap.dao.UserDAO;
import com.tap.daoimpl.DeliveryAgentDAOImpl;
import com.tap.daoimpl.OTPDAOImpl;
import com.tap.daoimpl.UserDAOImpl;
import com.tap.model.OTP;
import com.tap.util.SessionConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Verifies the 6-digit OTP for REGISTER or FORGOT_PASSWORD purposes,
 * for CUSTOMER or DELIVERY roles (admins do not use OTP login).
 */
public class VerifyOTPServlet extends HttpServlet {

    private final OTPDAO otpDAO = new OTPDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute(SessionConstants.PENDING_OTP_EMAIL);
        String purpose = (String) session.getAttribute(SessionConstants.PENDING_OTP_PURPOSE);
        String role = (String) session.getAttribute(SessionConstants.ROLE);
        String enteredOtp = request.getParameter("otp");

        if (email == null || purpose == null || role == null) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Session expired. Please start again.");
            request.getRequestDispatcher("/login-role.jsp").forward(request, response);
            return;
        }

        OTP latest = otpDAO.getLatestOTP(email, purpose);

        if (latest == null || latest.isUsed() || latest.isExpired() || !latest.getOtpCode().equals(enteredOtp)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Invalid or expired OTP. Please try again or resend OTP.");
            request.getRequestDispatcher("/otp-verify.jsp").forward(request, response);
            return;
        }

        otpDAO.markUsed(email, enteredOtp);

        if ("REGISTER".equals(purpose)) {
            if ("CUSTOMER".equals(role)) {
                userDAO.markVerified(email);
            } else if ("DELIVERY".equals(role)) {
                agentDAO.markVerified(email);
            }
            session.removeAttribute(SessionConstants.PENDING_OTP_EMAIL);
            session.removeAttribute(SessionConstants.PENDING_OTP_PURPOSE);
            request.setAttribute(SessionConstants.SUCCESS_MSG, "Account verified successfully! Please login.");
            String loginPage = "DELIVERY".equals(role) ? "/agent-login.jsp" : "/login.jsp";
            request.getRequestDispatcher(loginPage).forward(request, response);
        } else if ("FORGOT_PASSWORD".equals(purpose)) {
            // Mark OTP-verified flag so ResetPasswordServlet trusts this session
            session.setAttribute("otpVerifiedForReset", Boolean.TRUE);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login-role.jsp");
        }
    }
}
