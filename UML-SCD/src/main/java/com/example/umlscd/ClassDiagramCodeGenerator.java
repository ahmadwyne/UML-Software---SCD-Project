package com.example.umlscd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Handles the generation of Java code from the UML diagram and saves it to a text file.
 */
public class ClassDiagramCodeGenerator {

    /**
     * Generates Java code for all classes, interfaces, and relationships in the diagram and saves it to a file.
     *
     * @param diagram    The UML class diagram model.
     * @param outputFile The file to save the generated code.
     */
    public void generateCodeFiles(ClassDiagramD diagram, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Generate code for classes first
            for (UMLClassBox umlClass : diagram.getClasses()) {
                writer.write(generateClassCode(umlClass));
                writer.newLine();
            }

            // Generate code for interfaces (if needed)
            for (UMLInterfaceBox umlInterface : diagram.getInterfaces()) {
                writer.write(generateInterfaceCode(umlInterface));
                writer.newLine();
            }

            // Generate code for relationships (e.g., inheritance, association)
            for (UMLRelationship relationship : diagram.getRelationships()) {
                writer.write(generateRelationshipCode(relationship));
                writer.newLine();
            }

            System.out.println("Java code generated successfully and saved to " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save the generated code to file.");
        }
    }

    /**
     * Generates Java code for a single class.
     *
     * @param umlClass The UML class box representing a class.
     * @return The generated Java class code as a string.
     */
    private String generateClassCode(UMLClassBox umlClass) {
        StringBuilder classCode = new StringBuilder();

        // Class declaration
        classCode.append("public class ").append(umlClass.getName()).append(" {\n");

        // Fields (attributes)
        for (String attribute : umlClass.getAttributes()) {
            classCode.append("    private ").append(attribute).append(";\n");
        }

        // Methods
        for (String method : umlClass.getMethods()) {
            classCode.append("    public ").append(method).append(" {\n        // Method body\n    }\n");
        }

        // Close the class block after generating fields and methods
        //classCode.append("}\n");

        return classCode.toString();
    }

    /**
     * Generates Java code for an interface (if required).
     *
     * @param umlInterface The UML interface box representing an interface.
     * @return The generated Java interface code as a string.
     */
    private String generateInterfaceCode(UMLInterfaceBox umlInterface) {
        StringBuilder interfaceCode = new StringBuilder();

        // Interface declaration
        interfaceCode.append("public interface ").append(umlInterface.getName()).append(" {\n");

        // Methods
        for (String method : umlInterface.getMethods()) {
            interfaceCode.append("    public ").append(method).append(";\n");
        }

        // Close the interface block
        interfaceCode.append("}\n");

        return interfaceCode.toString();
    }

    private String generateRelationshipCode(UMLRelationship relationship) {
        StringBuilder relationshipCode = new StringBuilder();

        if (relationship.getType().equalsIgnoreCase("Inheritance")) {
            // Inheritance
            relationshipCode.append("public class ").append(relationship.getStartElementName())
                    .append(" extends ").append(relationship.getEndElementName()).append(" {\n");
            relationshipCode.append("    // Additional fields or methods for subclass\n");
            relationshipCode.append("}\n");
        }
        else if (relationship.getType().equalsIgnoreCase("Association")) {

        relationshipCode.append("    private ").append(relationship.getStartElementName()).append(" ").append(relationship.getEndElementName().toLowerCase()).append(";\n");
        relationshipCode.append("    // Additional fields or methods for subclass\n");
        relationshipCode.append("}\n");
            // Add more conditions here for other types of relationships (e.g., inheritance, composition)

             }
        return relationshipCode.toString();

    }
}