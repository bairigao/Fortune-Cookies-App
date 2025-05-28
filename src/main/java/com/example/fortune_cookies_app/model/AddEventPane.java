package com.example.fortune_cookies_app.model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.LocalDate;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;

/**
 * The AddEventPane class represents a user interface pane for adding events
 * to a calendar application. This pane includes UI components such as a title field,
 * description area, importance selection, and buttons for creating an event or going back.
 * It facilitates the communication between the calendar and event creation functionality.
 */
public class AddEventPane extends VBox{
    /**
     * Interface to facilitate communication between the AddEventPane and its parent component.
     *
     * The AddEventCommunication interface defines methods to handle the creation of events
     * and navigating back from the AddEventPane. It is implemented by parent components
     * to enable a two-way interaction for managing the addition of events and user navigation.
     */
    public interface AddEventCommunication {
        /**
         * Handles the event creation process when a new event is created within the system.
         *
         * @param eventData The details of the event to be created, encapsulated in an EventData object.
         *                  Includes attributes such as the event's date, importance level, title, and description.
         */
        void onEventCreated(EventData eventData);
        /**
         * Handles the behavior when the user navigates back from the current pane.
         *
         * This method is typically invoked to transition the user to a previous
         * state or pane in the application, such as closing the current form or
         * returning to the parent view container. Implementations of this method
         * define the specific back navigation logic, such as clearing temporary data
         * or updating the UI upon returning.
         */
        void onBack();
    }

    /**
     * Constructs the AddEventPane UI component for creating an event.
     * The pane includes fields for event details, a selection for event importance,
     * and buttons for creating or canceling the event.
     *
     * @param selectedDate The date selected in the calendar for the event to be created.
     * @param communication The communication interface for handling event creation
     *                       and navigation back to the previous UI.
     */
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

    /**
     * A static nested class to represent the details of an event.
     * This class is utilized to store and manage information about events,
     * including their date, importance level, title, and description.
     * It is designed to work in tandem with a database for persistent storage
     * and retrieval of event-related data, which will be accessible by UI or
     * calendar components.
     */
    public static class EventData {
        public final LocalDate date;
        public final int importance;
        public final String title;
        public final String description;

        /**
         * Constructs an EventData instance to store the details of an event.
         *
         * @param date The date of the event.
         * @param importance The importance level of the event.
         * @param title The title of the event.
         * @param description A description providing additional details about the event.
         */
        public EventData(LocalDate date, int importance, String title, String description) {
            this.date = date;
            this.importance = importance;
            this.title = title;
            this.description = description;
        }
    }
}
