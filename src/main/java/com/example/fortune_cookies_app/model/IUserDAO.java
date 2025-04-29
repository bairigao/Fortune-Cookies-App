package com.example.fortune_cookies_app.model;

public interface IUserDAO {
    public void createUser(User user);

    public void login(String userName, String password);

    public void updatePassword(User user, String currentPassword, String newPassword);


}
