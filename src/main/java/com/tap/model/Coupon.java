package com.tap.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Coupon {
    private int couponId;
    private String code;
    private String description;
    private double discountPercent;
    private double maxDiscount;
    private double minOrderValue;
    private boolean active;
    private Date validTill;
    private Timestamp createdAt;

    public Coupon() {}

    public Coupon(String code, String description, double discountPercent, double maxDiscount, double minOrderValue) {
        this.code = code;
        this.description = description;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
        this.minOrderValue = minOrderValue;
    }

    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }
    public double getMaxDiscount() { return maxDiscount; }
    public void setMaxDiscount(double maxDiscount) { this.maxDiscount = maxDiscount; }
    public double getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(double minOrderValue) { this.minOrderValue = minOrderValue; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Date getValidTill() { return validTill; }
    public void setValidTill(Date validTill) { this.validTill = validTill; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public double calculateDiscount(double orderValue) {
        if (!active || orderValue < minOrderValue) return 0;
        double discount = orderValue * (discountPercent / 100.0);
        if (maxDiscount > 0 && discount > maxDiscount) discount = maxDiscount;
        return discount;
    }

    @Override
    public String toString() {
        return "Coupon{code='" + code + "', discountPercent=" + discountPercent + ", active=" + active + "}";
    }
}
