package com.example.fortune_cookies_app.model;

public class AuthValidator {
    public static boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    public static boolean areSignupFieldsValid(String fName, String lName, String email, String password, String confirmPassword) {
        return !(fName.trim().isEmpty() ||
                lName.trim().isEmpty() ||
                email.trim().isEmpty() ||
                password.trim().isEmpty() ||
                confirmPassword.trim().isEmpty());
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && email.contains("@") && email.contains(".");
    }

    public static boolean areLoginFieldsValid(String email, String password) {
        return !isEmpty(email) && !isEmpty(password);
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
