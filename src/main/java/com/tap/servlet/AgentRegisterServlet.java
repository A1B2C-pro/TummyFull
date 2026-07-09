package com.tap.servlet;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.dao.OTPDAO;
import com.tap.daoimpl.DeliveryAgentDAOImpl;
import com.tap.daoimpl.OTPDAOImpl;
import com.tap.model.DeliveryAgent;
import com.tap.model.OTP;
import com.tap.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;

public class AgentRegisterServlet extends HttpServlet {

    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();
    private final OTPDAO otpDAO = new OTPDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String vehicleNumber = request.getParameter("vehicleNumber");

        if (ValidationUtil.isNullOrEmpty(fullName) || !ValidationUtil.isValidEmail(email)
                || !ValidationUtil.isValidPhone(phone) || !ValidationUtil.isValidPassword(password)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please fill all fields correctly.");
            request.getRequestDispatcher("/agent-register.jsp").forward(request, response);
            return;
        }

        if (agentDAO.emailExists(email)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "An account with this email already exists.");
            request.getRequestDispatcher("/agent-register.jsp").forward(request, response);
            return;
        }

        DeliveryAgent agent = new DeliveryAgent(fullName, email, phone, BCryptUtil.hashPassword(password), vehicleNumber);
        boolean created = agentDAO.registerAgent(agent);

        if (!created) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Registration failed. Please try again.");
            request.getRequestDispatcher("/agent-register.jsp").forward(request, response);
            return;
        }

        otpDAO.invalidateOldOTPs(email, "REGISTER");
        String otpCode = OTPUtil.generateOTP();
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + OTPUtil.OTP_VALIDITY_MINUTES * 60 * 1000L);
        otpDAO.saveOTP(new OTP(email, otpCode, "REGISTER", expiresAt));
        MailUtil.sendOTPEmail(email, otpCode, "Delivery Agent Registration");

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.PENDING_OTP_EMAIL, email);
        session.setAttribute(SessionConstants.PENDING_OTP_PURPOSE, "REGISTER");
        session.setAttribute(SessionConstants.ROLE, "DELIVERY");

        response.sendRedirect(request.getContextPath() + "/otp-verify.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/agent-register.jsp").forward(request, response);
    }
}
