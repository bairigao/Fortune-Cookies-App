package com.example.fortune_cookies_app.model;

/**
 * Represents a message in the system created by a user.
 * The class stores the message content, its associated user,
 * and an ID for the message itself.
 */
public class AIMessage {
    private int ID;
    private int userID;
    private String message;

    /**
     * Constructs an AIMessage instance with the specified user ID and message content.
     *
     * @param userID The unique identifier of the user associated with the message.
     * @param message The content of the message created by the user.
     */
    public AIMessage(int userID, String message){
        this.userID = userID;
        this.message = message;
    }

    /**
     * Retrieves the unique identifier of the message.
     *
     * @return The unique ID of the message.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique identifier for the message.
     *
     * @param ID The unique identifier to be assigned to the message.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Retrieves the unique identifier of the user associated with this message.
     *
     * @return The unique ID of the user who created the message.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Retrieves the content of the message.
     *
     * @return The content of the message as a String.
     */
    public String getMessage() {
        return message;
    }

}
