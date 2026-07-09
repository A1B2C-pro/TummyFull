package com.tap.model;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private int userId;
    private String userName;
    private Integer restaurantId;
    private Integer menuId;
    private double rating;
    private String comment;
    private String imageUrl;
    private boolean highlighted;
    private Timestamp createdAt;

    public Review() {}

    public Review(int userId, Integer restaurantId, Integer menuId, double rating, String comment) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.menuId = menuId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Integer getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Integer restaurantId) { this.restaurantId = restaurantId; }
    public Integer getMenuId() { return menuId; }
    public void setMenuId(Integer menuId) { this.menuId = menuId; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isHighlighted() { return highlighted; }
    public void setHighlighted(boolean highlighted) { this.highlighted = highlighted; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Review{reviewId=" + reviewId + ", userId=" + userId + ", rating=" + rating +
                ", comment='" + comment + "'}";
    }
}
