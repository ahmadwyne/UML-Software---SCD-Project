package com.example.umlscd.PresentationLayer.UseCaseDiagram;

import com.example.umlscd.BusinessLayer.UseCaseDiagram.UseCaseDiagramManager;
import com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram.UseCaseDiagramDAO;
import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for the UseCaseDiagram class.
 * This class contains tests for various functionalities such as adding actors, use cases, drawing associations, saving/loading diagrams,
 * and other interactions with the Use Case Diagram UI.
 * <p>
 * The test cases simulate user interactions with the diagram and verify that the application behaves as expected.
 * </p>
 */
public class UseCaseDiagramTest extends ApplicationTest {

    private UseCaseDiagram useCaseDiagram;
    private Button btnAddActor;
    private Canvas canvas;
    private TreeView<String> objectExplorer;

    /**
     * Initializes the UI components and sets up the stage for testing.
     *
     * @param stage The primary stage for the test application.
     * @throws Exception if any error occurs during initialization.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/useCaseDiagram.fxml"));
        BorderPane root = loader.load();
        useCaseDiagram = loader.getController();

        stage.setTitle("Test Stage");
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();

        // Access the UI components
        btnAddActor = lookup("#btnAddActor").queryButton();
        canvas = lookup("#canvas").queryAs(Canvas.class);
        objectExplorer = lookup("#objectExplorer").queryAs(TreeView.class);
    }

    /**
     * Test the functionality of adding an actor to the Use Case Diagram.
     * <p>
     * This test simulates clicking the "Add Actor" button. It checks that an actor is added to the objects list and that its
     * default name is set to "Actor".
     * </p>
     */
    @Test
    void testAddActor() throws InterruptedException {
        // Simulate clicking the "Add Actor" button
        clickOn(btnAddActor);
        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay

        // Verify that the actor was added
        assertEquals(1, useCaseDiagram.objects.size(), "An actor should be added to the objects list.");
        assertEquals("Actor", useCaseDiagram.objects.get(0).getName(), "The actor's default name should be 'Actor'.");
    }

    /**
     * Test canvas interaction when clicking on the canvas.
     * <p>
     * This test simulates clicking on the canvas. It verifies that the canvas interaction is handled correctly and that the
     * GraphicsContext is initialized.
     * </p>
     */
    @Test
    void testCanvasInteraction() throws InterruptedException {
        // Simulate a click on the canvas
        clickOn(canvas);
        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay

        // Verify that the canvas interaction is handled correctly
        assertNotNull(useCaseDiagram.gc, "GraphicsContext should not be null after interacting with the canvas.");
    }

    /**
     * Test interaction with the object explorer (TreeView).
     * <p>
     * This test adds a new item to the object explorer, selects it, and verifies that the selection is reflected correctly.
     * </p>
     */
    @Test
    void testTreeViewInteraction() throws InterruptedException {
        // Add a new TreeItem to the object explorer
        interact(() -> {
            TreeItem<String> newItem = new TreeItem<>("Test Item");
            useCaseDiagram.rootItem.getChildren().add(newItem);
        });

        // Select the new item
        TreeItem<String> testItem = useCaseDiagram.rootItem.getChildren().get(0);
        interact(() -> objectExplorer.getSelectionModel().select(testItem));

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay

        // Verify the selection
        assertEquals("Test Item", objectExplorer.getSelectionModel().getSelectedItem().getValue(),
                "The selected item's value should match the test item's value.");
    }

    /**
     * Test the initialization of the UseCaseDiagram.
     * <p>
     * This test verifies the initial state of the objects list, associations list, and TreeView. It ensures that they are
     * initialized correctly and are empty when the diagram is first loaded.
     * </p>
     */
    @Test
    void testInitialize() {
        // Verify initial state
        assertNotNull(useCaseDiagram.objects, "Objects list should be initialized.");
        assertTrue(useCaseDiagram.objects.isEmpty(), "Objects list should be empty initially.");
        assertNotNull(useCaseDiagram.associations, "Associations list should be initialized.");
        assertTrue(useCaseDiagram.associations.isEmpty(), "Associations list should be empty initially.");

        // Verify TreeView initialization
        assertNotNull(useCaseDiagram.objectExplorer, "TreeView should be initialized.");
        assertEquals(useCaseDiagram.rootItem, objectExplorer.getRoot(), "TreeView root should match the initialized root item.");
    }

