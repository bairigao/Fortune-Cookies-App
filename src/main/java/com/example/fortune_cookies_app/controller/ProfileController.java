package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    @FXML
    private Button confirmChangePassword;
    @FXML
    private Button profileBackButton;

    /**
     * Handles the logout button click.
     * @throws IOException if logging out fails
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
     * @throws IOException if loading password change scene fails
     */
    @FXML
    public void onProfileChangePassClick() throws IOException {
        Stage stage = (Stage) profileChangePass.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/change-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setScene(scene);

    }

    /**
     * Handles the confirm password button click in profile password change.
     * @throws IOException if going back to profile scene after password confirms fails
     */
    @FXML
    public void profileConfirmPassClick() throws IOException {
        Stage stage = (Stage) confirmChangePassword.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/profile-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setScene(scene);

    }

    /**
     * Handles the back button click in profile password change scene.
     * @throws IOException if loading profile scene fails
     */
    @FXML
    public void onBackButtonClick() throws IOException {
        Stage stage = (Stage) profileBackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/profile-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setScene(scene);

    }
}
