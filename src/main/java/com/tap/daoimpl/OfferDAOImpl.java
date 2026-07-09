package com.tap.daoimpl;

import com.tap.dao.OfferDAO;
import com.tap.model.Offer;
import com.tap.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferDAOImpl implements OfferDAO {

    @Override
    public List<Offer> getAllOffers() {
        List<Offer> list = new ArrayList<>();
        String sql = "SELECT * FROM offers ORDER BY created_at DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Offer> getActiveOffers() {
        List<Offer> list = new ArrayList<>();
        String sql = "SELECT * FROM offers WHERE is_active = 1 AND (valid_till IS NULL OR valid_till >= CURDATE())";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addOffer(Offer o) {
        String sql = "INSERT INTO offers (title, description, restaurant_id, discount_percent, is_active, valid_till) VALUES (?, ?, ?, ?, 1, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getTitle());
            ps.setString(2, o.getDescription());
            if (o.getRestaurantId() != null) ps.setInt(3, o.getRestaurantId()); else ps.setNull(3, Types.INTEGER);
            ps.setDouble(4, o.getDiscountPercent());
            ps.setDate(5, o.getValidTill());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateOffer(Offer o) {
        String sql = "UPDATE offers SET title=?, description=?, discount_percent=?, is_active=?, valid_till=? WHERE offer_id=?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getTitle());
            ps.setString(2, o.getDescription());
            ps.setDouble(3, o.getDiscountPercent());
            ps.setBoolean(4, o.isActive());
            ps.setDate(5, o.getValidTill());
            ps.setInt(6, o.getOfferId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteOffer(int offerId) {
        String sql = "DELETE FROM offers WHERE offer_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Offer mapRow(ResultSet rs) throws SQLException {
        Offer o = new Offer();
        o.setOfferId(rs.getInt("offer_id"));
        o.setTitle(rs.getString("title"));
        o.setDescription(rs.getString("description"));
        int rid = rs.getInt("restaurant_id");
        o.setRestaurantId(rs.wasNull() ? null : rid);
        o.setDiscountPercent(rs.getDouble("discount_percent"));
        o.setActive(rs.getBoolean("is_active"));
        o.setValidTill(rs.getDate("valid_till"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }
}
