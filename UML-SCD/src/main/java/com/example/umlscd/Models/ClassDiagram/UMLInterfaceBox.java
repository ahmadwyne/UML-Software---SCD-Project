package com.example.umlscd.Models.ClassDiagram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>UML Interface Box</h1>
 *
 * <p>The {@code UMLInterfaceBox} class represents a UML Interface within the UML Editor application.
 * It encapsulates the behaviors of a UML Interface, including its name, position, methods,
 * and visual representation. This class implements the {@code UMLElementBoxInterface} to ensure
 * consistent access and modification of UML elements.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *     <li>Managing the name, position (X and Y coordinates), and methods of the interface.</li>
 *     <li>Handling the visual representation of the interface as a {@code VBox} in the user interface.</li>
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
    private List<String> methods = new ArrayList<>();

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
    public UMLInterfaceBox() {
    }

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
    public String getName() {
        return name;
    }

    /**
     * Retrieves the X-coordinate position of the UML interface.
     *
     * @return A {@code double} value indicating the X-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Retrieves the Y-coordinate position of the UML interface.
     *
     * @return A {@code double} value indicating the Y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Retrieves the visual representation of the UML interface.
     *
     * @return A {@code VBox} representing the interface in the UI, or {@code null} if not set.
     */
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
     * Sets the name of the UML class.
     *
     * @param name A {@code String} containing the new name for the interface.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the X-coordinate position of the UML interface.
     *
     * @param x A {@code double} value indicating the new X-coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the X-coordinate position of the UML interface.
     *
     * @param y A {@code double} value indicating the new Y-coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Sets the visual representation of the UML interface.
     *
     * @param visualRepresentation A {@code VBox} representing the interface in the UI.
     */
    public void setVisualRepresentation(VBox visualRepresentation) {
        this.visualRepresentation = visualRepresentation;
    }

    /**
     * Sets the list of methods for the UML interface.
     *
     * <p>This method updates the interface's methods and refreshes the visual representation to display the new methods.</p>
     *
     * @param methods A {@code List} of {@code String} representing the new methods.
     */
    public void setMethods(List<String> methods) {
        this.methods = methods;
        if (visualRepresentation != null && visualRepresentation.getChildren().size() > 2) {
            Node node = visualRepresentation.getChildren().get(2);
            if (node instanceof VBox) {
                VBox methodsBox = (VBox) node;
                methodsBox.getChildren().clear();
                for (String method : methods) {
                    methodsBox.getChildren().add(new Label(method));
                }
            } else {
                throw new IllegalStateException("Expected a VBox as the third child of visualRepresentation for methods.");
            }
        }
    }
}

