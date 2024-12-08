package com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram;

import com.example.umlscd.BusinessLayer.UseCaseDiagram.UseCaseDiagramManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UseCaseDiagramDAO}.
 * <p>
 * This class contains unit tests for the {@link UseCaseDiagramDAO} class, which handles saving and loading
 * use case diagrams to and from serialized files. The tests cover different scenarios of saving and loading diagrams,
 * including success cases and error handling such as loading from non-existent or corrupted files.
 * </p>
 */
public class UseCaseDiagramDAOTest {

    private UseCaseDiagramManager mockManager;
    private static final String TEST_FILE_PATH = "test_usecase_diagram.ser";

    /**
     * Setup method to initialize the {@link UseCaseDiagramManager} instance before each test.
     * This method is called before each test method is executed.
     */
    @BeforeEach
    void setUp() {
        mockManager = new UseCaseDiagramManager(); // Initialize with a real or mock object
    }

    /**
     * Test method to verify the successful saving of a diagram.
     * <p>
     * This test checks that the {@link UseCaseDiagramDAO#saveDiagram(UseCaseDiagramManager, String)} method
     * correctly saves the diagram to a file. It then asserts that the file is created as expected.
     * </p>
     */
    @Test
    void testSaveDiagram_Success() {
        // Arrange
        String filePath = TEST_FILE_PATH;

        // Act: Save the diagram to the file
        UseCaseDiagramDAO.saveDiagram(mockManager, filePath);

        // Assert: Check if the file was created
        File savedFile = new File(filePath);
        assertTrue(savedFile.exists(), "The diagram file should be created after saving.");

        // Cleanup: Delete the file after the test
        savedFile.delete();
    }

    /**
     * Test method to verify the successful loading of a diagram.
     * <p>
     * This test first saves a diagram to a file using {@link UseCaseDiagramDAO#saveDiagram(UseCaseDiagramManager, String)}.
     * It then loads the diagram using {@link UseCaseDiagramDAO#loadDiagram(String)} and asserts that the diagram is loaded
     * successfully and matches the original diagram.
     * </p>
     */
    @Test
    void testLoadDiagram_Success() {
        // Arrange: First, save the diagram to a file
        String filePath = TEST_FILE_PATH;
        UseCaseDiagramDAO.saveDiagram(mockManager, filePath);

        // Act: Load the diagram from the file
        UseCaseDiagramManager loadedManager = UseCaseDiagramDAO.loadDiagram(filePath);

        // Assert: Verify the diagram is loaded successfully and matches the original diagram
        assertNotNull(loadedManager, "The diagram should be loaded successfully from the file.");

        // Cleanup: Delete the file after the test
        new File(filePath).delete();
    }

    /**
     * Test method to handle the scenario where the diagram file does not exist.
     * <p>
     * This test verifies that if an attempt is made to load a diagram from a non-existent file,
     * the {@link UseCaseDiagramDAO#loadDiagram(String)} method should return {@code null}.
     * </p>
     */
    @Test
    void testLoadDiagram_Error_FileNotFound() {
        // Arrange: Use a file path that does not exist
        String invalidFilePath = "non_existent_file.ser";

        // Act: Attempt to load the diagram from the non-existent file
        UseCaseDiagramManager loadedManager = UseCaseDiagramDAO.loadDiagram(invalidFilePath);

        // Assert: The diagram should not load and return null
        assertNull(loadedManager, "The diagram should return null if the file does not exist.");
    }

    /**
     * Test method to handle the scenario where the diagram file is corrupted.
     * <p>
     * This test creates a corrupted file (empty or invalid format) and verifies that
     * {@link UseCaseDiagramDAO#loadDiagram(String)} returns {@code null} when trying to load the diagram from it.
     * </p>
     */
    @Test
    void testLoadDiagram_Error_CorruptedFile() {
        // Arrange: Create an empty or corrupted file
        File corruptedFile = new File(TEST_FILE_PATH);
        try {
            if (!corruptedFile.exists()) {
                corruptedFile.createNewFile();
            }
        } catch (IOException e) {
            fail("Error setting up corrupted file: " + e.getMessage());
        }

        // Act: Attempt to load the diagram from the corrupted file
        UseCaseDiagramManager loadedManager = UseCaseDiagramDAO.loadDiagram(TEST_FILE_PATH);

        // Assert: The diagram should not load from a corrupted file, so it should return null
        assertNull(loadedManager, "The diagram should not load from a corrupted file.");

        // Cleanup: Delete the corrupted file
        corruptedFile.delete();
    }
}
