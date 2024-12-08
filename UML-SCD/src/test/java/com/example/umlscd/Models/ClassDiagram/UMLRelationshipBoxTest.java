package com.example.umlscd.Models.ClassDiagram;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UMLRelationshipBox} class.
 * <p>
 * This class tests the functionality of the {@link UMLRelationshipBox} model, which represents a relationship between
 * two UML elements. It verifies the initialization of properties through constructors, the correct behavior of setters,
 * and getter methods.
 * </p>
 */
class UMLRelationshipBoxTest {

    private UMLRelationshipBox relationshipBox;

    /**
     * Initializes a new {@link UMLRelationshipBox} instance before each test case.
     * <p>
     * This method is executed before each test to ensure the {@link UMLRelationshipBox} is in a clean state.
     * </p>
     */
    @BeforeEach
    void setUp() {
        Line mockLine = new Line();
        Text mockLabel = new Text("Relationship Label");
        Text mockStartMultiplicity = new Text("1");
        Text mockEndMultiplicity = new Text("0..*");

        relationshipBox = new UMLRelationshipBox(
                "Association",
                "ClassA",
                "ClassB",
                "relatesTo",
                "1",
                "0..*",
                mockLine,
                mockLabel,
                mockStartMultiplicity,
                mockEndMultiplicity
        );
    }

    /**
     * Tests the default constructor of {@link UMLRelationshipBox}.
     * <p>
     * This test ensures that a {@link UMLRelationshipBox} created with the default constructor has null values for all
     * fields except for the ones explicitly initialized.
     * </p>
     */
    @Test
    void testDefaultConstructor() {
        UMLRelationshipBox box = new UMLRelationshipBox();
        assertNull(box.getType(), "Type should be null");
        assertNull(box.getStartElementName(), "Start element name should be null");
        assertNull(box.getEndElementName(), "End element name should be null");
        assertNull(box.getName(), "Name should be null");
        assertNull(box.getStartMultiplicity(), "Start multiplicity should be null");
        assertNull(box.getEndMultiplicity(), "End multiplicity should be null");
        assertNull(box.getLine(), "Line should be null");
        assertNull(box.getLabel(), "Label should be null");
        assertNull(box.getStartMultiplicityText(), "Start multiplicity text should be null");
        assertNull(box.getEndMultiplicityText(), "End multiplicity text should be null");
    }

    /**
     * Tests the parameterized constructor of {@link UMLRelationshipBox}.
     * <p>
     * This test ensures that all fields are correctly initialized using the constructor with parameters.
     * </p>
     */
    @Test
    void testParameterizedConstructor() {
        assertEquals("Association", relationshipBox.getType(), "Type should be initialized correctly");
        assertEquals("ClassA", relationshipBox.getStartElementName(), "Start element name should be initialized correctly");
        assertEquals("ClassB", relationshipBox.getEndElementName(), "End element name should be initialized correctly");
        assertEquals("relatesTo", relationshipBox.getName(), "Name should be initialized correctly");
        assertEquals("1", relationshipBox.getStartMultiplicity(), "Start multiplicity should be initialized correctly");
        assertEquals("0..*", relationshipBox.getEndMultiplicity(), "End multiplicity should be initialized correctly");
        assertNotNull(relationshipBox.getLine(), "Line should be initialized correctly");
        assertNotNull(relationshipBox.getLabel(), "Label should be initialized correctly");
        assertNotNull(relationshipBox.getStartMultiplicityText(), "Start multiplicity text should be initialized correctly");
        assertNotNull(relationshipBox.getEndMultiplicityText(), "End multiplicity text should be initialized correctly");
    }

    /**
     * Tests the setter method for the relationship type.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the relationship type using the setter method.
     * </p>
     */
    @Test
    void testSetType() {
        relationshipBox.setType("Inheritance");
        assertEquals("Inheritance", relationshipBox.getType(), "Type should be updated correctly");
    }

