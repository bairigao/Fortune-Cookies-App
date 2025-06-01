package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for managing the trophy system in the Fortune Cookies App.
 * This controller handles the display and management of user login streak trophies.
 * Trophies are awarded for consecutive login days, with milestones at 1, 3, 7, 15, 30, 60, 90, 180, and 365 days.
 */
public class TrophyController implements Initializable {
    private static final int[] TROPHY_DAYS = {1, 3, 7, 15, 30, 60, 90, 180, 365};
    private static final int TROPHIES_PER_ROW = 3;

    @FXML private GridPane trophyGrid;
    private User user;
    private Image[] unlockedTrophies;
    private Image[] lockedTrophies;
    private ImageView[] trophyImages;
    private Label[] trophyLabels;

    /**
     * Initializes the trophy controller and loads all trophy images.
     * Creates the trophy grid layout and sets up the initial display.
     *
     * @param location The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize image arrays
        unlockedTrophies = new Image[TROPHY_DAYS.length];
        lockedTrophies = new Image[TROPHY_DAYS.length];

        // Load images
        try {
            for (int i = 0; i < TROPHY_DAYS.length; i++) {
                int days = TROPHY_DAYS[i];
                String unlockedPath = "/images/" + days + ".png";
                String lockedPath = "/images/" + days + "_locked.png";

                URL unlockedUrl = getClass().getResource(unlockedPath);
                URL lockedUrl = getClass().getResource(lockedPath);

                if (unlockedUrl == null) {
                    System.err.println("Failed to find unlocked trophy image: " + unlockedPath);
                } else {
                    unlockedTrophies[i] = new Image(unlockedUrl.toExternalForm());
                }

                if (lockedUrl == null) {
                    System.err.println("Failed to find locked trophy image: " + lockedPath);
                } else {
                    lockedTrophies[i] = new Image(lockedUrl.toExternalForm());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading trophy images: " + e.getMessage());
        }

        // Initialize trophy arrays
        trophyImages = new ImageView[TROPHY_DAYS.length];
        trophyLabels = new Label[TROPHY_DAYS.length];

        // Create trophy slots
        for (int i = 0; i < TROPHY_DAYS.length; i++) {
            VBox trophySlot = createTrophySlot(i);
            int row = i / TROPHIES_PER_ROW;
            int col = i % TROPHIES_PER_ROW;
            trophyGrid.add(trophySlot, col, row);
        }

        // Initialize trophy images and labels
        updateTrophies();
    }

    /**
     * Creates a trophy slot containing an image view and a label.
     * Each slot represents one trophy milestone in the grid.
     *
     * @param index The index of the trophy slot to create
     * @return A VBox containing the trophy image and label
     */
    private VBox createTrophySlot(int index) {
        VBox slot = new VBox(10); // 10 pixels spacing
        slot.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        trophyImages[index] = imageView;

        Label label = new Label();
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        trophyLabels[index] = label;

        slot.getChildren().addAll(imageView, label);
        return slot;
    }

    /**
     * Sets the current user and updates the trophy display.
     * This method is called when the trophy window is opened.
     *
     * @param user The user whose trophies should be displayed
     */
    public void setUser(User user) {
        this.user = user;
        updateTrophies();
    }

    /**
     * Updates the display of all trophies based on the user's current login streak.
     * Shows unlocked trophies for milestones the user has reached and locked trophies for those not yet achieved.
     */
    private void updateTrophies() {
        if (user == null) return;

        int streak = user.getLoginStreak();

        for (int i = 0; i < TROPHY_DAYS.length; i++) {
            int days = TROPHY_DAYS[i];

            if (streak >= days && unlockedTrophies[i] != null) {
                trophyImages[i].setImage(unlockedTrophies[i]);
                trophyLabels[i].setText(days == 1 ? "First Login!" : days + " Day Streak!");
            } else if (lockedTrophies[i] != null) {
                trophyImages[i].setImage(lockedTrophies[i]);
                trophyLabels[i].setText(days + " Days");
            }
        }
    }


}