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
     * Constructs the AddEventPane UI component for creating an event.
     * The pane includes fields for event details, a selection for event importance,
     * and buttons for creating or canceling the event.
     *
     * @param selectedDate The date selected in the calendar for the event to be created.
//     * @param communication The communication interface for handling event creation
     *                       and navigation back to the previous UI.
     */
    public AddEventPane(LocalDate selectedDate){
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_LEFT);

        //add event and date selected on the calendar
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
            Region circle = getRegion(i, selectedImportance, importanceCircles);
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

    private Region getRegion(int i, int[] selectedImportance, HBox importanceCircles) {
        Region circle = new Region();
        circle.setMinSize(20, 20);
        circle.setPrefSize(24, 24);
        circle.setMaxSize(24, 24);
        circle.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%; -fx-cursor: hand;");

        //click implementation, change colour update int, revert colour
        circle.setOnMouseClicked(e -> {
            selectedImportance[0]= i;
            for (int j = 0; j < importanceCircles.getChildren().size(); j++){
                Region c = (Region) importanceCircles.getChildren().get(j);
                if (j == i - 1) {
                    c.setStyle("-fx-background-color: " + getImportanceColor(i) + "; -fx-background-radius: 50%;");
                } else {
                    c.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%;");
                }
            }
        });
        return circle;
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
}