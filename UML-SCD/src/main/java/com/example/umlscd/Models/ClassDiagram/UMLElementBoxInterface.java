package com.example.umlscd.Models.ClassDiagram;

import javafx.scene.layout.VBox;

/**
 * <h1>UML Element Box Interface</h1>
 *
 * <p>The {@code UMLElementBoxInterface} defines a contract for UML element boxes within the UML Editor application.
 * This interface ensures that all UML elements, such as classes and interfaces, provide consistent methods
 * for accessing and modifying their fundamental properties. By implementing this interface, different UML
 * elements can be managed uniformly, facilitating operations like serialization, rendering, and user interactions.</p>
 *
 * <p>Key functionalities include:
 * <ul>
 *     <li>Retrieving the name of the UML element.</li>
 *     <li>Accessing and updating the X and Y coordinates of the element on the drawing pane.</li>
 *     <li>Obtaining and setting the visual representation of the element as a {@code VBox}.</li>
 * </ul>
 * </p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public interface UMLElementBoxInterface {

    /**
     * Retrieves the name of the UML element.
     *
     * <p>This method returns the identifier name of the UML element, such as the class name or interface name.</p>
     *
     * @return A {@code String} representing the name of the UML element.
     */
    String getName();

    /**
     * Retrieves the X-coordinate position of the UML element on the drawing pane.
     *
     * <p>This method returns the horizontal position of the UML element within the diagram's canvas.</p>
     *
     * @return A {@code double} value indicating the X-coordinate.
     */
    double getX();

    /**
     * Retrieves the Y-coordinate position of the UML element on the drawing pane.
     *
     * <p>This method returns the vertical position of the UML element within the diagram's canvas.</p>
     *
     * @return A {@code double} value indicating the Y-coordinate.
     */
    double getY();

    /**
     * Retrieves the visual representation of the UML element.
     *
     * <p>This method returns the {@code VBox} that visually represents the UML element in the user interface.
     * The {@code VBox} contains all necessary UI components to display the element's details.</p>
     *
     * @return A {@code VBox} representing the UML element's visual appearance.
     */
    VBox getVisualRepresentation();

    /**
     * Sets the name of the UML element.
     *
     * <p>This method updates the identifier name of the UML element, such as changing a class name
     * or interface name in the diagram.</p>
     *
     * @param name A {@code String} containing the new name for the UML element.
     */
    void setName(String name);

    /**
     * Sets the X-coordinate position of the UML element on the drawing pane.
     *
     * <p>This method updates the horizontal position of the UML element within the diagram's canvas.</p>
     *
     * @param x A {@code double} value indicating the new X-coordinate.
     */
    void setX(double x);

    /**
     * Sets the Y-coordinate position of the UML element on the drawing pane.
     *
     * <p>This method updates the vertical position of the UML element within the diagram's canvas.</p>
     *
     * @param y A {@code double} value indicating the new Y-coordinate.
     */
    void setY(double y);

    /**
     * Sets the visual representation of the UML element.
     *
     * <p>This method updates the {@code VBox} that visually represents the UML element in the user interface.
     * It allows for dynamic changes to the element's appearance based on user interactions or other operations.</p>
     *
     * @param visualRepresentation A {@code VBox} representing the new visual appearance of the UML element.
     */
    void setVisualRepresentation(VBox visualRepresentation);
}