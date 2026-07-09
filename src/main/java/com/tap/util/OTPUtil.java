package com.tap.util;

import java.security.SecureRandom;

/**
 * Generates numeric OTP codes.
 */
public class OTPUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    public static final int OTP_LENGTH = 6;
    public static final int OTP_VALIDITY_MINUTES = 5;

    private OTPUtil() {}

    public static String generateOTP() {
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
