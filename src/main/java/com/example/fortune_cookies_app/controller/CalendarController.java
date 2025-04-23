package com.example.fortune_cookies_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;

import java.time.YearMonth;

public class CalendarController {

    @FXML
    private Label monthLabel;

    @FXML
    private TextArea dailyMessage;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private ListView<String> studyTipsList;

    @FXML
    private Button savedMessagesButton;

    @FXML
    private Button addEventButton;

    @FXML
    private Label trophyStatus;

    @FXML
    private Label usernameLabel;

    @FXML
    private ImageView avatarIcon;

    @FXML
    private ImageView logoImage;

    private YearMonth currentMonth;

    @FXML
    public void initialize() {
        currentMonth = YearMonth.now();
        monthLabel.setText(currentMonth.getMonth().toString() + " " + currentMonth.getYear());

        // Placeholder
        dailyMessage.setText("AI-generated daily message...");
        trophyStatus.setText("LOGGED IN 6 DAYS IN A ROW, NEXT TROPHY: IN 1 DAY");
        usernameLabel.setText("USER123");

        // Load study tips
        studyTipsList.getItems().addAll(
                "Stay consistent with daily study",
                "Break sessions into 25-minute chunks",
                "Review your notes at the end of the day"
        );

        // TODO: Populate calendar dynamically
    }

    // TODO: Add methods to populate the calendar, handle events, etc.
}
