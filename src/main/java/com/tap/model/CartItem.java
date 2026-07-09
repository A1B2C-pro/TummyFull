package com.tap.model;

import java.io.Serializable;

/**
 * Session/db representation of one line item inside a user's cart.
 */
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cartItemId;
    private int menuId;
    private String menuName;
    private String imageUrl;
    private double price;
    private int quantity;
    private boolean veg;

    public CartItem() {}

    public CartItem(int menuId, String menuName, String imageUrl, double price, int quantity, boolean veg) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.veg = veg;
    }

    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }
    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }
    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }

    public double getSubtotal() { return price * quantity; }

    @Override
    public String toString() {
        return "CartItem{menuId=" + menuId + ", menuName='" + menuName + "', price=" + price +
                ", quantity=" + quantity + "}";
    }
}
