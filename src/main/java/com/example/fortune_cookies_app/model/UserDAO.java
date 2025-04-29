package com.example.fortune_cookies_app.model;

import java.sql.*;
import java.time.LocalDate;

public class UserDAO {
    private final Connection connection;

    public UserDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
    }

    /**
     * Initialises table within the database - only called within constructor.
     */
    private void createTable() {
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

    /**
     * Adds a user to the database
     * @param user User to be added to the database
     */
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
            ResultSet result = statement.getGeneratedKeys();
            result.next();
            int id = result.getInt(1);
            user.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the user in if email and password match the database
     * @param email Email to used to look up user
     * @param password Password crosschecked with database to check validity
     * @return True if passwords match, false otherwise
     */
    public boolean login(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String storedPassword = result.getString("password");

                // Compare the entered password with the stored password
                return password.equals(storedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * This method is used to pass the user object to the calendar upon login
     * @param email Email used to fetch user information from database
     * @return User object - passed to other methods to populate calendar & create events
     * @throws SQLException Unlikely to error due to only being executed after a successful login
     */
    public User getUser(String email) throws SQLException {
        String query = "SELECT id, firstName, lastName, lastLogin, loginStreak FROM users WHERE email = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet result = statement.executeQuery();
        result.next();
        // Create & return user
        User user = new User(
                result.getString("firstName"),
                result.getString("lastName"),
                email,
                result.getString("lastLogin"),
                result.getInt("loginStreak"));
        user.setId(result.getInt("id"));
        if (user.getLastLogin().equals(LocalDate.now().minusDays(1))) {
            updateStreak(user, user.getLoginStreak() + 1);
        } else if (user.getLastLogin().isBefore(LocalDate.now().minusDays(1))){
            updateStreak(user, 1);
        }
        return user;
    }


    /**
     * Updates the user's password
     * @param user The user whose password is being updated
     * @param currentPassword User's current password - must match password in database
     * @param newPassword The user's new password
     */
    public void updatePassword(User user, String currentPassword, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        if (currentPassword.equals(newPassword)) throw new IllegalArgumentException("Your new password cannot be the same as your current password.");
        if (!login(user.getEmail(), currentPassword)) throw new IllegalArgumentException("Current password incorrect. Please try again.");
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateStreak(User user, int streak) {
        String query = "UPDATE users SET loginStreak = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, streak);
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
