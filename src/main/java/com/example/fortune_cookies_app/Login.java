package com.example.fortune_cookies_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * The Login class serves as the entry point for the JavaFX application.
 * It initializes the primary stage, sets up the initial scene, and provides
 * functionality to change scenes within the application.
 */
public class Login extends Application {
    private static final String TITLE = "Fortune Cookie App";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static Stage setStage;

    /**
     * Initializes and displays the primary stage of the application.
     *
     * @param loginStage the primary stage provided by the JavaFX runtime
     * @throws IOException if there is an error loading the FXML resource
     */
    @Override
    public void start(Stage loginStage) throws IOException {
        setStage = loginStage;
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        loginStage.setTitle(TITLE);
        loginStage.setScene(scene);
        loginStage.show();
    }

    /**
     * Changes the current scene of the application by loading the specified FXML file
     * and setting it as the root of the current scene.
     *
     * @param fxml the name of the FXML file to be loaded for the new scene
     * @throws IOException if the FXML file cannot be loaded
     */
    public void changeScene(String fxml) throws IOException {
        Parent base = FXMLLoader.load(getClass().getResource(fxml));
        setStage.getScene().setRoot(base);
    }

    /**
     * The main entry point for the JavaFX application.
     * This method launches the JavaFX runtime which in turn calls the {@code start} method.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch();
    }
}

