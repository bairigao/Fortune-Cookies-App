package com.example.fortune_cookies_app.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates a connection so that we can interact with the database
 */
public class SqliteConnection {
    private static Connection instance = null;

    private SqliteConnection() {
        String url = "jdbc:sqlite:database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    /**
     * @return A connection instance with the databse
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }
}