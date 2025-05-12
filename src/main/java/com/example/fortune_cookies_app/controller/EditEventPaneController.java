package com.example.fortune_cookies_app.controller;
import com.example.fortune_cookies_app.model.Event;
import com.example.fortune_cookies_app.model.EventDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

/**
 * Controller for editing an existing event. mimics the addeventpane
 * but with data pre imported into the inputs.
 */
public class EditEventPaneController{
    @FXML private Button saveEditButton;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private HBox importanceCircles;
    @FXML private Button deleteButton;
    @FXML private Label titleErrorLabel;
    @FXML private Label descriptionErrorLabel;

    private Event event;
    private final EventDAO eventDAO = new EventDAO();
    private CalendarMainController calendarController;
    private int selectedImportance = 3;

    /**
     * Fills the 'addevent' interface with the preexisting database information
     *
     * @param event - event data to be edited
     */
    public void setEvent(Event event){
        this.event = event;
        selectedImportance = event.getImportance();
        titleField.setText(event.getEventName());
        descriptionArea.setText(event.getEventDescription());
        updateImportanceHighlight();
    }

    /**
     * allows access to the calendarMainController function
     * @param calendarMainController - sidepain main controller
     */
    public void setCalendarController(CalendarMainController calendarMainController){
        this.calendarController = calendarMainController;
    }

    /**
     * Initializes the edit event pane window which is a carbon copy
     * of the add event pane. fills out the relevant information from
     * the database ready for editing.
     */
    @FXML
    public void initialize(){
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
        titleField.textProperty().addListener((obs, previous, current) -> {
            if (current.length() > 50) {
                titleField.setText(previous);
                titleErrorLabel.setVisible(true);
            } else {
                titleErrorLabel.setVisible(false);
            }
        });
        descriptionErrorLabel.setVisible(false);
        descriptionArea.textProperty().addListener((obs, previous, current) -> {
            if (current.length() > 255) {
                descriptionArea.setText(previous);
                descriptionErrorLabel.setVisible(true);
            } else {
                descriptionErrorLabel.setVisible(false);
            }
        });
        updateImportanceHighlight();
        saveEditButton.setOnAction(e -> updateEvent());
        deleteButton.setOnAction(e -> deleteEvent());
    }

    private void updateImportanceHighlight(){
        for (int i = 0; i < importanceCircles.getChildren().size(); i++) {
            Region circle = (Region) importanceCircles.getChildren().get(i);
            if (i == selectedImportance - 1) {
                circle.setStyle("-fx-background-color: " + getImportanceColor(selectedImportance) + "; -fx-background-radius: 50%;");
            } else {
                circle.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%;");
            }
        }
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

    private void updateEvent(){
        if (event == null) {
            return;
        }
        String title = titleField.getText();
        String description = descriptionArea.getText();

        if (title.isEmpty()){
            return;
        }

        event.setEventName(title);
        event.setEventDescription(description);
        event.setImportance(selectedImportance);
        eventDAO.updateEvent(event);
        System.out.println("Event Updated");

        if (calendarController != null) {
            calendarController.populateCalendar();
            calendarController.defaultSidebar();
        }
    }

    private void deleteEvent(){
        if (event == null) {
            return;
        }
        eventDAO.deleteEvent(event);
        System.out.println("Event deleted");

        if (calendarController != null) {
            calendarController.populateCalendar();
            calendarController.defaultSidebar();
        }
    }


}