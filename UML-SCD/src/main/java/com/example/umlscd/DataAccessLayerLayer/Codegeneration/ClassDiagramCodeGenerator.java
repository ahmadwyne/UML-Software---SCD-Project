package com.example.umlscd.DataAccessLayerLayer.Codegeneration;

import com.example.umlscd.Models.ClassDiagram.ClassDiagramD;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;

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
            // Iterate through each class and handle relationships, attributes, and methods
            for (UMLClassBox umlClass : diagram.getClasses()) {
                // Generate the class code with relationships first
                writer.write(generateClassWithRelationships(umlClass, diagram.getRelationships(),diagram));
                writer.newLine();

                // Now add attributes and methods (to be done after relationships are handled)
                writer.write(generateAttributesAndMethods(umlClass));
                writer.newLine();
                writer.append("\n}");
            }

            // Generate code for interfaces (if any)
            for (UMLInterfaceBox umlInterface : diagram.getInterfaces()) {
                writer.write(generateInterfaceCode(umlInterface));
                writer.newLine();
            }

            System.out.println("Java code generated successfully and saved to " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save the generated code to file.");
        }
    }

    /**
     * Generates the class code including its relationships (like inheritance or associations).
     *
     * @param umlClass      The UML class box.
     * @param relationships The relationships for the UML diagram.
     * @return The Java code for the class, including its relationships.
     */
    private String generateClassWithRelationships(UMLClassBox umlClass, List<UMLRelationship> relationships, ClassDiagramD diagram) {
        StringBuilder classCode = new StringBuilder();
        classCode.append("\npublic class ").append(umlClass.getName());

        boolean hasInheritance = false;

        // Handle inheritance (extends or implements)
        for (UMLRelationship relationship : relationships) {
            if (relationship.getType().equalsIgnoreCase("Inheritance") &&
                    relationship.getStartElementName().equals(umlClass.getName())) {
                String parentName = relationship.getEndElementName();

                // Check if the parent is an interface
                boolean isInterface = diagram.getInterfaces().stream()
                        .anyMatch(umlInterface -> umlInterface.getName().equals(parentName));

                if (isInterface) {
                    classCode.append(" implements ").append(parentName);
                } else {
                    classCode.append(" extends ").append(parentName);
                }

                hasInheritance = true;
            }
        }
        // Open the class body
        classCode.append(" {");
        // Handle inheritance (extends)
        /*for (UMLRelationship relationship : relationships) {
            if (relationship.getType().equalsIgnoreCase("Inheritance") &&
                    relationship.getStartElementName().equals(umlClass.getName())) {
                classCode.append(" extends ").append(relationship.getEndElementName());
            }
        }

        // Open the class body
        classCode.append(" {");*/

        // Handle associations, aggregations, and compositions
        for (UMLRelationship relationship : relationships) {
            if (relationship.getStartElementName().equals(umlClass.getName())) {
                String associationType = relationship.getEndElementName(); // Associated class name
                String fieldName = relationship.getStartElementName();//Character.toLowerCase(associationType.charAt(0)) + associationType.substring(1);

                if (relationship.getType().equalsIgnoreCase("Association")) {
                    classCode.append("\n    private ").append(associationType).append(" ").append(fieldName).append(";");
                } else if (relationship.getType().equalsIgnoreCase("Aggregation")) {
                    classCode.append("\n    private ").append(associationType).append(" *").append(fieldName).append(";");
                } else if (relationship.getType().equalsIgnoreCase("Composition")) {
                    classCode.append("\n    private ").append(associationType).append(" *").append(fieldName).append(";");
                }
            }
        }

        //classCode.append("\n}"); // Close the class
        return classCode.toString();
    }


    /**
     * Generates the attributes and methods for the class.
     *
     * @param umlClass The UML class box.
     * @return The Java code for the attributes and methods.
     */
    private String generateAttributesAndMethods(UMLClassBox umlClass) {
        StringBuilder classBody = new StringBuilder();

        // Handle attributes (fields)
        for (String attribute : umlClass.getAttributes()) {
            // Extract visibility and type from the attribute string (e.g., "+ name: String")
            String visibility = attribute.substring(0, 1);  // "+" for public, "-" for private, "#" for protected
            String[] parts = attribute.substring(1).split(":");
            String attributeName = parts[0].trim();
            String attributeType = parts[1].trim();

            // Add the attribute with its visibility, type, and name
            classBody.append("\n    ").append(getVisibility(visibility))
                    .append(" ").append(attributeType).append(" ").append(attributeName).append(";");
        }

        // Handle methods
        for (String method : umlClass.getMethods()) {
            // Extract method visibility and signature (e.g., "+ save():void")
            String visibility = method.substring(0, 1);  // "+" for public, "-" for private, "#" for protected
            String methodSignature = method.substring(1);  // Remove visibility symbol

            String returnType = ""; // To store return type
            String methodName = ""; // To store method name
            String methodParams = ""; // To store method parameters

            // Case 1: Method with parameters
            if (methodSignature.contains("(")) {
                // Split before the parentheses to get the method name
                methodName = methodSignature.substring(0, methodSignature.indexOf("(")).trim();
                // Extract parameters
                methodParams = methodSignature.substring(methodSignature.indexOf("(") + 1, methodSignature.indexOf(")")).trim();
                // Extract return type (after the closing parenthesis)
                returnType = methodSignature.substring(methodSignature.indexOf(")") + 2).trim();
            } else {
                // Case 2: Method without parameters (like + save():void)
                String[] methodParts = methodSignature.split(":");
                returnType = methodParts[2].trim(); // Extract return type
                methodName = methodParts[0].trim(); // Extract method name
            }

            // Format parameters correctly if there are any
            StringBuilder formattedParams = new StringBuilder();
            if (!methodParams.isEmpty()) {
                String[] params = methodParams.split(",");  // Split parameters by commas
                for (String param : params) {
                    String[] paramParts = param.trim().split(":");  // Split parameter into type and name

                    if (paramParts.length == 2) {
                        formattedParams.append(paramParts[1].trim()).append(" ").append(paramParts[0].trim()).append(", ");
                    }
                }
                // Remove trailing comma and space if present
                if (formattedParams.length() > 0) {
                    formattedParams.setLength(formattedParams.length() - 2);
                }
            }

            // Handle methods with empty parentheses (no parameters)
            if (methodParams.isEmpty()) {
                classBody.append("\n\n    ").append(getVisibility(visibility))
                        .append(" ").append(returnType).append(" ").append(methodName).append("() {")
                        .append("\n        // TODO: Implement method")
                        .append("\n    }");
            } else {
                // Handle methods with parameters (correctly format the parameters)
                classBody.append("\n\n    ").append(getVisibility(visibility))
                        .append(" ").append(returnType).append(" ").append(methodName).append("(")
                        .append(formattedParams).append(") {")
                        .append("\n        // TODO: Implement method")
                        .append("\n    }");
            }
        }

        return classBody.toString();
    }

    /**
     * Convert visibility symbols to Java visibility keywords.
     *
     * @param visibility The visibility symbol ('+', '-', '#')
     * @return The corresponding Java visibility keyword.
     */
    private String getVisibility(String visibility) {
        switch (visibility) {
            case "+":
                return "public";
            case "-":
                return "private";
            case "#":
                return "protected";
            default:
                return "";
        }
    }


    /**
     * Generates the Java code for interfaces.
     *
     * @param umlInterface The UML interface.
     * @return The Java code for the interface.
     */
    private String generateInterfaceCode(UMLInterfaceBox umlInterface) {
        StringBuilder interfaceCode = new StringBuilder();

        // Start the interface definition
        interfaceCode.append("public interface ").append(umlInterface.getName()).append(" {");


        // Handle methods
        for (String method : umlInterface.getMethods()) {
            // Extract method visibility and signature (e.g., "+ save():void")
            String visibility = method.substring(0, 1);  // "+" for public, "-" for private, "#" for protected
            String methodSignature = method.substring(1);  // Remove visibility symbol

            String returnType = ""; // To store return type
            String methodName = ""; // To store method name
            String methodParams = ""; // To store method parameters

            // Case 1: Method with parameters
            if (methodSignature.contains("(")) {
                // Split before the parentheses to get the method name
                methodName = methodSignature.substring(0, methodSignature.indexOf("(")).trim();
                // Extract parameters
                methodParams = methodSignature.substring(methodSignature.indexOf("(") + 1, methodSignature.indexOf(")")).trim();
                // Extract return type (after the closing parenthesis)
                returnType = methodSignature.substring(methodSignature.indexOf(")") + 2).trim();
            } else {
                // Case 2: Method without parameters (like + save():void)
                String[] methodParts = methodSignature.split(":");
                returnType = methodParts[2].trim(); // Extract return type
                methodName = methodParts[0].trim(); // Extract method name
            }

            // Format parameters correctly if there are any
            StringBuilder formattedParams = new StringBuilder();
            if (!methodParams.isEmpty()) {
                String[] params = methodParams.split(",");  // Split parameters by commas
                for (String param : params) {
                    String[] paramParts = param.trim().split(":");  // Split parameter into type and name

                    if (paramParts.length == 2) {
                        formattedParams.append(paramParts[1].trim()).append(" ").append(paramParts[0].trim()).append(", ");
                    }
                }
                // Remove trailing comma and space if present
                if (formattedParams.length() > 0) {
                    formattedParams.setLength(formattedParams.length() - 2);
                }
            }

            // Handle methods with empty parentheses (no parameters)
            if (methodParams.isEmpty()) {
                interfaceCode.append("\n\n    ").append(getVisibility(visibility))
                        .append(" ").append(returnType).append(" ").append(methodName).append("() {")
                        .append("\n        // TODO: Implement method")
                        .append("\n    }");
            } else {
                // Handle methods with parameters (correctly format the parameters)
                interfaceCode.append("\n\n    ").append(getVisibility(visibility))
                        .append(" ").append(returnType).append(" ").append(methodName).append("(")
                        .append(formattedParams).append(") {")
                        .append("\n        // TODO: Implement method")
                        .append("\n    }");
            }
        }

        return interfaceCode.toString();
    }

    /*private String generateInterfaceCode(UMLInterfaceBox umlInterface) {
        StringBuilder interfaceCode = new StringBuilder();

        // Start the interface definition
        interfaceCode.append("public interface ").append(umlInterface.getName()).append(" {");
        // Add methods
        for (String method : umlInterface.getMethods()) {
            interfaceCode.append("\n    public void ").append(method).append("();");
        }

        interfaceCode.append("\n}");

        return interfaceCode.toString();
    }*/



}
