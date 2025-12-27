package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label messageLabel;

    private MainApplication mainApplication;
    private static final String USERS_FILE = "users.txt";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @FXML
    public void initialize() {
        // Enable text wrapping for message label
        messageLabel.setWrapText(true);

        // Disable button initially
        loginButton.setDisable(true);
        
        // Add listeners to enable/disable button based on input
        emailField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
    }

    private void validateInputs() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty()) {
            loginButton.setDisable(true);
            showDisableReason("Email is required.");
            return;
        }
        
        if (password.isEmpty()) {
            loginButton.setDisable(true);
            showDisableReason("Password is required.");
            return;
        }
        
        if (!isValidEmail(email)) {
            loginButton.setDisable(true);
            showDisableReason("Please enter a valid email address.");
            return;
        }
        
        // All validations passed
        loginButton.setDisable(false);
        messageLabel.setText("");
    }
    
    private void showDisableReason(String reason) {
        messageLabel.setText(reason);
        messageLabel.setTextFill(Color.RED);
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    @FXML
    protected void onLoginButtonClick() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Clear previous message
        messageLabel.setText("");

        // Validation: All fields required
        if (email.isEmpty() || password.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        // Validation: Email format
        if (!isValidEmail(email)) {
            showError("Please enter a valid email address.");
            return;
        }

        // Check credentials
        LoginResult result = validateCredentials(email, password);
        
        if (result == LoginResult.SUCCESS) {
            messageLabel.setText("");
            clearFields();
            showSuccess("User logged in.");

            // Switch to contact form scene after a short delay
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                try {
                    mainApplication.showContactFormScene();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error switching to contact form scene", ex);
                }
            });
            pause.play();
        } else if (result == LoginResult.USER_NOT_FOUND) {
            showError("User not found.");
        } else if (result == LoginResult.PASSWORD_MISMATCH) {
            showError("Password mismatch.");
        }
    }

    private LoginResult validateCredentials(String email, String password) {
        Path filePath = Paths.get(USERS_FILE);
        if (!Files.exists(filePath)) {
            return LoginResult.USER_NOT_FOUND;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String storedEmail = parts[1].trim();
                    String storedPassword = parts[2].trim();
                    
                    if (storedEmail.equalsIgnoreCase(email)) {
                        if (storedPassword.equals(password)) {
                            return LoginResult.SUCCESS;
                        } else {
                            return LoginResult.PASSWORD_MISMATCH;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading users file during login validation", e);
        }
        return LoginResult.USER_NOT_FOUND;
    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.GREEN);
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.RED);
    }

    @FXML
    protected void onSwitchToRegistrationClick() {
        try {
            mainApplication.showRegistrationScene();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error switching to registration scene", e);
        }
    }

    private enum LoginResult {
        SUCCESS, USER_NOT_FOUND, PASSWORD_MISMATCH
    }
}

