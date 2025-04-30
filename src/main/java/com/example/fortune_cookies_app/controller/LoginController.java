package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.Login;
import com.example.fortune_cookies_app.model.PasswordHasher;
import com.example.fortune_cookies_app.model.User;
import com.example.fortune_cookies_app.model.UserDAO;
import com.example.fortune_cookies_app.model.AuthValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    public TextField loginEmail;
    @FXML
    public PasswordField loginPassword;
    @FXML
    public Label loginError;
    private final UserDAO userDAO;
    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField newEmail;
    @FXML
    public PasswordField newPassword;
    @FXML
    public PasswordField confirmPassword;
    @FXML
    private Label signupError;



    public LoginController(){
        userDAO = new UserDAO();
    }

    /** Pulls email & password from relevant fields, passing those to the login method to log the user in
     * @throws IOException
     * @throws SQLException
     */
    public void onLoginClick() throws IOException, SQLException, NoSuchAlgorithmException {
        loginError.setVisible(false);
        String email = loginEmail.getText();
        String password = PasswordHasher.hashPassword(loginPassword.getText());

        //
        if (!AuthValidator.areLoginFieldsValid(email, loginPassword.getText())) {
            loginError.setText("Email and password must not be empty.");
            loginError.setVisible(true);
            return;
        }

        User user = userDAO.login(email, password);
        if (user != null){
            user.trackLogin(); //update streak and lastLogin
            System.out.println("Updating streak: " + user.getLoginStreak());
            System.out.println("Updating lastLogin: " + user.getLastLogin());
            userDAO.updateStreak(user);  // save changes to db
            toCalendar(user);
        } else {
            loginError.setText("Invalid email or password");
            loginError.setVisible(true);
        }
    }

    /** When signup button is clicked on login scene, scene changes to signup scene
     * @throws IOException
     */
    public void onSignupClick() throws IOException {
        toSignUp();
    }

    /** When the confirm button on the signup scene is clicked,
     * Pulls data from relevant fields and creates a new user object, inserting that user into the database
     * @throws IOException
     */
    public void onConfirmClick() throws IOException, NoSuchAlgorithmException {
        signupError.setVisible(false);

        String fName = firstName.getText();
        String lName = lastName.getText();
        String email = newEmail.getText();
        String rawPassword = newPassword.getText();
        String confirm = confirmPassword.getText();

        // Validate fields BEFORE hashing
        if (!AuthValidator.areSignupFieldsValid(fName, lName, email, rawPassword, confirm)) {
            signupError.setText("All fields must be filled.");
            signupError.setVisible(true);
            return;
        }

        if (!AuthValidator.isPasswordConfirmed(rawPassword, confirm)) {
            signupError.setText("Passwords do not match.");
            signupError.setVisible(true);
            return;
        }

        // Hash only after passing validation
        String hashedPassword = PasswordHasher.hashPassword(rawPassword);

        //  check if email already exists
        if (userDAO.checkEmail(email)) {
            signupError.setText("An account with this email already exists.");
            signupError.setVisible(true);
            return;
        }

        User user = new User(fName, lName, email, hashedPassword, 1);
        userDAO.createUser(user);
        toLogin();
    }


    /** When the back button is clicked on the signup scene, scene changes to login scene
     * @throws IOException
     */
    public void onBackClick() throws IOException {
        toLogin();
    }

    /** Method to change the scene to the login scene using the login scene fxml file name
     * @throws IOException
     */
    @FXML
    protected void toLogin() throws IOException {
        Login loginScene = new Login();
        loginScene.changeScene("login-view.fxml");
    }

    /** Method to change the scene to the signup scene using the signup scene fxml file name
     * @throws IOException
     */
    @FXML
    protected void toSignUp() throws IOException {
        Login loginScene = new Login();
        loginScene.changeScene("signup-view.fxml");

    }

    /**
     * This method transitions the user to the calendar upon successful login
     * @param user User that is passed to the calendar controller - used to populate the calendar & create new events
     * @throws IOException
     */
    protected void toCalendar(User user) throws IOException {

        FXMLLoader calendarLoader = new FXMLLoader(getClass().getResource("/com/example/fortune_cookies_app/calendar-view.fxml"));

            Parent calendarRoot = calendarLoader.load();
            CalendarMainController calendarController = calendarLoader.getController();

            calendarController.setUser(user);

            Scene calendarScene = new Scene(calendarRoot);

            Stage primaryStage = (Stage) loginEmail.getScene().getWindow();

            primaryStage.setScene(calendarScene);

            primaryStage.setTitle("Calendar");
            primaryStage.setMinWidth(1280);
            primaryStage.setMinHeight(720);
            primaryStage.show();

    }

}
