package com.tap.daoimpl;

import com.tap.dao.DeliveryAgentDAO;
import com.tap.model.DeliveryAgent;
import com.tap.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAgentDAOImpl implements DeliveryAgentDAO {

    @Override
    public boolean registerAgent(DeliveryAgent agent) {
        String sql = "INSERT INTO delivery_agents (full_name, email, phone, password_hash, vehicle_number, is_available, is_verified) " +
                     "VALUES (?, ?, ?, ?, ?, 1, 0)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, agent.getFullName());
            ps.setString(2, agent.getEmail());
            ps.setString(3, agent.getPhone());
            ps.setString(4, agent.getPasswordHash());
            ps.setString(5, agent.getVehicleNumber());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DeliveryAgent getAgentByEmail(String email) {
        String sql = "SELECT * FROM delivery_agents WHERE email = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DeliveryAgent getAgentById(int agentId) {
        String sql = "SELECT * FROM delivery_agents WHERE agent_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeliveryAgent> getAllAgents() {
        List<DeliveryAgent> list = new ArrayList<>();
        String sql = "SELECT * FROM delivery_agents ORDER BY created_at DESC";
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
    public List<DeliveryAgent> getAvailableAgents() {
        List<DeliveryAgent> list = new ArrayList<>();
        String sql = "SELECT * FROM delivery_agents WHERE is_available = 1";
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
    public boolean updateAvailability(int agentId, boolean available) {
        String sql = "UPDATE delivery_agents SET is_available = ? WHERE agent_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, agentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean markVerified(String email) {
        String sql = "UPDATE delivery_agents SET is_verified = 1 WHERE email = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePassword(String email, String newHashedPassword) {
        String sql = "UPDATE delivery_agents SET password_hash = ? WHERE email = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHashedPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAgent(DeliveryAgent agent) {
        String sql = "UPDATE delivery_agents SET full_name = ?, phone = ?, vehicle_number = ? WHERE agent_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, agent.getFullName());
            ps.setString(2, agent.getPhone());
            ps.setString(3, agent.getVehicleNumber());
            ps.setInt(4, agent.getAgentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAgent(int agentId) {
        String sql = "DELETE FROM delivery_agents WHERE agent_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean emailExists(String email) {
        String sql = "SELECT agent_id FROM delivery_agents WHERE email = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private DeliveryAgent mapRow(ResultSet rs) throws SQLException {
        DeliveryAgent a = new DeliveryAgent();
        a.setAgentId(rs.getInt("agent_id"));
        a.setFullName(rs.getString("full_name"));
        a.setEmail(rs.getString("email"));
        a.setPhone(rs.getString("phone"));
        a.setPasswordHash(rs.getString("password_hash"));
        a.setVehicleNumber(rs.getString("vehicle_number"));
        a.setAvailable(rs.getBoolean("is_available"));
        a.setVerified(rs.getBoolean("is_verified"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        return a;
    }
}
