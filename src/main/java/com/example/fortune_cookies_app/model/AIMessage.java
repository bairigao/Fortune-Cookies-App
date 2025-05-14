package com.example.fortune_cookies_app.model;

public class AIMessage {
    private int ID;
    private int userID;
    private String message;

    public AIMessage(int userID, String message){
        this.userID = userID;
        this.message = message;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserID() {
        return userID;
    }

    public String getMessage() {
        return message;
    }

}
