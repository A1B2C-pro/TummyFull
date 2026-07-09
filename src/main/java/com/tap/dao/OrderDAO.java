package com.tap.dao;

import com.tap.model.Order;
import java.util.List;

public interface OrderDAO {
    int placeOrder(Order order);
    Order getOrderById(int orderId);
    List<Order> getOrdersByUser(int userId);
    List<Order> getOrdersByAgent(int agentId);
    List<Order> getAllOrders();
    List<Order> getOrdersByStatus(String status);
    boolean updateOrderStatus(int orderId, String status);
    boolean assignAgent(int orderId, int agentId);
    boolean cancelOrder(int orderId);

    // Admin dashboard aggregate queries
    int getTotalOrdersCount();
    double getTotalRevenue();
    int getPendingOrdersCount();
    int getActiveDeliveriesCount();
    java.util.Map<String, Double> getMonthlySales();
    String getBestSellingItem();
}
