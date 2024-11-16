package com.example.umlscd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.image.Image;

public class Main extends Application {

    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/main.fxml"));
        Parent root = loader.load();

        // Set title and icon
        String fileName = "Untitled";
        window = primaryStage;
        window.setTitle("UML Editor: " + fileName);
        window.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Team_Rocket_Logo.jpg")));

        // Set the scene with a defined size
        Scene scene = new Scene(root, 1000, 600);
        window.setScene(scene);
        window.show();
    }
}

