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
import java.util.Locale;
import java.util.List;

/**
 * The CalendarMainController class is responsible for managing the main calendar interface of the application.
 * It provides functionalities for displaying the calendar, navigating between months, updating the user interface
 * with user-specific data, handling event highlights, and managing sidebar content.
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

    /**
     * Updates the label displaying the current month and year.
     * The label is set in the format "Month ####", where "Month" is the full name
     * of the current month and "####" is the year. The displayed values are derived
     * from the `currentMonth` field.
     */
    private void updateMonthLabel(){
        // updates the Month Label in the format "'month' ####"
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = currentMonth.getYear();
        monthLabel.setText(monthName + " " + year);
    }

    /**
     * Updates the welcome label to greet the current user.
     * Retrieves the user's first name via the `getFirstName` method from the `user` object
     * and sets the `welcomeUserLabel` text to display a personalized welcome message.
     */
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
            Label dayLabel = createDayLabel(day);

            //style setup for calendar date highlight on click
            Region highlight = createHighlightRegion(currentDate);

            Region eventHighlight = createEventHighlight(currentDate);

            StackPane cell = createCell(day, eventHighlight, highlight, dayLabel);

            calendarGrid.add(cell, column, row);
        }
    }

    /**
     * @param day An integer representing the day of the month corresponding to the calendar cell
     * @param eventHighlight The highlight region that will correspond to the highest importance event on that day of the month
     * @param highlight The highlight region that will be displayed when this cell is clicked
     * @param dayLabel The label for the day of the month
     * @return A stackpane representing a single calendar day
     */
    private StackPane createCell(int day, Region eventHighlight, Region highlight, Label dayLabel){
        StackPane cell = new StackPane(eventHighlight, highlight, dayLabel);
        cell.setAlignment(Pos.CENTER);
        cell.setPrefSize(80, 60);
        cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHgrow(cell, Priority.ALWAYS);
        GridPane.setVgrow(cell, Priority.ALWAYS);

        cell.setOnMouseClicked(e -> onDateClicked(cell, currentMonth.atDay(day)));
        return cell;
    }

    /**
     * @param day The int representation of the day of the month
     * @return a label with a number corresponding to the day of the month
     */
    private Label createDayLabel(int day){
        Label dayLabel = new Label(String.valueOf(day));
        dayLabel.setFont(new Font("Lucida Sans Unicode", 14));
        dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        dayLabel.setAlignment(Pos.CENTER);
        return dayLabel;
    }

    /**
     * @param date The date to check for events. An event on this date determines what colour it will be highlighted.
     * @return  The appropriately coloured region
     */
    private Region createHighlightRegion(LocalDate date){
        Region highlight = new Region();
        String highlightColor = "#bbbbbb";
        if (user != null) {
            List<Event> eventsForDay = eventDAO.fetchEventsDay(user, date);
            if (!eventsForDay.isEmpty()) {
                int importance = eventsForDay.stream().mapToInt(Event::getImportance).max().orElse(1);
                highlightColor = getClickedImportanceColour(importance);
            }
        }
        highlight.setStyle("-fx-background-color: " + highlightColor + "; -fx-background-radius: 50%;");
        highlight.setMinSize(46, 46);
        highlight.setPrefSize(46, 46);
        highlight.setMaxSize(46, 46);
        highlight.setVisible(false);

        return highlight;
    }

    /**
     * @param date The date to check for events. An event on this date determines what colour it will be highlighted.
     * @return The appropriately coloured region
     */
    private Region createEventHighlight(LocalDate date){
        Region eventHighlight = new Region();
        eventHighlight.setMinSize(46, 46);
        eventHighlight.setPrefSize(46, 46);
        eventHighlight.setMaxSize(46, 46);
        eventHighlight.setVisible(false);

        //check dates for events
        if (user != null) {
            List<Event> eventsForDay = eventDAO.fetchEventsDay(user, date);
            if (!eventsForDay.isEmpty()) {
                int highestImportance = eventsForDay.stream()
                        .mapToInt(Event::getImportance)
                        .max()
                        .orElse(1);
                eventHighlight.setVisible(true);
                eventHighlight.setStyle("-fx-background-color:" + getImportanceColour(highestImportance) + "; -fx-background-radius: 50%;");
            }
        }
        return eventHighlight;
    }

    /**
     * Determines the color code corresponding to the specified importance level.
     * Each importance level is mapped to a specific pastel color, while unknown levels default to light grey.
     *
     * @param level the importance level (1 to 5) indicating the severity or priority.
     *              Levels correspond as follows:
     *              1 - pastel blue, 2 - pastel green, 3 - soft yellow,
     *              4 - soft purple, 5 - soft red. Any other level defaults to light grey.
     * @return a string representing the hex color code associated with the given importance level.
     */

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
    //get importance colours
    private String getClickedImportanceColour(int level){
        return switch (level){
            case 1 -> "#90caf9"; // pastel blue
            case 2 -> "#81c784"; // pastel green
            case 3 -> "#fff176"; // soft yellow
            case 4 -> "#ba68c8"; // soft purple
            case 5 -> "#ef9a9a"; // soft red
            default -> "lightgrey";
        };
    }

    /**
     * Loads the default sidebar controller on calendar launch and as
     * a fallback for most other sidebar panes.
     */
    public void defaultSidebar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/default-sidebar-view.fxml"));
            VBox sidebar = loader.load();
            DefaultSidebarController controller = loader.getController();
            sidebarPane.getChildren().setAll(sidebar);

            javafx.application.Platform.runLater(() ->{
                controller.setUser(user);
                controller.setCurrentMonth(currentMonth);
            });
        } catch (IOException e){
            System.err.println("I/O Error occurred: " + e.getMessage());
        }
    }


    /**
     * Handles the logic when a date cell in the calendar is clicked.
     * Updates the sidebar view based on the selected date, loading either an "Add Event" pane
     * if no events exist for the date, or a "Date With Events" pane to display existing events.
     * Additionally, manages the visual state of the selected cell and the previously selected cell.
     *
     * @param cell The StackPane representing the clicked date cell.
     * @param date The LocalDate representing the date associated with the clicked cell.
     */
    //sidebar logic to handle the various different sidebars that will be available.
    private void onDateClicked(StackPane cell, LocalDate date){
        //turns off a previously selected cell
        if (selectedCell == cell){
            selectedCell.getChildren().get(1).setVisible(false);
            selectedCell = null;
            defaultSidebar();
            return;
        }
        if (selectedCell != null){
            selectedCell.getChildren().get(1).setVisible(false);
        }
        selectedCell = cell;
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
            } catch (Exception e){
                System.err.println("An error occurred: " + e.getMessage());
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
            } catch (Exception e){
                System.err.println("An error occurred: " + e.getMessage());
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
     * Replaces the content of the sidebar with the specified VBox pane.
     * This method sets the provided pane as the current view in the sidebar.
     *
     * @param pane the VBox pane to be displayed in the sidebar
     */
    public void sidebarAccess(VBox pane) {
        sidebarPane.getChildren().setAll(pane);
    }

}
