module com.example.fortune_cookies_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fortune_cookies_app to javafx.fxml;
    exports com.example.fortune_cookies_app;
}