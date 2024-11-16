package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MainController {

    @FXML
    private Canvas canvas;

    @FXML
    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillText("Welcome to UML Editor", 10, 20);
        // Additional setup for the canvas can be done here
    }
}
