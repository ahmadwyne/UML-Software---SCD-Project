package com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram;

import com.example.umlscd.BuisnessLayer.UseCaseDiagram.UseCaseDiagramManager;
import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UseCaseDiagramSerializerTest {

    private static final String TEST_FILE_PATH = "test_diagram.json";
    private UseCaseDiagramManager diagramManager;

    @BeforeAll
    static void setUpClass() {
        // Initialize JavaFX toolkit
        new JFXPanel(); // This initializes the JavaFX runtime
    }

    @BeforeEach
    void setUp() {
        diagramManager = new UseCaseDiagramManager();
        // Optionally, populate diagramManager with some sample data
    }

    @Test
    void testSaveDiagram() throws IOException {
        // Given: A populated UseCaseDiagramManager
        diagramManager.addObject(new UseCaseDiagramObject("actor", 50, 100, "Actor1"));
        diagramManager.addObject(new UseCaseDiagramObject("usecase", 150, 200, "UseCase1"));

        // When: Saving the diagram to a file
        UseCaseDiagramSerializer.saveDiagram(diagramManager, TEST_FILE_PATH);

        // Then: Check that the file exists and is not empty
        File file = new File(TEST_FILE_PATH);
        assertTrue(file.exists(), "File should be created");
        assertTrue(file.length() > 0, "File should contain data");
    }

    @Test
    void testLoadDiagram() throws IOException {
        // Given: A valid test file with a serialized UseCaseDiagramManager
        UseCaseDiagramSerializer.saveDiagram(diagramManager, TEST_FILE_PATH);

        // When: Loading the diagram from the file
        UseCaseDiagramManager loadedDiagram = UseCaseDiagramSerializer.loadDiagram(TEST_FILE_PATH);

        // Then: Ensure the loaded diagram is not null
        assertNotNull(loadedDiagram, "Loaded diagram should not be null");
        assertEquals(diagramManager.getObjects().size(), loadedDiagram.getObjects().size(), "Loaded diagram should have the same number of objects");
    }

    @Test
    void testSaveDiagramIOException() {
        // Given: A read-only file path or directory (e.g., a directory instead of a file)
        String invalidFilePath = "/readonly_directory/test_diagram.json";

        // When: Attempting to save the diagram to a restricted directory
        assertThrows(IOException.class, () -> {
            UseCaseDiagramSerializer.saveDiagram(diagramManager, invalidFilePath);
        }, "IOException should be thrown when saving to a restricted file path");
    }

    @Test
    void testLoadDiagramIOException() throws IOException {
        // Given: An invalid file path (could simulate a corrupt file here)
        String invalidFilePath = "invalid_diagram.json";

        // When: Attempting to load the diagram from a corrupt or invalid file
        assertThrows(IOException.class, () -> {
            UseCaseDiagramSerializer.loadDiagram(invalidFilePath);
        }, "IOException should be thrown when loading from an invalid or corrupt file");
    }

    @Test
    void testSaveAndLoadDiagram() throws IOException {
        // Given: A UseCaseDiagramManager with data
        diagramManager.addObject(new UseCaseDiagramObject("actor", 50, 100, "Actor1"));
        diagramManager.addObject(new UseCaseDiagramObject("usecase", 150, 200, "UseCase1"));

        // When: Saving the diagram
        UseCaseDiagramSerializer.saveDiagram(diagramManager, TEST_FILE_PATH);

        // Then: Load the diagram back
        UseCaseDiagramManager loadedDiagram = UseCaseDiagramSerializer.loadDiagram(TEST_FILE_PATH);

        // Ensure the loaded diagram is identical to the original
        assertEquals(diagramManager.getObjects().size(), loadedDiagram.getObjects().size(), "The number of objects should be the same");
        assertEquals(diagramManager.getObjects().get(0).getName(), loadedDiagram.getObjects().get(0).getName(), "The names of the objects should match");
    }

    @AfterEach
    void tearDown() {
        // Clean up the test file after each test
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}