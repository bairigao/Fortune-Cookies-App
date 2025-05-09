package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {
    Stage stage;

    @FXML
    private Pane profilePane;
    @FXML
    private Button profileChangePass;

    /**
     * Handles the logout button click.
     * @throws IOException if loading saved messages popup fails
     */
    @FXML
    public void onLogOutClick() throws IOException {
        stage = (Stage) profilePane.getScene().getWindow();
        Login loginScene = new Login();
        loginScene.changeScene("login-view.fxml");
        stage.close();

    }

    /**
     * Handles the change password button click in profile.
     * @throws IOException if loading saved messages popup fails
     */
    @FXML
    public void onProfileChangePassClick() throws IOException {
        Stage stage = (Stage) profileChangePass.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/change-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setScene(scene);

    }

    /**
     * Handles the change password button click in profile.
     * @throws IOException if loading saved messages popup fails
     */
    @FXML
    public void profileConfirmPassClick() throws IOException {
        Stage stage = (Stage) profileChangePass.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/profile-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setScene(scene);

    }
}
