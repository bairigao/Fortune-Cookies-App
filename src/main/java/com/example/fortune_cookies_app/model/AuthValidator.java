package com.example.fortune_cookies_app.model;

import java.util.regex.Pattern;

public class AuthValidator {

    private static final String NAME_REGEX = "^[A-Za-z]{2,50}$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private static final String EMAIL_REGEX =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
            "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);


    public static boolean areSignupFieldsValid(String fName, String lName, String email, String password, String confirmPassword) {
        return !(fName.trim().isEmpty() ||
                lName.trim().isEmpty() ||
                email.trim().isEmpty() ||
                password.trim().isEmpty() ||
                confirmPassword.trim().isEmpty());
    }

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidEmail(String email) {
        return isEmptyString(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean areLoginFieldsValid(String email, String password) {
        return isEmptyString(email) && isEmptyString(password);
    }

    public static boolean isStrongPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    private static boolean isEmptyString(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
