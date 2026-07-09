package com.tap.daoimpl;

import com.tap.dao.CartDAO;
import com.tap.model.Cart;
import com.tap.model.CartItem;
import com.tap.util.DBConnectionUtil;

import java.sql.*;

/**
 * Persists the session Cart object to the cart / cart_items tables so that
 * a customer's cart can survive across sessions if needed. The live/session
 * cart (HashMap<Integer, CartItem>) held in HttpSession is the primary
 * source of truth during an active session; this DAO is used to sync it.
 */
public class CartDAOImpl implements CartDAO {

    @Override
    public Cart getCartByUser(int userId) {
        Cart cart = new Cart();
        String cartSql = "SELECT * FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(cartSql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int cartId = rs.getInt("cart_id");
                    cart.setRestaurantId(rs.getInt("restaurant_id"));

                    String itemSql = "SELECT ci.menu_id, ci.quantity, m.name, m.image_url, m.price, m.offer_price, m.is_veg " +
                                      "FROM cart_items ci JOIN menu m ON ci.menu_id = m.menu_id WHERE ci.cart_id = ?";
                    try (PreparedStatement ps2 = conn.prepareStatement(itemSql)) {
                        ps2.setInt(1, cartId);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while (rs2.next()) {
                                double offerPrice = rs2.getDouble("offer_price");
                                boolean hasOffer = !rs2.wasNull();
                                double price = hasOffer ? offerPrice : rs2.getDouble("price");
                                CartItem ci = new CartItem(
                                        rs2.getInt("menu_id"),
                                        rs2.getString("name"),
                                        rs2.getString("image_url"),
                                        price,
                                        rs2.getInt("quantity"),
                                        rs2.getBoolean("is_veg")
                                );
                                cart.getItems().put(ci.getMenuId(), ci);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    @Override
    public boolean saveCart(int userId, Cart cart) {
        String upsertCartSql =
                "INSERT INTO cart (user_id, restaurant_id) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE restaurant_id = VALUES(restaurant_id)";
        try (Connection conn = DBConnectionUtil.getConnection()) {
            conn.setAutoCommit(false);
            int cartId;
            try (PreparedStatement ps = conn.prepareStatement(upsertCartSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                if (cart.getRestaurantId() > 0) ps.setInt(2, cart.getRestaurantId()); else ps.setNull(2, Types.INTEGER);
                ps.executeUpdate();
            }
            // fetch cart_id (works whether inserted or updated)
            try (PreparedStatement ps = conn.prepareStatement("SELECT cart_id FROM cart WHERE user_id = ?")) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    cartId = rs.getInt("cart_id");
                }
            }
            // clear old items then re-insert current snapshot (simplest consistent strategy)
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cart_items WHERE cart_id = ?")) {
                ps.setInt(1, cartId);
                ps.executeUpdate();
            }
            String insItem = "INSERT INTO cart_items (cart_id, menu_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insItem)) {
                for (CartItem ci : cart.getItems().values()) {
                    ps.setInt(1, cartId);
                    ps.setInt(2, ci.getMenuId());
                    ps.setInt(3, ci.getQuantity());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?"; // cascades to cart_items
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
