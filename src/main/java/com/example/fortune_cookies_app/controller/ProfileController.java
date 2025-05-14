package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.Login;
import com.example.fortune_cookies_app.model.PasswordHasher;
import com.example.fortune_cookies_app.model.User;
import com.example.fortune_cookies_app.model.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ProfileController {
    public PasswordField newPassword;
    public PasswordField confirmNewPasswordText;
    public Button profileBackButton;
    private final UserDAO userDAO = new UserDAO();
    public PasswordField currentPassword;
    private User user;
    Stage stage;

    @FXML
    private Pane profilePane;
    @FXML
    private Button profileChangePass;
    @FXML
    private Button confirmChangePassword;
    @FXML
    private Button profileLogoutButton;

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
        FXMLLoader profileLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/change-password-view.fxml"));
        Parent profileRoot = profileLoader.load();
        ProfileController profileController = profileLoader.getController();

        profileController.setUser(this.user);

        Scene profileScene = new Scene(profileRoot, 400, 300);

        stage.setTitle("Profile");
        stage.setScene(profileScene);
        stage.show();
    }

    /**
     * Handles the confirm password button click in profile password change.
     * @throws IOException if going back to profile scene after password confirms fails
     */
    @FXML
    public void profileConfirmPassClick() throws IOException, NoSuchAlgorithmException {
        String hashedCurrentPassword = PasswordHasher.hashPassword(currentPassword.getText());
        String hashedNewPassword = PasswordHasher.hashPassword(newPassword.getText());
        userDAO.updatePassword(this.user, hashedCurrentPassword, hashedNewPassword);
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
        Stage stage = (Stage) profileLogoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/profile-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setScene(scene);

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
