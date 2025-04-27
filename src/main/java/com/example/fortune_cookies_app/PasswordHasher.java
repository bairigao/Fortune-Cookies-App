package com.example.fortune_cookies_app;
import java.security.*;
import java.util.HexFormat;

public class PasswordHasher {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(password.getBytes());
        return HexFormat.of().formatHex(hash);
    }
}
