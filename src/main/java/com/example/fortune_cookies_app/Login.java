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

    @Override
    public void start(Stage loginStage) throws IOException {
        setStage = loginStage;
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        loginStage.setTitle(TITLE);
        loginStage.setScene(scene);
        loginStage.show();
    }

    //used to change scenes based on given fxml file name
    public void changeScene(String fxml) throws IOException {
        Parent base = FXMLLoader.load(getClass().getResource(fxml));
        setStage.getScene().setRoot(base);
    }

    public static void main(String[] args) {
        launch();
    }
}

