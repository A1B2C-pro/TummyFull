package com.tap.daoimpl;

import com.tap.dao.OTPDAO;
import com.tap.model.OTP;
import com.tap.util.DBConnectionUtil;

import java.sql.*;

public class OTPDAOImpl implements OTPDAO {

    @Override
    public boolean saveOTP(OTP otp) {
        String sql = "INSERT INTO otp_verification (email, otp_code, purpose, expires_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, otp.getEmail());
            ps.setString(2, otp.getOtpCode());
            ps.setString(3, otp.getPurpose());
            ps.setTimestamp(4, otp.getExpiresAt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OTP getLatestOTP(String email, String purpose) {
        String sql = "SELECT * FROM otp_verification WHERE email = ? AND purpose = ? " +
                     "ORDER BY otp_id DESC LIMIT 1";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, purpose);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OTP otp = new OTP();
                    otp.setOtpId(rs.getInt("otp_id"));
                    otp.setEmail(rs.getString("email"));
                    otp.setOtpCode(rs.getString("otp_code"));
                    otp.setPurpose(rs.getString("purpose"));
                    otp.setUsed(rs.getBoolean("is_used"));
                    otp.setCreatedAt(rs.getTimestamp("created_at"));
                    otp.setExpiresAt(rs.getTimestamp("expires_at"));
                    return otp;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean markUsed(String email, String otpCode) {
        String sql = "UPDATE otp_verification SET is_used = 1 WHERE email = ? AND otp_code = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otpCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void invalidateOldOTPs(String email, String purpose) {
        String sql = "UPDATE otp_verification SET is_used = 1 WHERE email = ? AND purpose = ? AND is_used = 0";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, purpose);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
