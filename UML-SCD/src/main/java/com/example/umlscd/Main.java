package com.example.umlscd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the WelcomePage as the initial scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml"));
            Parent root = loader.load();

            // Set the initial scene and show the primary stage
            Scene welcomeScene = new Scene(root, 1000, 600);
            primaryStage.setScene(welcomeScene);
            primaryStage.setTitle("Welcome to UML Editor");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}