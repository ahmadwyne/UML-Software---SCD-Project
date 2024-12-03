package com.example.umlscd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;

/**
 * <h1>UML Class Diagram Editor</h1>
 *
 * <p>The {@code Main} class serves as the entry point for the UML Class Diagram Editor application.
 * This JavaFX-based application allows users to create, edit, and manage UML class diagrams with ease.
 * Users can add classes, interfaces, and define various relationships such as associations, aggregations,
 * compositions, and inheritances. The editor provides functionalities to save and load diagrams,
 * ensuring data persistence, and export diagrams as image files (PNG or JPEG) for sharing and documentation purposes.</p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *     <li>Add and remove classes and interfaces.</li>
 *     <li>Define relationships between UML elements.</li>
 *     <li>Drag and reposition elements within the diagram.</li>
 *     <li>Save diagrams to JSON files and load existing diagrams.</li>
 *     <li>Export diagrams as high-quality image files.</li>
 * </ul>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class Main extends Application {

    /**
     * The {@code start} method is the main entry point for all JavaFX applications.
     * It is called after the application has been launched and is responsible for
     * setting up the primary stage and initializing the user interface.
     *
     * <p>This method performs the following tasks:
     * <ul>
     *     <li>Loads the {@code welcome.fxml} file to set up the initial scene.</li>
     *     <li>Sets the title and icon for the primary stage.</li>
     *     <li>Configures the scene dimensions and displays the primary stage to the user.</li>
     * </ul>
     * </p>
     *
     * @param primaryStage The primary stage for this application onto which the
     *                     application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the WelcomePage as the initial scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml"));
            Parent root = loader.load();

            // Set title and icon
            Stage window = primaryStage;
            window.setTitle("Welcome to UML Editor");
            window.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Team.png")));

            // Set the initial scene and show the primary stage
            Scene welcomeScene = new Scene(root, 1366, 768);
            primaryStage.setScene(welcomeScene);
            primaryStage.setTitle("Welcome to UML Editor");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The {@code main} method is the standard entry point for Java applications.
     * It launches the JavaFX application by invoking the {@code launch} method.
     *
     * @param args The command-line arguments passed to the application. These arguments
     *             are not used in this application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
