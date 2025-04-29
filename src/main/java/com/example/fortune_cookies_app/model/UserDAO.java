package com.example.fortune_cookies_app.model;

import java.sql.*;
import java.time.LocalDate;

public class UserDAO implements IUserDAO{
    private Connection connection;

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
                    + "loginStreak INTEGER NOT NULL DEFAULT 1"
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
    @Override
    public void createUser(User user) {
        String query = "INSERT INTO users (firstName, lastName, email, password, lastLogin, loginStreak) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, LocalDate.now().toString());
            statement.setInt(6, 1);
            statement.executeUpdate();
            // set the id for new user
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                user.setId(result.getInt(1));
            }
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
    @Override
    public User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getInt("loginStreak")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * This method is used to pass the user object to the calendar upon login
     * @param email Email used to fetch user information from database
     * @return User object - passed to other methods to populate calendar & create events
     * @throws SQLException Unlikely to error due to only being executed after a successful login
     */
    @Override
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                User user = new User(
                        result.getString("firstName"),
                        result.getString("lastName"),
                        email,
                        result.getString("lastLogin"),
                        result.getInt("loginStreak"));
                user.setId(result.getInt("id"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    /**
     * Updates the user's password
     * @param user The user whose password is being updated
     * @param currentPassword User's current password - must match password in database
     * @param newPassword The user's new password
     */
    @Override
    public void updatePassword(User user, String currentPassword, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        if (currentPassword.equals(newPassword)) throw new IllegalArgumentException("Your new password cannot be the same as your current password.");
        if (login(user.getEmail(), currentPassword) == null) throw new IllegalArgumentException("Current password incorrect. Please try again.");
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateStreak(User user) {
        String query = "UPDATE users SET loginStreak = ?, lastLogin = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getLoginStreak());
            statement.setString(2, user.getLastLogin().toString());
            statement.setInt(3, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
