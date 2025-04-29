package com.example.fortune_cookies_app.model;

public class AuthValidator {
    public boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    public boolean areSignupFieldsValid(String firstName, String lastName, String email, String password, String confirmPassword) {
        return !isEmpty(firstName) && !isEmpty(lastName) && !isEmpty(email)
                && !isEmpty(password) && !isEmpty(confirmPassword)
                && isPasswordConfirmed(password, confirmPassword)
                && isValidEmail(email);
    }

    public boolean isValidEmail(String email) {
        return !isEmpty(email) && email.contains("@") && email.contains(".");
    }

    public boolean areLoginFieldsValid(String email, String password) {
        return !isEmpty(email) && !isEmpty(password);
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
