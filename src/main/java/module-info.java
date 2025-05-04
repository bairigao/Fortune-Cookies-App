module com.example.fortune_cookies_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;


    opens com.example.fortune_cookies_app to javafx.fxml;
    exports com.example.fortune_cookies_app;
    exports com.example.fortune_cookies_app.model;
    exports com.example.fortune_cookies_app.controller;
    opens com.example.fortune_cookies_app.controller to javafx.fxml;
    opens com.example.fortune_cookies_app.model to javafx.fxml;

}