package com.example.umlscd.Models.ClassDiagram;

import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class UMLClassBoxTest {

    private UMLClassBox classBox;
    private VBox mockVBox;

    @BeforeEach
    void setUp() {
        mockVBox = new VBox();
        classBox = new UMLClassBox("TestClass", 100.0, 200.0, mockVBox);
    }

    @Test
    void testConstructorWithVisualRepresentation() {
        assertEquals("TestClass", classBox.getName(), "Name should be initialized correctly");
        assertEquals(100.0, classBox.getX(), "X coordinate should be initialized correctly");
        assertEquals(200.0, classBox.getY(), "Y coordinate should be initialized correctly");
        assertEquals(mockVBox, classBox.getVisualRepresentation(), "Visual representation should be set correctly");
        assertNotNull(classBox.getAttributes(), "Attributes list should be initialized");
        assertTrue(classBox.getAttributes().isEmpty(), "Attributes list should be empty initially");
        assertNotNull(classBox.getMethods(), "Methods list should be initialized");
        assertTrue(classBox.getMethods().isEmpty(), "Methods list should be empty initially");
    }

    @Test
    void testConstructorWithAttributesAndMethods() {
        List<String> attrs = Arrays.asList("private int id", "private String name");
        List<String> methods = Arrays.asList("public void setId(int id)", "public int getId()");
        UMLClassBox box = new UMLClassBox("AnotherClass", 150.0, 250.0, attrs, methods);

        assertEquals("AnotherClass", box.getName(), "Name should be initialized correctly");
        assertEquals(150.0, box.getX(), "X coordinate should be initialized correctly");
        assertEquals(250.0, box.getY(), "Y coordinate should be initialized correctly");
        assertNull(box.getVisualRepresentation(), "Visual representation should be null");
        assertEquals(attrs, box.getAttributes(), "Attributes should be initialized correctly");
        assertEquals(methods, box.getMethods(), "Methods should be initialized correctly");
    }

    @Test
    void testSetName() {
        classBox.setName("UpdatedClass");
        assertEquals("UpdatedClass", classBox.getName(), "Name should be updated correctly");
    }

    @Test
    void testSetX() {
        classBox.setX(300.0);
        assertEquals(300.0, classBox.getX(), "X coordinate should be updated correctly");
    }

    @Test
    void testSetY() {
        classBox.setY(400.0);
        assertEquals(400.0, classBox.getY(), "Y coordinate should be updated correctly");
    }

    @Test
    void testSetVisualRepresentation() {
        VBox newVBox = new VBox();
        classBox.setVisualRepresentation(newVBox);
        assertEquals(newVBox, classBox.getVisualRepresentation(), "Visual representation should be updated correctly");
    }

    @Test
    void testSetAttributes() {
        List<String> attrs = Arrays.asList("private int id", "private String name");
        classBox.setAttributes(attrs);
        assertEquals(attrs, classBox.getAttributes(), "Attributes should be set correctly");
    }

    @Test
    void testSetMethods() {
        List<String> methods = Arrays.asList("public void setId(int id)", "public int getId()");
        classBox.setMethods(methods);
        assertEquals(methods, classBox.getMethods(), "Methods should be set correctly");
    }

    @Test
    void testAttributesListInitialization() {
        UMLClassBox newBox = new UMLClassBox();
        assertNotNull(newBox.getAttributes(), "Attributes list should be initialized");
        assertTrue(newBox.getAttributes().isEmpty(), "Attributes list should be empty initially");
    }

    @Test
    void testMethodsListInitialization() {
        UMLClassBox newBox = new UMLClassBox();
        assertNotNull(newBox.getMethods(), "Methods list should be initialized");
        assertTrue(newBox.getMethods().isEmpty(), "Methods list should be empty initially");
    }
}
