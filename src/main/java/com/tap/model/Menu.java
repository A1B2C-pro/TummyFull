package com.tap.model;

import java.sql.Timestamp;

public class Menu {
    private int menuId;
    private int restaurantId;
    private int categoryId;
    private String name;
    private String description;
    private double price;
    private Double offerPrice;
    private String imageUrl;
    private boolean veg;
    private double rating;
    private boolean available;
    private Timestamp createdAt;

    public Menu() {}

    public Menu(int restaurantId, String name, String description, double price, String imageUrl, boolean veg) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.veg = veg;
    }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Double getOfferPrice() { return offerPrice; }
    public void setOfferPrice(Double offerPrice) { this.offerPrice = offerPrice; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public double getEffectivePrice() {
        return (offerPrice != null && offerPrice > 0) ? offerPrice : price;
    }

    @Override
    public String toString() {
        return "Menu{menuId=" + menuId + ", name='" + name + "', price=" + price +
                ", offerPrice=" + offerPrice + ", veg=" + veg + "}";
    }
}
