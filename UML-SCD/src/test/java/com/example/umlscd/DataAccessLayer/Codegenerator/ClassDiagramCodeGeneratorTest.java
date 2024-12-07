package com.example.umlscd.DataAccessLayer.Codegenerator;

import com.example.umlscd.Models.ClassDiagram.*;
import com.example.umlscd.DataAccessLayer.Codegeneration.ClassDiagramCodeGenerator;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ClassDiagramCodeGeneratorTest {

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
