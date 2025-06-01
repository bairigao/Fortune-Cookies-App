package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

/**
 * DefaultSidebarController class manages the sidebar functionality within the application.
 *
 * This class is responsible for controlling various UI components of the sidebar, interacting with
 * user data, events, and AI-generated content such as daily motivational messages and study tips.
 * The controller facilitates event loading, user interaction, and AI-based responses to enhance
 * user experience.
 *
 * Key Features:
 * - Displaying user-specific events within the sidebar.
 * - Generating and displaying AI-generated motivational messages and study tips.
 * - Handling user input and button interactions.
 * - Managing application state pertaining to the sidebar.
 */
public class DefaultSidebarController {

    /**
     * Represents a button in the sidebar interface used to save an AI-generated message.
     *
     * The button is associated with the functionality provided by the onSaveMessageClick()
     * method of the containing class. When clicked, it triggers the saving of the current
     * AI-generated message for the user to a persistent storage system using the MessageDAO.
     *
     * Purpose:
     * - Provides a UI element that allows users to save important messages conveniently.
     * - Interacts with other components of the sidebar to handle and store user-specific data.
     *
     * Integration Notes:
     * - This button is expected to interact with user data, message content, and database operations
     *   via the containing DefaultSidebarController class.
     * - Proper initialization and event handling must be ensured for its functionality.
     */
    public Button saveMessage;
    @FXML
    private VBox eventsList;
    @FXML
    private TextArea aiDailyMessage;
    @FXML
    private TextArea aiStudyTips;
    @FXML
    private Label streakLabel;


    private final EventDAO eventDAO = new EventDAO();
    private final AIMessageDAO MessageDAO = new AIMessageDAO();
    private String currentMessage;
    private User user;
    private YearMonth currentMonth;
    /**
     * Sets the current user and populate/refreshes the calendar
     * @param user - user
     */
    public void setUser(User user) {
        this.user = user;
        tryLoadEvents();
        updateStreakText();
    }

    /**
     * Updates the displayed login streak text based on the user's current login streak.
     *
     * The method retrieves the login streak value from the user object and updates the
     * streakLabel with a corresponding message. The message displayed adjusts based on
     * whether the streak represents a single day or multiple days.
     *
     * Behavior:
     * - If the streak is equal to 1, the label text indicates "LOGGED IN FOR: 1 DAY!".
     * - Otherwise, the label text indicates "LOGGED IN FOR: [streak] DAYS!" where
     *   [streak] is the value of the user's login streak.
     */
    private void updateStreakText() {
        int streak = user.getLoginStreak();
        if (streak == 1){
            streakLabel.setText("LOGGED IN FOR:" + " " + streak + " " + "DAY!");
        } else streakLabel.setText("LOGGED IN FOR:" + " " + streak + " " + "DAYS!");
    }

    /**
     * Sets the current month of the sidebar data so correct events can be pulled
     * @param currentMonth The current month
     */
    public void setCurrentMonth(YearMonth currentMonth) {
        this.currentMonth = currentMonth;
        tryLoadEvents();
    }

    /**
     * Attempts to load events, AI daily messages, and AI study tips for the sidebar.
     *
     * This method checks whether all required components (user, the current month,
     * and the events list) are available. If they are not null, it triggers the following actions:
     *
     * - Loads the user's events for the current month into the sidebar.
     * - Generates and loads an AI-generated daily message based on the next upcoming event.
     * - Generates and loads a list of AI-generated study tips for the user.
     *
     * Preconditions:
     * - A valid user must be set in the class before this method is called.
     * - The current month and events list must be properly initialized for the method to function.
     */
    private void tryLoadEvents() {
        if (user != null && currentMonth != null && eventsList != null) {
            loadEvents();
            loadAIDailyMessage();
            loadAIStudyTips();
        }
    }

