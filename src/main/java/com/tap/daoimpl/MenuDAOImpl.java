package com.tap.daoimpl;

import com.tap.dao.MenuDAO;
import com.tap.model.Menu;
import com.tap.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAOImpl implements MenuDAO {

    @Override
    public List<Menu> getMenuByRestaurant(int restaurantId) {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE restaurant_id = ? AND is_available = 1 ORDER BY menu_id";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Menu getMenuById(int menuId) {
        String sql = "SELECT * FROM menu WHERE menu_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Menu> searchMenu(String keyword) {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE is_available = 1 AND name LIKE ? ORDER BY rating DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addMenuItem(Menu m) {
        String sql = "INSERT INTO menu (restaurant_id, category_id, name, description, price, offer_price, image_url, is_veg, is_available) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getRestaurantId());
            ps.setInt(2, m.getCategoryId());
            ps.setString(3, m.getName());
            ps.setString(4, m.getDescription());
            ps.setDouble(5, m.getPrice());
            if (m.getOfferPrice() != null) ps.setDouble(6, m.getOfferPrice()); else ps.setNull(6, Types.DECIMAL);
            ps.setString(7, m.getImageUrl());
            ps.setBoolean(8, m.isVeg());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMenuItem(Menu m) {
        String sql = "UPDATE menu SET name=?, description=?, price=?, offer_price=?, image_url=?, is_veg=? WHERE menu_id=?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getDescription());
            ps.setDouble(3, m.getPrice());
            if (m.getOfferPrice() != null) ps.setDouble(4, m.getOfferPrice()); else ps.setNull(4, Types.DECIMAL);
            ps.setString(5, m.getImageUrl());
            ps.setBoolean(6, m.isVeg());
            ps.setInt(7, m.getMenuId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMenuItem(int menuId) {
        String sql = "DELETE FROM menu WHERE menu_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean setAvailability(int menuId, boolean available) {
        String sql = "UPDATE menu SET is_available = ? WHERE menu_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, menuId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Menu mapRow(ResultSet rs) throws SQLException {
        Menu m = new Menu();
        m.setMenuId(rs.getInt("menu_id"));
        m.setRestaurantId(rs.getInt("restaurant_id"));
        m.setCategoryId(rs.getInt("category_id"));
        m.setName(rs.getString("name"));
        m.setDescription(rs.getString("description"));
        m.setPrice(rs.getDouble("price"));
        double offerPrice = rs.getDouble("offer_price");
        m.setOfferPrice(rs.wasNull() ? null : offerPrice);
        m.setImageUrl(rs.getString("image_url"));
        m.setVeg(rs.getBoolean("is_veg"));
        m.setRating(rs.getDouble("rating"));
        m.setAvailable(rs.getBoolean("is_available"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }
}
