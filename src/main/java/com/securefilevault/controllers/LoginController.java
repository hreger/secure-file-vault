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

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerLink;
    @FXML private Label errorLabel;

    private final AuthService authService;
    private final DatabaseService databaseService;

    public LoginController() {
        this.databaseService = new DatabaseService();
        this.authService = new AuthService(databaseService);
    }

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        registerLink.setOnAction(event -> handleRegister());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }

        try {
            if (authService.authenticate(username, password)) {
                loadMainView();
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            errorLabel.setText("An error occurred during login");
            e.printStackTrace();
        }
    }

    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            errorLabel.setText("Failed to load registration screen");
            e.printStackTrace();
        }
    }

    private void loadMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            errorLabel.setText("Failed to load main view");
            e.printStackTrace();
        }
    }
} 