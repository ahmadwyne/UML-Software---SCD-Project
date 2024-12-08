package com.example.umlscd.Models.ClassDiagram;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * <h1>UML Relationship Box</h1>
 *
 * <p>The {@code UMLRelationshipBox} class represents a visual UML Relationship within the UML Editor application.
 * It encapsulates the graphical components and metadata associated with a UML relationship between two UML elements,
 * such as classes or interfaces. This class is designed to facilitate the rendering and management of relationships
 * like Association, Aggregation, Composition, and Inheritance in the user interface.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *     <li>Storing relationship metadata, including type, connected elements, name, and multiplicities.</li>
 *     <li>Managing the visual representation of the relationship through JavaFX {@code Line} and {@code Text} nodes.</li>
 *     <li>Providing methods to access and modify both the metadata and visual components.</li>
 *     <li>Converting the visual relationship into a serializable {@code UMLRelationship} model object.</li>
 * </ul>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class UMLRelationshipBox {

    /**
     * The type of the UML relationship.
     *
     * <p>Possible values include:
     * <ul>
     *     <li><b>Association:</b> A generic relationship indicating that two classes are related.</li>
     *     <li><b>Aggregation:</b> A specialized form of association indicating a whole-part relationship.</li>
     *     <li><b>Composition:</b> A strong form of aggregation indicating ownership and lifecycle dependency.</li>
     *     <li><b>Inheritance:</b> A relationship indicating that one class inherits from another.</li>
     * </ul>
     * </p>
     */
    private String type; // Association, Aggregation, Composition, Inheritance

    /**
     * The name of the UML element where the relationship starts.
     *
     * <p>This typically refers to the source class or interface in the relationship.</p>
     */
    private String startElementName;

    /**
     * The name of the UML element where the relationship ends.
     *
     * <p>This typically refers to the target class or interface in the relationship.</p>
     */
    private String endElementName;

    /**
     * The name of the UML relationship.
     *
     * <p>This can be used to describe the nature or role of the relationship between the two elements.</p>
     */
    private String name;

    /**
     * The multiplicity at the start end of the UML relationship.
     *
     * <p>Multiplicity defines how many instances of the start element can be associated with the end element.
     * Examples include "1", "0..*", "1..*", etc.</p>
     */
    private String startMultiplicity;

    /**
     * The multiplicity at the end end of the UML relationship.
     *
     * <p>Multiplicity defines how many instances of the end element can be associated with the start element.
     * Examples include "1", "0..*", "1..*", etc.</p>
     */
    private String endMultiplicity;

    /**
     * The Line node representing the relationship in the UI.
     *
     * <p>This field is marked as {@code transient} because {@code Line} is not serializable.
     * It is used solely for rendering the relationship graphically in the user interface.</p>
     */
    private transient Line line; // Transient since Line is not serializable

    /**
     * The Text node for displaying the relationship name in the UI.
     *
     * <p>This field is marked as {@code transient} because {@code Text} is not serializable.
     * It is used to render the name of the relationship near the graphical representation.</p>
     */
    private transient Text label; // Transient since Text is not serializable

    /**
     * The Text node for displaying the multiplicity at the start end of the relationship.
     *
     * <p>This field is marked as {@code transient} because {@code Text} is not serializable.
     * It is used to render the multiplicity near the start end of the relationship line.</p>
     */
    private transient Text startMultiplicityText;

    /**
     * The Text node for displaying the multiplicity at the end end of the relationship.
     *
     * <p>This field is marked as {@code transient} because {@code Text} is not serializable.
     * It is used to render the multiplicity near the end end of the relationship line.</p>
     */
    private transient Text endMultiplicityText;

    /**
     * Default constructor for Jackson deserialization.
     *
     * <p>This constructor is necessary for the Jackson library to deserialize JSON data into a {@code UMLRelationshipBox} instance.</p>
     */
    public UMLRelationshipBox() {
        // Required for Jackson deserialization
    }

    /**
     * Constructs a {@code UMLRelationshipBox} with specified parameters.
     *
     * <p>This constructor initializes the relationship with its type, connected elements, name, multiplicities,
     * and its visual representation in the UI.</p>
     *
     * @param type                  The type of relationship (e.g., Association).
     * @param startElementName      The name of the starting element.
     * @param endElementName        The name of the ending element.
     * @param name                  The name of the relationship.
     * @param startMultiplicity     The multiplicity at the start.
     * @param endMultiplicity       The multiplicity at the end.
     * @param line                  The {@code Line} node representing the relationship in the UI.
     * @param label                 The {@code Text} node for the relationship name.
     * @param startMultiplicityText The {@code Text} node for the start multiplicity.
     * @param endMultiplicityText   The {@code Text} node for the end multiplicity.
     */
    public UMLRelationshipBox(String type, String startElementName, String endElementName, String name,
                              String startMultiplicity, String endMultiplicity, Line line, Text label,
                              Text startMultiplicityText, Text endMultiplicityText) {
        this.type = type;
        this.startElementName = startElementName;
        this.endElementName = endElementName;
        this.name = name;
        this.startMultiplicity = startMultiplicity;
        this.endMultiplicity = endMultiplicity;
        this.line = line;
        this.label = label;
        this.startMultiplicityText = startMultiplicityText;
        this.endMultiplicityText = endMultiplicityText;
    }

    // Getters and Setters

    /**
     * Retrieves the type of the UML relationship.
     *
     * @return A {@code String} representing the type of the relationship.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the UML relationship.
     *
     * <p>Valid types include "Association", "Aggregation", "Composition", and "Inheritance".</p>
     *
     * @param type A {@code String} specifying the type of the relationship.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the name of the starting UML element in the relationship.
     *
     * @return A {@code String} representing the name of the starting element.
     */
    public String getStartElementName() {
        return startElementName;
    }

    /**
     * Sets the name of the starting UML element in the relationship.
     *
     * @param startElementName A {@code String} specifying the name of the starting element.
     */
    public void setStartElementName(String startElementName) {
        this.startElementName = startElementName;
    }

    /**
     * Retrieves the name of the ending UML element in the relationship.
     *
     * @return A {@code String} representing the name of the ending element.
     */
    public String getEndElementName() {
        return endElementName;
    }

    /**
     * Sets the name of the ending UML element in the relationship.
     *
     * @param endElementName A {@code String} specifying the name of the ending element.
     */
    public void setEndElementName(String endElementName) {
        this.endElementName = endElementName;
    }

    /**
     * Retrieves the name of the UML relationship.
     *
     * @return A {@code String} representing the name of the relationship.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the UML relationship.
     *
     * @param name A {@code String} specifying the new name for the relationship.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the multiplicity at the start end of the UML relationship.
     *
     * @return A {@code String} representing the start multiplicity.
     */
    public String getStartMultiplicity() {
        return startMultiplicity;
    }

    /**
     * Sets the multiplicity at the start end of the UML relationship.
     *
     * <p>Multiplicity defines how many instances of the start element can be associated with the end element.
     * Examples include "1", "0..*", "1..*", etc.</p>
     *
     * @param startMultiplicity A {@code String} specifying the start multiplicity.
     */
    public void setStartMultiplicity(String startMultiplicity) {
        this.startMultiplicity = startMultiplicity;
    }

    /**
     * Retrieves the multiplicity at the end end of the UML relationship.
     *
     * @return A {@code String} representing the end multiplicity.
     */
    public String getEndMultiplicity() {
        return endMultiplicity;
    }

    /**
     * Sets the multiplicity at the end end of the UML relationship.
     *
     * <p>Multiplicity defines how many instances of the end element can be associated with the start element.
     * Examples include "1", "0..*", "1..*", etc.</p>
     *
     * @param endMultiplicity A {@code String} specifying the end multiplicity.
     */
    public void setEndMultiplicity(String endMultiplicity) {
        this.endMultiplicity = endMultiplicity;
    }

    /**
     * Retrieves the {@code Line} node representing the relationship in the UI.
     *
     * @return The {@code Line} node, or {@code null} if not set.
     */
    public Line getLine() {
        return line;
    }

    /**
     * Sets the {@code Line} node representing the relationship in the UI.
     *
     * <p>This method assigns a {@code Line} to visually represent the relationship in the user interface.</p>
     *
     * @param line The {@code Line} node to set.
     */
    public void setLine(Line line) {
        this.line = line;
    }

    /**
     * Retrieves the {@code Text} node for displaying the relationship name in the UI.
     *
     * @return The {@code Text} node, or {@code null} if not set.
     */
    public Text getLabel() {
        return label;
    }

    /**
     * Sets the {@code Text} node for displaying the relationship name in the UI.
     *
     * <p>This method assigns a {@code Text} node to render the name of the relationship near its graphical representation.</p>
     *
     * @param label The {@code Text} node to set.
     */
    public void setLabel(Text label) {
        this.label = label;
    }

    /**
     * Retrieves the {@code Text} node for displaying the multiplicity at the start end of the relationship in the UI.
     *
     * @return The {@code Text} node, or {@code null} if not set.
     */
    public Text getStartMultiplicityText() {
        return startMultiplicityText;
    }

    /**
     * Sets the {@code Text} node for displaying the multiplicity at the start end of the relationship in the UI.
     *
     * <p>This method assigns a {@code Text} node to render the start multiplicity near the start end of the relationship line.</p>
     *
     * @param startMultiplicityText The {@code Text} node to set.
     */
    public void setStartMultiplicityText(Text startMultiplicityText) {
        this.startMultiplicityText = startMultiplicityText;
    }

    /**
     * Retrieves the {@code Text} node for displaying the multiplicity at the end end of the relationship in the UI.
     *
     * @return The {@code Text} node, or {@code null} if not set.
     */
    public Text getEndMultiplicityText() {
        return endMultiplicityText;
    }

    /**
     * Sets the {@code Text} node for displaying the multiplicity at the end end of the relationship in the UI.
     *
     * <p>This method assigns a {@code Text} node to render the end multiplicity near the end end of the relationship line.</p>
     *
     * @param endMultiplicityText The {@code Text} node to set.
     */
    public void setEndMultiplicityText(Text endMultiplicityText) {
        this.endMultiplicityText = endMultiplicityText;
    }

    /**
     * Converts this {@code UMLRelationshipBox} to a {@code UMLRelationship} model object.
     *
     * <p>This method creates a new {@code UMLRelationship} instance based on the current state of this {@code UMLRelationshipBox},
     * excluding the transient UI components. This is useful for serialization purposes where only the essential relationship data
     * needs to be persisted.</p>
     *
     * @return A {@code UMLRelationship} object representing this relationship.
     */
    public UMLRelationship getUmlRelationship() {
        return new UMLRelationship(
                this.type,
                this.startElementName,
                this.endElementName,
                this.name,
                this.startMultiplicity,
                this.endMultiplicity
        );
    }

    /**
     * Sets the Association Name for the relation
     *
     * @param newName A {@code String} specifying the new name for the relationship
     */
    public void setAssociationName(String newName) {
        this.name=newName;
    }
}