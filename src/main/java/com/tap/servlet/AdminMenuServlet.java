package com.tap.servlet;

import com.tap.dao.CategoryDAO;
import com.tap.dao.MenuDAO;
import com.tap.dao.RestaurantDAO;
import com.tap.daoimpl.CategoryDAOImpl;
import com.tap.daoimpl.MenuDAOImpl;
import com.tap.daoimpl.RestaurantDAOImpl;
import com.tap.model.Menu;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminMenuServlet extends HttpServlet {

    private final MenuDAO menuDAO = new MenuDAOImpl();
    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int restaurantId = ValidationUtil.parseIntOrZero(request.getParameter("restaurantId"));
        request.setAttribute("restaurants", restaurantDAO.getAllRestaurants());
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.setAttribute("selectedRestaurantId", restaurantId);
        if (restaurantId > 0) {
            request.setAttribute("menuItems", menuDAO.getMenuByRestaurant(restaurantId));
        }
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-menu.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int restaurantId = ValidationUtil.parseIntOrZero(request.getParameter("restaurantId"));

        switch (action == null ? "" : action) {
            case "add": {
                Menu m = buildFromRequest(request, new Menu());
                menuDAO.addMenuItem(m);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Food item added.");
                break;
            }
            case "edit": {
                int menuId = ValidationUtil.parseIntOrZero(request.getParameter("menuId"));
                Menu existing = menuDAO.getMenuById(menuId);
                if (existing != null) {
                    Menu m = buildFromRequest(request, existing);
                    m.setMenuId(menuId);
                    menuDAO.updateMenuItem(m);
                    request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Food item updated.");
                }
                break;
            }
            case "delete": {
                int menuId = ValidationUtil.parseIntOrZero(request.getParameter("menuId"));
                menuDAO.deleteMenuItem(menuId);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Food item deleted.");
                break;
            }
            case "toggleAvailable": {
                int menuId = ValidationUtil.parseIntOrZero(request.getParameter("menuId"));
                boolean available = "1".equals(request.getParameter("value"));
                menuDAO.setAvailability(menuId, available);
                break;
            }
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminMenuServlet?restaurantId=" + restaurantId);
    }

    private Menu buildFromRequest(HttpServletRequest request, Menu m) {
        m.setRestaurantId(ValidationUtil.parseIntOrZero(request.getParameter("restaurantId")));
        m.setCategoryId(ValidationUtil.parseIntOrZero(request.getParameter("categoryId")));
        m.setName(request.getParameter("name"));
        m.setDescription(request.getParameter("description"));
        m.setPrice(parseDoubleSafe(request.getParameter("price")));
        String offerPriceParam = request.getParameter("offerPrice");
        m.setOfferPrice(ValidationUtil.isNullOrEmpty(offerPriceParam) ? null : parseDoubleSafe(offerPriceParam));
        m.setImageUrl(request.getParameter("imageUrl"));
        m.setVeg("1".equals(request.getParameter("isVeg")));
        m.setAvailable(true);
        return m;
    }

    private double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0; }
    }
}