    /**
     * Tests the setter method for the start element name.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the start element name using the setter method.
     * </p>
     */
    @Test
    void testSetStartElementName() {
        relationshipBox.setStartElementName("NewStartClass");
        assertEquals("NewStartClass", relationshipBox.getStartElementName(), "Start element name should be updated correctly");
    }

    /**
     * Tests the setter method for the end element name.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the end element name using the setter method.
     * </p>
     */
    @Test
    void testSetEndElementName() {
        relationshipBox.setEndElementName("NewEndClass");
        assertEquals("NewEndClass", relationshipBox.getEndElementName(), "End element name should be updated correctly");
    }

    /**
     * Tests the setter method for the relationship name.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the name using the setter method.
     * </p>
     */
    @Test
    void testSetName() {
        relationshipBox.setName("newRelatesTo");
        assertEquals("newRelatesTo", relationshipBox.getName(), "Name should be updated correctly");
    }

    /**
     * Tests the setter method for the start multiplicity.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the start multiplicity using the setter method.
     * </p>
     */
    @Test
    void testSetStartMultiplicity() {
        relationshipBox.setStartMultiplicity("0..1");
        assertEquals("0..1", relationshipBox.getStartMultiplicity(), "Start multiplicity should be updated correctly");
    }

    /**
     * Tests the setter method for the end multiplicity.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the end multiplicity using the setter method.
     * </p>
     */
    @Test
    void testSetEndMultiplicity() {
        relationshipBox.setEndMultiplicity("1..*");
        assertEquals("1..*", relationshipBox.getEndMultiplicity(), "End multiplicity should be updated correctly");
    }

    /**
     * Tests the setter method for the line.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the line using the setter method.
     * </p>
     */
    @Test
    void testSetLine() {
        Line newLine = new Line();
        relationshipBox.setLine(newLine);
        assertEquals(newLine, relationshipBox.getLine(), "Line should be updated correctly");
    }

    /**
     * Tests the setter method for the label.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the label using the setter method.
     * </p>
     */
    @Test
    void testSetLabel() {
        Text newLabel = new Text("New Label");
        relationshipBox.setLabel(newLabel);
        assertEquals(newLabel, relationshipBox.getLabel(), "Label should be updated correctly");
    }

    /**
     * Tests the setter method for the start multiplicity text.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the start multiplicity text using the setter method.
     * </p>
     */
    @Test
    void testSetStartMultiplicityText() {
        Text newStartText = new Text("0..1");
        relationshipBox.setStartMultiplicityText(newStartText);
        assertEquals(newStartText, relationshipBox.getStartMultiplicityText(), "Start multiplicity text should be updated correctly");
    }

    /**
     * Tests the setter method for the end multiplicity text.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly updates the end multiplicity text using the setter method.
     * </p>
     */
    @Test
    void testSetEndMultiplicityText() {
        Text newEndText = new Text("1..*");
        relationshipBox.setEndMultiplicityText(newEndText);
        assertEquals(newEndText, relationshipBox.getEndMultiplicityText(), "End multiplicity text should be updated correctly");
    }

    /**
     * Tests the conversion of the {@link UMLRelationshipBox} to a {@link UMLRelationship}.
     * <p>
     * This test ensures that the {@link UMLRelationshipBox} correctly converts its data into a {@link UMLRelationship} object.
     * </p>
     */
    @Test
    void testGetUmlRelationship() {
        UMLRelationship relationship = relationshipBox.getUmlRelationship();
        assertEquals("Association", relationship.getType(), "UMLRelationship type should match");
        assertEquals("ClassA", relationship.getStartElementName(), "UMLRelationship start element name should match");
        assertEquals("ClassB", relationship.getEndElementName(), "UMLRelationship end element name should match");
        assertEquals("relatesTo", relationship.getName(), "UMLRelationship name should match");
        assertEquals("1", relationship.getStartMultiplicity(), "UMLRelationship start multiplicity should match");
        assertEquals("0..*", relationship.getEndMultiplicity(), "UMLRelationship end multiplicity should match");
    }
}
