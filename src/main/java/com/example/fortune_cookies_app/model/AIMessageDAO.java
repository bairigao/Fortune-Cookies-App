package com.example.fortune_cookies_app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The AIMessageDAO class is responsible for managing the persistence of AIMessage objects in a database.
 * It provides methods to save, delete, and retrieve AI messages. The class also ensures that the
 * necessary database table is created if it does not already exist upon initialization.
 */
public class AIMessageDAO {
    private final Connection connection;

    /**
     * Creates a connection with the database and initialises the users table if it does not exist
     */
    public AIMessageDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
    }


    /**
     * Initialises table within the database - only called within constructor.
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS messages ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user INTEGER NOT NULL,"
                    + "message TEXT NOT NULL,"
                    + "FOREIGN KEY(user) REFERENCES users(id) ON DELETE CASCADE,"
                    + "UNIQUE(user, message)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            System.err.println("Unexpected error occurred creating table: " + e.getMessage()); // Should never happen with current implementation
        }
    }

    /**
     * Saves the provided AIMessage to the database. The method first checks if the message
     * already exists in the database using the checkMessage method. If the message is not
     * already present, it inserts the message into the messages table and assigns a generated
     * ID to the message.
     *
     * @param message The AIMessage object containing the user ID and message content
     *                to be saved in the database.
     */
    public void saveMessage(AIMessage message) {
        if (checkMessage(message)) {
            System.out.println("You've already saved that message.");
            return;
        }
        String query = "INSERT INTO messages (user, message) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, message.getUserID());
            statement.setString(2, message.getMessage());
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                message.setID(result.getInt(1));
            }
        } catch (Exception e) {
            System.err.println("Unexpected error occurred saving message: " + e.getMessage()); // Should never happen with current implementation
        }
    }

    public void deleteMessage(AIMessage message) {
        String query = "DELETE FROM messages WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, message.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred deleting message: " + e.getMessage()); // Should never happen with current implementation
        }
    }

    /**
     * Fetches all messages associated with the specified user from the database.
     *
     * @param user The user whose messages are to be retrieved. The user's ID is used
     *             to query the database for the messages.
     * @return A list of {@code AIMessage} objects associated with the given user.
     *         If no messages are found, an empty list is returned.
     */
    public List<AIMessage> fetchMessages(User user) {
        List<AIMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM messages WHERE user = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                AIMessage message = new AIMessage(
                        result.getInt("user"),
                        result.getString("message"));
                message.setID(result.getInt("id"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred fetching messages: " + e.getMessage()); // Should never happen with current implementation
        }
        return messages;
    }

    /**
     * Checks if a given message exists in the database for a specific user.
     *
     * @param message The AIMessage object containing the user ID and message content
     *                to be checked in the database.
     * @return true if the message exists in the database, false otherwise.
     */
    public boolean checkMessage(AIMessage message){
        String selectquery = "SELECT * FROM messages WHERE user = ? AND message = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(selectquery);
            statement.setInt(1, message.getUserID());
            statement.setString(2, message.getMessage());
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (Exception e) {
            System.err.println("Unexpected error occurred saving message: " + e.getMessage()); // Should never happen with current implementation
            return false;
        }
    }
}
