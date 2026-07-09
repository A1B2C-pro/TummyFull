package com.tap.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Offer {
    private int offerId;
    private String title;
    private String description;
    private Integer restaurantId;
    private double discountPercent;
    private boolean active;
    private Date validTill;
    private Timestamp createdAt;

    public Offer() {}

    public Offer(String title, String description, Integer restaurantId, double discountPercent, Date validTill) {
        this.title = title;
        this.description = description;
        this.restaurantId = restaurantId;
        this.discountPercent = discountPercent;
        this.validTill = validTill;
    }

    public int getOfferId() { return offerId; }
    public void setOfferId(int offerId) { this.offerId = offerId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Integer restaurantId) { this.restaurantId = restaurantId; }
    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Date getValidTill() { return validTill; }
    public void setValidTill(Date validTill) { this.validTill = validTill; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Offer{offerId=" + offerId + ", title='" + title + "', discountPercent=" + discountPercent + "}";
    }
}
