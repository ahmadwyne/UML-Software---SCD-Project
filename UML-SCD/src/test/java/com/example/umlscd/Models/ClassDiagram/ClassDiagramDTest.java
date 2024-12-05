package com.example.umlscd.Models.ClassDiagram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class ClassDiagramDTest {

    private ClassDiagramD classDiagram;

    @BeforeEach
    void setUp() {
        classDiagram = new ClassDiagramD();
    }

    @Test
    void testInitialState() {
        assertNotNull(classDiagram.getClasses(), "Classes list should be initialized");
        assertTrue(classDiagram.getClasses().isEmpty(), "Classes list should be empty initially");

        assertNotNull(classDiagram.getInterfaces(), "Interfaces list should be initialized");
        assertTrue(classDiagram.getInterfaces().isEmpty(), "Interfaces list should be empty initially");

        assertNotNull(classDiagram.getRelationships(), "Relationships list should be initialized");
        assertTrue(classDiagram.getRelationships().isEmpty(), "Relationships list should be empty initially");
    }

    @Test
    void testSetAndGetClasses() {
        UMLClassBox class1 = new UMLClassBox();
        UMLClassBox class2 = new UMLClassBox();
        List<UMLClassBox> classes = Arrays.asList(class1, class2);

        classDiagram.setClasses(classes);
        assertEquals(classes, classDiagram.getClasses(), "Classes should be set and retrieved correctly");
    }

    @Test
    void testSetAndGetInterfaces() {
        UMLInterfaceBox interface1 = new UMLInterfaceBox();
        UMLInterfaceBox interface2 = new UMLInterfaceBox();
        List<UMLInterfaceBox> interfaces = Arrays.asList(interface1, interface2);

        classDiagram.setInterfaces(interfaces);
        assertEquals(interfaces, classDiagram.getInterfaces(), "Interfaces should be set and retrieved correctly");
    }

    @Test
    void testSetAndGetRelationships() {
        UMLRelationship rel1 = new UMLRelationship();
        UMLRelationship rel2 = new UMLRelationship();
        List<UMLRelationship> relationships = Arrays.asList(rel1, rel2);

        classDiagram.setRelationships(relationships);
        assertEquals(relationships, classDiagram.getRelationships(), "Relationships should be set and retrieved correctly");
    }

    @Test
    void testAddClass() {
        UMLClassBox classBox = new UMLClassBox();
        classDiagram.getClasses().add(classBox);
        assertTrue(classDiagram.getClasses().contains(classBox), "Class should be added to the classes list");
    }

    @Test
    void testAddInterface() {
        UMLInterfaceBox interfaceBox = new UMLInterfaceBox();
        classDiagram.getInterfaces().add(interfaceBox);
        assertTrue(classDiagram.getInterfaces().contains(interfaceBox), "Interface should be added to the interfaces list");
    }

    @Test
    void testAddRelationship() {
        UMLRelationship relationship = new UMLRelationship();
        classDiagram.getRelationships().add(relationship);
        assertTrue(classDiagram.getRelationships().contains(relationship), "Relationship should be added to the relationships list");
    }
}
