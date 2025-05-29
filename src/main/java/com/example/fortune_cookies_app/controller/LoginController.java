package com.example.fortune_cookies_app.controller;

import com.example.fortune_cookies_app.Login;
import com.example.fortune_cookies_app.model.AuthValidator;
import com.example.fortune_cookies_app.model.PasswordHasher;
import com.example.fortune_cookies_app.model.User;
import com.example.fortune_cookies_app.model.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * The LoginController class manages the user interactions between the login, sign-up,
 * and related scenes. It includes functionalities for user login, sign-up, password
 * change, and view navigation, as well as user-specific operations like streak tracking
 * and trophy checks.
 */
public class LoginController implements Initializable {
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
    @FXML
    private ComboBox<String> secureQuestionCombo;
    @FXML
    private TextField secureAnswer;
    @FXML
    private static final int[] TROPHY_DAYS = {1, 3, 7, 15, 30, 60, 90, 180, 365};



    /**
     * Default constructor for the LoginController class.
     * Initializes the UserDAO instance to manage user data access operations.
     */
    public LoginController() {
        userDAO = new UserDAO();
    }



    /**
     * Handles the login button click.
     * Validates user input, hashes the password, checks login credentials,
     * and transitions to the calendar view if successful.
     *
     * @throws IOException              if loading the next scene fails
     * @throws NoSuchAlgorithmException if the hashing algorithm is unavailable
     */
    @FXML
    public void onLoginClick() throws NoSuchAlgorithmException, IOException {
        loginError.setVisible(false);
        String email = loginEmail.getText();
        String password = PasswordHasher.hashPassword(loginPassword.getText());


        if (!AuthValidator.areLoginFieldsValid(email, loginPassword.getText())) {
            loginError.setText("Email and password must not be empty.");
            loginError.setVisible(true);
            return;
        }

        User user = userDAO.login(email, password);
        if (user != null) {
            user.trackLogin(); //update streak and lastLogin
            System.out.println("Updating streak: " + user.getLoginStreak());
            System.out.println("Updating lastLogin: " + user.getLastLogin());
            userDAO.updateStreak(user);  // save changes to db
            checkNewTrophies(user);
            toCalendar(user);
        } else {
            loginError.setText("Invalid email or password");
            loginError.setVisible(true);
        }
    }

    /**
     * Handles the sign-up button click from the login screen and transitions to the sign-up scene.
     *
     * @throws IOException if loading the signup scene fails
     */
    @FXML
    public void onSignupClick() throws IOException {
        Login loginScene = new Login();
        loginScene.changeScene("signup-view.fxml");
    }

    /**
     * Handles the Change Password button on the login screen.
     * @throws IOException if changing to change password scene fails
     */
    @FXML
    public void onChangePasswordClick() throws IOException {
        Login loginScene = new Login();
        loginScene.changeScene("login-passwordchange-view.fxml");
    }

    /**
     * Handles the confirm button click on the sign-up screen.
     * Validates input, checks for existing account, hashes the password,
     * and creates a new user account in the database.
     *
     * @throws IOException              if returning to login scene fails
     * @throws NoSuchAlgorithmException if password hashing algorithm is unavailable
     */
    @FXML
    public void onConfirmClick() throws IOException, NoSuchAlgorithmException {
        signupError.setVisible(false);

        String fName = firstName.getText();
        String lName = lastName.getText();
        String email = newEmail.getText();
        String rawPassword = newPassword.getText();
        String confirm = confirmPassword.getText();
        String selectedQuestion = secureQuestionCombo.getValue();
        String answer = secureAnswer.getText();


        if (!AuthValidator.areSignupFieldsValid(fName, lName, email, rawPassword, confirm)) {
            signupError.setText("All fields must be filled.");
            signupError.setVisible(true);
            return;
        }

        if (!AuthValidator.isValidName(fName)) {
            signupError.setText("Invalid first name");
            signupError.setVisible(true);
            return;
        }

        if (!AuthValidator.isValidName(lName)) {
            signupError.setText("Invalid last name");
            signupError.setVisible(true);
            return;
        }

        if (!AuthValidator.isValidEmail(email)) {
            signupError.setText("Invalid email format.");
            signupError.setVisible(true);
            return;
        }

        if (!AuthValidator.isPasswordConfirmed(rawPassword, confirm)) {
            signupError.setText("Passwords do not match.");
            signupError.setVisible(true);
            return;
        }

        if (!AuthValidator.isStrongPassword(rawPassword)) {
            signupError.setText("Password must be at least 8 characters with uppercase, lowercase, digit, and special character.");
            signupError.setVisible(true);
            return;
        }

        String hashedPassword = PasswordHasher.hashPassword(rawPassword);

        if (userDAO.checkEmail(email)) {
            signupError.setText("An account with this email already exists.");
            signupError.setVisible(true);
            return;
        }

        if (selectedQuestion == null || selectedQuestion.isEmpty()) {
            signupError.setText("Please select a security question.");
            signupError.setVisible(true);
            return;
        }

        if (answer == null || answer.isBlank()) {
            signupError.setText("Please provide an answer to the security question.");
            signupError.setVisible(true);
            return;
        }


        User user = new User(fName, lName, email, hashedPassword, selectedQuestion, answer, 1);
        userDAO.createUser(user);
        toLogin();
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

    /**
     * Navigates to the calendar view scene.
     * This method loads the calendar-view.fxml file, sets up the scene with a CalendarMainController,
     * and transitions the primary stage to display the calendar.
     *
     * @param user The user object that needs to be passed to the calendar
     * @throws IOException If the FXML file cannot be loaded
     * @param user User that is passed to the calendar controller - used to populate the calendar &amp; create new events
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
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

    /**
     * Checks if the user has earned any new trophies and displays appropriate notifications.
     * This method is called after successful login.
     *
     * @param user The user to check trophies for
     */
    private void checkNewTrophies(User user) {
        if (user == null) return;

        int streak = user.getLoginStreak();
        int previousStreak = streak - 1; // Get the previous streak value

        // Check if user just earned a new trophy by comparing with previous streak
        for (int days : TROPHY_DAYS) {
            if (streak == days && previousStreak < days) {
                String title = days == 1 ? "First Login!" : days + " Day Streak!";
                String content = days == 1 ?
                        "Congratulations on your first login!" :
                        "Amazing! You've logged in for " + days + " consecutive days!";
                showTrophyAlert(title, content);
            }
        }
    }

    /**
     * Displays a notification when a new trophy is earned.
     *
     * @param title The title of the trophy earned
     * @param content The congratulatory message to display
     */
    private void showTrophyAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("New Trophy Earned!");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Initializes the components and sets up event handlers for the login scene.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (secureQuestionCombo != null) {
            secureQuestionCombo.getItems().addAll(
                    "What is your pet's name?",
                    "What is your favorite food?",
                    "What is your mother's maiden name?",
                    "What city were you born in?"
            );
        }

        if (loginEmail != null){
            loginEmail.setOnKeyPressed(this::handleEnter);
            loginPassword.setOnKeyPressed(this::handleEnter);
        }
    }

    private void handleEnter(KeyEvent keyPress){
        if (keyPress.getCode() == KeyCode.ENTER){
            try {
                onLoginClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
