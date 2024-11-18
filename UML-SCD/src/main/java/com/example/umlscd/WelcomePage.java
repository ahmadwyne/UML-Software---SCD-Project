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

        // Button actions
        btnClassDiagram.setOnAction(event -> loadClassDiagram());
        btnUseCaseDiagram.setOnAction(event -> loadUseCaseDiagram());
    }

    private void loadUseCaseDiagram() {
        try {
            // Get the current stage (window)
            Stage stage = (Stage) btnUseCaseDiagram.getScene().getWindow();

            // Load the Use Case Diagram scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/useCaseDiagram.fxml"));
            Parent root = loader.load();

            // Set the Use Case Diagram scene in the current stage
            Scene useCaseScene = new Scene(root, 800, 600);
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
            Scene classDiagramScene = new Scene(root, 1000, 600);
            stage.setScene(classDiagramScene);
            stage.setTitle("UML Editor: Class Diagram");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
