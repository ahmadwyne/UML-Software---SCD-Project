package com.example.umlscd.Models.UseCaseDiagram;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Association} class.
 * <p>
 * This class tests the functionality of the {@link Association} model, which represents the relationship between two objects
 * in a use case diagram. It verifies the constructor, getters, setters, and other core functionalities of the {@link Association} class.
 * </p>
 */
class AssociationTest extends TestBase{

    /**
     * Tests the constructor and getter methods of the {@link Association} class.
     * <p>
     * This test ensures that the constructor initializes the {@link Association} object with the correct values
     * for the associated objects (obj1, obj2) and the type of relationship.
     * </p>
     */
    @Test
    void testConstructorAndGetters() {
        UseCaseDiagramObject obj1 = new UseCaseDiagramObject("actor", 100.0, 200.0, "Actor1");
        UseCaseDiagramObject obj2 = new UseCaseDiagramObject("usecase", 150.0, 250.0, "UseCase1");
        String type = "association";

        Association association = new Association(obj1, obj2, type);

        assertEquals(obj1, association.getObj1(), "Obj1 should be initialized correctly");
        assertEquals(obj2, association.getObj2(), "Obj2 should be initialized correctly");
        assertEquals(type, association.getType(), "Type should be initialized correctly");
    }

    /**
     * Tests the setter methods of the {@link Association} class.
     * <p>
     * This test ensures that the setter methods correctly update the associated objects (obj1, obj2) and the type of relationship
     * in the {@link Association} object.
     * </p>
     */
    @Test
    void testSetters() {
        UseCaseDiagramObject obj1 = new UseCaseDiagramObject("actor", 100.0, 200.0, "Actor1");
        UseCaseDiagramObject obj2 = new UseCaseDiagramObject("usecase", 150.0, 250.0, "UseCase1");
        String type = "association";

        Association association = new Association(obj1, obj2, type);

        UseCaseDiagramObject newObj1 = new UseCaseDiagramObject("actor", 120.0, 220.0, "Actor2");
        UseCaseDiagramObject newObj2 = new UseCaseDiagramObject("usecase", 180.0, 280.0, "UseCase2");
        String newType = "dependency";

        association.setObj1(newObj1);
        association.setObj2(newObj2);
        association.setType(newType);

        assertEquals(newObj1, association.getObj1(), "Obj1 should be updated correctly");
        assertEquals(newObj2, association.getObj2(), "Obj2 should be updated correctly");
        assertEquals(newType, association.getType(), "Type should be updated correctly");
    }
}
