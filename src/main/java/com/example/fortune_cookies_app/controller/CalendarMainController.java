package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

/**
 * Controls and builds the main functionality of the program,
 * Builds the calandar using a grid system and laysout the top bar and sidepane.
 * Controls the sidebar import logic.
 * Controls all calendar navigation and populates it with important information.
 */
public class CalendarMainController {
    //pulls from calendar-view.fxml
    @FXML private Label monthLabel;
    @FXML private Label previousMonth;
    @FXML private Label nextMonth;
    @FXML private Label welcomeUserLabel;
    @FXML private GridPane calendarGrid;
    @FXML private VBox sidebarPane;
    private final EventDAO eventDAO = new EventDAO();
    private User user;

    /**
     * initializes the current user after login
     * @return The user object that is logged in
     */
    public User getUser() {
        return user;
    }

    //logic for the month header
    private YearMonth currentMonth = YearMonth.now();
    private StackPane selectedCell = null;
    private LocalDate selectedDate = null;

    /**
     * Sets up the Calendar and deals with navigation
     */
    public void initialize() {
        previousMonth.setOnMouseClicked(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateMonthLabel();
            populateCalendar();
            //update defaultSidebar on month change
            defaultSidebar();
        });
        nextMonth.setOnMouseClicked(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthLabel();
            populateCalendar();
            defaultSidebar();
        });

        updateMonthLabel();

    }

    /**
     * Sets the current user and populate/refreshes the calendar
     * @param user - user
     */
    public void setUser(User user) {
        this.user = user;
        populateCalendar();
        updateWelcomeLabel();
        if (sidebarPane != null) {
            defaultSidebar();
        }
    }

    private void updateMonthLabel(){
        // updates the Month Label in the format "'month' ####"
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = currentMonth.getYear();
        monthLabel.setText(monthName + " " + year);
    }

    private void updateWelcomeLabel(){
        String name = user.getFirstName();
        welcomeUserLabel.setText("Welcome," + " " + name + "!");
    }

    /**
     * Fills the calendar with the relevant information. Dates, key dates,
     * importance colours, selected highlights ect.
     */
    public void populateCalendar(){
        //start fresh
        calendarGrid.getChildren().clear();

        LocalDate dayOne = currentMonth.atDay(1);
        //what weekday the month starts on
        int offset = dayOne.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentMonth.lengthOfMonth();

        // calculate where to place each day in column and row
        for (int day = 1; day <= daysInMonth; day++) {
            int cellIndex = day + offset - 1;
            int column = cellIndex % 7;
            int row = cellIndex / 7;
            LocalDate currentDate = currentMonth.atDay(day);

            //position the number correctly in the center of the grid position
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setFont(new Font("Lucida Sans Unicode", 14));
            dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);

            //style setup for calendar date highlight on click
            String highlightColor = "#bbbbbb";
            if (user != null) {
                List<Event> eventsForDay = eventDAO.fetchEventsDay(user, currentDate);
                if (!eventsForDay.isEmpty()) {
                    int importance = eventsForDay.stream().mapToInt(Event::getImportance).max().orElse(1);
                    highlightColor = switch (importance) {
                        case 1 -> "#90caf9"; // darker blue
                        case 2 -> "#81c784"; // darker green
                        case 3 -> "#fff176"; // darker yellow
                        case 4 -> "#ba68c8"; // darker purple
                        case 5 -> "#ef9a9a"; // darker red
                        default -> "#bbbbbb";
                    };
                }
            }

            Region highlight = new Region();
            highlight.setStyle("-fx-background-color: " + highlightColor + "; -fx-background-radius: 50%;");
            highlight.setMinSize(46, 46);
            highlight.setPrefSize(46, 46);
            highlight.setMaxSize(46, 46);
            highlight.setVisible(false);

            Region eventHighlight = new Region();
            eventHighlight.setMinSize(46, 46);
            eventHighlight.setPrefSize(46, 46);
            eventHighlight.setMaxSize(46, 46);
            eventHighlight.setVisible(false);

            //check dates for events
            if (user != null) {
                List<Event> eventsForDay = eventDAO.fetchEventsDay(user, currentDate);
                if (!eventsForDay.isEmpty()) {
                    int highestImportance = eventsForDay.stream()
                            .mapToInt(Event::getImportance)
                            .max()
                            .orElse(1);
                    System.out.println("Events found for: " + currentDate + " Highest importance: " + highestImportance);
                    eventHighlight.setVisible(true);
                    eventHighlight.setStyle("-fx-background-color:" + getImportanceColour(highestImportance) + "; -fx-background-radius: 50%;");
                }
            }

            StackPane cell = new StackPane(eventHighlight, highlight, dayLabel);
            cell.setAlignment(Pos.CENTER);
            cell.setPrefSize(80, 60);
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setHgrow(cell, Priority.ALWAYS);
            GridPane.setVgrow(cell, Priority.ALWAYS);

            final int currentDay = day;
            cell.setOnMouseClicked(e -> onDateClicked(cell, currentMonth.atDay(currentDay)));
            calendarGrid.add(cell, column, row);
        }
    }

    //get importance colours
    private String getImportanceColour(int level){
        return switch (level){
            case 1 -> "#bbdefb"; // pastel blue
            case 2 -> "#c8e6c9"; // pastel green
            case 3 -> "#fff9c4"; // soft yellow
            case 4 -> "#e1bee7"; // soft purple
            case 5 -> "#ffcdd2"; // soft red
            default -> "lightgrey";
        };
    }

    /**
     * Loads the default sidebarcontroller on calendar launch and as
     * a fallback for most other sidebarpanes.
     */
    public void defaultSidebar() {
        System.out.println("Running defaultSidebar");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/default-sidebar-view.fxml"));
            VBox sidebar = loader.load();
            DefaultSidebarController controller = loader.getController();
            sidebarPane.getChildren().setAll(sidebar);
            System.out.println("DefaultSidebarController loaded successfully");

            javafx.application.Platform.runLater(() ->{
                controller.setUser(user);
                controller.setCurrentMonth(currentMonth);
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    //sidebar logic to handle the various different sidebars that will be available.
    private void onDateClicked(StackPane cell, LocalDate date){
        //turns off a previously selected cell
        if (selectedCell == cell){
            selectedCell.getChildren().get(1).setVisible(false);
            selectedCell = null;
            selectedDate = null;
            defaultSidebar();
            return;
        }
        if (selectedCell != null){
            selectedCell.getChildren().get(1).setVisible(false);
        }
        selectedCell = cell;
        selectedDate = date;
        cell.getChildren().get(1).setVisible(true);

        sidebarPane.getChildren().clear();
        List<Event> eventsForDate = eventDAO.fetchEventsDay(user, date);
        if (eventsForDate.isEmpty()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/add-event-view.fxml"));
                VBox addEventPane = loader.load();
                AddEventPaneController controller = loader.getController();
                controller.setUser(user);
                controller.setDate(date);
                controller.setCalendarController(this);
                sidebarPane.getChildren().setAll(addEventPane);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/date-with-events-view.fxml"));
                VBox dateEventsPane = loader.load();
                DateWithEventsController controller = loader.getController();
                controller.setUser(user);
                controller.setDate(date);
                controller.setCalendarController(this);
                sidebarPane.getChildren().setAll(dateEventsPane);
            } catch (IOException e){
                e.printStackTrace();
            }
        }


    }

    /**
     * Handles the profile button click.
     * @throws IOException if loading profile popup fails
     */
    @FXML
    public void onProfileClick() throws IOException {

        Stage stage = new Stage();
        FXMLLoader profileLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/profile-view.fxml"));
        Parent profileRoot = profileLoader.load();
        ProfileController profileController = profileLoader.getController();

        profileController.setUser(this.user);

        Scene profileScene = new Scene(profileRoot, 400, 168);

        stage.setTitle("Profile");
        stage.setScene(profileScene);
        stage.show();
    }

    /**
     * Handles the trophy button click.
     * @throws IOException if loading trophy popup fails
     */
    @FXML
    public void onTrophyClick() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/trophy-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 647, 472);
        TrophyController trophyController = fxmlLoader.getController();
        trophyController.setUser(this.user);
        stage.setTitle("Trophies");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the saved messages button click.
     * @throws IOException if loading saved messages popup fails
     */
    @FXML
    public void onSavedMsgsClick() throws IOException {
        Stage stage = new Stage();
        FXMLLoader savedMessagesLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/savedmessages-view.fxml"));
        Parent savedMessagesRoot = savedMessagesLoader.load();
        SavedMessagesController messageController = savedMessagesLoader.getController();

        messageController.setUser(this.user);
        messageController.showMessages();

        Scene savedMessagesScene = new Scene(savedMessagesRoot, 647, 472);

        stage.setTitle("Saved Messages");
        stage.setScene(savedMessagesScene);
        stage.show();
    }

    /**
     * the date with events controller does its own internal sidepane manipulation
     * and needa a method to do it.
     * @param pane
     */
    public void sidebarAccess(VBox pane) {
        sidebarPane.getChildren().setAll(pane);
    }

}
