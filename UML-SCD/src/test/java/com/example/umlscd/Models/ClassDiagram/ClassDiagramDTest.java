package com.example.umlscd.Models.ClassDiagram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the {@link ClassDiagramD} class.
 * <p>
 * This class tests the functionality of the {@link ClassDiagramD} model, which holds the structure of a class diagram.
 * The tests validate that the internal lists for classes, interfaces, and relationships are initialized correctly, and that
 * adding new elements works as expected.
 * </p>
 */
class ClassDiagramDTest {

    private ClassDiagramD classDiagram;

    /**
     * Initializes a new {@link ClassDiagramD} instance before each test case.
     * <p>
     * This method is automatically executed before each test to ensure the {@link ClassDiagramD} is in a clean state.
     * </p>
     */
    @BeforeEach
    void setUp() {
        classDiagram = new ClassDiagramD();
    }

    /**
     * Verifies the initial state of the {@link ClassDiagramD} instance.
     * <p>
     * This test checks that the lists of classes, interfaces, and relationships are properly initialized to be non-null
     * and empty.
     * </p>
     */
    @Test
    void testInitialState() {
        assertNotNull(classDiagram.getClasses(), "Classes list should be initialized");
        assertTrue(classDiagram.getClasses().isEmpty(), "Classes list should be empty initially");

        assertNotNull(classDiagram.getInterfaces(), "Interfaces list should be initialized");
        assertTrue(classDiagram.getInterfaces().isEmpty(), "Interfaces list should be empty initially");

        assertNotNull(classDiagram.getRelationships(), "Relationships list should be initialized");
        assertTrue(classDiagram.getRelationships().isEmpty(), "Relationships list should be empty initially");
    }

    /**
     * Tests the setter and getter for the {@link UMLClassBox} list in {@link ClassDiagramD}.
     * <p>
     * This test ensures that the {@link ClassDiagramD} can correctly set and retrieve a list of classes.
     * </p>
     */
    @Test
    void testSetAndGetClasses() {
        UMLClassBox class1 = new UMLClassBox();
        UMLClassBox class2 = new UMLClassBox();
        List<UMLClassBox> classes = Arrays.asList(class1, class2);

        classDiagram.setClasses(classes);
        assertEquals(classes, classDiagram.getClasses(), "Classes should be set and retrieved correctly");
    }

    /**
     * Tests the setter and getter for the {@link UMLInterfaceBox} list in {@link ClassDiagramD}.
     * <p>
     * This test ensures that the {@link ClassDiagramD} can correctly set and retrieve a list of interfaces.
     * </p>
     */
    @Test
    void testSetAndGetInterfaces() {
        UMLInterfaceBox interface1 = new UMLInterfaceBox();
        UMLInterfaceBox interface2 = new UMLInterfaceBox();
        List<UMLInterfaceBox> interfaces = Arrays.asList(interface1, interface2);

        classDiagram.setInterfaces(interfaces);
        assertEquals(interfaces, classDiagram.getInterfaces(), "Interfaces should be set and retrieved correctly");
    }

    /**
     * Tests the setter and getter for the {@link UMLRelationship} list in {@link ClassDiagramD}.
     * <p>
     * This test ensures that the {@link ClassDiagramD} can correctly set and retrieve a list of relationships.
     * </p>
     */
    @Test
    void testSetAndGetRelationships() {
        UMLRelationship rel1 = new UMLRelationship();
        UMLRelationship rel2 = new UMLRelationship();
        List<UMLRelationship> relationships = Arrays.asList(rel1, rel2);

        classDiagram.setRelationships(relationships);
        assertEquals(relationships, classDiagram.getRelationships(), "Relationships should be set and retrieved correctly");
    }

    /**
     * Tests adding a {@link UMLClassBox} to the classes list in {@link ClassDiagramD}.
     * <p>
     * This test ensures that new classes can be successfully added to the class diagram's classes list.
     * </p>
     */
    @Test
    void testAddClass() {
        UMLClassBox classBox = new UMLClassBox();
        classDiagram.getClasses().add(classBox);
        assertTrue(classDiagram.getClasses().contains(classBox), "Class should be added to the classes list");
    }

    /**
     * Tests adding a {@link UMLInterfaceBox} to the interfaces list in {@link ClassDiagramD}.
     * <p>
     * This test ensures that new interfaces can be successfully added to the class diagram's interfaces list.
     * </p>
     */
    @Test
    void testAddInterface() {
        UMLInterfaceBox interfaceBox = new UMLInterfaceBox();
        classDiagram.getInterfaces().add(interfaceBox);
        assertTrue(classDiagram.getInterfaces().contains(interfaceBox), "Interface should be added to the interfaces list");
    }

    /**
     * Tests adding a {@link UMLRelationship} to the relationships list in {@link ClassDiagramD}.
     * <p>
     * This test ensures that new relationships can be successfully added to the class diagram's relationships list.
     * </p>
     */
    @Test
    void testAddRelationship() {
        UMLRelationship relationship = new UMLRelationship();
        classDiagram.getRelationships().add(relationship);
        assertTrue(classDiagram.getRelationships().contains(relationship), "Relationship should be added to the relationships list");
    }
}
