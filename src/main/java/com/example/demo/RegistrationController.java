package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class RegistrationController {
    private static final Logger logger = Logger.getLogger(RegistrationController.class.getName());
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerButton;
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
        registerButton.setDisable(true);
        
        // Add listeners to enable/disable button based on input
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
    }

    private void validateInputs() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        boolean isSuccessMessage = messageLabel.getTextFill().equals(Color.GREEN);

        if (name.isEmpty()) {
            registerButton.setDisable(true);
            if (!isSuccessMessage) {
                showDisableReason("Name is required.");
            }
            return;
        }
        
        if (email.isEmpty()) {
            registerButton.setDisable(true);
            if (!isSuccessMessage) {
                showDisableReason("Email is required.");
            }
            return;
        }
        
        if (password.isEmpty()) {
            registerButton.setDisable(true);
            if (!isSuccessMessage) {
                showDisableReason("Password is required.");
            }
            return;
        }
        
        if (confirmPassword.isEmpty()) {
            registerButton.setDisable(true);
            if (!isSuccessMessage) {
                showDisableReason("Confirm password is required.");
            }
            return;
        }
        
        if (!isValidEmail(email)) {
            registerButton.setDisable(true);
            if (!isSuccessMessage) {
                showDisableReason("Please enter a valid email address.");
            }
            return;
        }
        
        // All validations passed
        registerButton.setDisable(false);
        if (!isSuccessMessage) {
            messageLabel.setText("");
        }
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
    protected void onRegisterButtonClick() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Clear previous message
        messageLabel.setText("");

        // Validation: All fields required
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        // Validation: Email format
        if (!isValidEmail(email)) {
            showError("Please enter a valid email address.");
            return;
        }

        // Validation: Password match
        if (!password.equals(confirmPassword)) {
            showError("Password mismatch.");
            return;
        }

        // Check if user already exists
        if (isUserRegistered(email)) {
            showError("User already registered.");
            return;
        }

        // Save user to file
        try {
            saveUser(name, email, password);
            showSuccess("Registration completed. Please log in.");
            clearFields();
            
            // Switch to log-in scene after a short delay
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                try {
                    mainApplication.showLoginScene();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error switching to login scene after registration", ex);
                }
            });
            pause.play();
        } catch (IOException e) {
            showError("Error saving user data: " + e.getMessage());
        }
    }

    private boolean isUserRegistered(String email) {
        Path filePath = Paths.get(USERS_FILE);
        if (!Files.exists(filePath)) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading users file while checking if user is registered", e);
        }
        return false;
    }

    private void saveUser(String name, String email, String password) throws IOException {
        Path filePath = Paths.get(USERS_FILE);
        String userData = name + "," + email + "," + password + System.lineSeparator();
        
        Files.write(filePath, userData.getBytes(), 
                Files.exists(filePath) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
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
    protected void onSwitchToLoginClick() {
        try {
            mainApplication.showLoginScene();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error switching to login scene", e);
        }
    }
}
