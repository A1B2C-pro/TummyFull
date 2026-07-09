package com.tap.servlet;

import com.tap.dao.AdminDAO;
import com.tap.daoimpl.AdminDAOImpl;
import com.tap.model.Admin;
import com.tap.util.BCryptUtil;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AdminLoginServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Please enter both email and password.");
            request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
            return;
        }

        Admin admin = adminDAO.getAdminByEmail(email);

        if (admin == null || !BCryptUtil.checkPassword(password, admin.getPasswordHash())) {
            request.setAttribute(SessionConstants.ERROR_MSG, "Invalid admin credentials.");
            request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.ADMIN_USER, admin);
        session.setAttribute(SessionConstants.ROLE, "ADMIN");

        response.sendRedirect(request.getContextPath() + "/AdminDashboardServlet");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
    }
}
