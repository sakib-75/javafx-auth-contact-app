module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}