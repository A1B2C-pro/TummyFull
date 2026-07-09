package com.tap.util;

/**
 * Centralized session attribute key names to avoid typos scattered
 * across servlets and JSPs.
 */
public class SessionConstants {
    private SessionConstants() {}

    public static final String CUSTOMER_USER = "loggedInUser";     // com.tap.model.User
    public static final String ADMIN_USER = "loggedInAdmin";       // com.tap.model.Admin
    public static final String AGENT_USER = "loggedInAgent";       // com.tap.model.DeliveryAgent

    public static final String ROLE = "role"; // CUSTOMER / ADMIN / AGENT

    public static final String CART = "cart"; // com.tap.model.Cart

    public static final String PENDING_OTP_EMAIL = "pendingOtpEmail";
    public static final String PENDING_OTP_PURPOSE = "pendingOtpPurpose";
    public static final String PENDING_REGISTRATION = "pendingRegistration"; // temp holder map

    public static final String CHECKOUT_FINAL_TOTAL = "checkoutFinalTotal";
    public static final String APPLIED_COUPON = "appliedCoupon";

    public static final String ERROR_MSG = "errorMsg";
    public static final String SUCCESS_MSG = "successMsg";
}
