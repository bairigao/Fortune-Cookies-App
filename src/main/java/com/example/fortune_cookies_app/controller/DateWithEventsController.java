package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.model.Event;
import com.example.fortune_cookies_app.model.EventDAO;
import com.example.fortune_cookies_app.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.Node;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


/**
 * Controls aspects of the sidebar pane involved when a date on the calendar with an
 * existing event is selected.
 */
public class DateWithEventsController {
    public DateWithEventsController() {
        System.out.println("DateWithEventsController CONSTRUCTOR called");
    }
    @FXML private VBox eventEntries;
    @FXML private Button addEventButton;


    private LocalDate selectedDate;
    private User user;
    private CalendarMainController calendarController;
    private final EventDAO eventDAO = new EventDAO();

    /**
     * Sets the date based on the selected date
     *
     * @param date - LocalDate
     */
    public void setDate(LocalDate date){
        this.selectedDate = date;
        loadEvents();
    }

    /**
     * sets the current user for the side pane logic
     *
     * @param user - current user
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * connects side pane control to the CalendarMainController
     * @param controller - calendarController
     */
    public void setCalendarController(CalendarMainController controller){
        this.calendarController = controller;
    }

    /**
     * initializes after the FXML load
     * sets the Add event button at the top
     */
    @FXML
    public void initialize() {
        addEventButton.setOnAction(e -> loadAddEventPane());
        System.out.println("DateWithEventsController INITIALIZED");
    }

    /**
     * Pulls event ids from the database then pulls importance, title and description
     * data. presents them in a vbox with a manage and delete button
     */
    private void loadEvents(){
        eventEntries.getChildren().clear();
        if (user == null || selectedDate == null)
            return;

        List<Event> events = eventDAO.fetchEventsDay(user, selectedDate);
        for (Event event: events){
            VBox eventBox = new VBox(5);
            HBox titleRow = new HBox(8);
            Region circle = new Region();
            circle.setStyle("-fx-background-color: " + getImportanceColour(event.getImportance()) + "; -fx-background-radius: 50%;");
            circle.setMinSize(20, 20);
            circle.setPrefSize(20, 20);
            circle.setMaxSize(20, 20);

            Label titleLabel = new Label(event.getEventName());
            titleLabel.setStyle("-fx-font-size: 14;");
            titleRow.getChildren().addAll(circle, titleLabel);
            Label descriptionLabel = new Label(event.getEventDescription());
            descriptionLabel.setWrapText(true);
            descriptionLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #555555;");

            HBox buttonRow = new HBox(10);
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");
            editButton.setOnAction(e -> loadEditEventPane(event));
            deleteButton.setOnAction(e -> {
                 boolean confirm = true;
                if (confirm){
                    eventDAO.deleteEvent(event);
                    calendarController.populateCalendar();
                    calendarController.defaultSidebar();
                }
            });

            buttonRow.getChildren().addAll(editButton, deleteButton);
            eventBox.getChildren().addAll(titleRow, descriptionLabel,buttonRow);
            eventEntries.getChildren().add(eventBox);

            //debugging info
            System.out.println("Event found: " + event.getEventName());

        }
        System.out.println("Loading events for date: " + selectedDate);
        System.out.println("User ID: " + (user != null ? user.getId() : "null"));
    }

    /**
     * transition logic for going from the Datewithevents pane to the
     * addevents pane for adding an event to a date with an existing event.
     */
    private void loadAddEventPane(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/add-event-view.fxml"));
            VBox addEventPane = loader.load();
            AddEventPaneController controller = loader.getController();
            controller.setDate(selectedDate);
            controller.setUser(user);
            controller.setCalendarController(calendarController);
            calendarController.sidebarAccess(addEventPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads the Editevent pane, used to manage an existing event within a date.
     * @param event - specification of event.
     */
    private void loadEditEventPane(Event event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/edit-event-view.fxml"));
            VBox editEventPane = loader.load();
            EditEventPaneController controller = loader.getController();
            controller.setEvent(event);
            controller.setCalendarController(calendarController);
            calendarController.sidebarAccess(editEventPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Importance colours again
     * @param level - importance level data
     * @return - returns the hex-colour.
     */
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


}
