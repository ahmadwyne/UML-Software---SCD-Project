package com.example.umlscd.BuisnessLayer.UseCaseDiagram;

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

@ExtendWith(MockitoExtension.class) // Enables the use of Mockito annotations
public class UseCaseDiagramManagerTest {

    // Before all tests, initialize the JavaFX Toolkit
    @BeforeAll
    public static void setUpClass() {
        Platform.startup(() -> {}); // Starts the JavaFX toolkit
    }

    // Test Default Constructor
    @Test
    public void testDefaultConstructor() {
        UseCaseDiagramManager manager = new UseCaseDiagramManager();
        assertNotNull(manager.getObjects());
        assertNotNull(manager.getAssociations());
        assertTrue(manager.getObjects().isEmpty());
        assertTrue(manager.getAssociations().isEmpty());
    }

    // Test Parameterized Constructor
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

    // Test Getters and Setters
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

    // Test addObject method
    @Test
    public void testAddObject() {
        UseCaseDiagramObject object = new UseCaseDiagramObject("actor", 50, 100, "Actor1"); // Assuming the object has a no-args constructor
        UseCaseDiagramManager manager = new UseCaseDiagramManager();

        manager.addObject(object);

        assertEquals(1, manager.getObjects().size());
        assertEquals(object, manager.getObjects().get(0));
    }

    // Test addAssociation method
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
