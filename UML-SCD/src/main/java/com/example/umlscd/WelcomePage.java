package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * <h1>Welcome Page Controller</h1>
 *
 * <p>The {@code WelcomePage} class serves as the controller for the welcome screen of the UML Editor application.
 * It handles the initialization of UI components, manages user interactions with the "Class Diagram" and
 * "Use Case Diagram" buttons, and facilitates navigation to the respective diagram editing screens.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class WelcomePage {

    @FXML
    private Button btnClassDiagram;

    @FXML
    private Button btnUseCaseDiagram;

    @FXML
    private ImageView logoImageView;

    /**
     * Initializes the welcome page by setting up the logo image, adding hover effects to buttons,
     * and defining actions for button clicks.
     *
     * <p>This method is automatically called after the FXML file has been loaded. It ensures that
     * the UI components are properly configured and responsive to user interactions.</p>
     */
    @FXML
    private void initialize() {
        // Load the logo image
        logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/Team.png")));

        // Add hover effects for Class Diagram button
        btnClassDiagram.setOnMouseEntered(event -> applyHoverEffect(btnClassDiagram));
        btnClassDiagram.setOnMouseExited(event -> removeHoverEffect(btnClassDiagram));

        // Add hover effects for Use Case Diagram button
        btnUseCaseDiagram.setOnMouseEntered(event -> applyHoverEffect(btnUseCaseDiagram));
        btnUseCaseDiagram.setOnMouseExited(event -> removeHoverEffect(btnUseCaseDiagram));

        // Button actions
        btnClassDiagram.setOnAction(event -> loadClassDiagram());
        btnUseCaseDiagram.setOnAction(event -> loadUseCaseDiagram());
    }

    /**
     * Applies a hover effect to the specified button by changing its style to a highlighted state.
     *
     * @param button The {@code Button} to which the hover effect will be applied.
     */
    private void applyHoverEffect(Button button) {
        button.setStyle("-fx-font-size: 18px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #111111; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 230; -fx-pref-height: 60; -fx-background-radius: 15;");
    }

    /**
     * Removes the hover effect from the specified button by reverting its style to the default state.
     *
     * @param button The {@code Button} from which the hover effect will be removed.
     */
    private void removeHoverEffect(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 220; -fx-pref-height: 55; -fx-background-radius: 15;");
    }

    /**
     * Handles the mouse enter event for the "Class Diagram" button by applying a hover effect.
     *
     * @FXML This method is linked to the FXML file and is invoked when the mouse enters the "Class Diagram" button.
     */
    @FXML
    private void onMouseEnterClassDiagram() {
        btnClassDiagram.setStyle("-fx-font-size: 18px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 230; -fx-pref-height: 60; -fx-background-radius: 15;");
    }

    /**
     * Handles the mouse exit event for the "Class Diagram" button by removing the hover effect.
     *
     * @FXML This method is linked to the FXML file and is invoked when the mouse exits the "Class Diagram" button.
     */
    @FXML
    private void onMouseExitClassDiagram() {
        btnClassDiagram.setStyle("-fx-font-size: 16px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 220; -fx-pref-height: 55; -fx-background-radius: 15;");
    }

    /**
     * Handles the mouse enter event for the "Use Case Diagram" button by applying a hover effect.
     *
     * @FXML This method is linked to the FXML file and is invoked when the mouse enters the "Use Case Diagram" button.
     */
    @FXML
    private void onMouseEnterUseCaseDiagram() {
        btnUseCaseDiagram.setStyle("-fx-font-size: 18px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 230; -fx-pref-height: 60; -fx-background-radius: 15;");
    }

    /**
     * Handles the mouse exit event for the "Use Case Diagram" button by removing the hover effect.
     *
     * @FXML This method is linked to the FXML file and is invoked when the mouse exits the "Use Case Diagram" button.
     */
    @FXML
    private void onMouseExitUseCaseDiagram() {
        btnUseCaseDiagram.setStyle("-fx-font-size: 16px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 220; -fx-pref-height: 55; -fx-background-radius: 15;");
    }

    /**
     * Loads the Use Case Diagram scene by switching the current stage's scene to {@code useCaseDiagram.fxml}.
     *
     * <p>This method is invoked when the "Use Case Diagram" button is clicked. It loads the corresponding
     * FXML file, sets up the scene with specified dimensions, updates the stage title, and displays the
     * Use Case Diagram editor.</p>
     */
    private void loadUseCaseDiagram() {
        try {
            // Get the current stage (window)
            Stage stage = (Stage) btnUseCaseDiagram.getScene().getWindow();

            // Load the Use Case Diagram scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/useCaseDiagram.fxml"));
            Parent root = loader.load();

            // Set the Use Case Diagram scene in the current stage
            Scene useCaseScene = new Scene(root, 1366, 768);
            stage.setScene(useCaseScene);
            stage.setTitle("UML Editor: Use Case Diagram");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the Class Diagram scene by switching the current stage's scene to {@code classDiagram.fxml}.
     *
     * <p>This method is invoked when the "Class Diagram" button is clicked. It loads the corresponding
     * FXML file, sets up the scene with specified dimensions, updates the stage title, and displays the
     * Class Diagram editor.</p>
     */
    private void loadClassDiagram() {
        try {
            // Get the current stage (window)
            Stage stage = (Stage) btnClassDiagram.getScene().getWindow();

            // Load the Class Diagram scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/classDiagram.fxml"));
            Parent root = loader.load();

            // Set the Class Diagram scene in the current stage
            Scene classDiagramScene = new Scene(root, 1366, 768);
            stage.setScene(classDiagramScene);
            stage.setTitle("UML Editor: Class Diagram");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}