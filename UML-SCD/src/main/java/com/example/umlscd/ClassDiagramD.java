package com.example.umlscd;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the entire UML Class Diagram.
 */
public class ClassDiagramD {
    @JsonProperty("classes")
    private List<UMLClassBox> classes = new ArrayList<>();
    @JsonProperty("interfaces")
    private List<UMLInterfaceBox> interfaces = new ArrayList<>();
    @JsonProperty("relationships")
    private List<UMLRelationship> relationships = new ArrayList<>();

    // Getters and Setters

    public List<UMLClassBox> getClasses() {
        return classes;
    }

    public void setClasses(List<UMLClassBox> classes) {
        this.classes = classes;
    }

    public List<UMLInterfaceBox> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<UMLInterfaceBox> interfaces) {
        this.interfaces = interfaces;
    }

    public List<UMLRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<UMLRelationship> relationships) {
        this.relationships = relationships;
    }

    // Method to remove relationships involving a specific class
    public void removeRelationshipsByClassName(String className) {
        Iterator<UMLRelationship> iterator = relationships.iterator();

        while (iterator.hasNext()) {
            UMLRelationship relationship = iterator.next();
            // Check if the relationship involves the specified class
            if (relationship.getStartElementName().equals(className) || relationship.getEndElementName().equals(className)) {
                iterator.remove();  // Remove the relationship
            }
        }
    }
}
