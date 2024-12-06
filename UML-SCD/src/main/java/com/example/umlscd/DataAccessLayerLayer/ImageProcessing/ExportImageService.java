package com.example.umlscd.DataAccessLayerLayer.ImageProcessing;

import javafx.stage.FileChooser;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * <h1>Export Image Service</h1>
 *
 * <p>The {@code ExportImageService} class provides functionality to export the current state of a canvas
 * to an image file. It allows users to select a file location and name through a file chooser dialog,
 * captures the canvas content, and saves it as a PNG image. This enables users to save visual representations
 * of their diagrams or drawings created within the application.</p>
 *
 * <p>This class handles the conversion of JavaFX {@code Canvas} content into a format suitable for image files,
 * ensuring compatibility and high-quality output. It also manages exceptions related to file operations,
 * providing user feedback in case of errors.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class ExportImageService {

    /**
     * Exports the content of the provided {@code Canvas} to an image file in PNG format.
     *
     * <p>This method opens a file chooser dialog to allow the user to select the destination file path and name.
     * It captures the current state of the canvas, converts it into a {@code BufferedImage}, and writes it
     * to the selected file. If the user cancels the file selection, the method exits gracefully.
     * Any {@code IOException} encountered during the process is caught and logged.</p>
     *
     * @param canvas The {@code Canvas} whose content is to be exported as an image.
     */
    public void exportToImage(Canvas canvas) {
        // Create a FileChooser to allow the user to choose the location and filename
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        // Open a save dialog and get the file chosen by the user
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            // If the user selected a file, export the diagram to the chosen file
            try {
                // Create a writable image from the canvas content
                WritableImage writableImage = canvas.snapshot(null, null);

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
            System.out.println("No file selected.");
        }
    }
}
