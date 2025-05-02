package com.example.fortune_cookies_app.model;

import java.util.regex.Pattern;

/**
 * Utility class for validating user input for authentication and registration.
 * Provides static methods to check if inputs are non-empty and match expected patterns.
 */
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


    /**
     * Validates that all sign-up fields are non-empty.
     *
     * @param fName           user's first name
     * @param lName           user's last name
     * @param email           user's email address
     * @param password        user's password
     * @param confirmPassword password confirmation
     * @return true if all fields are filled; false otherwise
     */
    public static boolean areSignupFieldsValid(String fName, String lName, String email, String password, String confirmPassword) {
        return !(fName.trim().isEmpty() ||
                lName.trim().isEmpty() ||
                email.trim().isEmpty() ||
                password.trim().isEmpty() ||
                confirmPassword.trim().isEmpty());
    }

    /**
     * Checks if the name is valid (2-50 alphabetic characters).
     *
     * @param name the name to validate
     * @return true if valid; false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Validates the email format.
     *
     * @param email the email to validate
     * @return true if the email format is valid; false otherwise
     */
    public static boolean isValidEmail(String email) {
        return isNotEmptyString(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates that login fields are non-empty.
     *
     * @param email    user's email
     * @param password user's password
     * @return true if both fields are filled; false otherwise
     */
    public static boolean areLoginFieldsValid(String email, String password) {
        return isNotEmptyString(email) && isNotEmptyString(password);
    }

    /**
     * Checks if the password meets strength requirements:
     * minimum 8 characters, with uppercase, lowercase, digit, and special character.
     *
     * @param password the password to validate
     * @return true if password is strong; false otherwise
     */
    public static boolean isStrongPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Checks if the password and confirmation match.
     *
     * @param password        the password
     * @param confirmPassword the confirmation
     * @return true if both match; false otherwise
     */
    public static boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    private static boolean isNotEmptyString(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