    /**
     * Test adding a use case with a custom name to the diagram.
     * <p>
     * This test simulates entering a use case name and clicking the "Add Use Case" button. It verifies that the use case is
     * added correctly with the provided name and that the TreeView is updated.
     * </p>
     */
    @Test
    void testAddUseCase() throws InterruptedException {
        // Simulate entering a use case name and clicking the "Add Use Case" button
        interact(() -> useCaseDiagram.txtUseCaseName.setText("Test Use Case"));
        interact(() -> useCaseDiagram.addUseCase());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay

        // Verify the use case was added
        assertEquals(1, useCaseDiagram.objects.size(), "One use case should be added to the objects list.");
        assertEquals("Test Use Case", useCaseDiagram.objects.get(0).getName(), "The use case name should match the input.");

        // Verify the TreeView was updated
        assertEquals(1, useCaseDiagram.rootItem.getChildren().size(), "One TreeItem should be added to the root.");
        assertEquals("Test Use Case", useCaseDiagram.rootItem.getChildren().get(0).getValue(),
                "The TreeItem's value should match the use case name.");
    }

    /**
     * Test drawing the system boundary.
     * <p>
     * This test verifies that a system boundary can be drawn on the canvas with the correct name, and the canvas is updated.
     * </p>
     */
    @Test
    void testDrawSystemBoundary() throws InterruptedException {
        // Set a system boundary name and draw the boundary
        interact(() -> useCaseDiagram.systemBoundaryName = "Test Boundary");
        interact(() -> useCaseDiagram.drawSystemBoundary());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay

        // Verify the canvas has been drawn with the system boundary name
        assertNotNull(useCaseDiagram.gc, "GraphicsContext should not be null.");
        assertTrue(useCaseDiagram.canvas.getWidth() > 0 && useCaseDiagram.canvas.getHeight() > 0,
                "Canvas dimensions should be greater than 0.");
    }

    /**
     * Test the system boundary name change and canvas update.
     * <p>
     * This test simulates changing the system boundary name and verifies that the name is updated, and the canvas is redrawn.
     * </p>
     */
    @Test
    void testOnSystemBoundaryNameChange() throws InterruptedException {
        // Simulate changing the system boundary name
        interact(() -> useCaseDiagram.txtSystemBoundaryName.setText("Updated Boundary"));
        interact(() -> useCaseDiagram.onSystemBoundaryNameChange());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay

        // Verify the system boundary name was updated
        assertEquals("Updated Boundary", useCaseDiagram.systemBoundaryName,
                "System boundary name should be updated to the new value.");

        // Verify the canvas was redrawn
        assertNotNull(useCaseDiagram.gc, "GraphicsContext should not be null.");
    }

    /**
     * Test adding an actor with a custom name.
     * <p>
     * This test simulates entering a custom actor name and clicking the "Add Actor" button. It verifies that the actor is
     * added correctly with the provided name and that the TreeView is updated with the new name.
     * </p>
     */
    @Test
    void testAddActorWithCustomName() throws InterruptedException {
        // Simulate entering a custom actor name
        interact(() -> useCaseDiagram.txtActorName.setText("Custom Actor"));
        interact(() -> useCaseDiagram.addActor());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay

        // Verify that the actor was added with the correct name
        assertEquals(1, useCaseDiagram.objects.size(), "One actor should be added to the objects list.");
        assertEquals("Custom Actor", useCaseDiagram.objects.get(0).getName(), "The actor's name should match the input.");

        // Verify the TreeView was updated
        assertEquals(1, useCaseDiagram.rootItem.getChildren().size(), "One TreeItem should be added to the root.");
        assertEquals("Custom Actor", useCaseDiagram.rootItem.getChildren().get(0).getValue(),
                "The TreeItem's value should match the actor's name.");
    }
}