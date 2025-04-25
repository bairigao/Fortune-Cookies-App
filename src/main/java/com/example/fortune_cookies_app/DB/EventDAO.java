package com.example.fortune_cookies_app.DB;

import com.example.fortune_cookies_app.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class EventDAO {
    private Connection connection = null;

    public EventDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS events ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user KEY NOT NULL,"
                    + "date TEXT NOT NULL,"
                    + "eventName VARCHAR NOT NULL,"
                    + "eventDescription TEXT,"
                    + "FOREIGN KEY(user) REFERENCES users(id)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createEvent(Event event) {
        String query = "INSERT INTO events (user, date, eventName, eventDescription) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, event.getUserId());
            statement.setString(2, event.getDate().toString());
            statement.setString(3, event.getEventName());
            statement.setString(4, event.getEventDescription());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(Event event) {
        String query = "DELETE FROM events WHERE id = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "eventidgoeshere");
            statement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateEvent(Event event) {
        String query = "UPDATE events SET (date, eventName, eventDescription) VALUES (?, ?, ?) WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, event.getDate().toString());
            statement.setString(1, event.getEventName());
            statement.setString(1, event.getEventDescription());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}