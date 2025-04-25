package com.example.fortune_cookies_app;

import java.time.LocalDate;

public class Event {
    private int id;
    private LocalDate date;
    private String eventName;
    private String eventDescription = null;
    private int importance;
    private final int userId;

    public Event(LocalDate date, String eventName, String eventDescription, int importance, int userId){
        this.date = date;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.importance = importance;
        this.userId = userId;
    }
    public Event(LocalDate date, String eventName, int importance, int userId){
        this.date = date;
        this.eventName = eventName;
        this.importance = importance;
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        if (this.eventDescription == null) return "";
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getUserId() {
        return String.valueOf(userId);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
