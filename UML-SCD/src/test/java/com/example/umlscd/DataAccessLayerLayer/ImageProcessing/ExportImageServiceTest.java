package com.example.umlscd.DataAccessLayerLayer.ImageProcessing;

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
 * Test class for ExportImageService.
 */
@ExtendWith(ApplicationExtension.class)
public class ExportImageServiceTest {

    private Canvas canvas;
    private TestableExportImageService exportImageService;
    private File tempFile;

    /**
     * A subclass of ExportImageService that overrides file choosing logic for testing.
     * Instead of showing a dialog, it returns a known file.
     */
    private static class TestableExportImageService extends ExportImageService {
        private File fixedFile;

        public void setFixedFile(File file) {
            this.fixedFile = file;
        }

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

        // Optional: Load the image back to verify correctness
        BufferedImage loadedImage = ImageIO.read(tempFile);
        assertNotNull(loadedImage, "The loaded image should not be null.");
        // Check pixel color at a known drawn point
        int rgb = loadedImage.getRGB(100, 100);
        // Since we drew a BLUE rectangle at (50, 50, 100x100), pixel (100,100) should be blue.
        // ARGB format: 0xFF0000FF for blue
        assertEquals(0xFF0000FF, rgb, "Pixel should be blue.");
    }

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
