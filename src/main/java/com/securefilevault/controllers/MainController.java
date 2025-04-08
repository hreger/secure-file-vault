package com.securefilevault.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.securefilevault.services.EncryptionService;
import com.securefilevault.services.DatabaseService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private Button encryptButton;
    @FXML private Button decryptButton;
    @FXML private ListView<String> fileListView;
    @FXML private Label statusLabel;

    private final EncryptionService encryptionService;
    private final DatabaseService databaseService;
    private String currentUsername;

    public MainController() {
        this.encryptionService = new EncryptionService();
        this.databaseService = new DatabaseService();
    }

    @FXML
    public void initialize() {
        setupEventHandlers();
    }

    public void setUsername(String username) {
        this.currentUsername = username;
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    private void setupEventHandlers() {
        logoutButton.setOnAction(event -> handleLogout());
        encryptButton.setOnAction(event -> handleEncrypt());
        decryptButton.setOnAction(event -> handleDecrypt());
    }

    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            statusLabel.setText("Failed to logout");
            e.printStackTrace();
        }
    }

    private void handleEncrypt() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Encrypt");
        File selectedFile = fileChooser.showOpenDialog(encryptButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                String password = showPasswordDialog("Enter encryption password:");
                if (password != null) {
                    File encryptedFile = new File(selectedFile.getAbsolutePath() + ".encrypted");
                    encryptionService.encryptFile(selectedFile, encryptedFile, password);
                    fileListView.getItems().add(encryptedFile.getName());
                    statusLabel.setText("File encrypted successfully: " + encryptedFile.getName());
                }
            } catch (Exception e) {
                statusLabel.setText("Encryption failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void handleDecrypt() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Decrypt");
        File selectedFile = fileChooser.showOpenDialog(decryptButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                String password = showPasswordDialog("Enter decryption password:");
                if (password != null) {
                    String outputPath = selectedFile.getAbsolutePath().replace(".encrypted", "");
                    File decryptedFile = new File(outputPath);
                    encryptionService.decryptFile(selectedFile, decryptedFile, password);
                    fileListView.getItems().add(decryptedFile.getName());
                    statusLabel.setText("File decrypted successfully: " + decryptedFile.getName());
                }
            } catch (Exception e) {
                statusLabel.setText("Decryption failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String showPasswordDialog(String message) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Password Required");
        dialog.setHeaderText(message);

        PasswordField passwordField = new PasswordField();
        dialog.getDialogPane().setContent(passwordField);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return passwordField.getText();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }
} 