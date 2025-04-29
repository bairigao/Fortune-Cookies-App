package com.example.fortune_cookies_app.controller;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import java.time.Year;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;

public class AddEventPane extends VBox{
    //communication between Calendar.java and AddEventPane
    public interface AddEventCommunication {
        void onEventCreated(EventData eventData);
        void onBack();
    }

    public AddEventPane(LocalDate selectedDate, AddEventCommunication communication){
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_LEFT);

        //addevent and date selected on the calendar
        Label title =  new Label("Add Event - "+ selectedDate);
        title.setFont(new Font("Lucida Sans Unicode", 16));

        //1st implementation of an importance field will choose int 1-5 to update database.
        Label importanceLabel = new Label("Importance (1-5):");

        //small circles used to click the importance of the even
        HBox importanceCircles = new HBox(10);
        importanceCircles.setAlignment(Pos.CENTER_LEFT);

        // set a default level (always has to have a selection
        final int[] selectedImportance = {3};
        for(int i = 1; i <= 5; i++) {
            int level = i;
            Region circle = new Region();
            circle.setMinSize(20, 20);
            circle.setPrefSize(24, 24);
            circle.setMaxSize(24, 24);
            circle.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%; -fx-cursor: hand;");

            //click implementation, change colour update int, revert colour
            circle.setOnMouseClicked(e -> {
                selectedImportance[0]= level;
                for (int j = 0; j < importanceCircles.getChildren().size(); j++){
                    Region c = (Region) importanceCircles.getChildren().get(j);
                    if (j == level - 1) {
                        c.setStyle("-fx-background-color: " + getImportanceColor(level) + "; -fx-background-radius: 50%;");
                    } else {
                        c.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%;");
                    }
                }
            });
            importanceCircles.getChildren().add(circle);
        }
        //Title creation
        Label titleLabel = new Label("Event Title:");
        TextField titleField = new TextField();

        // more detail for an even section
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(4);

        //Buttons - back button (return arrow) - Create event button
        Button createEventButton = new Button("Create Event");
        Button returnButton = new Button("← Back");

        //hbox for the back button to always be top left of the pane
        HBox topBar = new HBox(returnButton);
        topBar.setAlignment(Pos.TOP_LEFT);

        //now i need a hbox to store the create button
        HBox bottomBar = new HBox(createEventButton);
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        // this is a temporary internal data hold for creating an event.
        //this will later be changed to a push pull request from the database. or some such
        createEventButton.setOnAction(e -> {
            EventData event = new EventData(
                    selectedDate,
                    selectedImportance[0],
                    titleField.getText(),
                    descriptionArea.getText()
            );
            communication.onEventCreated(event);
        });

        //functionality for the back button
        returnButton.setOnAction(e -> communication.onBack());

        // Add all UI components to the layout
        getChildren().addAll(
                topBar,
                title,
                importanceLabel, importanceCircles,
                titleLabel, titleField,
                descriptionLabel, descriptionArea,
                bottomBar
        );
    }

    // sets the colours for the importance circles
    private String getImportanceColor(int level) {
        return switch (level) {
            case 1 -> "#bbdefb"; // pastel blue
            case 2 -> "#c8e6c9"; // pastel green
            case 3 -> "#fff9c4"; // soft yellow
            case 4 -> "#e1bee7"; // soft purple
            case 5 -> "#ffcdd2"; // soft red
            default -> "lightgrey";
        };
    }

    // passes data - will be linked to the database to store information to be pulled by the calendar.java page
    public static class EventData {
        public final LocalDate date;
        public final int importance;
        public final String title;
        public final String description;

        public EventData(LocalDate date, int importance, String title, String description) {
            this.date = date;
            this.importance = importance;
            this.title = title;
            this.description = description;
        }
    }
}
