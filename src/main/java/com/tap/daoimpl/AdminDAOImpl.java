package com.tap.daoimpl;

import com.tap.dao.AdminDAO;
import com.tap.model.Admin;
import com.tap.util.DBConnectionUtil;

import java.sql.*;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin getAdminByEmail(String email) {
        String sql = "SELECT * FROM admins WHERE email = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Admin a = new Admin();
                    a.setAdminId(rs.getInt("admin_id"));
                    a.setFullName(rs.getString("full_name"));
                    a.setEmail(rs.getString("email"));
                    a.setPasswordHash(rs.getString("password_hash"));
                    a.setCreatedAt(rs.getTimestamp("created_at"));
                    return a;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
