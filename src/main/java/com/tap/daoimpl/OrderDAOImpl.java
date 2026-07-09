//package com.tap.daoimpl;
//
//import com.tap.dao.OrderDAO;
//import com.tap.model.Order;
//import com.tap.model.OrderItem;
//import com.tap.util.DBConnectionUtil;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//public class OrderDAOImpl implements OrderDAO {
//
//    @Override
//    public int placeOrder(Order order) {
//        String orderSql = "INSERT INTO orders (user_id, restaurant_id, delivery_address, delivery_instructions, " +
//                "customer_name, customer_phone, item_total, platform_fee, delivery_fee, gst, coupon_discount, " +
//                "coupon_code, final_total, payment_method, payment_status, order_status) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PLACED')";
//        try (Connection conn = DBConnectionUtil.getConnection()) {
//            conn.setAutoCommit(false);
//            int orderId;
//            try (PreparedStatement ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
//                ps.setInt(1, order.getUserId());
//                ps.setInt(2, order.getRestaurantId());
//                ps.setString(3, order.getDeliveryAddress());
//                ps.setString(4, order.getDeliveryInstructions());
//                ps.setString(5, order.getCustomerName());
//                ps.setString(6, order.getCustomerPhone());
//                ps.setDouble(7, order.getItemTotal());
//                ps.setDouble(8, order.getPlatformFee());
//                ps.setDouble(9, order.getDeliveryFee());
//                ps.setDouble(10, order.getGst());
//                ps.setDouble(11, order.getCouponDiscount());
//                ps.setString(12, order.getCouponCode());
//                ps.setDouble(13, order.getFinalTotal());
//                ps.setString(14, order.getPaymentMethod());
//                ps.setString(15, "COD".equals(order.getPaymentMethod()) ? "PENDING" : "PAID");
//                ps.executeUpdate();
//                try (ResultSet keys = ps.getGeneratedKeys()) {
//                    keys.next();
//                    orderId = keys.getInt(1);
//                }
//            }
//            String itemSql = "INSERT INTO order_items (order_id, menu_id, item_name, quantity, price) VALUES (?, ?, ?, ?, ?)";
//            try (PreparedStatement ps = conn.prepareStatement(itemSql)) {
//                for (OrderItem oi : order.getOrderItems()) {
//                    ps.setInt(1, orderId);
//                    ps.setInt(2, oi.getMenuId());
//                    ps.setString(3, oi.getItemName());
//                    ps.setInt(4, oi.getQuantity());
//                    ps.setDouble(5, oi.getPrice());
//                    ps.addBatch();
//                }
//                ps.executeBatch();
//            }
//            conn.commit();
//            return orderId;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }
//
//    @Override
//    public Order getOrderById(int orderId) {
//        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
//                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
//                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id WHERE o.order_id = ?";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, orderId);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    Order order = mapRow(rs);
//                    order.setOrderItems(getOrderItems(conn, orderId));
//                    return order;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public List<Order> getOrdersByUser(int userId) {
//        List<Order> list = new ArrayList<>();
//        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
//                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
//                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id " +
//                     "WHERE o.user_id = ? ORDER BY o.placed_at DESC";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, userId);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Order order = mapRow(rs);
//                    order.setOrderItems(getOrderItems(conn, order.getOrderId()));
//                    list.add(order);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    @Override
//    public List<Order> getOrdersByAgent(int agentId) {
//        List<Order> list = new ArrayList<>();
//        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
//                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
//                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id " +
//                     "WHERE o.agent_id = ? ORDER BY o.placed_at DESC";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, agentId);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Order order = mapRow(rs);
//                    order.setOrderItems(getOrderItems(conn, order.getOrderId()));
//                    list.add(order);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    @Override
//    public List<Order> getAllOrders() {
//        List<Order> list = new ArrayList<>();
//        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
//                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
//                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id ORDER BY o.placed_at DESC";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                Order order = mapRow(rs);
//                order.setOrderItems(getOrderItems(conn, order.getOrderId()));
//                list.add(order);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    @Override
//    public List<Order> getOrdersByStatus(String status) {
//        List<Order> list = new ArrayList<>();
//        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
//                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
//                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id " +
//                     "WHERE o.order_status = ? ORDER BY o.placed_at DESC";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, status);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Order order = mapRow(rs);
//                    order.setOrderItems(getOrderItems(conn, order.getOrderId()));
//                    list.add(order);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    @Override
//    public boolean updateOrderStatus(int orderId, String status) {
//        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, status);
//            ps.setInt(2, orderId);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public boolean assignAgent(int orderId, int agentId) {
//        String sql = "UPDATE orders SET agent_id = ?, order_status = 'ACCEPTED' WHERE order_id = ?";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, agentId);
//            ps.setInt(2, orderId);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public boolean cancelOrder(int orderId) {
//        return updateOrderStatus(orderId, "CANCELLED");
//    }
//
//    @Override
//    public int getTotalOrdersCount() {
//        String sql = "SELECT COUNT(*) FROM orders";
//        return scalarInt(sql);
//    }
//
//    @Override
//    public double getTotalRevenue() {
//        String sql = "SELECT COALESCE(SUM(final_total),0) FROM orders WHERE order_status = 'DELIVERED'";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) return rs.getDouble(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    @Override
//    public int getPendingOrdersCount() {
//        String sql = "SELECT COUNT(*) FROM orders WHERE order_status IN ('PLACED','ACCEPTED','PREPARING')";
//        return scalarInt(sql);
//    }
//
//    @Override
//    public int getActiveDeliveriesCount() {
//        String sql = "SELECT COUNT(*) FROM orders WHERE order_status IN ('READY_FOR_PICKUP','OUT_FOR_DELIVERY')";
//        return scalarInt(sql);
//    }
//
//    @Override
//    public Map<String, Double> getMonthlySales() {
//        Map<String, Double> map = new LinkedHashMap<>();
//        String sql = "SELECT DATE_FORMAT(placed_at, '%Y-%m') AS ym, SUM(final_total) AS total " +
//                     "FROM orders WHERE order_status = 'DELIVERED' GROUP BY ym ORDER BY ym DESC LIMIT 12";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) map.put(rs.getString("ym"), rs.getDouble("total"));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return map;
//    }
//
//    @Override
//    public String getBestSellingItem() {
//        String sql = "SELECT item_name, SUM(quantity) AS qty FROM order_items GROUP BY item_name ORDER BY qty DESC LIMIT 1";
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) return rs.getString("item_name");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return "N/A";
//    }
//
//    private int scalarInt(String sql) {
//        try (Connection conn = DBConnectionUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) return rs.getInt(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    private List<OrderItem> getOrderItems(Connection conn, int orderId) throws SQLException {
//        List<OrderItem> items = new ArrayList<>();
//        String sql = "SELECT * FROM order_items WHERE order_id = ?";
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, orderId);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    OrderItem oi = new OrderItem();
//                    oi.setOrderItemId(rs.getInt("order_item_id"));
//                    oi.setOrderId(rs.getInt("order_id"));
//                    oi.setMenuId(rs.getInt("menu_id"));
//                    oi.setItemName(rs.getString("item_name"));
//                    oi.setQuantity(rs.getInt("quantity"));
//                    oi.setPrice(rs.getDouble("price"));
//                    items.add(oi);
//                }
//            }
//        }
//        return items;
//    }
//
//    private Order mapRow(ResultSet rs) throws SQLException {
//        Order o = new Order();
//        o.setOrderId(rs.getInt("order_id"));
//        o.setUserId(rs.getInt("user_id"));
//        o.setRestaurantId(rs.getInt("restaurant_id"));
//        o.setRestaurantName(rs.getString("restaurant_name"));
//        int agentId = rs.getInt("agent_id");
//        o.setAgentId(rs.wasNull() ? null : agentId);
//        o.setAgentName(rs.getString("agent_name"));
//        o.setDeliveryAddress(rs.getString("delivery_address"));
//        o.setDeliveryInstructions(rs.getString("delivery_instructions"));
//        o.setCustomerName(rs.getString("customer_name"));
//        o.setCustomerPhone(rs.getString("customer_phone"));
//        o.setItemTotal(rs.getDouble("item_total"));
//        o.setPlatformFee(rs.getDouble("platform_fee"));
//        o.setDeliveryFee(rs.getDouble("delivery_fee"));
//        o.setGst(rs.getDouble("gst"));
//        o.setCouponDiscount(rs.getDouble("coupon_discount"));
//        o.setCouponCode(rs.getString("coupon_code"));
//        o.setFinalTotal(rs.getDouble("final_total"));
//        o.setPaymentMethod(rs.getString("payment_method"));
//        o.setPaymentStatus(rs.getString("payment_status"));
//        o.setOrderStatus(rs.getString("order_status"));
//        o.setPlacedAt(rs.getTimestamp("placed_at"));
//        o.setUpdatedAt(rs.getTimestamp("updated_at"));
//        return o;
//    }
//}


