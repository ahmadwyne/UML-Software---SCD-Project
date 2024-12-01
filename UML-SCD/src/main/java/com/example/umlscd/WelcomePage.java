package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class WelcomePage {

    @FXML
    private Button btnClassDiagram;

    @FXML
    private Button btnUseCaseDiagram;

    @FXML
    private ImageView logoImageView;

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

    private void applyHoverEffect(Button button) {
        button.setStyle("-fx-font-size: 18px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #111111; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 230; -fx-pref-height: 60; -fx-background-radius: 15;");
    }

    private void removeHoverEffect(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 220; -fx-pref-height: 55; -fx-background-radius: 15;");
    }

    @FXML
    private void onMouseEnterClassDiagram() {
        btnClassDiagram.setStyle("-fx-font-size: 18px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 230; -fx-pref-height: 60; -fx-background-radius: 15;");
    }

    @FXML
    private void onMouseExitClassDiagram() {
        btnClassDiagram.setStyle("-fx-font-size: 16px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 220; -fx-pref-height: 55; -fx-background-radius: 15;");
    }

    @FXML
    private void onMouseEnterUseCaseDiagram() {
        btnUseCaseDiagram.setStyle("-fx-font-size: 18px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 230; -fx-pref-height: 60; -fx-background-radius: 15;");
    }

    @FXML
    private void onMouseExitUseCaseDiagram() {
        btnUseCaseDiagram.setStyle("-fx-font-size: 16px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #242424; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 220; -fx-pref-height: 55; -fx-background-radius: 15;");
    }


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
