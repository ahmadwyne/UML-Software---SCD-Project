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

/**
 * Unit tests for the {@link Main} class, which is the entry point for the UML editor application.
 * <p>
 * This class contains tests for verifying the correctness of the user interface elements in the main application window.
 * It uses TestFX to interact with JavaFX components and verify that the primary stage, scene, and UI elements
 * (such as buttons, images, and text) are displayed correctly.
 * </p>
 */
public class MainTest extends ApplicationTest {

    private Stage primaryStage;

    /**
     * Sets up the test by launching the {@link Main} application and storing the primary stage.
     * This method is automatically called by TestFX before each test method is executed.
     *
     * @param stage the primary stage of the JavaFX application
     * @throws Exception if any error occurs during the application launch
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Launch the Main application and store the stage
        new Main().start(stage);
        this.primaryStage = stage; // Store the stage for later use in tests
    }

    /**
     * Verifies that the primary stage is showing and has the correct title.
     * <p>
     * This test checks that the main window is visible after startup and that the window's title is
     * set to "Welcome to UML Editor" as expected.
     * </p>
     */
    @Test
    void testPrimaryStageIsShowing() {
        // Now you can directly use primaryStage without getWindow()
        assertTrue(primaryStage.isShowing(), "Primary stage should be visible after startup.");
        assertEquals("Welcome to UML Editor", primaryStage.getTitle(),
                "The stage title should match 'Welcome to UML Editor'.");
    }

    /**
     * Verifies that the scene dimensions are set correctly and the scene is not null.
     * <p>
     * This test ensures that the initial dimensions of the scene are 1366x768 as expected for the main window.
     * </p>
     */
    @Test
    void testSceneDimensionsAndComponents() {
        Scene scene = primaryStage.getScene();
        assertNotNull(scene, "Scene should not be null.");

        // Verify scene dimensions with a small tolerance
        assertEquals(1366, scene.getWidth(), 1.0, "Scene width should be 1366.");
        assertEquals(768, scene.getHeight(), 1.0, "Scene height should be 768.");
    }

    /**
     * Verifies the presence of the logo ImageView in the scene and checks if an image is set.
     * <p>
     * This test ensures that the logo image is displayed in the main window, and that the ImageView
     * has an image set to it.
     * </p>
     */
    @Test
    void testLogoImageViewPresence() {
        ImageView logoImageView = lookup("#logoImageView").queryAs(ImageView.class);
        assertNotNull(logoImageView, "Logo ImageView should be present in the scene.");
        assertNotNull(logoImageView.getImage(), "Logo ImageView should have an image set.");
    }

    /**
     * Verifies the presence and text of the buttons in the main scene.
     * <p>
     * This test ensures that both the "Class Diagram" and "Use Case Diagram" buttons are visible in the scene
     * and that they contain the expected text.
     * </p>
     */
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

    /**
     * Verifies the presence of the welcome text in the main scene.
     * <p>
     * This test checks whether the "Welcome to the UML Editor" text is present in the scene.
     * It verifies that the user is greeted with the expected welcome message when the application starts.
     * </p>
     */
    @Test
    void testWelcomeTextPresence() {
        // Check if a text node with the exact text "Welcome to the UML Editor" exists
        // If not found, consider giving the Text node an fx:id in your FXML and query by that instead.
        Text welcomeText = lookup(hasText("Welcome to the UML Editor")).queryAs(Text.class);
        assertNotNull(welcomeText, "Should find a Text node with 'Welcome to the UML Editor' text.");
    }

    /**
     * Verifies that the primary stage has at least one icon set.
     * <p>
     * This test ensures that the application window has an icon displayed, which is typically shown
     * in the application taskbar and window title.
     * </p>
     */
    @Test
    void testPrimaryStageIcon() {
        assertFalse(primaryStage.getIcons().isEmpty(), "Primary stage should have at least one icon set.");
    }
}
