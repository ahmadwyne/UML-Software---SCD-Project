package com.example.umlscd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles serialization and deserialization of the UML Class Diagram.
 */
public class ClassDiagramSerializer {
    private final ObjectMapper objectMapper;

    public ClassDiagramSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Serializes the ClassDiagramD object to a JSON file.
     *
     * @param diagram The ClassDiagramD to serialize.
     * @param file    The file to write the JSON to.
     * @throws IOException If an I/O error occurs.
     */
    public void serialize(ClassDiagramD diagram, File file) throws IOException {
        objectMapper.writeValue(file, diagram);
    }

    /**
     * Deserializes a JSON file into a ClassDiagramD object.
     *
     * @param file The JSON file to deserialize.
     * @return The deserialized ClassDiagramD.
     * @throws IOException If an I/O error occurs.
     */
    public ClassDiagramD deserialize(File file) throws IOException {
        ClassDiagramD diagram = objectMapper.readValue(file, ClassDiagramD.class);
        System.out.println("Deserialized diagram: ");
        System.out.println("Classes: " + diagram.getClasses());
        System.out.println("Interfaces: " + diagram.getInterfaces());
        System.out.println("Relationships: " + diagram.getRelationships());
        return diagram;
    }

    /**
     * Restores the diagram in the UI based on the deserialized data.
     *
     * @param diagram The deserialized ClassDiagramD.
     * @param manager The ClassDiagramManager to update.
     */
    public void restoreDiagram(ClassDiagramD diagram, ClassDiagramManager manager) {
        System.out.println("Restoring diagram...");

        // Restore classes (ensure we do this on JavaFX application thread)
        Platform.runLater(() -> {
            restoreClasses(diagram.getClasses(), manager);
            restoreInterfaces(diagram.getInterfaces(), manager);
            restoreRelationships(diagram.getRelationships(), manager);
        });
    }

    private void restoreClasses(List<UMLClassBox> classes, ClassDiagramManager manager) {
        System.out.println("Restoring classes...");

        List<UMLClassBox> classBoxesCopy = new ArrayList<>(classes);
        for (UMLClassBox umlClass : classBoxesCopy) {
            System.out.println("Restoring class: " + umlClass.getName());
            manager.reCreateClassBox(
                    umlClass.getName(),
                    umlClass.getX(),
                    umlClass.getY(),
                    umlClass.getAttributes(),
                    umlClass.getMethods() // Corrected to pass methods
            );
        }

        // Debugging: Print the classBoxMap contents
        System.out.println("classBoxMap after restoring classes: " + manager.getClassBoxMap().keySet());
    }

    private void restoreInterfaces(List<UMLInterfaceBox> interfaces, ClassDiagramManager manager) {
        System.out.println("Restoring interfaces...");

        List<UMLInterfaceBox> interfaceBoxesCopy = new ArrayList<>(interfaces);
        for (UMLInterfaceBox umlInterface : interfaceBoxesCopy) {
            System.out.println("Restoring interface: " + umlInterface.getName());
            manager.reCreateInterfaceBox(umlInterface.getName(), umlInterface.getX(), umlInterface.getY());
        }

        // Debugging: Print the classBoxMap contents
        System.out.println("classBoxMap after restoring interfaces: " + manager.getClassBoxMap().keySet());
    }

    private void restoreRelationships(List<UMLRelationship> relationships, ClassDiagramManager manager) {
        System.out.println("Restoring relationships...");

        List<UMLRelationship> relationshipsCopy = new ArrayList<>(relationships);
        for (UMLRelationship relationship : relationshipsCopy) {
            System.out.println("Restoring relationship: " + relationship.getName());
            manager.createRelationshipFromSerialization(relationship);
        }
    }
}
