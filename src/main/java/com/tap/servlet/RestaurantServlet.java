package com.tap.servlet;

import com.tap.dao.CategoryDAO;
import com.tap.dao.RestaurantDAO;
import com.tap.daoimpl.CategoryDAOImpl;
import com.tap.daoimpl.RestaurantDAOImpl;
import com.tap.model.Restaurant;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Displays nearby Bangalore restaurants. Supports keyword search and
 * simple sort/filter query params: ?q=&sort=rating|fast&veg=1
 */
public class RestaurantServlet extends HttpServlet {

    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("q");
        String sort = request.getParameter("sort");

        List<Restaurant> restaurants;
        if (!ValidationUtil.isNullOrEmpty(query)) {
            restaurants = restaurantDAO.searchRestaurants(query.trim());
        } else {
            restaurants = restaurantDAO.getActiveRestaurants();
        }

        if ("rating".equals(sort)) {
            restaurants.sort(Comparator.comparingDouble(Restaurant::getRating).reversed());
        } else if ("fast".equals(sort)) {
            restaurants.sort(Comparator.comparing(Restaurant::getDeliveryTime));
        }

        request.setAttribute("restaurants", restaurants);
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.setAttribute("searchQuery", query);
        request.getRequestDispatcher("/WEB-INF/jsp/user/restaurant.jsp").forward(request, response);
    }
}
