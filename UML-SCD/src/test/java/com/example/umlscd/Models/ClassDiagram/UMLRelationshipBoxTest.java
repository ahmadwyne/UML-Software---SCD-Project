package com.example.umlscd.Models.ClassDiagram;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UMLRelationshipBoxTest {

    private UMLRelationshipBox relationshipBox;

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

    @Test
    void testSetType() {
        relationshipBox.setType("Inheritance");
        assertEquals("Inheritance", relationshipBox.getType(), "Type should be updated correctly");
    }

    @Test
    void testSetStartElementName() {
        relationshipBox.setStartElementName("NewStartClass");
        assertEquals("NewStartClass", relationshipBox.getStartElementName(), "Start element name should be updated correctly");
    }

    @Test
    void testSetEndElementName() {
        relationshipBox.setEndElementName("NewEndClass");
        assertEquals("NewEndClass", relationshipBox.getEndElementName(), "End element name should be updated correctly");
    }

    @Test
    void testSetName() {
        relationshipBox.setName("newRelatesTo");
        assertEquals("newRelatesTo", relationshipBox.getName(), "Name should be updated correctly");
    }

    @Test
    void testSetStartMultiplicity() {
        relationshipBox.setStartMultiplicity("0..1");
        assertEquals("0..1", relationshipBox.getStartMultiplicity(), "Start multiplicity should be updated correctly");
    }

    @Test
    void testSetEndMultiplicity() {
        relationshipBox.setEndMultiplicity("1..*");
        assertEquals("1..*", relationshipBox.getEndMultiplicity(), "End multiplicity should be updated correctly");
    }

    @Test
    void testSetLine() {
        Line newLine = new Line();
        relationshipBox.setLine(newLine);
        assertEquals(newLine, relationshipBox.getLine(), "Line should be updated correctly");
    }

    @Test
    void testSetLabel() {
        Text newLabel = new Text("New Label");
        relationshipBox.setLabel(newLabel);
        assertEquals(newLabel, relationshipBox.getLabel(), "Label should be updated correctly");
    }

    @Test
    void testSetStartMultiplicityText() {
        Text newStartText = new Text("0..1");
        relationshipBox.setStartMultiplicityText(newStartText);
        assertEquals(newStartText, relationshipBox.getStartMultiplicityText(), "Start multiplicity text should be updated correctly");
    }

    @Test
    void testSetEndMultiplicityText() {
        Text newEndText = new Text("1..*");
        relationshipBox.setEndMultiplicityText(newEndText);
        assertEquals(newEndText, relationshipBox.getEndMultiplicityText(), "End multiplicity text should be updated correctly");
    }

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
