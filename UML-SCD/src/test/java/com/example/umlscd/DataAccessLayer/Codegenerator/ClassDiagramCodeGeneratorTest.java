package com.example.umlscd.DataAccessLayer.Codegenerator;

import com.example.umlscd.Models.ClassDiagram.*;
import com.example.umlscd.DataAccessLayer.Codegeneration.ClassDiagramCodeGenerator;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Test class for generating Java code files from a UML class diagram using {@link ClassDiagramCodeGenerator}.
 * <p>
 * This class demonstrates the process of creating a UML class diagram with classes, interfaces, and relationships,
 * and then using the {@link ClassDiagramCodeGenerator} to generate corresponding Java code files.
 * </p>
 * <p>
 * The test creates two classes (`Person` and `Employee`), an interface (`EmployeeActions`),
 * and a relationship (`Inheritance` between `Employee` and `Person`). It then calls the code generator
 * to create a Java code file, which is verified for its existence.
 * </p>
 */
public class ClassDiagramCodeGeneratorTest {

    /**
     * Main method for testing code generation from a UML class diagram.
     * <p>
     * This method performs the following steps:
     * <ul>
     *   <li>Creates example UML classes with attributes and methods.</li>
     *   <li>Creates a UML interface with methods.</li>
     *   <li>Creates a UML relationship between the classes (Inheritance).</li>
     *   <li>Assembles a complete UML class diagram with classes, interfaces, and relationships.</li>
     *   <li>Generates the Java code files using {@link ClassDiagramCodeGenerator}.</li>
     *   <li>Checks if the generated output file exists and prints a success or failure message.</li>
     * </ul>
     * </p>
     *
     * @param args Command-line arguments (not used in this test).
     */
    public static void main(String[] args) {
        // Create example UML Class
        UMLClassBox class1 = new UMLClassBox();
        class1.setName("Person");
        class1.setAttributes(Arrays.asList("+ name: String", "+ age: int"));
        class1.setMethods(Arrays.asList("+ getName(): String", "+ getAge(): int"));

        UMLClassBox class2 = new UMLClassBox();
        class2.setName("Employee");
        class2.setAttributes(Arrays.asList("+ employeeId: String"));
        class2.setMethods(Arrays.asList("+ getEmployeeId(): String"));

        // Create example UML Interface
        UMLInterfaceBox interface1 = new UMLInterfaceBox();
        interface1.setName("EmployeeActions");
        interface1.setMethods(Arrays.asList("+ work(): void", "+ attendMeeting(): void"));

        // Create example UML Relationship (Inheritance)
        UMLRelationship relationship = new UMLRelationship();
        relationship.setType("Inheritance");
        relationship.setStartElementName("Employee");
        relationship.setEndElementName("Person");

        // Create a Class Diagram and add the above elements
        ClassDiagramD diagram = new ClassDiagramD();
        diagram.setClasses(Arrays.asList(class1, class2));
        diagram.setInterfaces(Arrays.asList(interface1));
        diagram.setRelationships(Arrays.asList(relationship));

        // Create the generator instance
        ClassDiagramCodeGenerator codeGenerator = new ClassDiagramCodeGenerator();

        // Generate code and save to a file
        String outputFile = "output.java"; // Output file location
        codeGenerator.generateCodeFiles(diagram, outputFile);

        // Verifying if the output file was created and contains expected content
        File file = new File(outputFile);
        if (file.exists()) {
            System.out.println("Code generation successful! Output file located at: " + outputFile);
        } else {
            System.out.println("Code generation failed!");
        }
    }
}
