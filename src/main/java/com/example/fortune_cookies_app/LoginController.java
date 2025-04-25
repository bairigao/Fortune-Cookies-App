package com.example.fortune_cookies_app;

import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {

    //change where the buttons will lead the user accordingly later,
    //currently only leads from login to signup and vice versa

    //when Sign Up button is clicked on Login screen
    public void onSignupClick() throws IOException {
        toSignUp();
    }

    //when Login button is clicked on Login screen
    public void onLoginClick() throws IOException {
        toSignUp();
    }

    //when Back button is clicked on Signup screen
    public void onBackClick() throws IOException {
        toLogin();
    }

    //when Sign Up button is clicked on Signup screen
    public void onSignupConfirm() throws IOException {
        toLogin();
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

}
