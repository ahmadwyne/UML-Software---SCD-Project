package com.example.umlscd.DataAccessLayer.Serializers.ClassDiagram;

import com.example.umlscd.Models.ClassDiagram.ClassDiagramD;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

class ClassDiagramSerializerTest {

    private ClassDiagramSerializer serializer;
    private ClassDiagramManager mockManager;

    @BeforeEach
    void setUp() {
        // Initialize JavaFX toolkit (required for Platform.runLater())
        new JFXPanel(); // This initializes the JavaFX toolkit

        serializer = new ClassDiagramSerializer();
        mockManager = mock(ClassDiagramManager.class);
    }

    @Test
    void testSerialize() throws IOException {
        // Prepare test data
        UMLClassBox class1 = new UMLClassBox();
        class1.setName("Person");
        class1.setAttributes(Arrays.asList("+ name: String", "+ age: int"));
        class1.setMethods(Arrays.asList("+ getName(): String", "+ getAge(): int"));

        UMLClassBox class2 = new UMLClassBox();
        class2.setName("Employee");
        class2.setAttributes(Arrays.asList("+ employeeId: String"));
        class2.setMethods(Arrays.asList("+ getEmployeeId(): String"));

        UMLInterfaceBox interface1 = new UMLInterfaceBox();
        interface1.setName("EmployeeActions");
        interface1.setMethods(Arrays.asList("+ work(): void", "+ attendMeeting(): void"));

        UMLRelationship relationship = new UMLRelationship();
        relationship.setType("Inheritance");
        relationship.setStartElementName("Employee");
        relationship.setEndElementName("Person");

        ClassDiagramD diagram = new ClassDiagramD();
        diagram.setClasses(Arrays.asList(class1, class2));
        diagram.setInterfaces(Arrays.asList(interface1));
        diagram.setRelationships(Arrays.asList(relationship));

        // File for serialization
        File outputFile = new File("test-diagram.json");

        // Test serialization
        serializer.serialize(diagram, outputFile);

        // Verify the file was created
        assert(outputFile.exists());
    }

    @Test
    void testDeserialize() throws IOException {
        // Prepare a test file (assuming this file exists in the project)
        File inputFile = new File("test-diagram.json");

        // Deserialize the file
        ClassDiagramD deserializedDiagram = serializer.deserialize(inputFile);

        // Verify deserialization by checking if the diagram contains the expected elements
        assert(deserializedDiagram.getClasses().size() > 0);
        assert(deserializedDiagram.getInterfaces().size() > 0);
        assert(deserializedDiagram.getRelationships().size() > 0);
    }

    @Test
    void testRestoreDiagram() {
        // Prepare test data
        UMLClassBox class1 = new UMLClassBox();
        class1.setName("Person");
        class1.setAttributes(Arrays.asList("+ name: String", "+ age: int"));
        class1.setMethods(Arrays.asList("+ getName(): String", "+ getAge(): int"));

        UMLInterfaceBox interface1 = new UMLInterfaceBox();
        interface1.setName("EmployeeActions");
        interface1.setMethods(Arrays.asList("+ work(): void", "+ attendMeeting(): void"));

        UMLRelationship relationship = new UMLRelationship();
        relationship.setType("Inheritance");
        relationship.setStartElementName("Employee");
        relationship.setEndElementName("Person");

        ClassDiagramD diagram = new ClassDiagramD();
        diagram.setClasses(Arrays.asList(class1));
        diagram.setInterfaces(Arrays.asList(interface1));  // Ensure interfaces list is correctly set
        diagram.setRelationships(Arrays.asList(relationship));

        // Call the restore method
        serializer.restoreDiagram(diagram, mockManager);

        // Verify interactions with the mock ClassDiagramManager
        verify(mockManager, times(1)).reCreateClassBox(anyString(), anyDouble(), anyDouble(), anyList(), anyList());
        verify(mockManager, times(1)).reCreateInterfaceBox(anyString(), anyDouble(), anyDouble(), anyList());
        verify(mockManager, times(1)).createRelationshipFromSerialization(any(UMLRelationship.class));
    }
}
