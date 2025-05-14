package com.example.fortune_cookies_app.controller;


import com.example.fortune_cookies_app.model.AIMessage;
import com.example.fortune_cookies_app.model.AIMessageDAO;
import com.example.fortune_cookies_app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * Responsible for populating the list view with the messages that the user has saved
 */
public class SavedMessagesController {

    @FXML
    private ListView<String> savedMessages;
    private User user;
    private final AIMessageDAO MessageDAO = new AIMessageDAO();


    public void showMessages(){
        List<AIMessage> savedMessagesList = MessageDAO.fetchMessages(user);
        for (AIMessage aiMessage : savedMessagesList) {
            savedMessages.getItems().add(aiMessage.getMessage());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
