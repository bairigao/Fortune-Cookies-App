package com.example.fortune_cookies_app.model;


import java.time.LocalDate;

/**
 * A class to represent a user
 */
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate lastLogin;
    private int id;
    private int loginStreak;

    public User(String firstName, String lastName, String email, String password, int loginStreak) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.lastLogin = LocalDate.now();
        this.loginStreak = loginStreak;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoginStreak() {
        return loginStreak;
    }


    /**
     *
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
