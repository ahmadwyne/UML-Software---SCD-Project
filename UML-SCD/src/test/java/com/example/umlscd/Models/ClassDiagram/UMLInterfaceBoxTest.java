package com.example.umlscd.Models.ClassDiagram;

import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the {@link UMLInterfaceBox} class.
 * <p>
 * This class tests the functionality of the {@link UMLInterfaceBox} model, which represents an interface in a UML class diagram.
 * It validates the constructor, setter methods, and getter methods of the {@link UMLInterfaceBox}.
 * </p>
 */
class UMLInterfaceBoxTest {

    private UMLInterfaceBox interfaceBox;
    private VBox mockVBox;

    /**
     * Initializes a new {@link UMLInterfaceBox} instance before each test case.
     * <p>
     * This method is executed before each test to ensure the {@link UMLInterfaceBox} is in a clean state with a mock
     * {@link VBox} for visual representation.
     * </p>
     */
    @BeforeEach
    void setUp() {
        mockVBox = new VBox();
        interfaceBox = new UMLInterfaceBox("TestInterface", 50.0, 75.0, mockVBox);
    }

    /**
     * Tests the constructor of {@link UMLInterfaceBox} with visual representation.
     * <p>
     * This test ensures that the constructor correctly initializes the name, coordinates (X and Y), visual representation,
     * and the empty list for methods.
     * </p>
     */
    @Test
    void testConstructorWithVisualRepresentation() {
        assertEquals("TestInterface", interfaceBox.getName(), "Name should be initialized correctly");
        assertEquals(50.0, interfaceBox.getX(), "X coordinate should be initialized correctly");
        assertEquals(75.0, interfaceBox.getY(), "Y coordinate should be initialized correctly");
        assertEquals(mockVBox, interfaceBox.getVisualRepresentation(), "Visual representation should be set correctly");
        assertNotNull(interfaceBox.getMethods(), "Methods list should be initialized");
        assertTrue(interfaceBox.getMethods().isEmpty(), "Methods list should be empty initially");
    }

    /**
     * Tests the setter method for the name in {@link UMLInterfaceBox}.
     * <p>
     * This test ensures that the {@link UMLInterfaceBox} correctly updates the name when using the setter method.
     * </p>
     */
    @Test
    void testSetName() {
        interfaceBox.setName("UpdatedInterface");
        assertEquals("UpdatedInterface", interfaceBox.getName(), "Name should be updated correctly");
    }

    /**
     * Tests the setter method for the X coordinate in {@link UMLInterfaceBox}.
     * <p>
     * This test ensures that the {@link UMLInterfaceBox} correctly updates the X coordinate when using the setter method.
     * </p>
     */
    @Test
    void testSetX() {
        interfaceBox.setX(150.0);
        assertEquals(150.0, interfaceBox.getX(), "X coordinate should be updated correctly");
    }

    /**
     * Tests the setter method for the Y coordinate in {@link UMLInterfaceBox}.
     * <p>
     * This test ensures that the {@link UMLInterfaceBox} correctly updates the Y coordinate when using the setter method.
     * </p>
     */
    @Test
    void testSetY() {
        interfaceBox.setY(225.0);
        assertEquals(225.0, interfaceBox.getY(), "Y coordinate should be updated correctly");
    }

    /**
     * Tests the setter method for the visual representation in {@link UMLInterfaceBox}.
     * <p>
     * This test ensures that the {@link UMLInterfaceBox} correctly updates its visual representation when using the setter method.
     * </p>
     */
    @Test
    void testSetVisualRepresentation() {
        VBox newVBox = new VBox();
        interfaceBox.setVisualRepresentation(newVBox);
        assertEquals(newVBox, interfaceBox.getVisualRepresentation(), "Visual representation should be updated correctly");
    }

    /**
     * Tests the setter method for the methods list in {@link UMLInterfaceBox}.
     * <p>
     * This test ensures that the {@link UMLInterfaceBox} correctly updates its list of methods when using the setter method.
     * </p>
     */
    @Test
    void testSetMethods() {
        List<String> methods = Arrays.asList("public void start()", "public void stop()");
        interfaceBox.setMethods(methods);
        assertEquals(methods, interfaceBox.getMethods(), "Methods should be set correctly");
    }

    /**
     * Verifies that the methods list is initialized correctly in the default constructor.
     * <p>
     * This test checks that a newly created {@link UMLInterfaceBox} object has its methods list initialized to an empty list.
     * </p>
     */
    @Test
    void testMethodsListInitialization() {
        UMLInterfaceBox newBox = new UMLInterfaceBox();
        assertNotNull(newBox.getMethods(), "Methods list should be initialized");
        assertTrue(newBox.getMethods().isEmpty(), "Methods list should be empty initially");
    }
}
