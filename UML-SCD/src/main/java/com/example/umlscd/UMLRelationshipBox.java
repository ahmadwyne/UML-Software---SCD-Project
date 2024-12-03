package com.example.umlscd;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * Represents a UML Relationship in the diagram.
 */
public class UMLRelationshipBox {
    private String type; // Association, Aggregation, Composition, Inheritance
    private String startElementName;
    private String endElementName;
    private String name;
    private String startMultiplicity;
    private String endMultiplicity;

    private transient Line line; // Transient since Line is not serializable
    private transient Text label; // Transient since Text is not serializable
    private transient Text startMultiplicityText;
    private transient Text endMultiplicityText;

    /**
     * Default constructor for Jackson.
     */
    public UMLRelationshipBox() {
        // Required for Jackson deserialization
    }

    /**
     * Constructs a UMLRelationshipBox with specified parameters.
     *
     * @param type                  The type of relationship (e.g., Association).
     * @param startElementName      The name of the starting element.
     * @param endElementName        The name of the ending element.
     * @param name                  The name of the relationship.
     * @param startMultiplicity     The multiplicity at the start.
     * @param endMultiplicity       The multiplicity at the end.
     * @param line                  The Line node representing the relationship in the UI.
     * @param label                 The Text node for the relationship name.
     * @param startMultiplicityText The Text node for the start multiplicity.
     * @param endMultiplicityText   The Text node for the end multiplicity.
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

    public String getType() {
        return type;
    }

    public String getStartElementName() {
        return startElementName;
    }

    public String getEndElementName() {
        return endElementName;
    }

    public String getName() {
        return name;
    }

    public String getStartMultiplicity() {
        return startMultiplicity;
    }

    public String getEndMultiplicity() {
        return endMultiplicity;
    }

    public Line getLine() {
        return line;
    }

    public Text getLabel() {
        return label;
    }

    public Text getStartMultiplicityText() {
        return startMultiplicityText;
    }

    public Text getEndMultiplicityText() {
        return endMultiplicityText;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStartElementName(String startElementName) {
        this.startElementName = startElementName;
    }

    public void setEndElementName(String endElementName) {
        this.endElementName = endElementName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartMultiplicity(String startMultiplicity) {
        this.startMultiplicity = startMultiplicity;
    }

    public void setEndMultiplicity(String endMultiplicity) {
        this.endMultiplicity = endMultiplicity;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public void setLabel(Text label) {
        this.label = label;
    }

    public void setStartMultiplicityText(Text startMultiplicityText) {
        this.startMultiplicityText = startMultiplicityText;
    }

    public void setEndMultiplicityText(Text endMultiplicityText) {
        this.endMultiplicityText = endMultiplicityText;
    }

    /**
     * Converts this UMLRelationshipBox to a UMLRelationship model object.
     *
     * @return A UMLRelationship object representing this relationship.
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
}
