package com.example.umlscd.PresentationLayer;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.awt.SystemColor.window;
import static org.junit.jupiter.api.Assertions.*;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;

public class WelcomePageTest extends ApplicationTest {

    private Button btnClassDiagram;
    private Button btnUseCaseDiagram;
    private ImageView logoImageView;
    private Stage stage;

    @BeforeEach
    public void setUp() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get the controller from the loaded FXML
            WelcomePage controller = loader.getController();

            // Set the scene with the loaded root node
            Scene scene = new Scene(root, 1366, 768);
            stage = new Stage();
            stage.setScene(scene);
            stage.show();
            //this.stage = stage;

            // Access the buttons and other components from the controller
            btnClassDiagram = controller.btnClassDiagram;
            btnUseCaseDiagram = controller.btnUseCaseDiagram;
            logoImageView = controller.logoImageView;
        });
        // Wait for JavaFX to initialize properly
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testButtonHoverEffects() {
        // Test hover effect on the Class Diagram button
        moveTo(btnClassDiagram);
        sleep(500);  // Give it time to apply the effect
        assertEquals("-fx-font-size: 18px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #111111; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 230; -fx-pref-height: 60; -fx-background-radius: 15;", btnClassDiagram.getStyle());

        // Test hover effect on the Use Case Diagram button
        moveTo(btnUseCaseDiagram);
        sleep(500);  // Give it time to apply the effect
        assertEquals("-fx-font-size: 18px; -fx-font-family: 'Verdana'; -fx-font-weight: bold; " +
                "-fx-background-color: #111111; -fx-text-fill: white; -fx-cursor: hand; " +
                "-fx-pref-width: 230; -fx-pref-height: 60; -fx-background-radius: 15;", btnUseCaseDiagram.getStyle());
    }

    @Test
    public void testCLassDiagramButtonActions() {
        // Simulate clicking on the "Class Diagram" button
        clickOn(btnClassDiagram);
        WaitForAsyncUtils.waitForFxEvents();  // Wait for UI to update

        // Debug the title and root to see what's going wrong
        System.out.println("Stage Title after Class Diagram click: " + stage.getTitle());
        System.out.println("Root of the scene: " + stage.getScene().getRoot().toString());

        // Validate that the scene was changed to the Class Diagram
        assertTrue(stage.getTitle().contains("Class Diagram"), "Title should contain 'Class Diagram'");

        // Check if the root contains the classDiagramPane ID
        assertNotNull(stage.getScene().lookup("#drawingPane"), "Class Diagram pane should be present in the scene");
    }

    @Test
    public void testUseCaseDiagramButtonActions() {
        // Simulate clicking on the "Use Case Diagram" button
        clickOn(btnUseCaseDiagram);
        WaitForAsyncUtils.waitForFxEvents();  // Wait for UI to update

        // Debug again to check the changes after the second button click
        System.out.println("Stage Title after Use Case Diagram click: " + stage.getTitle());
        System.out.println("Root of the scene: " + stage.getScene().getRoot().toString());

        // Check if the scene changed to Use Case Diagram
        assertTrue(stage.getTitle().contains("Use Case Diagram"), "Title should contain 'Use Case Diagram'");

        // Check if the root contains the useCaseDiagramPane ID
        assertNotNull(stage.getScene().lookup("#canvas"), "Use Case Diagram Canvas should be present in the scene");
    }

    @Test
    public void testLogoImageLoading() {
        // Check if the logo image is loaded properly
        assertNotNull(logoImageView.getImage(), "Logo image should be loaded");
    }

    @Test
    public void testInitialButtonStyles() {
        // Check if the button styles contain the expected properties (with flexibility for slight differences)
        String expectedStylePart1 = "-fx-font-family: 'Verdana'; -fx-font-weight: bold;";
        String expectedStylePart2 = "-fx-text-fill: white; -fx-cursor: hand;";

        // Assert that the button's style contains expected parts
        assertTrue(btnClassDiagram.getStyle().contains(expectedStylePart1));
        assertTrue(btnClassDiagram.getStyle().contains(expectedStylePart2));

        // Similarly for other button
        assertTrue(btnUseCaseDiagram.getStyle().contains(expectedStylePart1));
        assertTrue(btnUseCaseDiagram.getStyle().contains(expectedStylePart2));
    }

}
