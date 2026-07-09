package com.tap.model;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int menuId;
    private String itemName;
    private int quantity;
    private double price;

    public OrderItem() {}

    public OrderItem(int menuId, String itemName, int quantity, double price) {
        this.menuId = menuId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getSubtotal() { return price * quantity; }

    @Override
    public String toString() {
        return "OrderItem{itemName='" + itemName + "', quantity=" + quantity + ", price=" + price + "}";
    }
}
