package com.tap.model;

import java.sql.Timestamp;

public class OTP {
    private int otpId;
    private String email;
    private String otpCode;
    private String purpose;  // REGISTER, LOGIN, FORGOT_PASSWORD
    private boolean used;
    private Timestamp createdAt;
    private Timestamp expiresAt;

    public OTP() {}

    public OTP(String email, String otpCode, String purpose, Timestamp expiresAt) {
        this.email = email;
        this.otpCode = otpCode;
        this.purpose = purpose;
        this.expiresAt = expiresAt;
    }

    public int getOtpId() { return otpId; }
    public void setOtpId(int otpId) { this.otpId = otpId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Timestamp expiresAt) { this.expiresAt = expiresAt; }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.before(new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public String toString() {
        return "OTP{email='" + email + "', otpCode='" + otpCode + "', purpose='" + purpose +
                "', used=" + used + "}";
    }
}
