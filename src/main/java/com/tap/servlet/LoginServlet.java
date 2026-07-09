package com.tap.servlet;

import com.tap.dao.UserDAO;
import com.tap.daoimpl.UserDAOImpl;
import com.tap.model.User;
import com.tap.util.BCryptUtil;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please enter both email and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.getUserByEmail(email);

        if (user == null || !BCryptUtil.checkPassword(password, user.getPasswordHash())) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Invalid email or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (user.isBlocked()) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Your account has been blocked. Please contact support.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (!user.isVerified()) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please verify your email via OTP before logging in.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.CUSTOMER_USER, user);
        session.setAttribute(SessionConstants.ROLE, "CUSTOMER");

        response.sendRedirect(request.getContextPath() + "/RestaurantServlet");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
