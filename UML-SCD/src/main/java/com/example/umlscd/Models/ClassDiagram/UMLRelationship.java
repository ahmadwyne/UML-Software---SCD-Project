package com.example.umlscd.Models.ClassDiagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.Node;

/**
 * <h1>UML Relationship</h1>
 *
 * <p>The {@code UMLRelationship} class represents a UML Relationship within the UML Editor application.
 * It encapsulates the properties of a relationship between two UML elements, such as classes or interfaces.
 * This class is designed to be serializable, allowing for the persistence and retrieval of relationship
 * data through JSON serialization and deserialization using the Jackson library.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *     <li>Storing the type of relationship (e.g., Association, Aggregation, Composition, Inheritance).</li>
 *     <li>Maintaining references to the start and end elements involved in the relationship.</li>
 *     <li>Capturing additional details such as the relationship name and multiplicities at both ends.</li>
 *     <li>Providing methods to access and modify the relationship's properties.</li>
 * </ul>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class UMLRelationship {

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
    @JsonProperty("type")
    private String type; // Association, Aggregation, Composition, Inheritance

    /**
     * The name of the UML element where the relationship starts.
     *
     * <p>This typically refers to the source class or interface in the relationship.</p>
     */
    @JsonProperty("startElementName")
    private String startElementName;

    /**
     * The name of the UML element where the relationship ends.
     *
     * <p>This typically refers to the target class or interface in the relationship.</p>
     */
    @JsonProperty("endElementName")
    private String endElementName;

    /**
     * The name of the UML relationship.
     *
     * <p>This can be used to describe the nature or role of the relationship between the two elements.</p>
     */
    @JsonProperty("name")
    private String name;

    /**
     * The multiplicity at the start end of the UML relationship.
     *
     * <p>Multiplicity defines how many instances of the start element can be associated with the end element.
     * Examples include "1", "0..*", "1..*", etc.</p>
     */
    @JsonProperty("startMultiplicity")
    private String startMultiplicity;

    /**
     * The multiplicity at the end end of the UML relationship.
     *
     * <p>Multiplicity defines how many instances of the end element can be associated with the start element.
     * Examples include "1", "0..*", "1..*", etc.</p>
     */
    @JsonProperty("endMultiplicity")
    private String endMultiplicity;

    /**
     * Default constructor for Jackson deserialization.
     *
     * <p>This constructor is necessary for the Jackson library to deserialize JSON data into a {@code UMLRelationship} instance.</p>
     */
    public UMLRelationship() {}

    /**
     * Constructs a {@code UMLRelationship} with the specified properties.
     *
     * <p>This constructor initializes the relationship with its type, start and end element names, name, and multiplicities.</p>
     *
     * @param type               The type of the relationship (e.g., Association, Aggregation).
     * @param startElementName   The name of the starting UML element in the relationship.
     * @param endElementName     The name of the ending UML element in the relationship.
     * @param name               The name of the relationship.
     * @param startMultiplicity  The multiplicity at the start end of the relationship.
     * @param endMultiplicity    The multiplicity at the end end of the relationship.
     */
    public UMLRelationship(String type, String startElementName, String endElementName, String name,
                           String startMultiplicity, String endMultiplicity) {
        this.type = type;
        this.startElementName = startElementName;
        this.endElementName = endElementName;
        this.name = name;
        this.startMultiplicity = startMultiplicity;
        this.endMultiplicity = endMultiplicity;
    }

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
     * Provides a string representation of the UML relationship.
     *
     * <p>This method overrides the {@code toString()} method to return a detailed description of the relationship,
     * including its type, connected elements, name, and multiplicities.</p>
     *
     * @return A {@code String} detailing the properties of the UML relationship.
     */
    @Override
    public String toString() {
        return "UMLRelationship{" +
                "type='" + type + '\'' +
                ", startElementName='" + startElementName + '\'' +
                ", endElementName='" + endElementName + '\'' +
                ", name='" + name + '\'' +
                ", startMultiplicity='" + startMultiplicity + '\'' +
                ", endMultiplicity='" + endMultiplicity + '\'' +
                '}';
    }



}