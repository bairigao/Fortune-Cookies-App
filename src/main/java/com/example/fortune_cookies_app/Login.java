package com.example.fortune_cookies_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {
    public static final String TITLE = "Fortune Cookie App";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private static Stage setStage;

    /**
     * Creates the stage when application is run
     * @param loginStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
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
     * Grabs the scene dimensions set in start and applies it to any given fxml file
     * to change the scene to.
     * @param fxml fxml file name to change the scene to
     * @throws IOException
     */
    public void changeScene(String fxml) throws IOException {
        Parent base = FXMLLoader.load(getClass().getResource(fxml));
        setStage.getScene().setRoot(base);
    }

    public static void main(String[] args) {
        launch();
    }
}

