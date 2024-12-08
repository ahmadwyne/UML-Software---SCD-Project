package com.example.umlscd.Models.ClassDiagram;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UMLRelationship} class.
 * <p>
 * This class tests the functionality of the {@link UMLRelationship} model, which represents a relationship between two UML elements.
 * It verifies that constructors, setters, getters, and the toString method function as expected.
 * </p>
 */
class UMLRelationshipTest {

    /**
     * Tests the default constructor of {@link UMLRelationship}.
     * <p>
     * This test ensures that a {@link UMLRelationship} created with the default constructor has null values for all fields.
     * </p>
     */
    @Test
    void testDefaultConstructor() {
        UMLRelationship relationship = new UMLRelationship();
        assertNull(relationship.getType(), "Type should be null");
        assertNull(relationship.getStartElementName(), "Start element name should be null");
        assertNull(relationship.getEndElementName(), "End element name should be null");
        assertNull(relationship.getName(), "Name should be null");
        assertNull(relationship.getStartMultiplicity(), "Start multiplicity should be null");
        assertNull(relationship.getEndMultiplicity(), "End multiplicity should be null");
    }

    /**
     * Tests the parameterized constructor of {@link UMLRelationship}.
     * <p>
     * This test ensures that all fields are correctly initialized when using the constructor with parameters.
     * </p>
     */
    @Test
    void testParameterizedConstructor() {
        UMLRelationship relationship = new UMLRelationship(
                "Association",
                "ClassA",
                "ClassB",
                "relatesTo",
                "1",
                "0..*"
        );

        assertEquals("Association", relationship.getType(), "Type should be initialized correctly");
        assertEquals("ClassA", relationship.getStartElementName(), "Start element name should be initialized correctly");
        assertEquals("ClassB", relationship.getEndElementName(), "End element name should be initialized correctly");
        assertEquals("relatesTo", relationship.getName(), "Name should be initialized correctly");
        assertEquals("1", relationship.getStartMultiplicity(), "Start multiplicity should be initialized correctly");
        assertEquals("0..*", relationship.getEndMultiplicity(), "End multiplicity should be initialized correctly");
    }

    /**
     * Tests the setter and getter methods of {@link UMLRelationship}.
     * <p>
     * This test ensures that the setter methods correctly update the attributes of the {@link UMLRelationship} object,
     * and that the getter methods retrieve the expected values.
     * </p>
     */
    @Test
    void testSettersAndGetters() {
        UMLRelationship relationship = new UMLRelationship();

        relationship.setType("Inheritance");
        assertEquals("Inheritance", relationship.getType(), "Type should be set correctly");

        relationship.setStartElementName("ParentClass");
        assertEquals("ParentClass", relationship.getStartElementName(), "Start element name should be set correctly");

        relationship.setEndElementName("ChildClass");
        assertEquals("ChildClass", relationship.getEndElementName(), "End element name should be set correctly");

        relationship.setName("extends");
        assertEquals("extends", relationship.getName(), "Name should be set correctly");

        relationship.setStartMultiplicity("1");
        assertEquals("1", relationship.getStartMultiplicity(), "Start multiplicity should be set correctly");

        relationship.setEndMultiplicity("1..*");
        assertEquals("1..*", relationship.getEndMultiplicity(), "End multiplicity should be set correctly");
    }

    /**
     * Tests the {@link UMLRelationship#toString()} method.
     * <p>
     * This test ensures that the {@link UMLRelationship} object's string representation is correct.
     * The {@link UMLRelationship#toString()} method should return a string containing all the details of the relationship.
     * </p>
     */
    @Test
    void testToString() {
        UMLRelationship relationship = new UMLRelationship(
                "Composition",
                "Container",
                "Contained",
                "contains",
                "1",
                "0..*"
        );

        String expected = "UMLRelationship{type='Composition', startElementName='Container', endElementName='Contained', name='contains', startMultiplicity='1', endMultiplicity='0..*'}";
        assertEquals(expected, relationship.toString(), "toString should return the correct string representation");
    }
}
