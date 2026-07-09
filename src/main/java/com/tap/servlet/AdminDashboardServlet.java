package com.tap.servlet;

import com.tap.dao.*;
import com.tap.daoimpl.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminDashboardServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
    private final DeliveryAgentDAO agentDAO = new DeliveryAgentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("totalOrders", orderDAO.getTotalOrdersCount());
        request.setAttribute("totalRevenue", orderDAO.getTotalRevenue());
        request.setAttribute("totalUsers", userDAO.getAllUsers().size());
        request.setAttribute("totalRestaurants", restaurantDAO.getAllRestaurants().size());
        request.setAttribute("activeDeliveries", orderDAO.getActiveDeliveriesCount());
        request.setAttribute("bestSellingItem", orderDAO.getBestSellingItem());
        request.setAttribute("monthlySales", orderDAO.getMonthlySales());
        request.setAttribute("pendingOrders", orderDAO.getPendingOrdersCount());
        request.setAttribute("totalAgents", agentDAO.getAllAgents().size());

        request.getRequestDispatcher("/WEB-INF/jsp/admin/admin-dashboard.jsp").forward(request, response);
    }
}
