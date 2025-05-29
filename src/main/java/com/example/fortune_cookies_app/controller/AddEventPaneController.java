package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.model.Event;
import com.example.fortune_cookies_app.model.EventDAO;
import com.example.fortune_cookies_app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

import java.time.LocalDate;

/**
 * Controller class responsible for managing the "Add Event" pane functionality.
 *
 * This class handles the user interface setup, including configuring importance selection,
 * input validation for event details (title and description), and the logic for creating
 * and saving new events to the database. It also integrates with the main calendar view
 * via the `CalendarMainController`.
 */
public class AddEventPaneController {

    @FXML private Button createEventButton;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private HBox importanceCircles;

    private int selectedImportance = 3; // default importance level
    //needed to set the date for an event addition (will pull the seleceted date from the calendarMain)
    private LocalDate selectedDate;
    private final EventDAO eventDAO = new EventDAO();
    private User user;
    private CalendarMainController calendarController;

    /**
     * Sets the date based on the selected date
     *
     * @param date - LocalDate
     */
    public void setDate(LocalDate date){
        this.selectedDate = date;
    }
    /**
     * sets the current user for the side pane logic
     *
     * @param user - current user
     */
    public void setUser(User user){
        this.user = user;
    }
    @FXML private Label titleErrorLabel;
    @FXML private Label descriptionErrorLabel;

    /**
     * sets up the sidebar ui and handles the importance circles, errors,
     * and the creation event button
     */
    @FXML
    public void initialize() {
        // Setup importance circles (1 to 5)
        for (int i = 1; i <= 5; i++) {
            Region circle = new Region();
            circle.setMinSize(20, 20);
            circle.setPrefSize(24, 24);
            circle.setMaxSize(24, 24);
            circle.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%; -fx-cursor: hand;");

            final int level = i;
            circle.setOnMouseClicked((MouseEvent e) -> {
                selectedImportance = level;
                updateImportanceHighlight();
            });

            importanceCircles.getChildren().add(circle);
        }

        titleErrorLabel.setVisible(false);
        titleField.textProperty().addListener((obs, previousType, newText) ->{
            if (newText.length() > 50){
                titleField.setText(previousType);
                titleErrorLabel.setVisible(true);
            } else {
                titleErrorLabel.setVisible(false);
            }
        });

        descriptionErrorLabel.setVisible(false);
        descriptionArea.textProperty().addListener((obs, previousType, newText) ->{
            if (newText.length() > 255){
                descriptionArea.setText(previousType);
                descriptionErrorLabel.setVisible(true);
            } else {
                descriptionErrorLabel.setVisible(false);
            }
        });

        updateImportanceHighlight();
        createEventButton.setOnAction(e -> createEvent());
    }

    /**
     * Updates the visual styles of the importance circles to highlight the selected level of importance.
     *
     * This method iterates through each child node (assumed to be a circle) in the `importanceCircles` container.
     * It applies a unique style to the circle corresponding to the `selectedImportance` level, while resetting
     * the styles of all other circles to default.
     *
     * The highlighted circle uses a color associated with the current `selectedImportance` level, which is
     * determined by calling the `getImportanceColor` method. The styles applied include background color and
     * rounded geometry.
     *
     * For all other circles that are not selected, their styles are reset to light grey with the same rounded
     * geometry.
     */
    private void updateImportanceHighlight() {
        for (int i = 0; i < importanceCircles.getChildren().size(); i++) {
            Region circle = (Region) importanceCircles.getChildren().get(i);
            if (i == selectedImportance - 1) {
                circle.setStyle("-fx-background-color: " + getImportanceColor(selectedImportance) + "; -fx-background-radius: 50%;");
            } else {
                circle.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%;");
            }
        }
    }

    /**
     * Returns the color code associated with a specific importance level.
     * The color codes represent the intensity or priority of the importance level:
     * - Level 1: Pastel blue (#bbdefb)
     * - Level 2: Pastel green (#c8e6c9)
     * - Level 3: Soft yellow (#fff9c4)
     * - Level 4: Soft purple (#e1bee7)
     * - Level 5: Soft red (#ffcdd2)
     * For any unspecified level, the default color is light grey.
     *
     * @param level the importance level, ranging from 1 to 5
     * @return a string representing the hexadecimal color code corresponding to the given importance level
     */
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
     * Sets the calendar controller for managing the main calendar view and its interactions.
     *
     * @param calendarController the controller responsible for handling calendar-related operations
     */
    public void setCalendarController(CalendarMainController calendarController) {
        this.calendarController = calendarController;
    }
    /**
     * Creates a new event based on user input and adds it to the database.
     *
     * This method first validates the selected date, user, and event details before creating the event.
     * If validation fails, appropriate error messages are printed to the console, and the method exits early.
     * The event details include a title, description (optional), and importance level. The title is a required field.
     *
     * If all validations pass, the method creates an instance of the Event object with the provided information,
     * using the selected date, title, description, importance, and user ID. It then calls the `createEvent` method
     * from the `eventDAO` to persist the event in the database.
     *
     * The method also interacts with the `calendarController` to refresh the calendar view and reset the sidebar
     * interface after a successful event creation. If the `calendarController` is not set, these steps are skipped.
     *
     * Error Messages:
     * - "Error-Date/User" if either the selected date or the user is null.
     * - "Title Required" if the title field is empty.
     */
    private void createEvent(){
        if (selectedDate == null || user == null){
            System.out.println("Error-Date/User");
            return;
        }
        String title = titleField.getText();
        String description = descriptionArea.getText();
        int importance = selectedImportance;
        if (title.isEmpty()){
            System.out.println("Title Required");
            return;
        }
        Event newEvent = new Event(selectedDate, title, description, importance, user.getId());
        eventDAO.createEvent(newEvent);
        System.out.println("Event Created");
        if (calendarController !=null) {
            calendarController.populateCalendar();
            calendarController.defaultSidebar();
        }
    }

}