package com.example.umlscd.Models.ClassDiagram;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UMLRelationshipTest {

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
