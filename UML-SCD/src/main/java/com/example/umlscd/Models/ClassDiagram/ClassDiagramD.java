package com.example.umlscd.Models.ClassDiagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <h1>Class Diagram Representation</h1>
 *
 * <p>The {@code ClassDiagramD} class represents the entire UML Class Diagram within the UML Editor application.
 * It encapsulates all the classes, interfaces, and relationships that constitute the diagram. This class serves
 * as the core model that holds the structural information of the UML diagram, facilitating serialization and
 * deserialization processes for saving and loading diagrams.</p>
 *
 * <p>Key functionalities include:
 * <ul>
 *     <li>Maintaining a list of UML classes.</li>
 *     <li>Maintaining a list of UML interfaces.</li>
 *     <li>Maintaining a list of UML relationships between classes and interfaces.</li>
 *     <li>Providing getters and setters for each of the lists to allow easy access and modification.</li>
 * </ul>
 * </p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class ClassDiagramD {
    /**
     * The list of UML classes present in the diagram.
     */
    @JsonProperty("classes")
    private List<UMLClassBox> classes = new ArrayList<>();

    /**
     * The list of UML interfaces present in the diagram.
     */
    @JsonProperty("interfaces")
    private List<UMLInterfaceBox> interfaces = new ArrayList<>();

    /**
     * The list of UML relationships between classes and interfaces in the diagram.
     */
    @JsonProperty("relationships")
    private List<UMLRelationship> relationships = new ArrayList<>();

    /**
     * Retrieves the list of UML classes in the diagram.
     *
     * @return A {@code List} containing all {@code UMLClassBox} instances.
     */
    public List<UMLClassBox> getClasses() {
        return classes;
    }

    /**
     * Sets the list of UML classes in the diagram.
     *
     * <p>This method replaces the current list of classes with the provided list.</p>
     *
     * @param classes A {@code List} of {@code UMLClassBox} instances to set.
     */
    public void setClasses(List<UMLClassBox> classes) {
        this.classes = classes;
    }

    /**
     * Retrieves the list of UML interfaces in the diagram.
     *
     * @return A {@code List} containing all {@code UMLInterfaceBox} instances.
     */
    public List<UMLInterfaceBox> getInterfaces() {
        return interfaces;
    }

    /**
     * Sets the list of UML interfaces in the diagram.
     *
     * <p>This method replaces the current list of interfaces with the provided list.</p>
     *
     * @param interfaces A {@code List} of {@code UMLInterfaceBox} instances to set.
     */
    public void setInterfaces(List<UMLInterfaceBox> interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * Retrieves the list of UML relationships in the diagram.
     *
     * @return A {@code List} containing all {@code UMLRelationship} instances.
     */
    public List<UMLRelationship> getRelationships() {
        return relationships;
    }

    /**
     * Sets the list of UML relationships in the diagram.
     *
     * <p>This method replaces the current list of relationships with the provided list.</p>
     *
     * @param relationships A {@code List} of {@code UMLRelationship} instances to set.
     */
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