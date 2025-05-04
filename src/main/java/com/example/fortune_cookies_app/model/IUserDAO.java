package com.example.fortune_cookies_app.model;

/**
 * Interface to define the user class
 */
public interface IUserDAO {
    void createUser(User user);

    User login(String userName, String password);

    void updatePassword(User user, String currentPassword, String newPassword);

    void updateStreak(User user);

    boolean checkEmail(String email);
}
