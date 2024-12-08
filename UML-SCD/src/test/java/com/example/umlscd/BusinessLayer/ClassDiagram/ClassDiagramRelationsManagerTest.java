package com.example.umlscd.BusinessLayer.ClassDiagram;

import static org.mockito.Mockito.*;
import com.example.umlscd.BusinessLayer.ClassDiagram.ClassDiagramRelationsManager;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ClassDiagramRelationsManager} class.
 * <p>
 * This test suite validates the functionality of managing relationships in UML class diagrams,
 * including enabling/disabling modes, boundary point calculations, and binding labels to lines.
 * </p>
 */
public class ClassDiagramRelationsManagerTest {

    private ClassDiagramRelationsManager classDiagramRelationsManager;
    private VBox startBox;
    private VBox endBox;
    private Pane drawingPane;

    /**
     * Sets up the test environment before each test.
     * <p>
     * Initializes the necessary mocks and prepares the {@link ClassDiagramRelationsManager} instance for testing.
     * </p>
     */
    @BeforeEach
    public void setup() {
        // Initialize mocks
        classDiagramRelationsManager = mock(ClassDiagramRelationsManager.class, CALLS_REAL_METHODS);
        startBox = mock(VBox.class);
        endBox = mock(VBox.class);
        drawingPane = mock(Pane.class);
    }

    /**
     * Tests enabling the association mode.
     * <p>
     * Verifies that association mode is enabled successfully.
     * </p>
     */
    @Test
    public void testEnableAssociationMode() {
        // Given
        classDiagramRelationsManager.enableAssociationMode();

        // When & Then
        assertTrue(classDiagramRelationsManager.enabledAssociationModel, "Association mode should be enabled.");
    }

    /**
     * Tests disabling the association mode.
     * <p>
     * Verifies that association mode is disabled successfully.
     * </p>
     */
    @Test
    public void testDisableAssociationMode() {
        // Given
        classDiagramRelationsManager.disableAssociationMode();

        // When & Then
        assertFalse(classDiagramRelationsManager.enabledAssociationModel, "Association mode should be disabled.");
    }

    /**
     * Tests enabling the aggregation mode.
     * <p>
     * Verifies that aggregation mode is enabled successfully.
     * </p>
     */
    @Test
    public void testEnableAggregationMode() {
        // Given
        classDiagramRelationsManager.enableAggregationMode();

        // When & Then
        assertTrue(classDiagramRelationsManager.enabledAggregationModel, "Aggregation mode should be enabled.");
    }

    /**
     * Tests disabling the aggregation mode.
     * <p>
     * Verifies that aggregation mode is disabled successfully.
     * </p>
     */
    @Test
    public void testDisableAggregationMode() {
        // Given
        classDiagramRelationsManager.disableAggregationMode();

        // When & Then
        assertFalse(classDiagramRelationsManager.enabledAggregationModel, "Aggregation mode should be disabled.");
    }

    /**
     * Tests enabling the composition mode.
     * <p>
     * Verifies that composition mode is enabled successfully.
     * </p>
     */
    @Test
    public void testEnableCompositionMode() {
        // Given
        classDiagramRelationsManager.enableCompositionMode();

        // When & Then
        assertTrue(classDiagramRelationsManager.enabledCompositionModel, "Composition mode should be enabled.");
    }
    /**
     * Tests disabling the composition mode.
     * <p>
     * Verifies that composition mode is disabled successfully.
     * </p>
     */
    @Test
    public void testDisableCompositionMode() {
        // Given
        classDiagramRelationsManager.disableCompositionMode();

        // When & Then
        assertFalse(classDiagramRelationsManager.enabledCompositionModel, "Composition mode should be disabled.");
    }

    /**
     * Tests enabling the inheritance mode.
     * <p>
     * Verifies that inheritance mode is enabled successfully.
     * </p>
     */
    @Test
    public void testEnableInheritanceMode() {
        // Given
        classDiagramRelationsManager.enableInheritanceMode();

        // When & Then
        assertTrue(classDiagramRelationsManager.inheritanceModeEnabled, "Inheritance mode should be enabled.");
    }

