package com.example.fortune_cookies_app.model;

import com.example.fortune_cookies_app.controller.User;

public interface IUserDAO {
    public void createUser(User user);

    public void login(String userName, String password);

    public void updatePassword(User user, String currentPassword, String newPassword);


}
