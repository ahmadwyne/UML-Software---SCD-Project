package com.example.umlscd.Models.ClassDiagram;

import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class UMLInterfaceBoxTest {

    private UMLInterfaceBox interfaceBox;
    private VBox mockVBox;

    @BeforeEach
    void setUp() {
        mockVBox = new VBox();
        interfaceBox = new UMLInterfaceBox("TestInterface", 50.0, 75.0, mockVBox);
    }

    @Test
    void testConstructorWithVisualRepresentation() {
        assertEquals("TestInterface", interfaceBox.getName(), "Name should be initialized correctly");
        assertEquals(50.0, interfaceBox.getX(), "X coordinate should be initialized correctly");
        assertEquals(75.0, interfaceBox.getY(), "Y coordinate should be initialized correctly");
        assertEquals(mockVBox, interfaceBox.getVisualRepresentation(), "Visual representation should be set correctly");
        assertNotNull(interfaceBox.getMethods(), "Methods list should be initialized");
        assertTrue(interfaceBox.getMethods().isEmpty(), "Methods list should be empty initially");
    }

    @Test
    void testSetName() {
        interfaceBox.setName("UpdatedInterface");
        assertEquals("UpdatedInterface", interfaceBox.getName(), "Name should be updated correctly");
    }

    @Test
    void testSetX() {
        interfaceBox.setX(150.0);
        assertEquals(150.0, interfaceBox.getX(), "X coordinate should be updated correctly");
    }

    @Test
    void testSetY() {
        interfaceBox.setY(225.0);
        assertEquals(225.0, interfaceBox.getY(), "Y coordinate should be updated correctly");
    }

    @Test
    void testSetVisualRepresentation() {
        VBox newVBox = new VBox();
        interfaceBox.setVisualRepresentation(newVBox);
        assertEquals(newVBox, interfaceBox.getVisualRepresentation(), "Visual representation should be updated correctly");
    }

    @Test
    void testSetMethods() {
        List<String> methods = Arrays.asList("public void start()", "public void stop()");
        interfaceBox.setMethods(methods);
        assertEquals(methods, interfaceBox.getMethods(), "Methods should be set correctly");
    }

    @Test
    void testMethodsListInitialization() {
        UMLInterfaceBox newBox = new UMLInterfaceBox();
        assertNotNull(newBox.getMethods(), "Methods list should be initialized");
        assertTrue(newBox.getMethods().isEmpty(), "Methods list should be empty initially");
    }
}