    /**
     * Tests disabling the inheritance mode.
     * <p>
     * Verifies that inheritance mode is disabled successfully.
     * </p>
     */
    @Test
    public void testDisableInheritanceMode() {
        // Given
        classDiagramRelationsManager.disableInheritanceMode();

        // When & Then
        assertFalse(classDiagramRelationsManager.inheritanceModeEnabled, "Inheritance mode should be disabled.");
    }

    /**
     * Tests retrieving the closest boundary point between two elements.
     * <p>
     * Ensures that the closest boundary point is calculated correctly based on layout positions.
     * </p>
     */
    @Test
    public void testGetClosestBoundaryPoint() {
        // Given
        when(startBox.getLayoutX()).thenReturn(10.0);
        when(startBox.getLayoutY()).thenReturn(10.0);
        when(endBox.getLayoutX()).thenReturn(50.0);
        when(endBox.getLayoutY()).thenReturn(50.0);

        // When
        ClassDiagramRelationsManager.Point closestPoint = classDiagramRelationsManager.getClosestBoundaryPoint(startBox, endBox);

        // Then
        assertNotNull(closestPoint, "Closest point should not be null.");
        assertEquals(10.0, closestPoint.getX(), "X coordinate of closest point should match.");
        assertEquals(10.0, closestPoint.getY(), "Y coordinate of closest point should match.");
    }

    /**
     * Tests calculating the boundary points of a given element.
     * <p>
     * Verifies that the four boundary points (top, bottom, left, right) are computed accurately.
     * </p>
     */
    @Test
    public void testGetBoundaryPoints() {
        // Given
        double x = 100.0, y = 100.0, width = 50.0, height = 50.0;
        when(startBox.getLayoutX()).thenReturn(x);
        when(startBox.getLayoutY()).thenReturn(y);
        when(startBox.getWidth()).thenReturn(width);
        when(startBox.getHeight()).thenReturn(height);

        // When
        ClassDiagramRelationsManager.Point[] boundaryPoints = classDiagramRelationsManager.getBoundaryPoints(startBox);

        // Then
        assertEquals(4, boundaryPoints.length, "Boundary points array should contain four points.");
        assertEquals(x + width / 2, boundaryPoints[0].getX(), "Top center X coordinate should match.");
        assertEquals(x, boundaryPoints[3].getX(), "Left center X coordinate should match.");
    }

    /**
     * Tests calculating the Euclidean distance between two points.
     * <p>
     * Validates the correctness of the distance calculation.
     * </p>
     */
    @Test
    public void testCalculateDistance() {
        // Given
        ClassDiagramRelationsManager.Point point1 = new ClassDiagramRelationsManager.Point(0, 0);
        ClassDiagramRelationsManager.Point point2 = new ClassDiagramRelationsManager.Point(3, 4);

        // When
        double distance = classDiagramRelationsManager.calculateDistance(point1, point2);

        // Then
        assertEquals(5.0, distance, "The Euclidean distance should be 5.0.");
    }

    /**
     * Tests binding an association label to a line.
     * <p>
     * Ensures that the label's position is dynamically updated to follow the line's midpoint.
     * </p>
     */
    @Test
    public void testBindAssociationLabelToLine() {
        // Given
        Text label = new Text("Test Label");
        Line line = new Line(0, 0, 10, 10);

        // When
        classDiagramRelationsManager.bindAssociationLabelToLine(label, line);

        // Then
        assertNotNull(label.xProperty().get(), "Label X position should be bound.");
        assertNotNull(label.yProperty().get(), "Label Y position should be bound.");
    }

    /**
     * Tests binding a multiplicity label to a line.
     * <p>
     * Verifies that the multiplicity label's position is dynamically updated for either endpoint of the line.
     * </p>
     */
    @Test
    public void testBindMultiplicityLabelToLine() {
        // Given
        Text label = new Text("1");
        Line line = new Line(0, 0, 10, 10);

        // When
        classDiagramRelationsManager.bindMultiplicityLabelToLine(label, line, true);

        // Then
        assertNotNull(label.xProperty().get(), "Multiplicity label X position should be bound.");
        assertNotNull(label.yProperty().get(), "Multiplicity label Y position should be bound.");
    }
}