package com.tap.daoimpl;

import com.tap.dao.ReviewDAO;
import com.tap.model.Review;
import com.tap.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    private static final String BASE_SELECT =
            "SELECT r.*, u.full_name AS user_name FROM reviews r JOIN users u ON r.user_id = u.user_id ";

    @Override
    public List<Review> getReviewsByRestaurant(int restaurantId) {
        List<Review> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE r.restaurant_id = ? ORDER BY r.created_at DESC";
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
    public List<Review> getReviewsByMenu(int menuId) {
        List<Review> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE r.menu_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Review> getAllReviews() {
        List<Review> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY r.created_at DESC";
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
    public List<Review> getHighlightedReviews() {
        List<Review> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE r.is_highlighted = 1 ORDER BY r.created_at DESC";
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
    public boolean addReview(Review r) {
        String sql = "INSERT INTO reviews (user_id, restaurant_id, menu_id, rating, comment, image_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getUserId());
            if (r.getRestaurantId() != null) ps.setInt(2, r.getRestaurantId()); else ps.setNull(2, Types.INTEGER);
            if (r.getMenuId() != null) ps.setInt(3, r.getMenuId()); else ps.setNull(3, Types.INTEGER);
            ps.setDouble(4, r.getRating());
            ps.setString(5, r.getComment());
            ps.setString(6, r.getImageUrl());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean highlightReview(int reviewId, boolean highlight) {
        String sql = "UPDATE reviews SET is_highlighted = ? WHERE review_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, highlight);
            ps.setInt(2, reviewId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Review mapRow(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setReviewId(rs.getInt("review_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setUserName(rs.getString("user_name"));
        int rid = rs.getInt("restaurant_id");
        r.setRestaurantId(rs.wasNull() ? null : rid);
        int mid = rs.getInt("menu_id");
        r.setMenuId(rs.wasNull() ? null : mid);
        r.setRating(rs.getDouble("rating"));
        r.setComment(rs.getString("comment"));
        r.setImageUrl(rs.getString("image_url"));
        r.setHighlighted(rs.getBoolean("is_highlighted"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}
