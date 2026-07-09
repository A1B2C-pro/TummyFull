package com.tap.servlet;

import com.tap.dao.UserDAO;
import com.tap.daoimpl.UserDAOImpl;
import com.tap.model.User;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionConstants.CUSTOMER_USER);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // Refresh from DB in case admin changed something
        User fresh = userDAO.getUserById(user.getUserId());
        session.setAttribute(SessionConstants.CUSTOMER_USER, fresh);
        request.setAttribute("user", fresh);
        request.getRequestDispatcher("/WEB-INF/jsp/user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionConstants.CUSTOMER_USER);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");

        if (ValidationUtil.isNullOrEmpty(fullName) || !ValidationUtil.isValidPhone(phone)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please enter a valid name and 10-digit phone number.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/jsp/user/profile.jsp").forward(request, response);
            return;
        }

        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
        user.setCity(ValidationUtil.isNullOrEmpty(city) ? "Bangalore" : city);

        boolean updated = userDAO.updateProfile(user);
        session.setAttribute(SessionConstants.CUSTOMER_USER, user);

        request.setAttribute(updated ? SessionConstants.SUCCESS_MSG : SessionConstants.ERROR_MSG,
                updated ? "Profile updated successfully." : "Failed to update profile.");
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/jsp/user/profile.jsp").forward(request, response);
    }
}
