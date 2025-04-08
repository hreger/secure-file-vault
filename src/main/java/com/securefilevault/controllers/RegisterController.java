package com.securefilevault.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.securefilevault.services.AuthService;
import com.securefilevault.services.DatabaseService;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton;
    @FXML private Hyperlink loginLink;
    @FXML private Label errorLabel;

    private final AuthService authService;
    private final DatabaseService databaseService;
    private final SecureRandom secureRandom;

    public RegisterController() {
        this.databaseService = new DatabaseService();
        this.authService = new AuthService(databaseService);
        this.secureRandom = new SecureRandom();
    }

    @FXML
    public void initialize() {
        registerButton.setOnAction(event -> handleRegister());
        loginLink.setOnAction(event -> handleLogin());
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        try {
            // Generate salt
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
            String saltBase64 = Base64.getEncoder().encodeToString(salt);

            // Hash password
            String passwordHash = authService.hashPassword(password, salt);

            // Create user
            if (databaseService.createUser(username, passwordHash, saltBase64)) {
                loadLoginView();
            } else {
                errorLabel.setText("Username already exists");
            }
        } catch (Exception e) {
            errorLabel.setText("Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            errorLabel.setText("Failed to load login screen");
            e.printStackTrace();
        }
    }

    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            errorLabel.setText("Failed to load login view");
            e.printStackTrace();
        }
    }
} 