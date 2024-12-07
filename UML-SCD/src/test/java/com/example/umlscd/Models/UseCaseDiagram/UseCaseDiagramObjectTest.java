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
 * Test class for UseCaseDiagramObject.
 */
@ExtendWith(MocitoExtension.class)
class UseCaseDiagramObjectTest extends TestBase {

    private UseCaseDiagramObject obj;

    @BeforeEach
    void setUp() {
        obj = new UseCaseDiagramObject("usecase", 150.0, 250.0, "UseCase1");
        obj.initializeTransientFields();
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("usecase", obj.getType(), "Type should be initialized correctly");
        assertEquals(150.0, obj.getX(), "X coordinate should be initialized correctly");
        assertEquals(250.0, obj.getY(), "Y coordinate should be initialized correctly");
        assertEquals("UseCase1", obj.getName(), "Name should be initialized correctly");
    }

    @Test
    void testSetName() {
        obj.setName("UpdatedUseCase");
        assertEquals("UpdatedUseCase", obj.getName(), "Name should be updated correctly");
    }

    @Test
    void testSetX() {
        obj.setX(200.0);
        assertEquals(200.0, obj.getX(), "X coordinate should be updated correctly");
    }

    @Test
    void testSetY() {
        obj.setY(300.0);
        assertEquals(300.0, obj.getY(), "Y coordinate should be updated correctly");
    }

    @Test
    void testSetType() {
        obj.setType("actor");
        assertEquals("actor", obj.getType(), "Type should be updated correctly");
    }

    @Test
    void testContainsActorInside() {
        obj.setType("actor");
        // Actor is a circle with radius 25
        assertTrue(obj.contains(150.0, 250.0), "Point at center should be inside actor");
        assertTrue(obj.contains(160.0, 250.0), "Point within radius should be inside actor");
    }

    @Test
    void testContainsActorOutside() {
        obj.setType("actor");
        assertFalse(obj.contains(150.0, 300.0), "Point outside actor should not be inside");
        assertFalse(obj.contains(200.0, 250.0), "Point outside actor should not be inside");
    }

    @Test
    void testContainsUseCaseInside() {
        obj.setType("usecase");
        // Use case is an ellipse: x=150, y=250, width=100, height=50
        assertTrue(obj.contains(150.0, 250.0), "Point at center should be inside usecase");
        assertTrue(obj.contains(170.0, 250.0), "Point within ellipse should be inside usecase");
    }

    @Test
    void testContainsUseCaseOutside() {
        obj.setType("usecase");
        assertFalse(obj.contains(50.0, 250.0), "Point outside usecase should not be inside");
        assertFalse(obj.contains(150.0, 300.0), "Point outside usecase should not be inside");
    }

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
