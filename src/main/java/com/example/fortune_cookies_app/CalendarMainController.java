package com.example.fortune_cookies_app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.layout.Priority;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;


public class CalendarMainController {
    //pulls from calendar-view.fxml
    @FXML private Label monthLabel;
    @FXML private Label previousMonth;
    @FXML private Label nextMonth;
    @FXML private GridPane calendarGrid;
    @FXML private VBox sidebarPane;

    private User user;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    //logic for the month header
    private YearMonth currentMonth = YearMonth.now();
    private StackPane selctedCell = null;
    private LocalDate selectedDate = null;

    public void initialize() {
        previousMonth.setOnMouseClicked(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateMonthLabel();
            populateCalendar();
        });
        nextMonth.setOnMouseClicked(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthLabel();
            populateCalendar();
        });
        updateMonthLabel();
        populateCalendar();
        defaultSidebar();
    }

    private void updateMonthLabel(){
        // updates the Month Label in th formate "'month' ####"
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = currentMonth.getYear();
        monthLabel.setText(monthName + " " + year);
    }

    //logic for assigning numbers to the calendar grid. and correctly offsetting them to the
    //correct days.
    private void populateCalendar(){
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


            StackPane cell = new StackPane(highlight, dayLabel);
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
    //sidebar before a date is pressed
    private void defaultSidebar(){
        sidebarPane.getChildren().clear();

        //placeholder logic for the future
        Label placeholderLabel = new Label("No date selected.\n\nYou can select a date to view or add events.");
        placeholderLabel.setWrapText(true);
        placeholderLabel.setStyle("-fx-font-size: 14; -fx-font-family: 'Lucida Sans Unicode';");

        sidebarPane.getChildren().add(placeholderLabel);
    }

    //sidebar logic to handle the various different sidebars that will be available.
    private void onDateClicked(StackPane cell, LocalDate date){
        //turns off a previously selected cell
        if (selctedCell != null){
            selctedCell.getChildren().get(0).setVisible(false);
        }
        selctedCell = cell;
        selectedDate = date;
        cell.getChildren().get(0).setVisible(true);


    }

}
