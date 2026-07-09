package com.tap.dao;

import com.tap.model.Coupon;
import java.util.List;

public interface CouponDAO {
    List<Coupon> getAllCoupons();
    Coupon getCouponByCode(String code);
    boolean addCoupon(Coupon coupon);
    boolean updateCoupon(Coupon coupon);
    boolean deleteCoupon(int couponId);
}
