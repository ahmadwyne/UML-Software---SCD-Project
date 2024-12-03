package com.example.umlscd;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
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
}
