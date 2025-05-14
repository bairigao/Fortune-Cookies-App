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

/**
 * Controller for the Change Password view.
 * Handles the password reset process including email verification,
 * security question validation, and password updating.
 */
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

    /**
     * Handles the action when the "Next" button is clicked on the email step.
     * Verifies if the entered email exists in the database and moves to the security question step.
     *
     * @param actionEvent the event triggered by clicking the button
     */
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

    /**
     * Handles the action when the "Next" button is clicked on the security question step.
     * Validates the user's answer and proceeds to the password change step if correct.
     */
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

    /**
     * Handles the action when the "Change Password" button is clicked.
     * Validates the new password strength and confirmation before updating the user's password.
     */
    @FXML
    private void onPasswordChangeSubmit() {
        String newPwd = newPasswordField.getText();
        String confirmPwd = confirmPasswordField.getText();

        if (!AuthValidator.isPasswordChanged(currentUser.getPassword(), newPwd)) {
            showError("Your new password cannot be the same as your current password.");
            return;
        }

        if (!AuthValidator.isStrongPassword(newPwd)) {
            showError("Password must be at least 8 characters long and include an uppercase letter, lowercase letter, number, and special character.");
            return;
        }

        if (!AuthValidator.isPasswordConfirmed(newPwd, confirmPwd)) {
            showError("Passwords do not match.");
            return;
        }

        userDAO.resetPassword(currentUser, newPwd);
        currentUser.setPassword(newPwd);
        showSuccess("Password successfully changed!");
    }

    /**
     * Displays an error message in red on the feedback label.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        feedbackLabel.setTextFill(Color.RED);
        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);
    }

    /**
     * Displays a success message in green on the feedback label.
     *
     * @param message the success message to display
     */
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