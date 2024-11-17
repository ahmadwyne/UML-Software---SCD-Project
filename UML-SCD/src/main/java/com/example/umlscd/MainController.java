package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class MainController {

    @FXML
    private Button homeButton;

    @FXML
    private void initialize() {
        // Set up action for the Home button to return to the welcome page
        homeButton.setOnAction(event -> goToWelcomePage());
    }

    private void goToWelcomePage() {
        try {
            // Get the current stage (window)
            Stage stage = (Stage) homeButton.getScene().getWindow();

            // Load the Welcome page scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml"));
            Parent root = loader.load();

            // Set the Welcome page scene in the current stage
            Scene welcomeScene = new Scene(root, 800, 600);
            stage.setScene(welcomeScene);
            stage.setTitle("Welcome to UML Editor");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
