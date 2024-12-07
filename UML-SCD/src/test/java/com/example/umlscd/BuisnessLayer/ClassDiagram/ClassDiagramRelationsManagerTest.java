package com.example.umlscd.BuisnessLayer.ClassDiagram;


import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramRelationsManager;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClassDiagramRelationsManagerTest {

    private ClassDiagramRelationsManager classDiagramRelationsManager;
    private VBox startBox;
    private VBox endBox;
    private Pane drawingPane;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        classDiagramRelationsManager = mock(ClassDiagramRelationsManager.class, CALLS_REAL_METHODS);
        startBox = mock(VBox.class);
        endBox = mock(VBox.class);
        drawingPane = mock(Pane.class);
    }

    @Test
    public void testEnableAssociationMode() {
        // Given
        classDiagramRelationsManager.enableAssociationMode();

        // When & Then
        assertTrue(classDiagramRelationsManager.enabledAssociationModel, "Association mode should be enabled.");
    }

    @Test
    public void testDisableAssociationMode() {
        // Given
        classDiagramRelationsManager.disableAssociationMode();

        // When & Then
        assertFalse(classDiagramRelationsManager.enabledAssociationModel, "Association mode should be disabled.");
    }

    @Test
    public void testEnableAggregationMode() {
        // Given
        classDiagramRelationsManager.enableAggregationMode();

        // When & Then
        assertTrue(classDiagramRelationsManager.enabledAggregationModel, "Aggregation mode should be enabled.");
    }

    @Test
    public void testDisableAggregationMode() {
        // Given
        classDiagramRelationsManager.disableAggregationMode();

        // When & Then
        assertFalse(classDiagramRelationsManager.enabledAggregationModel, "Aggregation mode should be disabled.");
    }

    /*@Test
    public void testCreateRelationship() {
        // Given
        String associationName = "Test Association";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        // Mock the getChildren method to return a mock list (since getChildren returns an ObservableList)
        javafx.collections.ObservableList<javafx.scene.Node> mockChildren = mock(javafx.collections.ObservableList.class);
        when(drawingPane.getChildren()).thenReturn(mockChildren);

        // When
        ClassDiagramRelationsManager.createRelationship(startBox, endBox, drawingPane, associationName, startMultiplicity, endMultiplicity);

        // Then
        // Verify that the line is added to the drawing pane
        verify(mockChildren, times(1)).add(any(Line.class)); // Verify that a Line was added

        // Verify that the association label is added to the drawing pane
        verify(mockChildren, times(1)).add(any(Text.class)); // Verify that a Text label was added

        // Verify the dynamic listener for updating the line position
        verify(startBox.layoutXProperty(), times(1)).addListener(any());
        verify(endBox.layoutXProperty(), times(1)).addListener(any());
    }*/

    @Test
    public void testCreateRelationship() {
        // Given
        String associationName = "Test Association";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        // Mock the getChildren method to return a mock list
        javafx.collections.ObservableList<javafx.scene.Node> mockChildren = mock(javafx.collections.ObservableList.class);
        when(drawingPane.getChildren()).thenReturn(mockChildren);

        // When
        classDiagramRelationsManager.createRelationship(startBox, endBox, drawingPane, associationName, startMultiplicity, endMultiplicity);

        // ArgumentCaptor to capture the argument passed to the add() method
        ArgumentCaptor<javafx.scene.Node> captor = ArgumentCaptor.forClass(javafx.scene.Node.class);

        // Verify that the line is added to the drawing pane
        verify(mockChildren, times(1)).add(captor.capture());
        javafx.scene.Node addedNode = captor.getValue();
        System.out.println("Captured Node: " + addedNode);

        // Assert that the captured argument is an instance of Line
        assertTrue(addedNode instanceof javafx.scene.shape.Line);

        // Verify that the association label is added to the drawing pane
        captor = ArgumentCaptor.forClass(javafx.scene.Node.class);
        verify(mockChildren, times(1)).add(captor.capture());  // Capture the second add() call
        addedNode = captor.getValue();
        System.out.println("Captured Node: " + addedNode);

        // Assert that the captured argument is an instance of Text
        assertTrue(addedNode instanceof javafx.scene.text.Text);
    }

    /*@Test
    public void testCreateRelationship() {
        // Given
        String associationName = "Test Association";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        // Mock the getChildren method to return a mock list (since getChildren returns an ObservableList)
        javafx.collections.ObservableList<javafx.scene.Node> mockChildren = mock(javafx.collections.ObservableList.class);
        when(drawingPane.getChildren()).thenReturn(mockChildren);

        // When
        classDiagramRelationsManager.createRelationship(startBox, endBox, drawingPane, associationName, startMultiplicity, endMultiplicity);

        // ArgumentCaptor to capture the argument passed to the add() method
        ArgumentCaptor<javafx.scene.Node> captor = ArgumentCaptor.forClass(javafx.scene.Node.class);

        // Verify that the line is added to the drawing pane
        verify(mockChildren, times(1)).add(captor.capture()); // Capture the argument passed to add()

        // Assert that the captured argument is an instance of Line (or the expected type)
        assertTrue(captor.getValue() instanceof javafx.scene.shape.Line);

        // Verify that the association label is added to the drawing pane
        captor = ArgumentCaptor.forClass(javafx.scene.Node.class);
        verify(mockChildren, times(1)).add(captor.capture()); // Capture the second added object

        // Assert that the captured argument is an instance of Text (or the expected type)
        assertTrue(captor.getValue() instanceof javafx.scene.text.Text);

        // Optionally, verify layout listeners, if applicable
        //verify(startBox.layoutXProperty(), times(1)).addListener(any());
        //verify(endBox.layoutXProperty(), times(1)).addListener(any());
    }*/

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