package com.tap.daoimpl;

import com.tap.dao.OrderDAO;
import com.tap.model.Order;
import com.tap.model.OrderItem;
import com.tap.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public int placeOrder(Order order) {
        String orderSql = "INSERT INTO orders (user_id, restaurant_id, delivery_address, delivery_instructions, " +
                "customer_name, customer_phone, item_total, platform_fee, delivery_fee, gst, coupon_discount, " +
                "coupon_code, final_total, payment_method, payment_status, order_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PLACED')";
        try (Connection conn = DBConnectionUtil.getConnection()) {
            conn.setAutoCommit(false);
            int orderId;
            try (PreparedStatement ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getUserId());
                ps.setInt(2, order.getRestaurantId());
                ps.setString(3, order.getDeliveryAddress());
                ps.setString(4, order.getDeliveryInstructions());
                ps.setString(5, order.getCustomerName());
                ps.setString(6, order.getCustomerPhone());
                ps.setDouble(7, order.getItemTotal());
                ps.setDouble(8, order.getPlatformFee());
                ps.setDouble(9, order.getDeliveryFee());
                ps.setDouble(10, order.getGst());
                ps.setDouble(11, order.getCouponDiscount());
                ps.setString(12, order.getCouponCode());
                ps.setDouble(13, order.getFinalTotal());
                ps.setString(14, order.getPaymentMethod());
                ps.setString(15, "COD".equals(order.getPaymentMethod()) ? "PENDING" : "PAID");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    orderId = keys.getInt(1);
                }
            }
            String itemSql = "INSERT INTO order_items (order_id, menu_id, item_name, quantity, price) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(itemSql)) {
                for (OrderItem oi : order.getOrderItems()) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, oi.getMenuId());
                    ps.setString(3, oi.getItemName());
                    ps.setInt(4, oi.getQuantity());
                    ps.setDouble(5, oi.getPrice());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
            return orderId;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id WHERE o.order_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapRow(rs);
                    order.setOrderItems(getOrderItems(conn, orderId));
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByUser(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id " +
                     "WHERE o.user_id = ? ORDER BY o.placed_at DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRow(rs);
                    order.setOrderItems(getOrderItems(conn, order.getOrderId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order> getOrdersByAgent(int agentId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id " +
                     "WHERE o.agent_id = ? ORDER BY o.placed_at DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRow(rs);
                    order.setOrderItems(getOrderItems(conn, order.getOrderId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id ORDER BY o.placed_at DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = mapRow(rs);
                order.setOrderItems(getOrderItems(conn, order.getOrderId()));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, r.name AS restaurant_name, d.full_name AS agent_name " +
                     "FROM orders o JOIN restaurants r ON o.restaurant_id = r.restaurant_id " +
                     "LEFT JOIN delivery_agents d ON o.agent_id = d.agent_id " +
                     "WHERE o.order_status = ? ORDER BY o.placed_at DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRow(rs);
                    order.setOrderItems(getOrderItems(conn, order.getOrderId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean assignAgent(int orderId, int agentId) {
        String sql = "UPDATE orders SET agent_id = ?, order_status = 'ACCEPTED' WHERE order_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agentId);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancelOrder(int orderId) {
        return updateOrderStatus(orderId, "CANCELLED");
    }

    @Override
    public int getTotalOrdersCount() {
        String sql = "SELECT COUNT(*) FROM orders";
        return scalarInt(sql);
    }

    @Override
    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(final_total),0) FROM orders WHERE payment_status = 'PAID' OR order_status = 'DELIVERED'";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getPendingOrdersCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_status IN ('PLACED','ACCEPTED','PREPARING')";
        return scalarInt(sql);
    }

    @Override
    public int getActiveDeliveriesCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_status IN ('READY_FOR_PICKUP','OUT_FOR_DELIVERY')";
        return scalarInt(sql);
    }

    @Override
    public Map<String, Double> getMonthlySales() {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(placed_at, '%Y-%m') AS ym, SUM(final_total) AS total " +
                     "FROM orders WHERE order_status = 'DELIVERED' GROUP BY ym ORDER BY ym DESC LIMIT 12";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getString("ym"), rs.getDouble("total"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public String getBestSellingItem() {
        String sql = "SELECT item_name, SUM(quantity) AS qty FROM order_items GROUP BY item_name ORDER BY qty DESC LIMIT 1";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getString("item_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    private int scalarInt(String sql) {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List<OrderItem> getOrderItems(Connection conn, int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem oi = new OrderItem();
                    oi.setOrderItemId(rs.getInt("order_item_id"));
                    oi.setOrderId(rs.getInt("order_id"));
                    oi.setMenuId(rs.getInt("menu_id"));
                    oi.setItemName(rs.getString("item_name"));
                    oi.setQuantity(rs.getInt("quantity"));
                    oi.setPrice(rs.getDouble("price"));
                    items.add(oi);
                }
            }
        }
        return items;
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        o.setRestaurantId(rs.getInt("restaurant_id"));
        o.setRestaurantName(rs.getString("restaurant_name"));
        int agentId = rs.getInt("agent_id");
        o.setAgentId(rs.wasNull() ? null : agentId);
        o.setAgentName(rs.getString("agent_name"));
        o.setDeliveryAddress(rs.getString("delivery_address"));
        o.setDeliveryInstructions(rs.getString("delivery_instructions"));
        o.setCustomerName(rs.getString("customer_name"));
        o.setCustomerPhone(rs.getString("customer_phone"));
        o.setItemTotal(rs.getDouble("item_total"));
        o.setPlatformFee(rs.getDouble("platform_fee"));
        o.setDeliveryFee(rs.getDouble("delivery_fee"));
        o.setGst(rs.getDouble("gst"));
        o.setCouponDiscount(rs.getDouble("coupon_discount"));
        o.setCouponCode(rs.getString("coupon_code"));
        o.setFinalTotal(rs.getDouble("final_total"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setPlacedAt(rs.getTimestamp("placed_at"));
        o.setUpdatedAt(rs.getTimestamp("updated_at"));
        return o;
    }
}


