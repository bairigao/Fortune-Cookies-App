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
    //change where the buttons will lead the user accordingly later,
    //currently only leads from login to signup and vice versa

    //when Sign Up button is clicked on Login screen
    public void onSignupClick() throws IOException {
        toSignUp();
    }

    /** Pulls email & password from relevant fields, passing those to the login method to log the user in
     * @throws IOException
     * @throws SQLException
     */
    public void onLoginClick() throws IOException, SQLException {
        String email = loginEmail.getText();
        String password = loginPassword.getText();
        if (userDAO.login(email, password)){
            toCalendar(userDAO.getUser(email));
        }
    }

    //when Back button is clicked on Signup screen
    public void onBackClick() throws IOException {
        toLogin();
    }

    /** Pulls data from relevant fields and creates a new user object, inserting that user into the database
     * @throws IOException
     */
    //when Sign Up button is clicked on Signup screen
    public void onSignupConfirm() throws IOException {
        String fname = firstName.getText();
        String lname = lastName.getText();
        String email = newEmail.getText();
        String password = newPassword.getText();
        String confirm = confirmPassword.getText();

        // Ensure all fields are filled in, and passwords match
        if (!fname.isEmpty() &&
                !lname.isEmpty() &&
                !email.isEmpty() &&
                !password.isEmpty() &&
                !confirm.isEmpty() &&
                password.equals(confirm)) {
            User user = new User(fname, lname, email, password);
            userDAO.createUser(user);
            toLogin();
        }
    }

    //changes scene to login screen
    @FXML
    protected void toLogin() throws IOException {
        Login loginScreen = new Login();
        loginScreen.changeScene("login-view.fxml");
    }

    //changes scene to sign up
    @FXML
    protected void toSignUp() throws IOException {
        Login loginScreen = new Login();
        loginScreen.changeScene("signup-view.fxml");

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
