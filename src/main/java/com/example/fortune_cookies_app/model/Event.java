package com.example.fortune_cookies_app.model;

import java.time.LocalDate;

/**
 * Represents an Event with details such as event date, name, description, importance,
 * and associated user ID.
 */
public class Event {
    private int id;
    private LocalDate date;
    private String eventName;
    private String eventDescription = null;
    private int importance;
    private final int userId;

    /**
     * @param date Date on which the event takes place
     * @param eventName Name of the event
     * @param eventDescription Description of the event
     * @param importance The importance of the event
     * @param userId The userID for the user
     */
    public Event(LocalDate date, String eventName, String eventDescription, int importance, int userId){
        this.date = date;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.importance = importance;
        this.userId = userId;
    }

    /**
     * @param date Date on which the event takes place
     * @param eventName Name of the event
     * @param importance The importance of the event
     * @param userId The userID for the user
     */
    public Event(LocalDate date, String eventName, int importance, int userId){
        this.date = date;
        this.eventName = eventName;
        this.importance = importance;
        this.userId = userId;
    }

    /**
     * Retrieves the date of the event.
     *
     * @return the date on which the event takes place
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date for the event.
     *
     * @param date the date on which the event takes place
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Retrieves the name of the event.
     *
     * @return the event name as a String
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the event.
     *
     * @param eventName the name of the event to be set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Retrieves the description of the event. If the description is not set, it returns an empty string.
     *
     * @return the description of the event as a String, or an empty string if the description is null
     */
    public String getEventDescription() {
        if (this.eventDescription == null) return "";
        return eventDescription;
    }

    /**
     * Updates the description of the event.
     *
     * @param eventDescription the description of the event to be set
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Retrieves the importance level of the event.
     *
     * @return the importance of the event as an integer
     */
    public int getImportance() {
        return importance;
    }

    /**
     * Sets the importance level of the event.
     *
     * @param importance the importance value to be assigned to the event
     */
    public void setImportance(int importance) {
        this.importance = importance;
    }

    /**
     * Retrieves the user ID associated with the event.
     *
     * @return the user ID as an integer
     */
    public int getUserId() { return this.userId; }

    /**
     * Sets the ID of the event.
     *
     * @param id the unique identifier to be assigned to the event
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the unique identifier for the event.
     *
     * @return the ID of the event as an integer
     */
    public int getId() {
        return id;
    }
}
