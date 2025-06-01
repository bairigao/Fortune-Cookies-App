package com.example.fortune_cookies_app.model;
import java.sql.Connection;

/**
 * Abstract class for data access objects
 */
public abstract class AbstractDAO {

    protected Connection connection;


    /**
     * Initialises SQL connection and creates initial table if it does not exist
     */
    public AbstractDAO(){
        this.connection = SqliteConnection.getInstance();
        createTable();
    }

    /**
     * Creates the initial table if it does not exist
     */
    protected abstract void createTable();
}
