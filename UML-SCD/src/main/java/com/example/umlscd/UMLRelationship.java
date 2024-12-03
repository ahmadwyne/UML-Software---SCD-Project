package com.example.umlscd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a serializable UML Relationship.
 */
public class UMLRelationship {
    @JsonProperty("type")
    private String type; // Association, Aggregation, Composition, Inheritance

    @JsonProperty("startElementName")
    private String startElementName;

    @JsonProperty("endElementName")
    private String endElementName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("startMultiplicity")
    private String startMultiplicity;

    @JsonProperty("endMultiplicity")
    private String endMultiplicity;

    // Default constructor for Jackson
    public UMLRelationship() {}

    public UMLRelationship(String type, String startElementName, String endElementName, String name,
                           String startMultiplicity, String endMultiplicity) {
        this.type = type;
        this.startElementName = startElementName;
        this.endElementName = endElementName;
        this.name = name;
        this.startMultiplicity = startMultiplicity;
        this.endMultiplicity = endMultiplicity;
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
