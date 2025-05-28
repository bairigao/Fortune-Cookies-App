package com.example.fortune_cookies_app.model;


import java.time.LocalDate;

/**
 * Represents a user within the system, including personal details, security credentials,
 * and activity tracking information such as login streaks and last login dates.
 */
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private LocalDate lastLogin;
    private int id;
    private int loginStreak;

    /**
     * Constructs a new User with the specified details.
     *
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email address of the user
     * @param password The password for the user's account
     * @param securityQuestion The security question associated with the user's account
     * @param securityAnswer The answer to the security question
     * @param loginStreak The current login streak of the user
     */
    public User(String firstName, String lastName, String email, String password,
                String securityQuestion, String securityAnswer, int loginStreak) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.lastLogin = LocalDate.now();
        this.loginStreak = loginStreak;
    }


    /**
     * Retrieves the first name of the user.
     *
     * @return the first name of the user as a String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to be assigned to the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the last login date of the user.
     *
     * @return the last login date as a LocalDate
     */
    public LocalDate getLastLogin() {
        return lastLogin;
    }

    /**
     * Updates the last login date of the user.
     *
     * @param lastLogin the date of the last login to be set, as a LocalDate
     */
    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Retrieves the password associated with the user.
     *
     * @return the password of the user as a String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the password for the user.
     *
     * @param password the new password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return the last name of the user as a String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to be assigned to the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the email address associated with the user.
     *
     * @return the email address of the user as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email address of the user.
     *
     * @param email the new email address to be set for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the security question associated with the user's account.
     *
     * @return the security question as a String
     */
    public String getSecurityQuestion() { return securityQuestion; }

    /**
     * Retrieves the security answer associated with the user's account.
     *
     * @return the security answer as a String
     */
    public String getSecurityAnswer() { return securityAnswer; }

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the ID of the user as an integer
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the unique identifier to be assigned to the user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the current login streak of the user.
     *
     * @return the login streak of the user as an integer
     */
    public int getLoginStreak() {
        return loginStreak;
    }

    /**
     * Updates the current login streak for the user.
     *
     * @param loginStreak the new login streak value to be set
     */
    public void setLoginStreak(int loginStreak) {
        this.loginStreak = loginStreak;
    }

    /**
     * Updates the user's login streak based on the last login date.
     * The lastLogin field is updated to the current date after the logic is applied.
     */
    public void trackLogin() {
        LocalDate today = LocalDate.now();
        if (lastLogin != null) {
            if (lastLogin.plusDays(1).equals(today)) {
                loginStreak += 1;
            } else if (!lastLogin.equals(today)) {
                loginStreak = 1;
            }
        } else {
            loginStreak = 1;
        }
        lastLogin = today;
    }


}
