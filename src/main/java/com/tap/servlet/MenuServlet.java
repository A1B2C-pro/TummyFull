package com.tap.servlet;

import com.tap.dao.MenuDAO;
import com.tap.dao.RestaurantDAO;
import com.tap.daoimpl.MenuDAOImpl;
import com.tap.daoimpl.RestaurantDAOImpl;
import com.tap.model.Menu;
import com.tap.model.Restaurant;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Displays the menu for a given restaurant (?restaurantId=). Supports
 * keyword search and veg/non-veg/price filters via query params.
 */
public class MenuServlet extends HttpServlet {

    private final MenuDAO menuDAO = new MenuDAOImpl();
    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String restaurantIdParam = request.getParameter("restaurantId");
        String keyword = request.getParameter("q");
        String vegFilter = request.getParameter("veg");     // "1" = veg only
        String sort = request.getParameter("sort");         // priceAsc | priceDesc

        if (ValidationUtil.isNullOrEmpty(restaurantIdParam)) {
            response.sendRedirect(request.getContextPath() + "/RestaurantServlet");
            return;
        }

        int restaurantId = ValidationUtil.parseIntOrZero(restaurantIdParam);
        Restaurant restaurant = restaurantDAO.getRestaurantById(restaurantId);

        if (restaurant == null) {
            response.sendRedirect(request.getContextPath() + "/RestaurantServlet");
            return;
        }

        List<Menu> menuItems;
        if (!ValidationUtil.isNullOrEmpty(keyword)) {
            menuItems = new ArrayList<>();
            for (Menu m : menuDAO.searchMenu(keyword.trim())) {
                if (m.getRestaurantId() == restaurantId) menuItems.add(m);
            }
        } else {
            menuItems = menuDAO.getMenuByRestaurant(restaurantId);
        }

        if ("1".equals(vegFilter)) {
            menuItems.removeIf(m -> !m.isVeg());
        }

        if ("priceAsc".equals(sort)) {
            menuItems.sort(Comparator.comparingDouble(Menu::getEffectivePrice));
        } else if ("priceDesc".equals(sort)) {
            menuItems.sort(Comparator.comparingDouble(Menu::getEffectivePrice).reversed());
        }

        request.setAttribute("restaurant", restaurant);
        request.setAttribute("menuItems", menuItems);
        request.getRequestDispatcher("/WEB-INF/jsp/user/menu.jsp").forward(request, response);
    }
}
