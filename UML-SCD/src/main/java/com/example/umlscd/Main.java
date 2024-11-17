package com.example.umlscd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml"));
        Parent root = loader.load();

        // Set title and icon
        window = primaryStage;
        window.setTitle("Welcome to UML Editor");
        window.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Team.png")));

        // Set the welcome page scene
        Scene scene = new Scene(root, 800, 600);
        window.setScene(scene);
        window.show();
    }
}