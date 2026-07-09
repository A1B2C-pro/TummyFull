package com.tap.daoimpl;

import com.tap.dao.CouponDAO;
import com.tap.model.Coupon;
import com.tap.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAOImpl implements CouponDAO {

    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupons ORDER BY created_at DESC";
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
    public Coupon getCouponByCode(String code) {
        String sql = "SELECT * FROM coupons WHERE code = ? AND is_active = 1";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addCoupon(Coupon c) {
        String sql = "INSERT INTO coupons (code, description, discount_percent, max_discount, min_order_value, is_active, valid_till) VALUES (?, ?, ?, ?, ?, 1, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCode());
            ps.setString(2, c.getDescription());
            ps.setDouble(3, c.getDiscountPercent());
            ps.setDouble(4, c.getMaxDiscount());
            ps.setDouble(5, c.getMinOrderValue());
            ps.setDate(6, c.getValidTill());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCoupon(Coupon c) {
        String sql = "UPDATE coupons SET description=?, discount_percent=?, max_discount=?, min_order_value=?, is_active=?, valid_till=? WHERE coupon_id=?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDescription());
            ps.setDouble(2, c.getDiscountPercent());
            ps.setDouble(3, c.getMaxDiscount());
            ps.setDouble(4, c.getMinOrderValue());
            ps.setBoolean(5, c.isActive());
            ps.setDate(6, c.getValidTill());
            ps.setInt(7, c.getCouponId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCoupon(int couponId) {
        String sql = "DELETE FROM coupons WHERE coupon_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Coupon mapRow(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setCouponId(rs.getInt("coupon_id"));
        c.setCode(rs.getString("code"));
        c.setDescription(rs.getString("description"));
        c.setDiscountPercent(rs.getDouble("discount_percent"));
        c.setMaxDiscount(rs.getDouble("max_discount"));
        c.setMinOrderValue(rs.getDouble("min_order_value"));
        c.setActive(rs.getBoolean("is_active"));
        c.setValidTill(rs.getDate("valid_till"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
