package com.tap.servlet;

import com.tap.dao.OrderDAO;
import com.tap.dao.UserDAO;
import com.tap.daoimpl.OrderDAOImpl;
import com.tap.daoimpl.UserDAOImpl;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin: view all users, block/unblock, delete, view a user's order history.
 * action param: list (default) | block | unblock | delete | viewOrders
 */
public class AdminUserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String viewOrdersFor = request.getParameter("viewOrdersFor");
        if (!ValidationUtil.isNullOrEmpty(viewOrdersFor)) {
            int userId = ValidationUtil.parseIntOrZero(viewOrdersFor);
            request.setAttribute("targetUser", userDAO.getUserById(userId));
            request.setAttribute("userOrders", orderDAO.getOrdersByUser(userId));
        }
        request.setAttribute("users", userDAO.getAllUsers());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int userId = ValidationUtil.parseIntOrZero(request.getParameter("userId"));

        switch (action == null ? "" : action) {
            case "block":
                userDAO.blockUser(userId, true);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "User blocked.");
                break;
            case "unblock":
                userDAO.blockUser(userId, false);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "User unblocked.");
                break;
            case "delete":
                userDAO.deleteUser(userId);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "User deleted.");
                break;
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminUserServlet");
    }
}
