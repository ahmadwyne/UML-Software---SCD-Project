package com.example.umlscd.BusinessLayer.UseCaseDiagram;

import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import com.example.umlscd.Models.UseCaseDiagram.Association;
import com.example.umlscd.DataAccessLayer.ImageProcessing.ExportImageService;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.stage.FileChooser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link UseCaseDiagramManager} class.
 * This class contains tests for creating, editing, and handling use case diagram relationships within a UML diagram.
 * <p>
 * The tests ensure that the {@link UseCaseDiagramManager} works as expected, covering scenarios such as object addition,
 * association creation, and export to image functionality. They also include handling scenarios like empty object lists,
 * duplicate associations, and interactions with the user interface.
 * </p>
 */
@ExtendWith(MockitoExtension.class) // Enables the use of Mockito annotations
public class UseCaseDiagramManagerTest {

    /**
     * Initializes the JavaFX toolkit before all tests.
     * This method is annotated with @BeforeAll and starts the JavaFX toolkit.
     */
    @BeforeAll
    public static void setUpClass() {
        Platform.startup(() -> {}); // Starts the JavaFX toolkit
    }

    /**
     * Tests the default constructor of the UseCaseDiagramManager class.
     * <p>
     * It verifies that the manager is initialized with empty object and association lists.
     * </p>
     */
    @Test
    public void testDefaultConstructor() {
        UseCaseDiagramManager manager = new UseCaseDiagramManager();
        assertNotNull(manager.getObjects());
        assertNotNull(manager.getAssociations());
        assertTrue(manager.getObjects().isEmpty());
        assertTrue(manager.getAssociations().isEmpty());
    }

    /**
     * Tests the parameterized constructor of the UseCaseDiagramManager class.
     * <p>
     * It verifies that the manager is initialized with the provided lists of objects and associations,
     * and the system boundary name.
     * </p>
     */
    @Test
    public void testParameterizedConstructor() {
        ArrayList<UseCaseDiagramObject> objects = new ArrayList<>();
        ArrayList<Association> associations = new ArrayList<>();
        String systemBoundaryName = "Test Boundary";

        UseCaseDiagramManager manager = new UseCaseDiagramManager(objects, associations, systemBoundaryName);

        assertEquals(objects, manager.getObjects());
        assertEquals(associations, manager.getAssociations());
        assertEquals(systemBoundaryName, manager.getSystemBoundaryName());
    }
    /**
     * Tests the getter and setter methods of the UseCaseDiagramManager class.
     * <p>
     * It verifies that objects, associations, and system boundary name can be correctly set and retrieved.
     * </p>
     */
    @Test
    public void testSettersAndGetters() {
        ArrayList<UseCaseDiagramObject> objects = new ArrayList<>();
        ArrayList<Association> associations = new ArrayList<>();
        String systemBoundaryName = "Test Boundary";

        UseCaseDiagramManager manager = new UseCaseDiagramManager();
        manager.setObjects(objects);
        manager.setAssociations(associations);
        manager.setSystemBoundaryName(systemBoundaryName);

        assertEquals(objects, manager.getObjects());
        assertEquals(associations, manager.getAssociations());
        assertEquals(systemBoundaryName, manager.getSystemBoundaryName());
    }

    /**
     * Tests the addObject method of the UseCaseDiagramManager class.
     * <p>
     * It verifies that a UseCaseDiagramObject can be successfully added to the manager.
     * </p>
     */
    @Test
    public void testAddObject() {
        UseCaseDiagramObject object = new UseCaseDiagramObject("actor", 50, 100, "Actor1"); // Assuming the object has a no-args constructor
        UseCaseDiagramManager manager = new UseCaseDiagramManager();

        manager.addObject(object);

        assertEquals(1, manager.getObjects().size());
        assertEquals(object, manager.getObjects().get(0));
    }

    /**
     * Tests the addAssociation method of the UseCaseDiagramManager class.
     * <p>
     * It verifies that an Association can be successfully added to the manager.
     * </p>
     */
    @Test
    public void testAddAssociation() {
        Association association = new Association(new UseCaseDiagramObject("actor", 50, 100, "Actor1"), new UseCaseDiagramObject("actor", 50, 100, "Actor2"), "association"); // Assuming the object has a no-args constructor
        UseCaseDiagramManager manager = new UseCaseDiagramManager();

        manager.addAssociation(association);

        assertEquals(1, manager.getAssociations().size());
        assertEquals(association, manager.getAssociations().get(0));
    }

    // Test exportDiagramToImage method
    @Mock
    private ExportImageService exportImageService;

    @Mock
    private Canvas canvas;

    /**
     * Tests the exportDiagramToImage method of the UseCaseDiagramManager class.
     * <p>
     * It verifies that the export to image functionality works correctly using a mock ExportImageService.
     * </p>
     */
    @Test
    public void testExportDiagramToImage() {
        // Create an instance of UseCaseDiagramManager
        UseCaseDiagramManager manager = new UseCaseDiagramManager();

        // Simulate export method execution on the JavaFX Application Thread
        Platform.runLater(() -> {
            manager.exportDiagramToImage(canvas);

            // Verify that the exportToImage method was called
            verify(exportImageService, times(1)).exportToImage(canvas);
        });

        // Wait for the platform to finish execution
        try {
            Thread.sleep(1000); // A brief sleep to allow Platform.runLater() to execute
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}