package com.example.umlscd;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.ObjectInputStream;
import java.io.Serializable;

public class UseCaseDiagramObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private double x;
    private double y;
    private String name;

    private transient TextField nameField; // Mark as transient
    private transient boolean isNameFieldVisible; // Transient field for visibility state

    public UseCaseDiagramObject(String type, double x, double y, String name) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.name = name;
        initializeTransientFields();
    }

    private void initializeTransientFields() {
        this.nameField = new TextField(name);
        this.nameField.setVisible(false);
        this.isNameFieldVisible = false;
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject(); // Deserialize non-transient fields
        initializeTransientFields(); // Reinitialize transient fields
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK); // Set text color to black

        if ("actor".equals(type)) {
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 15, y - 15, 30, 30); // Head
            gc.strokeLine(x, y + 15, x, y + 50); // Body
            gc.strokeLine(x, y + 25, x - 15, y + 40); // Left Arm
            gc.strokeLine(x, y + 25, x + 15, y + 40); // Right Arm
            gc.strokeLine(x, y + 50, x - 10, y + 70); // Left Leg
            gc.strokeLine(x, y + 50, x + 10, y + 70); // Right Leg
            gc.fillText(name, x - 15, y + 90); // Name text
        } else if ("usecase".equals(type)) {
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 50, y - 25, 100, 50); // Use case body
            gc.fillText(name, x - 30, y); // Name text
        }

        // Draw name text field (only if visible)
        if (isNameFieldVisible && nameField != null) {
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
        if (nameField == null) initializeTransientFields(); // Ensure field is initialized
        this.isNameFieldVisible = true;
        nameField.setVisible(true);
    }

    public void hideNameField() {
        if (nameField != null) {
            this.isNameFieldVisible = false;
            nameField.setVisible(false);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (nameField != null) nameField.setText(name);
    }

    public void updateNameFromTextField() {
        if (nameField != null) {
            this.name = nameField.getText().trim();
        }
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
