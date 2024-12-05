package com.example.umlscd.EntryPoint;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.NodeQueryUtils.hasText;

public class MainTest extends ApplicationTest {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        // Launch the Main application and store the stage
        new Main().start(stage);
        this.primaryStage = stage; // Store the stage for later use in tests
    }

    @Test
    void testPrimaryStageIsShowing() {
        // Now you can directly use primaryStage without getWindow()
        assertTrue(primaryStage.isShowing(), "Primary stage should be visible after startup.");
        assertEquals("Welcome to UML Editor", primaryStage.getTitle(),
                "The stage title should match 'Welcome to UML Editor'.");
    }

    @Test
    void testSceneDimensionsAndComponents() {
        Scene scene = primaryStage.getScene();
        assertNotNull(scene, "Scene should not be null.");

        // Verify scene dimensions with a small tolerance
        assertEquals(1366, scene.getWidth(), 1.0, "Scene width should be 1366.");
        assertEquals(768, scene.getHeight(), 1.0, "Scene height should be 768.");
    }

    @Test
    void testLogoImageViewPresence() {
        ImageView logoImageView = lookup("#logoImageView").queryAs(ImageView.class);
        assertNotNull(logoImageView, "Logo ImageView should be present in the scene.");
        assertNotNull(logoImageView.getImage(), "Logo ImageView should have an image set.");
    }

    @Test
    void testButtonsPresence() {
        Button btnClassDiagram = lookup("#btnClassDiagram").queryAs(Button.class);
        assertNotNull(btnClassDiagram, "Class Diagram button should be present.");
        assertEquals("Class Diagram", btnClassDiagram.getText(),
                "Class Diagram button should have the correct text.");

        Button btnUseCaseDiagram = lookup("#btnUseCaseDiagram").queryAs(Button.class);
        assertNotNull(btnUseCaseDiagram, "Use Case Diagram button should be present.");
        assertEquals("Use Case Diagram", btnUseCaseDiagram.getText(),
                "Use Case Diagram button should have the correct text.");
    }

    @Test
    void testWelcomeTextPresence() {
        // Check if a text node with the exact text "Welcome to the UML Editor" exists
        // If not found, consider giving the Text node an fx:id in your FXML and query by that instead.
        Text welcomeText = lookup(hasText("Welcome to the UML Editor")).queryAs(Text.class);
        assertNotNull(welcomeText, "Should find a Text node with 'Welcome to the UML Editor' text.");
    }

    @Test
    void testPrimaryStageIcon() {
        assertFalse(primaryStage.getIcons().isEmpty(), "Primary stage should have at least one icon set.");
    }
}
