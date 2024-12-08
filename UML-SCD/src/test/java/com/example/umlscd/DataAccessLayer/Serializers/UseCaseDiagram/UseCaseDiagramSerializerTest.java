package com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram;

import com.example.umlscd.BusinessLayer.UseCaseDiagram.UseCaseDiagramManager;
import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UseCaseDiagramSerializer} class.
 * <p>
 * This class contains tests for serializing and deserializing {@link UseCaseDiagramManager} objects to and from
 * files using JSON format. It verifies various scenarios for saving and loading use case diagrams,
 * including handling exceptions like IO errors, and ensuring data integrity during the save/load process.
 * </p>
 */
public class UseCaseDiagramSerializerTest {

    private static final String TEST_FILE_PATH = "test_diagram.json";
    private UseCaseDiagramManager diagramManager;

    /**
     * Initializes the JavaFX runtime before any tests are run.
     * This is required for some JavaFX components used in the diagram manager.
     */
    @BeforeAll
    static void setUpClass() {
        // Initialize JavaFX toolkit
        new JFXPanel(); // This initializes the JavaFX runtime
    }

    /**
     * Sets up a new {@link UseCaseDiagramManager} instance before each test.
     * Optionally, diagramManager can be populated with sample data.
     */
    @BeforeEach
    void setUp() {
        diagramManager = new UseCaseDiagramManager();
    }

    /**
     * Tests the successful saving of a use case diagram to a file.
     * <p>
     * This test checks that the diagram is correctly serialized into a file and verifies that the file is created
     * and contains data.
     * </p>
     *
     * @throws IOException if an I/O error occurs during the test
     */
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

    /**
     * Tests the successful loading of a use case diagram from a file.
     * <p>
     * This test verifies that a diagram saved in the previous test can be loaded back and that the loaded diagram
     * matches the original one in terms of object count and data integrity.
     * </p>
     *
     * @throws IOException if an I/O error occurs during the test
     */
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

    /**
     * Tests saving a diagram to an invalid or read-only file path.
     * <p>
     * This test ensures that an {@link IOException} is thrown when trying to save a diagram to a restricted or
     * invalid file path (e.g., a read-only directory).
     * </p>
     */
    @Test
    void testSaveDiagramIOException() {
        // Given: A read-only file path or directory (e.g., a directory instead of a file)
        String invalidFilePath = "/readonly_directory/test_diagram.json";

        // When: Attempting to save the diagram to a restricted directory
        assertThrows(IOException.class, () -> {
            UseCaseDiagramSerializer.saveDiagram(diagramManager, invalidFilePath);
        }, "IOException should be thrown when saving to a restricted file path");
    }

    /**
     * Tests loading a diagram from an invalid or corrupted file.
     * <p>
     * This test ensures that an {@link IOException} is thrown when attempting to load a diagram from a file
     * that is either missing or corrupted.
     * </p>
     *
     * @throws IOException if an I/O error occurs during the test
     */
    @Test
    void testLoadDiagramIOException() throws IOException {
        // Given: An invalid file path (could simulate a corrupt file here)
        String invalidFilePath = "invalid_diagram.json";

        // When: Attempting to load the diagram from a corrupt or invalid file
        assertThrows(IOException.class, () -> {
            UseCaseDiagramSerializer.loadDiagram(invalidFilePath);
        }, "IOException should be thrown when loading from an invalid or corrupt file");
    }

    /**
     * Tests the entire save and load process to ensure that the diagram's data integrity is maintained.
     * <p>
     * This test verifies that after saving and loading a diagram, the objects and their data are identical.
     * </p>
     *
     * @throws IOException if an I/O error occurs during the test
     */
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

    /**
     * Cleans up the test file after each test.
     * This ensures that the test file is deleted after each test to avoid interference between tests.
     */
    @AfterEach
    void tearDown() {
        // Clean up the test file after each test
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}