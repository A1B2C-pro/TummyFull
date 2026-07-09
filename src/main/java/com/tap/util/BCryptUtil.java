package com.tap.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Wraps jBCrypt for password hashing / verification.
 * jbcrypt-0.4.jar must be present in WEB-INF/lib.
 */
public class BCryptUtil {

    private static final int SALT_ROUNDS = 10;

    private BCryptUtil() {}

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(SALT_ROUNDS));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) return false;
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // hashedPassword was not a valid bcrypt hash
            return false;
        }
    }
}
