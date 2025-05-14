package com.example.fortune_cookies_app.model;

import java.sql.*;
import java.time.LocalDate;

/**
 * A class for interacting with the User table in the database
 */
public class UserDAO implements IUserDAO {
    private final Connection connection;

    /**
     * Creates a connection with the database and initialises the users table if it does not exist
     */
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
                    + "loginStreak INTEGER NOT NULL DEFAULT 1,"
                    + "secureQuestion TEXT NOT NULL,"
                    + "secureAnswer TEXT NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new user record in the database.
     * Sets the user's ID based on the generated database ID.
     *
     * @param user User object containing all required user information to be stored
     */
    @Override
    public void createUser(User user) {
        String query = "INSERT INTO users (firstName, lastName, email, password, lastLogin, loginStreak, secureQuestion, secureAnswer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, LocalDate.now().toString());
            statement.setInt(6, 1);
            statement.setString(7, user.getSecurityQuestion());
            statement.setString(8, user.getSecurityAnswer());
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
     * Authenticates a user based on email and password.
     * If authentication is successful, returns a User object with user information.
     *
     * @param email    Email address used to identify the user
     * @param password Password to verify user's identity
     * @return User object if authentication is successful, null otherwise
     */
    @Override
    public User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("secureQuestion"),
                        resultSet.getString("secureAnswer"),
                        resultSet.getInt("loginStreak")
                );
                user.setId(resultSet.getInt("id"));
                String lastLoginStr = resultSet.getString("lastLogin");
                if (lastLoginStr != null && !lastLoginStr.isEmpty()) {
                    user.setLastLogin(LocalDate.parse(lastLoginStr));
                }
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Updates the user's password
     *
     * @param user            The user whose password is being updated
     * @param newPassword     The user's new password
     */
    @Override
    public void updatePassword(User user, String newPassword) {
        if (user.getPassword().equals(newPassword)) {
            throw new IllegalArgumentException("Your new password cannot be the same as your current password.");
        }

        String query = "UPDATE users SET password = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            String hashedPwd = PasswordHasher.hashPassword(newPassword);
            statement.setString(1, hashedPwd);
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Updates the user's login streak and last login date in the database.
     * This method should be called after a successful login to maintain
     * the user's consecutive login records.
     *
     * @param user The user whose login streak and last login date need to be updated
     */
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

    /**
     * Checks if an email address already exists in the database.
     * Useful for preventing duplicate user registrations.
     *
     * @param email The email address to check
     * @return true if the email already exists in the database, false otherwise
     */
    @Override
    public boolean checkEmail(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("secureQuestion"),
                            resultSet.getString("secureAnswer"),
                            resultSet.getInt("loginStreak")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Use logger in real apps
        }
        return null;
    }

}
