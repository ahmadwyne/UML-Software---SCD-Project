package com.example.umlscd.Models.UseCaseDiagram;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UseCaseDiagramObject}.
 * <p>
 * This class contains unit tests for the {@link UseCaseDiagramObject} class, ensuring that all
 * the getters, setters, and behavior of {@code UseCaseDiagramObject} work as expected.
 * </p>
 */
@ExtendWith(MocitoExtension.class)
class UseCaseDiagramObjectTest extends TestBase {

    private UseCaseDiagramObject obj;

    /**
     * Initializes the test by creating a new {@link UseCaseDiagramObject} and calling
     * {@link UseCaseDiagramObject#initializeTransientFields()} to set up any transient fields.
     */
    @BeforeEach
    void setUp() {
        obj = new UseCaseDiagramObject("usecase", 150.0, 250.0, "UseCase1");
        obj.initializeTransientFields();
    }

    /**
     * Tests the constructor and getter methods of the {@link UseCaseDiagramObject}.
     * <p>
     * This test verifies that the constructor initializes the object with the expected values.
     * </p>
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("usecase", obj.getType(), "Type should be initialized correctly");
        assertEquals(150.0, obj.getX(), "X coordinate should be initialized correctly");
        assertEquals(250.0, obj.getY(), "Y coordinate should be initialized correctly");
        assertEquals("UseCase1", obj.getName(), "Name should be initialized correctly");
    }

    /**
     * Tests the {@link UseCaseDiagramObject#setName(String)} method and verifies the name is updated correctly.
     * <p>
     * The name should be updated to the new value passed in the setter method.
     * </p>
     */
    @Test
    void testSetName() {
        obj.setName("UpdatedUseCase");
        assertEquals("UpdatedUseCase", obj.getName(), "Name should be updated correctly");
    }

    /**
     * Tests the {@link UseCaseDiagramObject #setX(Double)} method and verifies the X coordinate is updated correctly.
     * <p>
     * The X coordinate should be updated to the new value passed in the setter method.
     * </p>
     */
    @Test
    void testSetX() {
        obj.setX(200.0);
        assertEquals(200.0, obj.getX(), "X coordinate should be updated correctly");
    }

    /**
     * Tests the {@link UseCaseDiagramObject #setY(Double)} method and verifies the Y coordinate is updated correctly.
     * <p>
     * The Y coordinate should be updated to the new value passed in the setter method.
     * </p>
     */
    @Test
    void testSetY() {
        obj.setY(300.0);
        assertEquals(300.0, obj.getY(), "Y coordinate should be updated correctly");
    }

    /**
     * Tests the {@link UseCaseDiagramObject#setType(String)} method and verifies the type is updated correctly.
     * <p>
     * The type should be updated to the new value passed in the setter method.
     * </p>
     */
    @Test
    void testSetType() {
        obj.setType("actor");
        assertEquals("actor", obj.getType(), "Type should be updated correctly");
    }

    /**
     * Tests the {@link UseCaseDiagramObject #contains(Double, Double)} method when the type is "actor".
     * <p>
     * The actor is represented as a circle with a radius of 25. This test checks that points inside the actor's circle
     * are correctly identified as inside, and points outside the circle are identified as outside.
     * </p>
     */
    @Test
    void testContainsActorInside() {
        obj.setType("actor");
        // Actor is a circle with radius 25
        assertTrue(obj.contains(150.0, 250.0), "Point at center should be inside actor");
        assertTrue(obj.contains(160.0, 250.0), "Point within radius should be inside actor");
    }

    /**
     * Tests the {@link UseCaseDiagramObject #contains(Double, Double)} method when the type is "actor".
     * <p>
     * This test verifies that points outside the actor's circle are correctly identified as outside the actor.
     * </p>
     */
    @Test
    void testContainsActorOutside() {
        obj.setType("actor");
        assertFalse(obj.contains(150.0, 300.0), "Point outside actor should not be inside");
        assertFalse(obj.contains(200.0, 250.0), "Point outside actor should not be inside");
    }

    /**
     * Tests the {@link UseCaseDiagramObject #contains(Double, Double)} method when the type is "usecase".
     * <p>
     * The use case is represented as an ellipse. This test checks that points inside the ellipse are correctly identified as inside.
     * </p>
     */
    @Test
    void testContainsUseCaseInside() {
        obj.setType("usecase");
        // Use case is an ellipse: x=150, y=250, width=100, height=50
        assertTrue(obj.contains(150.0, 250.0), "Point at center should be inside usecase");
        assertTrue(obj.contains(170.0, 250.0), "Point within ellipse should be inside usecase");
    }

    /**
     * Tests the {@link UseCaseDiagramObject #contains(Double, Double)} method when the type is "usecase".
     * <p>
     * This test verifies that points outside the ellipse are correctly identified as outside the use case.
     * </p>
     */
    @Test
    void testContainsUseCaseOutside() {
        obj.setType("usecase");
        assertFalse(obj.contains(50.0, 250.0), "Point outside usecase should not be inside");
        assertFalse(obj.contains(150.0, 300.0), "Point outside usecase should not be inside");
    }

    /**
     * Tests the {@link UseCaseDiagramObject#draw(GraphicsContext)} method when the type is "actor".
     * <p>
     * This test verifies that the actor is drawn correctly, including the oval and lines representing the actor's features.
     * It also checks that the name is drawn below the actor.
     * </p>
     */
    @Test
    void testDrawActor() {
        obj.setType("actor");
        GraphicsContext gc = mock(GraphicsContext.class);

        obj.draw(gc);

        // Verify that strokeOval and strokeLine are called with correct parameters
        verify(gc).setFill(Color.BLACK);
        verify(gc).setStroke(Color.BLACK);
        verify(gc).strokeOval(150.0 - 15, 250.0 - 15, 30, 30);
        verify(gc).strokeLine(150.0, 250.0 + 15, 150.0, 250.0 + 50);
        verify(gc).strokeLine(150.0, 250.0 + 25, 150.0 - 15, 250.0 + 40);
        verify(gc).strokeLine(150.0, 250.0 + 25, 150.0 + 15, 250.0 + 40);
        verify(gc).strokeLine(150.0, 250.0 + 50, 150.0 - 10, 250.0 + 70);
        verify(gc).strokeLine(150.0, 250.0 + 50, 150.0 + 10, 250.0 + 70);
        verify(gc).fillText("UseCase1", 150.0 - 15, 250.0 + 90);
    }

    /**
     * Tests the {@link UseCaseDiagramObject#draw(GraphicsContext)} method when the type is "usecase".
     * <p>
     * This test verifies that the use case is drawn correctly, including the ellipse representing the use case
     * and the name drawn in the center of the ellipse.
     * </p>
     */
    @Test
    void testDrawUseCase() {
        obj.setType("usecase");
        GraphicsContext gc = mock(GraphicsContext.class);

        obj.draw(gc);

        // Verify that strokeOval and fillText are called with correct parameters
        verify(gc).setFill(Color.BLACK);
        verify(gc).setStroke(Color.BLACK);
        verify(gc).strokeOval(150.0 - 50, 250.0 - 25, 100, 50);
        verify(gc).fillText("UseCase1", 150.0 - 30, 250.0);
    }
}
