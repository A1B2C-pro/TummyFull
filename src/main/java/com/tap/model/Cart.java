package com.tap.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Session-based shopping cart. Only one restaurant allowed per cart at a time.
 * Backed by a HashMap<Integer, CartItem> keyed by menuId, as required.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private int restaurantId;
    private String restaurantName;
    private Map<Integer, CartItem> items = new LinkedHashMap<>();

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public Map<Integer, CartItem> getItems() { return items; }
    public void setItems(Map<Integer, CartItem> items) { this.items = items; }

    public boolean isEmpty() { return items.isEmpty(); }

    public int getTotalItemCount() {
        int count = 0;
        for (CartItem ci : items.values()) count += ci.getQuantity();
        return count;
    }

    public double getItemTotal() {
        double total = 0;
        for (CartItem ci : items.values()) total += ci.getSubtotal();
        return total;
    }

    public void clear() {
        items.clear();
        restaurantId = 0;
        restaurantName = null;
    }

    @Override
    public String toString() {
        return "Cart{restaurantId=" + restaurantId + ", items=" + items.size() + "}";
    }
}
