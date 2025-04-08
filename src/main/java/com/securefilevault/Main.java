package com.securefilevault;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Starting application...");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            System.out.println("FXML loaded successfully");
            
            primaryStage.setTitle("Secure File Vault");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
            System.out.println("Stage shown");
        } catch (Exception e) {
            System.err.println("Error starting application:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Launching JavaFX application...");
        launch(args);
    }
} 