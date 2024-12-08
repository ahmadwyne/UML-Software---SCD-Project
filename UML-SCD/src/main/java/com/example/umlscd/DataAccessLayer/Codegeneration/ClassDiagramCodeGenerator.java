package com.example.umlscd.DataAccessLayer.Codegeneration;

import com.example.umlscd.Models.ClassDiagram.ClassDiagramD;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * <h1>Class Diagram Code Generator</h1>
 *
 * <p>The {@code ClassDiagramCodeGenerator} class is responsible for generating Java code based on a UML class diagram.
 * It generates Java classes and interfaces, including their relationships (inheritance, associations, aggregations, compositions),
 * attributes, and methods, and saves the generated code to a specified file.</p>
 *
 * <p>This class is designed to interact with the UML class diagram model, specifically {@link ClassDiagramD},
 * and generates code that accurately reflects the diagram's structure. It handles code generation for both
 * classes and interfaces, along with their relationships and method definitions, based on the information in the diagram.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class ClassDiagramCodeGenerator {

    /**
     * Generates Java code for classes and interfaces in the given UML diagram and saves it to a specified file.
     *
     * <p>The method iterates through the diagram's classes and interfaces, generating code for each, including relationships,
     * attributes, and methods. The generated code is written to the provided output file.</p>
     *
     * @param diagram    The UML class diagram containing the classes and interfaces.
     * @param outputFile The file where the generated Java code will be saved.
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
     * Generates Java code for a class, including its inheritance, associations, aggregations, and compositions.
     *
     * <p>This method generates the class definition, handling relationships such as inheritance (extends or implements),
     * and associations, aggregations, or compositions, based on the provided UML class and relationships.</p>
     *
     * @param umlClass    The UML class box representing the class to generate code for.
     * @param relationships The list of relationships related to the UML diagram.
     * @param diagram     The UML diagram containing all classes and relationships.
     * @return The Java code for the class with relationships included.
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

        // Handle associations, aggregations, and compositions
        for (UMLRelationship relationship : relationships) {
            if (relationship.getStartElementName().equals(umlClass.getName())) {
                String associationType = relationship.getEndElementName(); // Associated class name
                String fieldName = relationship.getStartElementName();

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
     * Generates the Java code for the attributes and methods of a given UML class.
     *
     * <p>This method processes the attributes and methods of the provided UML class,
     * formatting them according to Java syntax. It handles visibility, parameterization,
     * and method signatures, generating appropriate code for each attribute and method.</p>
     *
     * @param umlClass The UML class box containing the attributes and methods.
     * @return The Java code representing the attributes and methods of the class.
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
     * <p>Function to return the converted visibility for code.<p/>
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
     * Generates the Java code for a given UML interface, including its methods.
     *
     * <p>This method processes the methods of the provided UML interface, formatting them
     * according to Java syntax. It handles method visibility, parameters, and return types
     * to generate the appropriate code.</p>
     *
     * @param umlInterface The UML interface box containing the methods.
     * @return The Java code representing the interface, including its methods.
     */
    private String generateInterfaceCode(UMLInterfaceBox umlInterface) {
        StringBuilder interfaceCode = new StringBuilder();

        // Start the interface definition
        interfaceCode.append("\npublic interface ").append(umlInterface.getName()).append(" {");


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
        interfaceCode.append("\n}");
        return interfaceCode.toString();
    }
}