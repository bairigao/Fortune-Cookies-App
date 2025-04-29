package com.example.fortune_cookies_app;

import com.example.fortune_cookies_app.DB.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginController {
    public TextField loginEmail;
    public PasswordField loginPassword;
    private final UserDAO userDAO;
    public TextField firstName;
    public TextField lastName;
    public TextField newEmail;
    public PasswordField newPassword;
    public PasswordField confirmPassword;

    public LoginController(){
        userDAO = new UserDAO();
    }

    /** Pulls email & password from relevant fields, passing those to the login method to log the user in
     * @throws IOException
     * @throws SQLException
     */
    public void onLoginClick() throws IOException, SQLException, NoSuchAlgorithmException {
        String email = loginEmail.getText();
        String password = PasswordHasher.hashPassword(loginPassword.getText());
        if (userDAO.login(email, password)){
            toCalendar(userDAO.getUser(email));
        }
    }

    /** Upon clicking the signup button on the login scene, transitions user to the signup scene
     * @throws IOException
     */
    public void onSignupClick() throws IOException {
        toSignUp();
    }

    /** Upon clicking the confirm button on the signup scene,
     * Pulls data from relevant fields and creates a new user object, inserting that user into the database
     * @throws IOException
     */
    public void onConfirmClick() throws IOException, NoSuchAlgorithmException {
        String fname = firstName.getText();
        String lname = lastName.getText();
        String email = newEmail.getText();
        String password = PasswordHasher.hashPassword(newPassword.getText());
        String confirm = PasswordHasher.hashPassword(confirmPassword.getText());

        // Ensure all fields are filled in, and passwords match
        if (!fname.isEmpty() &&
                !lname.isEmpty() &&
                !email.isEmpty() &&
                !password.isEmpty() &&
                !confirm.isEmpty() &&
                password.equals(confirm)) {
            User user = new User(fname, lname, email, password, 1);
            userDAO.createUser(user);
            toLogin();
        }
    }

    /** Upon clicking the back button on the signup scene, transitions user to the login scene
     * @throws IOException
     */
    public void onBackClick() throws IOException {
        toLogin();
    }

    /** Method to change the scene to the login scene
     * @throws IOException
     */
    @FXML
    protected void toLogin() throws IOException {
        Login loginScene = new Login();
        loginScene.changeScene("login-view.fxml");
    }

    /** Method to change the scene to the signup scene
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
            FXMLLoader calendarLoader = new FXMLLoader(getClass().getResource("calendar-view.fxml"));

            Parent calendarRoot = calendarLoader.load();
            CalendarMainController calendarController = calendarLoader.getController();

            calendarController.setUser(user);

            Scene calendarScene = new Scene(calendarRoot);

            Stage primaryStage = (Stage) loginEmail.getScene().getWindow();

            primaryStage.setScene(calendarScene);

            primaryStage.setTitle("Calendar");

            primaryStage.show();
    }

}
