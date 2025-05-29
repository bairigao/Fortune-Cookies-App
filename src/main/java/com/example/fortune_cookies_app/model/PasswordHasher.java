package com.example.fortune_cookies_app.model;
import java.security.*;
import java.util.HexFormat;

/**
 * Does what it says - hashes passwords. SHA-512 Encryption
 */
public class PasswordHasher {
    /**
     * @param password User's password as plaintext
     * @return User's hashed password for storage/comparison in database
     * @throws NoSuchAlgorithmException Should not throw this error - just don't change the algorithm
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(password.getBytes());
        return HexFormat.of().formatHex(hash);
    }
}
