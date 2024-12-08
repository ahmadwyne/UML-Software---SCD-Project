package com.example.umlscd.DataAccessLayer.ImageProcessing;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ExportImageService}.
 * <p>
 * This class tests the functionality of exporting a diagram (rendered on a {@link Canvas}) to an image file
 * using the {@link ExportImageService} class.
 * </p>
 * <p>
 * The class includes tests to verify that the image export correctly generates an image file, checks the file's
 * existence and non-emptiness, and verifies the content of the image by checking pixel colors. It also includes
 * tests to handle scenarios like user cancellation where no file is selected.
 * </p>
 */
@ExtendWith(ApplicationExtension.class)
public class ExportImageServiceTest {

    private Canvas canvas;
    private TestableExportImageService exportImageService;
    private File tempFile;

    /**
     * A subclass of {@link ExportImageService} that overrides file choosing logic for testing.
     * <p>
     * Instead of showing a file dialog to the user, this class allows for setting a fixed file for testing.
     * </p>
     */
    private static class TestableExportImageService extends ExportImageService {
        private File fixedFile;

        /**
         * Sets the file to be used for exporting the image during the test.
         *
         * @param file The file to which the image will be exported.
         */
        public void setFixedFile(File file) {
            this.fixedFile = file;
        }

        /**
         * Exports the contents of the given {@link Canvas} to an image file.
         * <p>
         * This method creates a writable image from the canvas, converts it to a {@link BufferedImage},
         * and saves the image in PNG format to the provided file location.
         * </p>
         *
         * @param canvas The canvas whose contents will be exported.
         */
        @Override
        public void exportToImage(Canvas canvas) {
            // Instead of opening a dialog, we directly use the fixedFile.
            File file = fixedFile;

            if (file != null) {
                try {
                    // Create a writable image from the canvas content
                    javafx.scene.image.WritableImage writableImage = canvas.snapshot(null, null);

                    // Convert WritableImage to BufferedImage
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

                    // Save the image to the user-selected file location (with PNG format)
                    ImageIO.write(bufferedImage, "PNG", file);

                    System.out.println("Diagram exported to: " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error exporting diagram to image: " + e.getMessage());
                }
            } else {
                System.out.println("No file selected (test mode).");
            }
        }
    }

    /**
     * Set up method to initialize the canvas, export service, and temporary file for testing.
     * <p>
     * This method prepares the environment for the tests by creating a {@link Canvas} with a simple blue rectangle
     * and setting up the {@link TestableExportImageService} with a temporary file for saving the exported image.
     * </p>
     *
     * @throws IOException if an error occurs while creating the temporary file.
     */
    @BeforeEach
    void setUp() throws IOException {
        // Initialize the canvas and draw something to test exporting
        canvas = new Canvas(200, 200);
        var gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(50, 50, 100, 100);

        exportImageService = new TestableExportImageService();

        // Create a temporary file for testing output (deleted on exit)
        tempFile = File.createTempFile("export_test", ".png");
        tempFile.deleteOnExit();

        exportImageService.setFixedFile(tempFile);
    }

    /**
     * Test method to verify that exporting to an image creates a file.
     * <p>
     * This test simulates exporting a diagram to an image by calling the export method and then checks:
     * <ul>
     *   <li>Whether the file is created.</li>
     *   <li>Whether the file is not empty.</li>
     *   <li>Whether the correct pixel color is saved in the image.</li>
     * </ul>
     * </p>
     *
     * @throws IOException if an error occurs during file operations or image loading.
     */
    @Test
    void testExportToImageCreatesFile() throws IOException {
        Platform.runLater(() -> {
            exportImageService.exportToImage(canvas);
        });

        // Wait for JavaFX events to complete before checking results
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(tempFile.exists(), "The exported file should exist.");
        assertTrue(tempFile.length() > 0, "The exported file should not be empty.");

        // Then: Verify that the file is not empty
        assertTrue(tempFile.exists(), "The exported file should exist.");
        assertTrue(tempFile.length() > 0, "The exported file should not be empty.");

        // Load the image back to verify correctness
        BufferedImage loadedImage = ImageIO.read(tempFile);
        assertNotNull(loadedImage, "The loaded image should not be null.");
        // Check pixel color at a known drawn point
        int rgb = loadedImage.getRGB(100, 100);
        // Since we drew a BLUE rectangle at (50, 50, 100x100), pixel (100,100) should be blue.
        // ARGB format: 0xFF0000FF for blue
        assertEquals(0xFF0000FF, rgb, "Pixel should be blue.");
    }

    /**
     * Test method to verify that exporting without a selected file (simulating user cancellation) does not throw an error.
     * <p>
     * This test ensures that the export process can handle scenarios where no file is selected and that it doesn't
     * cause any exceptions to be thrown.
     * </p>
     */
    @Test
    void testExportWithoutFile() {
        // Set no file to simulate user cancellation
        exportImageService.setFixedFile(null);

        // Capture the console output by some means or just rely on code coverage:
        // The code prints "No file selected." in this case
        // Just ensure it doesn't crash
        assertDoesNotThrow(() -> exportImageService.exportToImage(canvas),
                "Exporting with no file should not throw an exception.");
    }
}
