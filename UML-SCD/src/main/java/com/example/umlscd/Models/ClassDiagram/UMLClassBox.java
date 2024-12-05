package com.example.umlscd.Models.ClassDiagram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>UML Class Box</h1>
 *
 * <p>The {@code UMLClassBox} class represents a UML Class within the UML Editor application.
 * It encapsulates the properties and behaviors of a UML class, including its name,
 * position, attributes, methods, and visual representation. This class implements
 * the {@code UMLElementBoxInterface} to ensure consistent access and modification
 * of UML elements.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *     <li>Managing the name, position (X and Y coordinates), attributes, and methods of the class.</li>
 *     <li>Handling the visual representation of the class as a {@code VBox} in the user interface.</li>
 *     <li>Facilitating synchronization between the model and the view by updating the UI upon changes.</li>
 * </ul>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class UMLClassBox implements UMLElementBoxInterface {

    /**
     * The name of the UML class.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The X-coordinate position of the class on the drawing pane.
     */
    @JsonProperty("x")
    private double x;

    /**
     * The Y-coordinate position of the class on the drawing pane.
     */
    @JsonProperty("y")
    private double y;

    /**
     * The list of attributes belonging to the class.
     */
    @JsonProperty("attributes")
    private List<String> attributes = new ArrayList<>();

    /**
     * The list of methods belonging to the class.
     */
    @JsonProperty("methods")
    private List<String> methods = new ArrayList<>();

    /**
     * The visual representation of the UML class as a {@code VBox} in the user interface.
     * This field is ignored during JSON serialization/deserialization.
     */
    @JsonIgnore
    private VBox visualRepresentation; // Not serializable

    /**
     * Default constructor for Jackson deserialization.
     *
     * <p>This constructor is necessary for the Jackson library to deserialize JSON data into a {@code UMLClassBox} instance.</p>
     */
    public UMLClassBox() {}

    /**
     * Constructs a {@code UMLClassBox} with the specified name, position, and visual representation.
     *
     * <p>This constructor initializes the class with a name, X and Y coordinates, and its visual {@code VBox} representation.</p>
     *
     * @param name                 The name of the class.
     * @param x                    The X-coordinate position on the drawing pane.
     * @param y                    The Y-coordinate position on the drawing pane.
     * @param visualRepresentation The {@code VBox} representing the class in the UI.
     */
    public UMLClassBox(String name, double x, double y, VBox visualRepresentation) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.visualRepresentation = visualRepresentation;
    }

    /**
     * Constructs a {@code UMLClassBox} with the specified name, position, attributes, and methods.
     *
     * <p>This constructor initializes the class with a name, X and Y coordinates, as well as lists of attributes and methods.
     * The visual {@code VBox} representation is set separately.</p>
     *
     * @param name       The name of the class.
     * @param x          The X-coordinate position on the drawing pane.
     * @param y          The Y-coordinate position on the drawing pane.
     * @param attributes The list of attributes for the class.
     * @param methods    The list of methods for the class.
     */
    public UMLClassBox(String name, double x, double y, List<String> attributes, List<String> methods) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.attributes = attributes;
        this.methods = methods;
        // visualRepresentation is set separately
    }

    /**
     * Retrieves the name of the UML class.
     *
     * @return A {@code String} representing the name of the class.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the X-coordinate position of the UML class.
     *
     * @return A {@code double} value indicating the X-coordinate.
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Retrieves the Y-coordinate position of the UML class.
     *
     * @return A {@code double} value indicating the Y-coordinate.
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Retrieves the list of attributes of the UML class.
     *
     * @return A {@code List} of {@code String} representing the class's attributes.
     */
    public List<String> getAttributes() {
        return attributes;
    }

    /**
     * Retrieves the list of methods of the UML class.
     *
     * @return A {@code List} of {@code String} representing the class's methods.
     */
    public List<String> getMethods() {
        return methods;
    }

    /**
     * Retrieves the visual representation of the UML class.
     *
     * @return A {@code VBox} representing the class in the UI, or {@code null} if not set.
     */
    @Override
    public VBox getVisualRepresentation() {
        return visualRepresentation;
    }

    /**
     * Sets the name of the UML class.
     *
     * <p>This method updates the name of the class and reflects the change in its visual representation
     * by updating the corresponding label.</p>
     *
     * @param name A {@code String} containing the new name for the class.
     */
    @Override
    public void setName(String name) {
        this.name = name;
        if (visualRepresentation != null) {
            Label classNameLabel = (Label) visualRepresentation.getChildren().get(0);
            classNameLabel.setText(name);
        }
    }

    /**
     * Sets the X-coordinate position of the UML class.
     *
     * <p>This method updates the X-coordinate of the class and moves its visual representation to the new position.</p>
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
     * Sets the Y-coordinate position of the UML class.
     *
     * <p>This method updates the Y-coordinate of the class and moves its visual representation to the new position.</p>
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
     * Sets the list of attributes for the UML class.
     *
     * <p>This method updates the class's attributes and refreshes the visual representation to display the new attributes.</p>
     *
     * @param attributes A {@code List} of {@code String} representing the new attributes.
     */
    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
        if (visualRepresentation != null) {
            VBox attributesBox = (VBox) visualRepresentation.getChildren().get(1);
            attributesBox.getChildren().clear();
            for (String attribute : attributes) {
                attributesBox.getChildren().add(new Label(attribute));
            }
        }
    }

    /**
     * Sets the list of methods for the UML class.
     *
     * <p>This method updates the class's methods and refreshes the visual representation to display the new methods.</p>
     *
     * @param methods A {@code List} of {@code String} representing the new methods.
     */
    public void setMethods(List<String> methods) {
        this.methods = methods;
        if (visualRepresentation != null) {
            VBox methodsBox = (VBox) visualRepresentation.getChildren().get(2);
            methodsBox.getChildren().clear();
            for (String method : methods) {
                methodsBox.getChildren().add(new Label(method));
            }
        }
    }

    /**
     * Sets the visual representation of the UML class.
     *
     * <p>This method assigns a {@code VBox} to represent the class in the user interface. It allows for dynamic updates
     * to the UI based on changes to the class's properties.</p>
     *
     * @param visualRepresentation A {@code VBox} representing the class in the UI.
     */
    @Override
    public void setVisualRepresentation(VBox visualRepresentation) {
        this.visualRepresentation = visualRepresentation;
        // Optionally, update the UI based on current model data
    }
}