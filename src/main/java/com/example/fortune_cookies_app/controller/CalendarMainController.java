package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.model.Event;
import com.example.fortune_cookies_app.model.EventDAO;
import com.example.fortune_cookies_app.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.List;

public class CalendarMainController {
    //pulls from calendar-view.fxml
    @FXML private Label monthLabel;
    @FXML private Label previousMonth;
    @FXML private Label nextMonth;
    @FXML private GridPane calendarGrid;
    @FXML private VBox sidebarPane;
    private final EventDAO eventDAO = new EventDAO();

    private User user;
    public User getUser() {
        return user;
    }

    //logic for the month header
    private YearMonth currentMonth = YearMonth.now();
    private StackPane selectedCell = null;
    private LocalDate selectedDate = null;

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
    public void setUser(User user) {
        this.user = user;
        populateCalendar();
        if (sidebarPane != null) {
            defaultSidebar();
        }
    }

    private void updateMonthLabel(){
        // updates the Month Label in th formate "'month' ####"
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = currentMonth.getYear();
        monthLabel.setText(monthName + " " + year);
    }

    //logic for assigning numbers to the calendar grid. and correctly offsetting them to the
    //correct days.
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
            Region highlight = new Region();
            highlight.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%;");
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
    //sidebar before a date is pressed
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
                controller.setDate(date);
                controller.setUser(user);
                controller.setCalendarController(this);
                sidebarPane.getChildren().setAll(addEventPane);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            Label placeholderLabel = new Label("Placeholder, date selected with an existing event");
            placeholderLabel.setWrapText(true);
            placeholderLabel.setStyle("-fx-font-size: 14; -fx-font-family: 'Lucida Sans Unicode';");

            sidebarPane.getChildren().add(placeholderLabel);
        }
    }

}
