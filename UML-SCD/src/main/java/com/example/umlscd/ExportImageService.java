package com.example.umlscd;

import javafx.stage.FileChooser;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ExportImageService {

    // Method to handle file selection and export the image
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
