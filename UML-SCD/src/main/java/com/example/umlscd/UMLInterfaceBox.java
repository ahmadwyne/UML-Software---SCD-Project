package com.example.umlscd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>UML Interface Box</h1>
 *
 * <p>The {@code UMLInterfaceBox} class represents a UML Interface within the UML Editor application.
 * It encapsulates the properties and behaviors of a UML interface, including its name, position,
 * and methods. This class implements the {@code UMLElementBoxInterface} to ensure consistent access
 * and modification of UML elements.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *     <li>Managing the name and position (X and Y coordinates) of the interface.</li>
 *     <li>Handling the list of methods associated with the interface.</li>
 *     <li>Managing the visual representation of the interface as a {@code VBox} in the user interface.</li>
 *     <li>Facilitating synchronization between the model and the view by updating the UI upon changes.</li>
 * </ul>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class UMLInterfaceBox implements UMLElementBoxInterface {

    /**
     * The name of the UML interface.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The X-coordinate position of the interface on the drawing pane.
     */
    @JsonProperty("x")
    private double x;

    /**
     * The Y-coordinate position of the interface on the drawing pane.
     */
    @JsonProperty("y")
    private double y;

    /**
     * The list of methods belonging to the interface.
     */
    @JsonProperty("methods")
    private List<String> methods = new ArrayList<>(); // Added methods list

    /**
     * The visual representation of the UML interface as a {@code VBox} in the user interface.
     * This field is ignored during JSON serialization/deserialization.
     */
    @JsonIgnore
    private VBox visualRepresentation;

    /**
     * Default constructor for Jackson deserialization.
     *
     * <p>This constructor is necessary for the Jackson library to deserialize JSON data into a {@code UMLInterfaceBox} instance.</p>
     */
    public UMLInterfaceBox() {}

    /**
     * Constructs a {@code UMLInterfaceBox} with the specified name, position, and visual representation.
     *
     * <p>This constructor initializes the interface with a name, X and Y coordinates, and its visual {@code VBox} representation.</p>
     *
     * @param name                 The name of the interface.
     * @param x                    The X-coordinate position on the drawing pane.
     * @param y                    The Y-coordinate position on the drawing pane.
     * @param visualRepresentation The {@code VBox} representing the interface in the UI.
     */
    public UMLInterfaceBox(String name, double x, double y, VBox visualRepresentation) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.visualRepresentation = visualRepresentation;
    }

    /**
     * Retrieves the name of the UML interface.
     *
     * @return A {@code String} representing the name of the interface.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the X-coordinate position of the UML interface.
     *
     * @return A {@code double} value indicating the X-coordinate.
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Retrieves the Y-coordinate position of the UML interface.
     *
     * @return A {@code double} value indicating the Y-coordinate.
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Retrieves the visual representation of the UML interface.
     *
     * @return A {@code VBox} representing the interface in the UI, or {@code null} if not set.
     */
    @Override
    public VBox getVisualRepresentation() {
        return visualRepresentation;
    }

    /**
     * Retrieves the list of methods of the UML interface.
     *
     * @return A {@code List} of {@code String} representing the interface's methods.
     */
    public List<String> getMethods() { // Added getter for methods
        return methods;
    }

    /**
     * Sets the name of the UML interface.
     *
     * <p>This method updates the name of the interface. If the visual representation is set,
     * it also updates the corresponding label in the UI to reflect the new name.</p>
     *
     * @param name A {@code String} containing the new name for the interface.
     */
    @Override
    public void setName(String name) {
        this.name = name;
        if (visualRepresentation != null) {
            Label interfaceNameLabel = (Label) visualRepresentation.getChildren().get(1);
            interfaceNameLabel.setText(name);
        }
    }

    /**
     * Sets the X-coordinate position of the UML interface.
     *
     * <p>This method updates the X-coordinate of the interface and moves its visual representation to the new position.</p>
     *
     * @param x A {@code double} value indicating the new X-coordinate.
     */
    @Override
    public void setX(double x) {
        this.x = x;
        if (visualRepresentation != null) {
            visualRepresentation.setLayoutX(x);
        }
    }

    /**
     * Sets the Y-coordinate position of the UML interface.
     *
     * <p>This method updates the Y-coordinate of the interface and moves its visual representation to the new position.</p>
     *
     * @param y A {@code double} value indicating the new Y-coordinate.
     */
    @Override
    public void setY(double y) {
        this.y = y;
        if (visualRepresentation != null) {
            visualRepresentation.setLayoutY(y);
        }
    }

    /**
     * Sets the list of methods for the UML interface.
     *
     * <p>This method updates the interface's methods and refreshes the visual representation to display the new methods.</p>
     *
     * @param methods A {@code List} of {@code String} representing the new methods.
     */
    public void setMethods(List<String> methods) { // Added setter for methods
        this.methods = methods;
        if (visualRepresentation != null) {
            VBox methodsBox = (VBox) visualRepresentation.getChildren().get(2); // Assuming methodsBox is at index 2
            methodsBox.getChildren().clear();
            for (String method : methods) {
                methodsBox.getChildren().add(new Label(method));
            }
        }
    }

    /**
     * Sets the visual representation of the UML interface.
     *
     * <p>This method assigns a {@code VBox} to represent the interface in the user interface. It allows for dynamic updates
     * to the UI based on changes to the interface's properties.</p>
     *
     * @param visualRepresentation A {@code VBox} representing the interface in the UI.
     */
    @Override
    public void setVisualRepresentation(VBox visualRepresentation) {
        this.visualRepresentation = visualRepresentation;
        // Optionally, update the UI based on current model data
    }
}