    /**
     * loads all the user events into the sidebar and includes the title
     * as additional information including the importance colour.
     */
    public void loadEvents(){
        if (user == null || currentMonth ==null)
            return;
        eventsList.getChildren().clear();

        List<Event> events = eventDAO.fetchEvents(user);
        for (Event event: events) {
            LocalDate eventDate = event.getDate();
            YearMonth eventMonth = YearMonth.from(eventDate);

            if(eventMonth.equals(currentMonth)) {
                HBox eventItem = new HBox(8);
                eventItem.setPrefHeight(24);
                Region circle = new Region();
                circle.setStyle("-fx-background-color: " + getImportanceColour(event.getImportance()) + "; -fx-background-radius: 50%;");
                circle.setMinSize(20, 20);
                circle.setPrefSize(20, 20);
                circle.setMaxSize(20, 20);

                Label eventTitle = new Label(event.getEventName());
                eventTitle.setStyle("-fx-font-size: 14;");
                eventItem.getChildren().addAll(circle, eventTitle);
                eventsList.getChildren().add(eventItem);

            }
        }


    }


    private String getImportanceColour(int level) {
        return switch (level) {
            case 1 -> "#bbdefb";
            case 2 -> "#c8e6c9";
            case 3 -> "#fff9c4";
            case 4 -> "#e1bee7";
            case 5 -> "#ffcdd2";
            default -> "lightgrey";
        };
    }

    private static String sessionDailyMessage = null;
    /**
     *  Loads the AI daily message based on the next upcoming event and updates the TextArea
     *  with a daily motivational message generated by the AI.
     * <p>
     * The prompt instructs the AI to generate a short, uplifting daily message for the next event
     * without any follow-up questions or multiple options.
     * </p>
     */
    private void loadAIDailyMessage() {
        if (sessionDailyMessage != null) {
            aiDailyMessage.setText(sessionDailyMessage);
            return;
        }

        LocalDate today = LocalDate.now();

        eventDAO.fetchEvents(user).stream()
                .filter(e -> !e.getDate().isBefore(today))
                .min(Comparator.comparing(Event::getDate))
                .ifPresent(next -> {
                    aiDailyMessage.setText("Thinking about '" + next.getEventName() + "'…");

                    String prompt = String.format(
                            "Give me a short, uplifting daily message for my next event '%s' on %s. " +
                                    "Don't ask questions, just give me a motivational message and never multiple options.",
                            next.getEventName(), next.getDate()
                    );

                    OllamaClient.askAsync(prompt, response -> {
                        sessionDailyMessage = (response != null && response.getResponse() != null)
                                ? response.getResponse()
                                : "AI returned no message.";

                        Platform.runLater(() -> aiDailyMessage.setText(sessionDailyMessage));
                    });
                });
    }

    private static String sessionStudyTips = null;
    /**
     * Loads the AI study tips and updates the TextArea with a list of personal study tips
     * generated by the AI.
     * <p>
     * The prompt instructs the AI to generate a list of 5 personal study tips for a student
     * studying.
     * </p>
     */
    private void loadAIStudyTips() {
        if (sessionStudyTips != null) {
            aiStudyTips.setText(sessionStudyTips);
            return;
        }

        String prompt = "Generate a list of 5 personal study tips for a student who is studying for exams. Don't ask questions, just give me the tips.";
        aiStudyTips.setText("Generating personal study tips...!");

        OllamaClient.askAsync(prompt, response -> {
            sessionStudyTips = (response != null && response.getResponse() != null)
                    ? response.getResponse()
                    : "AI returned no message.";

            Platform.runLater(() -> aiStudyTips.setText(sessionStudyTips));
        });
    }

    /**
     * Handles the event when the user clicks to save a message.
     *
     * This method creates a new AIMessage object using the current user's ID and the
     * current message. It then saves the generated message to the database using
     * the MessageDAO class.
     *
     * Behavior:
     * - A new AIMessage instance is constructed with the associated user ID and current message string.
     * - The MessageDAO.saveMessage() method is called to persist the created message in the database.
     * - If the message already exists (validated by the MessageDAO), the saving operation will not proceed.
     *
     * Preconditions:
     * - The user must be properly set with a valid ID.
     * - The current message string should be populated.
     *
     * Postconditions:
     * - The new AIMessage is saved in the persistent storage if it doesn't already exist.
     */
    public void onSaveMessageClick() {
        AIMessage newMessage = new AIMessage(user.getId(), sessionDailyMessage);
        MessageDAO.saveMessage(newMessage);
    }
}