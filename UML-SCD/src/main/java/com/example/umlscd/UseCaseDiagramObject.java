package com.example.umlscd;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <h1>Use Case Diagram Object</h1>
 *
 * <p>The {@code UseCaseDiagramObject} class represents individual elements within a use case diagram,
 * specifically actors and use cases. It encapsulates properties such as type, position coordinates,
 * and name. The class provides functionalities to render the object on a canvas, detect containment of
 * mouse events, and manage the visibility and editing of the object's name through a text field.</p>
 *
 * <p>This class implements {@code Serializable} to allow objects to be serialized and deserialized,
 * facilitating saving and loading of diagrams. It also integrates with Jackson annotations to support
 * JSON serialization, ensuring compatibility with modern data interchange formats.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class UseCaseDiagramObject implements Serializable {
    /**
     * Serial version UID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The type of the diagram object, either "actor" or "usecase".
     */
    private String type;

    /**
     * The X-coordinate position of the object on the canvas.
     */
    private double x;

    /**
     * The Y-coordinate position of the object on the canvas.
     */
    private double y;

    /**
     * The name of the object, representing either the actor's name or the use case's name.
     */
    private String name;

    /**
     * The text field used for editing the object's name.
     * <p>
     * This field is marked as transient to exclude it from serialization,
     * as UI components should not be serialized.
     */
    private transient TextField nameField; // Mark as transient

    /**
     * Indicates whether the name editing text field is currently visible.
     * <p>
     * This field is marked as transient to exclude it from serialization,
     * as UI state should not be serialized.
     */
    private transient boolean isNameFieldVisible; // Transient field for visibility state

    /**
     * Constructs a {@code UseCaseDiagramObject} with the specified type, position, and name.
     *
     * <p>This constructor is used during JSON deserialization to initialize the object's state.</p>
     *
     * @param type The type of the diagram object ("actor" or "usecase").
     * @param x    The X-coordinate position on the canvas.
     * @param y    The Y-coordinate position on the canvas.
     * @param name The name of the object.
     */
    @JsonCreator
    public UseCaseDiagramObject(@JsonProperty("type") String type,
                                @JsonProperty("x") double x,
                                @JsonProperty("y") double y,
                                @JsonProperty("name") String name) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.name = name;
        initializeTransientFields();
    }

    /**
     * Initializes transient fields that are not serialized.
     *
     * <p>This method sets up the {@code nameField} and ensures it is initially hidden.</p>
     */
    private void initializeTransientFields() {
        this.nameField = new TextField(name);
        this.nameField.setVisible(false);
        this.isNameFieldVisible = false;
    }

    /**
     * Custom deserialization logic to handle transient fields.
     *
     * <p>This method is called during deserialization to ensure that transient fields are properly initialized.</p>
     *
     * @param ois The {@code ObjectInputStream} from which the object is being deserialized.
     * @throws Exception If an error occurs during deserialization.
     */
    @Serial
    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject(); // Deserialize non-transient fields
        initializeTransientFields(); // Reinitialize transient fields
    }

    /**
     * Sets the X-coordinate position of the object.
     *
     * @param x The new X-coordinate position.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the Y-coordinate position of the object.
     *
     * @param y The new Y-coordinate position.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Renders the use case diagram object on the provided graphics context.
     *
     * <p>Depending on the object's type, it draws either an actor or a use case with appropriate styling.
     * If the name field is visible, it positions the text field accordingly.</p>
     *
     * @param gc The {@code GraphicsContext} used for drawing on the canvas.
     */
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

    /**
     * Determines whether the object contains the specified point.
     *
     * <p>This method is used to detect if a mouse event occurred within the bounds of the object,
     * facilitating interactions such as selection, dragging, or deletion.</p>
     *
     * @param mouseX The X-coordinate of the point to check.
     * @param mouseY The Y-coordinate of the point to check.
     * @return {@code true} if the object contains the point; {@code false} otherwise.
     */
    public boolean contains(double mouseX, double mouseY) {
        if ("actor".equals(type)) {
            return Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2) <= 25 * 25;
        } else if ("usecase".equals(type)) {
            return Math.pow(mouseX - x, 2) / 2500 + Math.pow(mouseY - y, 2) / 625 <= 1;
        }
        return false;
    }

    /**
     * Displays the name editing text field for the object.
     *
     * <p>This method makes the {@code nameField} visible, allowing the user to edit the object's name.</p>
     */
    public void showNameField() {
        if (nameField == null) initializeTransientFields(); // Ensure field is initialized
        this.isNameFieldVisible = true;
        nameField.setVisible(true);
    }

    /**
     * Hides the name editing text field for the object.
     *
     * <p>This method hides the {@code nameField}, concluding the name editing process.</p>
     */
    public void hideNameField() {
        if (nameField != null) {
            this.isNameFieldVisible = false;
            nameField.setVisible(false);
        }
    }

    /**
     * Retrieves the name of the object.
     *
     * @return A {@code String} representing the object's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the object and updates the name editing text field if it exists.
     *
     * @param name The new name to assign to the object.
     */
    public void setName(String name) {
        this.name = name;
        if (nameField != null) nameField.setText(name);
    }

    /**
     * Updates the object's name based on the current text in the name editing text field.
     *
     * <p>This method is typically called when the user commits a name change through the UI.</p>
     */
    public void updateNameFromTextField() {
        if (nameField != null) {
            this.name = nameField.getText().trim();
        }
    }

    /**
     * Retrieves the X-coordinate position of the object.
     *
     * @return The X-coordinate position.
     */
    public double getX() {
        return x;
    }

    /**
     * Retrieves the Y-coordinate position of the object.
     *
     * @return The Y-coordinate position.
     */
    public double getY() {
        return y;
    }

    /**
     * Retrieves the type of the object.
     *
     * @return A {@code String} indicating the object's type ("actor" or "usecase").
     */
    public String getType() {
        return type;
    }
}
