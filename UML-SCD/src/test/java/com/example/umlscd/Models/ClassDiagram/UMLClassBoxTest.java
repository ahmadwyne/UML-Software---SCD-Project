package com.example.umlscd.Models.ClassDiagram;

import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the {@link UMLClassBox} class.
 * <p>
 * This class tests the functionality of the {@link UMLClassBox} model, which represents a class in a UML class diagram.
 * It validates that the class constructor, setter methods, and getter methods are functioning as expected.
 * </p>
 */
class UMLClassBoxTest {

    private UMLClassBox classBox;
    private VBox mockVBox;

    /**
     * Initializes a new {@link UMLClassBox} instance before each test case.
     * <p>
     * This method is executed before each test to ensure the {@link UMLClassBox} is in a clean state with a mock
     * {@link VBox} for visual representation.
     * </p>
     */
    @BeforeEach
    void setUp() {
        mockVBox = new VBox();
        classBox = new UMLClassBox("TestClass", 100.0, 200.0, mockVBox);
    }

    /**
     * Tests the constructor of {@link UMLClassBox} with visual representation.
     * <p>
     * This test ensures that the constructor correctly initializes the name, coordinates (X and Y), visual representation,
     * and the empty lists for attributes and methods.
     * </p>
     */
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

    /**
     * Tests the constructor of {@link UMLClassBox} with attributes and methods.
     * <p>
     * This test ensures that the constructor initializes the class with a specific set of attributes and methods,
     * and that the visual representation is null in this case.
     * </p>
     */
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

    /**
     * Tests the setter method for the name in {@link UMLClassBox}.
     * <p>
     * This test ensures that the {@link UMLClassBox} correctly updates the name when using the setter method.
     * </p>
     */
    @Test
    void testSetName() {
        classBox.setName("UpdatedClass");
        assertEquals("UpdatedClass", classBox.getName(), "Name should be updated correctly");
    }

    /**
     * Tests the setter method for the X coordinate in {@link UMLClassBox}.
     * <p>
     * This test ensures that the {@link UMLClassBox} correctly updates the X coordinate when using the setter method.
     * </p>
     */
    @Test
    void testSetX() {
        classBox.setX(300.0);
        assertEquals(300.0, classBox.getX(), "X coordinate should be updated correctly");
    }

    /**
     * Tests the setter method for the Y coordinate in {@link UMLClassBox}.
     * <p>
     * This test ensures that the {@link UMLClassBox} correctly updates the Y coordinate when using the setter method.
     * </p>
     */
    @Test
    void testSetY() {
        classBox.setY(400.0);
        assertEquals(400.0, classBox.getY(), "Y coordinate should be updated correctly");
    }

    /**
     * Tests the setter method for the visual representation in {@link UMLClassBox}.
     * <p>
     * This test ensures that the {@link UMLClassBox} correctly updates its visual representation when using the setter method.
     * </p>
     */
    @Test
    void testSetVisualRepresentation() {
        VBox newVBox = new VBox();
        classBox.setVisualRepresentation(newVBox);
        assertEquals(newVBox, classBox.getVisualRepresentation(), "Visual representation should be updated correctly");
    }

    /**
     * Tests the setter method for the attributes list in {@link UMLClassBox}.
     * <p>
     * This test ensures that the {@link UMLClassBox} correctly updates its list of attributes when using the setter method.
     * </p>
     */
    @Test
    void testSetAttributes() {
        List<String> attrs = Arrays.asList("private int id", "private String name");
        classBox.setAttributes(attrs);
        assertEquals(attrs, classBox.getAttributes(), "Attributes should be set correctly");
    }

    /**
     * Tests the setter method for the methods list in {@link UMLClassBox}.
     * <p>
     * This test ensures that the {@link UMLClassBox} correctly updates its list of methods when using the setter method.
     * </p>
     */
    @Test
    void testSetMethods() {
        List<String> methods = Arrays.asList("public void setId(int id)", "public int getId()");
        classBox.setMethods(methods);
        assertEquals(methods, classBox.getMethods(), "Methods should be set correctly");
    }

    /**
     * Verifies that the attributes list is initialized correctly in the default constructor.
     * <p>
     * This test checks that a newly created {@link UMLClassBox} object has its attributes list initialized to an empty list.
     * </p>
     */
    @Test
    void testAttributesListInitialization() {
        UMLClassBox newBox = new UMLClassBox();
        assertNotNull(newBox.getAttributes(), "Attributes list should be initialized");
        assertTrue(newBox.getAttributes().isEmpty(), "Attributes list should be empty initially");
    }

    /**
     * Verifies that the methods list is initialized correctly in the default constructor.
     * <p>
     * This test checks that a newly created {@link UMLClassBox} object has its methods list initialized to an empty list.
     * </p>
     */
    @Test
    void testMethodsListInitialization() {
        UMLClassBox newBox = new UMLClassBox();
        assertNotNull(newBox.getMethods(), "Methods list should be initialized");
        assertTrue(newBox.getMethods().isEmpty(), "Methods list should be empty initially");
    }
}
