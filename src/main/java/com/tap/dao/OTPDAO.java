package com.tap.dao;

import com.tap.model.OTP;

public interface OTPDAO {
    boolean saveOTP(OTP otp);
    OTP getLatestOTP(String email, String purpose);
    boolean markUsed(String email, String otpCode);
    void invalidateOldOTPs(String email, String purpose);
}
