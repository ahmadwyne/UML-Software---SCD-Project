package com.example.umlscd.DataAccessLayer.Serializers.ClassDiagram;

import com.example.umlscd.Models.ClassDiagram.ClassDiagramD;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.example.umlscd.BusinessLayer.ClassDiagram.ClassDiagramManager;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassDiagramSerializer}.
 * <p>
 * This class tests the serialization and deserialization functionality of the {@link ClassDiagramSerializer} class,
 * as well as the restoration of a diagram using the restored objects. It verifies that a ClassDiagram can be correctly
 * serialized into a file, deserialized back, and restored into a manager object while ensuring that the proper
 * interactions with the manager occur.
 * </p>
 */
class ClassDiagramSerializerTest {

    private ClassDiagramSerializer serializer;
    private ClassDiagramManager mockManager;

    /**
     * Set up the test environment before each test.
     * <p>
     * This method initializes the JavaFX toolkit (required for JavaFX classes such as {@link JFXPanel})
     * and creates an instance of {@link ClassDiagramSerializer} and a mock of {@link ClassDiagramManager}.
     * </p>
     */
    @BeforeEach
    void setUp() {
        // Initialize JavaFX toolkit (required for Platform.runLater())
        new JFXPanel(); // This initializes the JavaFX toolkit

        serializer = new ClassDiagramSerializer();
        mockManager = mock(ClassDiagramManager.class);
    }

    /**
     * Test method to verify that the class diagram can be serialized to a file.
     * <p>
     * This test checks that the {@link ClassDiagramSerializer#serialize(ClassDiagramD, File)} method correctly
     * serializes a diagram object to a file. It asserts that the file is created after serialization.
     * </p>
     *
     * @throws IOException if an I/O error occurs during serialization.
     */
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

    /**
     * Test method to verify that the class diagram can be deserialized from a file.
     * <p>
     * This test verifies that the {@link ClassDiagramSerializer#deserialize(File)} method can correctly read a serialized
     * class diagram from a file and return a valid {@link ClassDiagramD} object. It then checks that the diagram contains
     * the expected number of classes, interfaces, and relationships.
     * </p>
     *
     * @throws IOException if an I/O error occurs during deserialization.
     */
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

    /**
     * Test method to verify that a diagram can be restored to a manager.
     * <p>
     * This test checks that the {@link ClassDiagramSerializer#restoreDiagram(ClassDiagramD, ClassDiagramManager)} method
     * correctly restores a {@link ClassDiagramD} object to the {@link ClassDiagramManager}. It verifies that the mock
     * manager is correctly interacted with during the restoration process.
     * </p>
     */
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
