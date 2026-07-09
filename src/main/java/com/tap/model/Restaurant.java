package com.tap.model;

import java.sql.Timestamp;

public class Restaurant {
    private int restaurantId;
    private String name;
    private String cuisine;
    private int categoryId;
    private String imageUrl;
    private double rating;
    private String deliveryTime;
    private String address;
    private String city;
    private String offerText;
    private boolean open;
    private boolean active;
    private Timestamp createdAt;

    public Restaurant() {}

    public Restaurant(String name, String cuisine, int categoryId, String imageUrl, String address, String city) {
        this.name = name;
        this.cuisine = cuisine;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.address = address;
        this.city = city;
    }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getOfferText() { return offerText; }
    public void setOfferText(String offerText) { this.offerText = offerText; }
    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Restaurant{restaurantId=" + restaurantId + ", name='" + name + "', cuisine='" + cuisine +
                "', rating=" + rating + ", open=" + open + "}";
    }
}
