package com.example.fortune_cookies_app.DB;

import com.example.fortune_cookies_app.Event;
import com.example.fortune_cookies_app.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private Connection connection = null;

    public EventDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
    }

    /**
     * Initialises the table within the database - only called within DAO constructor.
     */
    private void createTable() {
        // Create table if not exists
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an event to the database
     * @param event Event to be added to the database
     */
    public void createEvent(Event event) {
        String query = "INSERT INTO events (user, date, eventName, eventDescription, importance) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, event.getUserId());
            statement.setString(2, event.getDate().toString());
            statement.setString(3, event.getEventName());
            statement.setString(4, event.getEventDescription());
            statement.setInt(5, event.getImportance());
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            result.next();
            int id = result.getInt(1);
            event.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an event from the database
     * @param event Event to be deleted
     */
    public void deleteEvent(Event event) {
        String query = "DELETE FROM events WHERE id = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, event.getId());
            statement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing event
     * @param event Event to be updated
     */
    public void updateEvent(Event event) {
        String query = "UPDATE events SET (date, eventName, eventDescription) VALUES (?, ?, ?) WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, event.getDate().toString());
            statement.setString(2, event.getEventName());
            statement.setString(3, event.getEventDescription());
            statement.setInt(4, event.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches events to populate the calendar on login
     * @param user The user for whom the events are being fetched
     * @return Returns a list of events
     */
    public List<Event> fetchEvents(User user){
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE user = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            ResultSet result = statement.executeQuery();
            while (result.next()){
                Event event = new Event(
                        LocalDate.parse(result.getString("date")),
                        result.getString("eventName"),
                        result.getString("eventDescription"),
                        result.getInt("importance"),
                        result.getInt("user"));
                event.setId(result.getInt("id"));
                events.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    return events;
    }
}