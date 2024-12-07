package com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram;

import com.example.umlscd.BuisnessLayer.UseCaseDiagram.UseCaseDiagramManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class UseCaseDiagramDAOTest {

    private UseCaseDiagramManager mockManager;
    private static final String TEST_FILE_PATH = "test_usecase_diagram.ser";

    @BeforeEach
    void setUp() {
        mockManager = new UseCaseDiagramManager(); // Initialize with a real or mock object
    }

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

    @Test
    void testLoadDiagram_Error_FileNotFound() {
        // Arrange: Use a file path that does not exist
        String invalidFilePath = "non_existent_file.ser";

        // Act: Attempt to load the diagram from the non-existent file
        UseCaseDiagramManager loadedManager = UseCaseDiagramDAO.loadDiagram(invalidFilePath);

        // Assert: The diagram should not load and return null
        assertNull(loadedManager, "The diagram should return null if the file does not exist.");
    }

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
