package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class ContactFormController {
    private static final Logger logger = Logger.getLogger(ContactFormController.class.getName());
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField mobileField;
    @FXML
    private Button submitButton;
    @FXML
    private Label messageLabel;

    private MainApplication mainApplication;
    private static final String CONTACTS_FILE = "contacts.txt";
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^(017|018|019|016|015|013|014)\\d{8}$");

    public void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @FXML
    public void initialize() {
        // Enable text wrapping for message label
        messageLabel.setWrapText(true);
        
        // Disable button initially
        submitButton.setDisable(true);
        
        // Add listeners to enable/disable button based on input
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        mobileField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
    }

    private void validateInputs() {
        String name = nameField.getText().trim();
        String mobile = mobileField.getText().trim();
        
        // Don't overwrite success messages - they will be cleared by the 3-second timer
        boolean isSuccessMessage = messageLabel.getTextFill().equals(Color.GREEN);
        if (isSuccessMessage) {
            // Still update button state, but don't change the message
            if (name.isEmpty() || mobile.isEmpty()) {
                submitButton.setDisable(true);
            } else if (!isValidMobileNumber(mobile)) {
                submitButton.setDisable(true);
            } else {
                submitButton.setDisable(false);
            }
            return;
        }

        if (name.isEmpty()) {
            submitButton.setDisable(true);
            showDisableReason("Name is required.");
            return;
        }
        
        if (mobile.isEmpty()) {
            submitButton.setDisable(true);
            showDisableReason("Mobile number is required.");
            return;
        }
        
        if (!isValidMobileNumber(mobile)) {
            submitButton.setDisable(true);
            if (mobile.length() != 11) {
                showDisableReason("Mobile number must be 11 digits.");
            } else {
                showDisableReason("Number must start with 017, 018, 019, 016, 015, 013, or 014.");
            }
            return;
        }
        
        // All validations passed
        submitButton.setDisable(false);
        messageLabel.setText("");
    }
    
    private void showDisableReason(String reason) {
        messageLabel.setText(reason);
        messageLabel.setTextFill(Color.RED);
    }

    private boolean isValidMobileNumber(String mobile) {
        if (mobile.length() != 11) {
            return false;
        }
        return MOBILE_PATTERN.matcher(mobile).matches();
    }

    @FXML
    protected void onSubmitButtonClick() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String mobile = mobileField.getText().trim();

        // Clear previous message
        messageLabel.setText("");

        // Validation: Name and mobile required
        if (name.isEmpty() || mobile.isEmpty()) {
            showError("Name and mobile number are required.");
            return;
        }

        // Validation: Mobile number format
        if (!isValidMobileNumber(mobile)) {
            showError("Number must start with 017, 018, 019, 016, 015, 013, or 014.");
            return;
        }

        // Check if mobile already exists
        if (isMobileExists(mobile)) {
            showError("Mobile number already in the contact list.");
            return;
        }

        // Save contact to file
        try {
            saveContact(name, address, mobile);
            showSuccess("Contact added.");
            clearFields();
            
            // After 3 seconds, show disable reason
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> {
                // Clear success message so validateInputs can show disable reason
                if (messageLabel.getTextFill().equals(Color.GREEN)) {
                    messageLabel.setTextFill(Color.RED);
                    messageLabel.setText("");
                }
                // validateInputs();
            });
            pause.play();
        } catch (IOException e) {
            showError("Error saving contact data: " + e.getMessage());
        }
    }

    private boolean isMobileExists(String mobile) {
        Path filePath = Paths.get(CONTACTS_FILE);
        if (!Files.exists(filePath)) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String storedMobile = parts[2].trim();
                    if (storedMobile.equals(mobile)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading contacts file while checking mobile existence", e);
        }
        return false;
    }

    private void saveContact(String name, String address, String mobile) throws IOException {
        Path filePath = Paths.get(CONTACTS_FILE);
        String contactData = name + "," + address + "," + mobile + System.lineSeparator();
        
        Files.write(filePath, contactData.getBytes(), 
                Files.exists(filePath) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
    }

    private void clearFields() {
        nameField.clear();
        addressField.clear();
        mobileField.clear();
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
    protected void onLogoutButtonClick() {
        try {
            mainApplication.showLoginScene();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error switching to login scene", e);
        }
    }
}
