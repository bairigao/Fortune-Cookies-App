package com.example.fortune_cookies_app;

import com.example.fortune_cookies_app.DB.EventDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;

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

    //need to pull user and dat selected data
    public void setDate(LocalDate date){
        this.selectedDate = date;
    }
    public void setUser(User user){
        this.user = user;
    }
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

        updateImportanceHighlight();
        createEventButton.setOnAction(e -> createEvent());
    }

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

    //create event functionality tied to the create event button
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
    }

    // Later we will hook these buttons into the main calendar controller!
}