package com.example.fortune_cookies_app;

import com.example.fortune_cookies_app.DB.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public TextField loginEmail;
    public PasswordField loginPassword;
    private UserDAO userDAO;
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

    //when Login button is clicked on Login screen
    public void onLoginClick() throws IOException {
        if (userDAO.login(loginEmail, loginPassword)){
            toCalendar();
        }
    }

    //when Back button is clicked on Signup screen
    public void onBackClick() throws IOException {
        toLogin();
    }

    //when Sign Up button is clicked on Signup screen
    public void onSignupConfirm() throws IOException {
        String fname = firstName.getText();
        String lname = lastName.getText();
        String email = newEmail.getText();
        String password = newPassword.getText();
        String confirm = confirmPassword.getText();

        if(!fname.isEmpty() && !lname.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirm.isEmpty() && password.equals(confirm)){
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

    protected void toCalendar(){
        Stage calendarStage = new Stage();
    }

}
