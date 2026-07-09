package com.tap.servlet;

import com.tap.dao.MenuDAO;
import com.tap.dao.RestaurantDAO;
import com.tap.daoimpl.MenuDAOImpl;
import com.tap.daoimpl.RestaurantDAOImpl;
import com.tap.model.Cart;
import com.tap.model.CartItem;
import com.tap.model.Menu;
import com.tap.model.Restaurant;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Session-based shopping cart servlet. Cart is stored as com.tap.model.Cart
 * (HashMap<Integer, CartItem> keyed by menuId) in the HttpSession.
 * Only one restaurant's items are allowed in the cart at a time; adding
 * an item from a different restaurant clears the existing cart first
 * (the JSP warns the user before submitting when this would happen).
 *
 * Actions (param "action"): add, increase, decrease, remove, clear
 */
public class CartServlet extends HttpServlet {

    private final MenuDAO menuDAO = new MenuDAOImpl();
    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(SessionConstants.CART);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(SessionConstants.CART, cart);
        }

        String action = request.getParameter("action");
        int menuId = ValidationUtil.parseIntOrZero(request.getParameter("menuId"));

        switch (action == null ? "" : action) {
            case "add": {
                Menu menu = menuDAO.getMenuById(menuId);
                if (menu != null && menu.isAvailable()) {
                    if (!cart.isEmpty() && cart.getRestaurantId() != menu.getRestaurantId()) {
                        // Different restaurant -> reset cart (single-restaurant rule)
                        cart.clear();
                    }
                    if (cart.isEmpty()) {
                        Restaurant r = restaurantDAO.getRestaurantById(menu.getRestaurantId());
                        cart.setRestaurantId(menu.getRestaurantId());
                        cart.setRestaurantName(r != null ? r.getName() : "");
                    }
                    CartItem existing = cart.getItems().get(menuId);
                    if (existing != null) {
                        existing.setQuantity(existing.getQuantity() + 1);
                    } else {
                        CartItem item = new CartItem(menu.getMenuId(), menu.getName(), menu.getImageUrl(),
                                menu.getEffectivePrice(), 1, menu.isVeg());
                        cart.getItems().put(menuId, item);
                    }
                }
                break;
            }
            case "increase": {
                CartItem item = cart.getItems().get(menuId);
                if (item != null) item.setQuantity(item.getQuantity() + 1);
                break;
            }
            case "decrease": {
                CartItem item = cart.getItems().get(menuId);
                if (item != null) {
                    if (item.getQuantity() <= 1) {
                        cart.getItems().remove(menuId);
                    } else {
                        item.setQuantity(item.getQuantity() - 1);
                    }
                }
                break;
            }
            case "remove": {
                cart.getItems().remove(menuId);
                break;
            }
            case "clear": {
                cart.clear();
                break;
            }
            default:
                break;
        }

        if (cart.isEmpty()) {
            cart.clear();
        }

        String redirectTo = request.getParameter("redirectTo");
        if (ValidationUtil.isNullOrEmpty(redirectTo)) {
            redirectTo = request.getContextPath() + "/CartServlet";
        }
        response.sendRedirect(redirectTo);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(SessionConstants.CART);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(SessionConstants.CART, cart);
        }
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/WEB-INF/jsp/user/cart.jsp").forward(request, response);
    }
}
