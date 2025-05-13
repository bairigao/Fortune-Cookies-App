package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.Login;
import com.example.fortune_cookies_app.model.AuthValidator;
import com.example.fortune_cookies_app.model.User;
import com.example.fortune_cookies_app.model.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class PasswordController {
    @FXML
    private VBox step1Pane, step2Pane, step3Pane;
    @FXML
    private TextField emailField, securityAnswerField;
    @FXML
    private PasswordField newPasswordField, confirmPasswordField;
    @FXML
    private Label securityQuestionLabel, feedbackLabel;

    private final UserDAO userDAO = new UserDAO();
    private User currentUser;

    public void onEmailSubmit(ActionEvent actionEvent) {
        String email = emailField.getText().trim().toLowerCase();
        currentUser = userDAO.findByEmail(email);
        if (currentUser != null) {
            securityQuestionLabel.setText(currentUser.getSecurityQuestion());
            feedbackLabel.setVisible(false);
            step1Pane.setVisible(false); step1Pane.setManaged(false);
            step2Pane.setVisible(true);  step2Pane.setManaged(true);
        } else {
            showError("Email not found.");
        }
    }

    @FXML
    private void onSecurityAnswerSubmit() {
        String answer = securityAnswerField.getText().trim();
        if (answer.equalsIgnoreCase(currentUser.getSecurityAnswer())) {
            feedbackLabel.setVisible(false);
            step2Pane.setVisible(false); step2Pane.setManaged(false);
            step3Pane.setVisible(true);  step3Pane.setManaged(true);
        } else {
            showError("Incorrect answer.");
        }
    }

    @FXML
    private void onPasswordChangeSubmit() {
        String newPwd = newPasswordField.getText();
        String confirmPwd = confirmPasswordField.getText();

        if (!AuthValidator.isStrongPassword(newPwd)) {
            showError("Password must be at least 8 characters long and include an uppercase letter, lowercase letter, number, and special character.");
            return;
        }

        if (!AuthValidator.isPasswordConfirmed(newPwd, confirmPwd)) {
            showError("Passwords do not match.");
            return;
        }

        userDAO.updatePassword(currentUser, currentUser.getPassword(), newPwd);
        showSuccess("Password successfully changed!");
    }

    private void showError(String message) {
        feedbackLabel.setTextFill(Color.RED);
        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        feedbackLabel.setTextFill(Color.GREEN);
        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);
    }

    /**
     * Handles the back button click on the sign-up screen and returns to the login scene.
     *
     * @throws IOException if loading the login scene fails
     */
    @FXML
    public void onBackClick() throws IOException {
        toLogin();
    }

    /**
     * Changes the current scene to the login view.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    protected void toLogin() throws IOException {
        Login loginScene = new Login();
        loginScene.changeScene("login-view.fxml");
    }
}