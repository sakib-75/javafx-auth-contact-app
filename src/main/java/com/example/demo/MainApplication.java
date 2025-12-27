package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        showLoginScene();
        stage.setTitle("JavaFX Application");
        stage.setResizable(true);
        stage.show();
    }

    public void showLoginScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController controller = fxmlLoader.getController();
        controller.setMainApplication(this);
        primaryStage.setScene(scene);
    }

    public void showRegistrationScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        RegistrationController controller = fxmlLoader.getController();
        controller.setMainApplication(this);
        primaryStage.setScene(scene);
    }

    public void showContactFormScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("contact-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ContactFormController controller = fxmlLoader.getController();
        controller.setMainApplication(this);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
