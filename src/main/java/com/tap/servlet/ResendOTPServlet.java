package com.tap.servlet;

import com.tap.dao.OTPDAO;
import com.tap.daoimpl.OTPDAOImpl;
import com.tap.model.OTP;
import com.tap.util.MailUtil;
import com.tap.util.OTPUtil;
import com.tap.util.SessionConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;

public class ResendOTPServlet extends HttpServlet {

    private final OTPDAO otpDAO = new OTPDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute(SessionConstants.PENDING_OTP_EMAIL);
        String purpose = (String) session.getAttribute(SessionConstants.PENDING_OTP_PURPOSE);

        if (email == null || purpose == null) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Session expired. Please start again.");
            request.getRequestDispatcher("/login-role.jsp").forward(request, response);
            return;
        }

        otpDAO.invalidateOldOTPs(email, purpose);
        String otpCode = OTPUtil.generateOTP();
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + OTPUtil.OTP_VALIDITY_MINUTES * 60 * 1000L);
        otpDAO.saveOTP(new OTP(email, otpCode, purpose, expiresAt));
        MailUtil.sendOTPEmail(email, otpCode, purpose);

        request.setAttribute(SessionConstants.SUCCESS_MSG, "A new OTP has been sent to " + email);
        request.getRequestDispatcher("/otp-verify.jsp").forward(request, response);
    }
}
