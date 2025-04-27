module com.example.fortune_cookies_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.fortune_cookies_app to javafx.fxml;
    exports com.example.fortune_cookies_app;
    exports com.example.fortune_cookies_app.DB; // For DAO classes

}