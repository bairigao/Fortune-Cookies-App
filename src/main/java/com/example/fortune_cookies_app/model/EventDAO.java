package com.example.fortune_cookies_app.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * A class for interacting with the Event table in the database
 */
public class EventDAO {
    private Connection connection = null;

    /**
     * Creates a connection with the database and initialises the events table if it does not exist
     */
    public EventDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
    }

    /**
     * Creates the 'events' table in the database if it does not already exist. The table includes
     * columns for event details such as id, user, date, event name, description, and importance.
     *
     * Columns:
     * - id: The primary key, auto-incremented for each event.
     * - user: References the 'id' column in the 'users' table. A foreign key relationship is
     *   established, with cascading deletion.
     * - date: The date of the event, stored as text.
     * - eventName: A non-null name of the event, stored as a variable-length string (VARCHAR).
     * - eventDescription: A detailed, optional description of the event stored as text.
     * - importance: Represents the priority or importance level of the event, stored as an integer.
     *
     * If the table creation fails due to a SQLException, an error message is printed to the standard error stream.
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS events ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user INTEGER NOT NULL,"
                    + "date TEXT NOT NULL,"
                    + "eventName VARCHAR NOT NULL,"
                    + "eventDescription TEXT,"
                    + "importance INTEGER NOT NULL,"
                    + "FOREIGN KEY(user) REFERENCES users(id) ON DELETE CASCADE"
                    + ")";
            statement.execute(query);
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred creating table: " + e.getMessage()); // Should never happen with current implementation
        }
    }

    /**
     * Adds an event to the database
     *
     * @param event Event to be added to the database
     */
    public void createEvent(Event event) {
        String query = "INSERT INTO events (user, date, eventName, eventDescription, importance) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, event.getUserId());
            statement.setString(2, event.getDate().toString());
            statement.setString(3, event.getEventName());
            statement.setString(4, event.getEventDescription());
            statement.setInt(5, event.getImportance());
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            result.next();
            int id = result.getInt(1);
            event.setId(id);
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred creating event: " + e.getMessage()); // Should never happen with current implementation
        }
    }

    /**
     * Deletes an event from the database
     *
     * @param event Event to be deleted
     */
    public void deleteEvent(Event event) {
        String query = "DELETE FROM events WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, event.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred deleting event: " + e.getMessage()); // Should never happen with current implementation
        }
    }

    /**
     * Updates an existing event
     *
     * @param event Event to be updated
     */
    public void updateEvent(Event event) {
        String query = "UPDATE events SET date = ?, eventName = ?, eventDescription = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, event.getDate().toString());
            statement.setString(2, event.getEventName());
            statement.setString(3, event.getEventDescription());
            statement.setInt(4, event.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred updating event: " + e.getMessage()); // Should never happen with current implementation
        }
    }

    /**
     * Fetches events to populate the calendar on login *
     * @param user The user for whom the events are being fetched
     * @return Returns a list of events
     */
    public List<Event> fetchEvents(User user) {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE user = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Event event = new Event(
                        LocalDate.parse(result.getString("date")),
                        result.getString("eventName"),
                        result.getString("eventDescription"),
                        result.getInt("importance"),
                        result.getInt("user"));
                event.setId(result.getInt("id"));
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred fetching events: " + e.getMessage()); // Should never happen with current implementation
        }
        return events;
    }

    /**
     * Fetches all events for a user on a specified day
     * @param EventID The ID of the event to be fetched
     * @return The event with the ID supplied
     */
    public Event fetchSingleEvent(int EventID) {
        String query = "SELECT * FROM events WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, EventID);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Event event = new Event(
                        LocalDate.parse(result.getString("date")),
                        result.getString("eventName"),
                        result.getString("eventDescription"),
                        result.getInt("importance"),
                        result.getInt("user"));
                event.setId(result.getInt("id"));
                return event;
            }
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred fetching event: " + e.getMessage()); // Should never happen with current implementation
        }
        return null;
    }

    /**
     * @param user User whose events are being retrieved
     * @param date The dates of the events
     * @return A list of the events for the user and day supplied
     */
    public List<Event> fetchEventsDay(User user, LocalDate date) {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE user = ? AND date = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            statement.setString(2, date.toString());
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Event event = new Event(
                        LocalDate.parse(result.getString("date")),
                        result.getString("eventName"),
                        result.getString("eventDescription"),
                        result.getInt("importance"),
                        result.getInt("user"));
                event.setId(result.getInt("id"));
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred fetching events: " + e.getMessage()); // Should never happen with current implementation
        }
        return events;
    }

    /**
     * Clears all events for a specified user - main used for testing
     * @param userID The user whose event are being cleared
     */
    public void clearEvents(int userID) {
        String query = "DELETE FROM events WHERE user = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Unexpected error occurred clearing events: " + e.getMessage()); // Should never happen with current implementation
        }
    }
}
