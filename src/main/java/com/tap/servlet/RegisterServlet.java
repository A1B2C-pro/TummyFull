package com.tap.servlet;

import com.tap.dao.OTPDAO;
import com.tap.dao.UserDAO;
import com.tap.daoimpl.OTPDAOImpl;
import com.tap.daoimpl.UserDAOImpl;
import com.tap.model.OTP;
import com.tap.model.User;
import com.tap.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;

public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();
    private final OTPDAO otpDAO = new OTPDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String address = request.getParameter("address");

        if (ValidationUtil.isNullOrEmpty(fullName) || !ValidationUtil.isValidEmail(email)
                || !ValidationUtil.isValidPhone(phone) || !ValidationUtil.isValidPassword(password)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please fill all fields correctly. Phone must be 10 digits, password min 6 characters.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (userDAO.emailExists(email)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "An account with this email already exists.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(BCryptUtil.hashPassword(password));
        user.setAddress(address);
        user.setCity("Bangalore");
        user.setVerified(false);

        boolean created = userDAO.registerUser(user);
        if (!created) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Registration failed. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Generate & send OTP
        otpDAO.invalidateOldOTPs(email, "REGISTER");
        String otpCode = OTPUtil.generateOTP();
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + OTPUtil.OTP_VALIDITY_MINUTES * 60 * 1000L);
        otpDAO.saveOTP(new OTP(email, otpCode, "REGISTER", expiresAt));
        MailUtil.sendOTPEmail(email, otpCode, "Account Registration");

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.PENDING_OTP_EMAIL, email);
        session.setAttribute(SessionConstants.PENDING_OTP_PURPOSE, "REGISTER");
        session.setAttribute(SessionConstants.ROLE, "CUSTOMER");

        response.sendRedirect(request.getContextPath() + "/otp-verify.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
