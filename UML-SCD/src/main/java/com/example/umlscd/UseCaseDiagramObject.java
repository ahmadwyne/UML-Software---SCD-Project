package com.example.umlscd;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class UseCaseDiagramObject {

    private String type;
    private double x;
    private double y;
    private String name;
    private TextField nameField;
    private boolean isNameFieldVisible;

    public UseCaseDiagramObject(String type, double x, double y, String name) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.name = name;
        this.isNameFieldVisible = false;
        this.nameField = new TextField(name);
        this.nameField.setVisible(false);
    }
    // Add setters for x and y
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK); // Set text color to black

        if ("actor".equals(type)) {
            // Draw actor figure (head, body, arms, legs)
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 15, y - 15, 30, 30); // Head (circle)
            gc.strokeLine(x, y + 15, x, y + 50); // Body (line)
            gc.strokeLine(x, y + 25, x - 15, y + 40); // Left Arm (line)
            gc.strokeLine(x, y + 25, x + 15, y + 40); // Right Arm (line)
            gc.strokeLine(x, y + 50, x - 10, y + 70); // Left Leg (line)
            gc.strokeLine(x, y + 50, x + 10, y + 70); // Right Leg (line)

            // Draw actor's name below the actor figure (remove the above name field)
            gc.fillText(name, x - 15, y + 90); // Name text below the figure
        } else if ("usecase".equals(type)) {
            // Draw use case body (ellipse)
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 50, y - 25, 100, 50); // Use case body (ellipse)

            // Draw use case's name inside the oval (centered)
            gc.fillText(name, x - 30, y); // Name inside the oval (centered)
        }

        // Draw name text field (only if it is visible)
        if (isNameFieldVisible) {
            nameField.setLayoutX(x - nameField.getWidth() / 2);
            nameField.setLayoutY(y - 35);
        }
    }

    public boolean contains(double mouseX, double mouseY) {
        if ("actor".equals(type)) {
            return Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2) <= 25 * 25;
        } else if ("usecase".equals(type)) {
            return Math.pow(mouseX - x, 2) / 2500 + Math.pow(mouseY - y, 2) / 625 <= 1;
        }
        return false;
    }

    public void showNameField() {
        this.isNameFieldVisible = true;
        nameField.setVisible(true);
    }

    public void hideNameField() {
        this.isNameFieldVisible = false;
        nameField.setVisible(false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameField.setText(name);
    }

    public void updateNameFromTextField() {
        this.name = nameField.getText().trim();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getType() {
        return type;
    }
}