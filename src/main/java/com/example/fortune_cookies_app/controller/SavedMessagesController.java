package com.example.fortune_cookies_app.controller;


import com.example.fortune_cookies_app.model.AIMessage;
import com.example.fortune_cookies_app.model.AIMessageDAO;
import com.example.fortune_cookies_app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * The SavedMessagesController is responsible for managing the display of saved messages
 * in a ListView and interacting with the underlying data model by utilizing the AIMessageDAO.
 * It provides functionality to fetch and display messages for a specific user and allows setting
 * and getting the current user.
 */
public class SavedMessagesController {

    @FXML
    private ListView<String> savedMessages;
    private User user;
    private final AIMessageDAO MessageDAO = new AIMessageDAO();


    /**
     * Populates the list view with messages fetched from the database for the current user.
     *
     * This method retrieves a list of messages associated with the currently
     * set user by invoking the fetchMessages method of the MessageDAO. Each
     * message in the retrieved list is then added to the `savedMessages`
     * ListView for display purposes.
     *
     * The method assumes that the `user` field has been properly set before
     * being invoked. If no user is set, fetchMessages may return an
     * empty list, resulting in no messages being displayed.
     */
    public void showMessages(){
        List<AIMessage> savedMessagesList = MessageDAO.fetchMessages(user);
        for (AIMessage aiMessage : savedMessagesList) {
            savedMessages.getItems().add(aiMessage.getMessage());
        }
    }

    /**
     * Retrieves the current user.
     *
     * @return the user associated with the SavedMessagesController, or null if no user is set
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the current user for the SavedMessagesController.
     *
     * @param user the User object to be associated with the controller
     */
    public void setUser(User user) {
        this.user = user;
    }
}
