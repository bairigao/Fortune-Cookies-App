package com.example.fortune_cookies_app.DB;

import com.example.fortune_cookies_app.User;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;
import java.time.LocalDate;

public class UserDAO {
    private final Connection connection;

    public UserDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "firstName VARCHAR NOT NULL,"
                    + "lastName VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL UNIQUE,"
                    + "password VARCHAR NOT NULL,"
                    + "lastLogin DATE NOT NULL,"
                    + "loginStreak VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUser(User user) {
        String query = "INSERT INTO users (firstName, lastName, email, password, lastLogin, loginStreak) VALUES (?, ?, ?, ?, ?, 0)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, String.valueOf(LocalDate.now()));
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean login(TextField email, PasswordField password) {
        String query = "SELECT password FROM users WHERE email = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email.getText());
            ResultSet result = statement.executeQuery();
            if (result.next()){
                String storedPassword = result.getString("password");
                String enteredPassword = password.getText();

                // Compare the entered password with the stored password
                return enteredPassword.equals(storedPassword);
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void updatePassword(User user, String currentPassword, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        try {
            if (currentPassword.equals(newPassword))
                throw new IllegalArgumentException("Your new password cannot be the same as your current password.");
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setString(2, user.getFirstName());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
