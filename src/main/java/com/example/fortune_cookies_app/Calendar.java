package com.example.fortune_cookies_app;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import java.time.Year;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
import com.example.fortune_cookies_app.AddEventPane;
import java.util.*;
import javafx.scene.control.Button;
import javafx.scene.Node;

public class Calendar extends Application {

    private YearMonth currentMonth = YearMonth.now();
    private Label monthNow;
    private GridPane calendarGrid;
    private StackPane chosenDate = null;
    private VBox sidebarImplementation;
    private LocalDate selectedDate = null;
    //temporary event storage.
    private final Map<LocalDate, List<AddEventPane.EventData>> events = new HashMap<>();

    private VBox createCenteredBox(Node child) {
        VBox centeredBox = new VBox(child);
        centeredBox.setAlignment(Pos.CENTER); // Center horizontally & vertically
        centeredBox.setPrefHeight(Double.MAX_VALUE); // Take up space
        VBox.setVgrow(centeredBox, Priority.ALWAYS);
        return centeredBox;
    }

    public VBox getSidebarImplementation() {
        return sidebarImplementation;
    }
    //fills the grid with numbers for the current month
    //adjust date positions horizontally and vertically to follow standard calendar design
    private void updateCalendarGrid() {
        calendarGrid.getChildren().clear();
        LocalDate dayOne = currentMonth.atDay(1);
        int daysInMonth = currentMonth.lengthOfMonth();
        int dayOffset = dayOne.getDayOfWeek().getValue() % 7;
        for (int day = 1; day <= daysInMonth; day++) {
            int calendarIndex = day + dayOffset - 1;
            int column = calendarIndex % 7;
            int row = calendarIndex / 7;

            Label dateLabel = new Label(String.valueOf(day));
            dateLabel.setStyle("-fx-font-size: 14; -fx-font-family: 'Lucida Sans Unicode';");
            dateLabel.setAlignment(Pos.CENTER);
            dateLabel.setMaxWidth(Double.MAX_VALUE);
            dateLabel.setMaxHeight(Double.MAX_VALUE);
            //setup the coloured circles for selected dates
            Region dateCircle = new Region();
            dateCircle.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 50%;");
            dateCircle.setMinSize(42, 42);
            dateCircle.setPrefSize(46, 46);
            dateCircle.setMaxSize(46, 46);
            dateCircle.setVisible(false);

            StackPane cell = new StackPane(dateCircle, dateLabel);
            cell.setPrefSize(120, 80);
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            //mouse interaction with dates
            // implement a deslect ffunction aswell
            final int currentDay = day;
            cell.setOnMouseClicked(e -> {
                LocalDate clickedDate = currentMonth.atDay(currentDay); // day is the loop variable
                if (chosenDate == cell) {
                    // Deselect if clicked again
                    cell.getChildren().get(0).setVisible(false);
                    chosenDate = null;
                    selectedDate = null;
                    sidebarImplementation.getChildren().clear();
                } else {
                    // Deselect previously selected cell
                    if (chosenDate != null) {
                        chosenDate.getChildren().get(0).setVisible(false);
                    }

                    // Highlight this cell
                    dateCircle.setVisible(true);
                    chosenDate = cell;
                    selectedDate = clickedDate;

                    // If there are no events for this date, show "Add Event" button/form
                    if (!events.containsKey(clickedDate)) {
                        // Create an "Add Event" button
                        Button addEventBtn = new Button("Add Event");
                        addEventBtn.setStyle("-fx-font-size: 14; -fx-font-family: 'Lucida Sans Unicode';");

                        addEventBtn.setOnAction(ev -> {
                            sidebarImplementation.getChildren().setAll(
                                    new AddEventPane(clickedDate, new AddEventPane.AddEventCommunication() {
                                        @Override
                                        public void onEventCreated(AddEventPane.EventData eventData) {
                                            events.putIfAbsent(clickedDate, new ArrayList<>());
                                            events.get(clickedDate).add(eventData);
                                            sidebarImplementation.getChildren().clear();
                                        }

                                        @Override
                                        public void onBack() {
                                            // Go back to just the Add button
                                            sidebarImplementation.getChildren().setAll(createCenteredBox(addEventBtn));
                                        }
                                    })
                            );
                        });

                        sidebarImplementation.getChildren().setAll(createCenteredBox(addEventBtn));

                    } else {
                        // We'll handle event details here later
                        sidebarImplementation.getChildren().clear(); // Placeholder for now
                    }
                }
            });
            calendarGrid.add(cell, column, row);
        }
    }
    private void updateMonthLabel(){
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = currentMonth.getYear();
        monthNow.setText(monthName + " " + year);
    }
    @Override
    public void start(Stage calendarWindow) {

        //Need a top header for the calendar to control current month
        //Labels for the top header gui elements
        Label lastMonth = new Label("<");
        Label nextMonth = new Label(">");
        //cant hard code it anymore, need it to update auto
        monthNow = new Label();
        updateMonthLabel();
        //stylize and size font ect for the header label
        lastMonth.setStyle("-fx-font-size: 16; -fx-cursor: hand; -fx-font-family: 'Lucida Sans Unicode';");
        nextMonth.setStyle("-fx-font-size: 16; -fx-cursor: hand; -fx-font-family: 'Lucida Sans Unicode';");
        monthNow.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-font-family: 'Lucida Sans Unicode';");

        //going to implement layout piecemeal and come back to it later if needed.
        //horizontal box to hold the 3 labels
        //has been expanded now to keep the arrows left and right in one position as no system to choose what month you want to go to exists(yet)
        GridPane header = new GridPane();
        header.setPadding(new Insets(20));
        header.setHgap(10);
        header.setVgap(10);
        header.setAlignment(Pos.CENTER);
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPercentWidth(33);
        col2.setPercentWidth(34);
        col3.setPercentWidth(33);
        header.getColumnConstraints().addAll(col1, col2, col3);
        GridPane.setHalignment(monthNow, javafx.geometry.HPos.CENTER);
        header.add(lastMonth, 0, 0);
        header.add(monthNow, 1, 0);
        header.add(nextMonth, 2, 0);
        GridPane.setHalignment(lastMonth, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(nextMonth, javafx.geometry.HPos.LEFT);
        GridPane.setHalignment(monthNow, javafx.geometry.HPos.CENTER);

        //Going to keep the Days of the week header separate from the dates because they need to further implementation
        HBox daysHeader = new HBox();
        daysHeader.setSpacing(10);
        daysHeader.setAlignment(Pos.CENTER);
        daysHeader.setPadding(new Insets(0, 0, 10, 0));
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",};
        for (String day : days) {
            Label daysLabel = new Label(day);
            daysLabel.setStyle("-fx-font-size: 14; -fx-font-family: 'Lucida Sans Unicode'; -fx-alignment: center;");
            daysLabel.setAlignment(Pos.CENTER);
            daysLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(daysLabel, Priority.ALWAYS);
            daysHeader.getChildren().add(daysLabel);
        }

        //grid system for the implementation of the calendar tracking and functionality. will need days added correctly for the month
        //days will need to skip cells until the correct day at the start of the month. must account for longer months.
        calendarGrid = new GridPane();
        for (int i = 0; i <7; i++){
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 7);
            col.setHgrow(Priority.ALWAYS);
            calendarGrid.getColumnConstraints().add(col);
        }
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / 6);
            row.setVgrow(Priority.ALWAYS);
            calendarGrid.getRowConstraints().add(row);
        }
        calendarGrid.setHgap(10);
        calendarGrid.setVgap(10);
        calendarGrid.setAlignment(Pos.CENTER);
        calendarGrid.setPadding(new Insets(10, 0, 0, 0));
        updateCalendarGrid();

        // vertical box to house the calendar boxes, dates ect. boxes need to be clickable (probably a later issue)
        VBox calendarImplementation = new VBox(header, daysHeader, calendarGrid);
        calendarImplementation.setPadding(new Insets(20));
        calendarImplementation.setSpacing(10);
        calendarImplementation.setMinWidth(960);
        HBox.setHgrow(calendarImplementation, Priority.ALWAYS);
        VBox.setVgrow(calendarGrid, Priority.ALWAYS);

        //divider line for aesthetic reasons. if someone else can implement it better. go nuts.
        Pane divider = new Pane();
        divider.setStyle("-fx-background-color: #cccccc;");
        //need to max the vertical growth so the pane always scales with window size (futureproofish)
        VBox.setVgrow(divider, Priority.ALWAYS);
        divider.setMaxHeight(Double.MAX_VALUE);
        divider.setPrefWidth(2);
        divider.setMaxWidth(2);
        divider.setMinWidth(2);
        //need to wrap the pane for placement and to set the top and bottom limits (aesthetic choice)
        VBox dividerLimits = new VBox(divider);
        dividerLimits.setAlignment(Pos.CENTER);
        dividerLimits.setPadding(new Insets(36, 0, 36, 0));
        dividerLimits.setPrefWidth(2);

        //sidebar region. will fill with something (Buttons, calendar date information, inserts, messages of the day?) something.
        sidebarImplementation = new VBox();
        sidebarImplementation.setAlignment(Pos.CENTER);
        sidebarImplementation.setPrefHeight(Double.MAX_VALUE);
        VBox.setVgrow(sidebarImplementation, Priority.ALWAYS);
        sidebarImplementation.setMinWidth(318);
        HBox.setHgrow(sidebarImplementation, Priority.ALWAYS);

        //replace the original window controls with a scene, to better control the spacing of the window
        HBox mainLayout = new HBox(calendarImplementation, dividerLimits, sidebarImplementation);
        mainLayout.setSpacing(0);
        Scene scene = new Scene(mainLayout, 1280, 720);
        calendarWindow.setMinWidth(1280);
        calendarWindow.setMinHeight(720);
        calendarWindow.setTitle("Calendar Test Window");
        calendarWindow.setScene(scene);
        calendarWindow.show();

        //functionality for clicking the month arrows left and right
        lastMonth.setOnMouseClicked(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateMonthLabel();
            updateCalendarGrid();
        });

        nextMonth.setOnMouseClicked(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthLabel();
            updateCalendarGrid();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}