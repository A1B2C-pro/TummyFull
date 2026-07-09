package com.tap.servlet;

import com.tap.dao.CategoryDAO;
import com.tap.dao.RestaurantDAO;
import com.tap.daoimpl.CategoryDAOImpl;
import com.tap.daoimpl.RestaurantDAOImpl;
import com.tap.model.Restaurant;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles list/add/edit/delete/activate/deactivate for restaurants.
 * action param: list (default) | add | edit | delete | toggleActive | toggleOpen
 */
public class AdminRestaurantServlet extends HttpServlet {

    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("restaurants", restaurantDAO.getAllRestaurants());
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-restaurants.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "" : action) {
            case "add": {
                Restaurant r = buildFromRequest(request, new Restaurant());
                restaurantDAO.addRestaurant(r);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Restaurant added successfully.");
                break;
            }
            case "edit": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("restaurantId"));
                Restaurant existing = restaurantDAO.getRestaurantById(id);
                if (existing != null) {
                    Restaurant r = buildFromRequest(request, existing);
                    r.setRestaurantId(id);
                    restaurantDAO.updateRestaurant(r);
                    request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Restaurant updated successfully.");
                }
                break;
            }
            case "delete": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("restaurantId"));
                restaurantDAO.deleteRestaurant(id);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Restaurant deleted.");
                break;
            }
            case "toggleActive": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("restaurantId"));
                boolean active = "1".equals(request.getParameter("value"));
                restaurantDAO.setActive(id, active);
                break;
            }
            case "toggleOpen": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("restaurantId"));
                boolean open = "1".equals(request.getParameter("value"));
                restaurantDAO.setOpen(id, open);
                break;
            }
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminRestaurantServlet");
    }

    private Restaurant buildFromRequest(HttpServletRequest request, Restaurant r) {
        r.setName(request.getParameter("name"));
        r.setCuisine(request.getParameter("cuisine"));
        r.setCategoryId(ValidationUtil.parseIntOrZero(request.getParameter("categoryId")));
        r.setImageUrl(request.getParameter("imageUrl"));
        r.setDeliveryTime(request.getParameter("deliveryTime"));
        r.setAddress(request.getParameter("address"));
        r.setCity("Bangalore");
        r.setOfferText(request.getParameter("offerText"));
        r.setOpen("1".equals(request.getParameter("isOpen")));
        r.setActive(true);
        return r;
    }
}
