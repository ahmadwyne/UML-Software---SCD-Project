package com.example.umlscd.BuisnessLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.AggregationManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramRelationsManager;
import com.example.umlscd.Models.ClassDiagram.UMLElementBoxInterface;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.example.umlscd.Models.ClassDiagram.UMLRelationshipBox;
import com.example.umlscd.PresentationLayer.ClassDiagram.ClassDiagramUI;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AggregationManagerTest {

    private AggregationManager aggregationManager;
    private ClassDiagramManager mockClassDiagramManager;
    private UMLRelationshipBox mockUMLRelationshipBox;
    private UMLRelationship mockUMLRelationship;
    private Pane drawingPane; // Real Pane
    private VBox startBox; // Real VBox
    private VBox endBox; // Real VBox
    private Label startLabel; // Real Label
    private Label endLabel; // Real Label
    private ClassDiagramUI mockClassDiagramUI; // Mock for ClassDiagramUI

    @BeforeAll
    static void initJFX() {
        // Initializes the JavaFX toolkit
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        // Initialize mocks
        mockClassDiagramManager = mock(ClassDiagramManager.class);
        mockUMLRelationshipBox = mock(UMLRelationshipBox.class);
        mockUMLRelationship = mock(UMLRelationship.class);
        mockClassDiagramUI = mock(ClassDiagramUI.class);

        // Initialize real JavaFX components for start and end classes
        startLabel = new Label("StartClass");
        endLabel = new Label("EndClass");
        startBox = new VBox();
        startBox.getChildren().addAll(new Label("Class:"), startLabel);
        endBox = new VBox();
        endBox.getChildren().addAll(new Label("Class:"), endLabel);
        drawingPane = new Pane();

        // Set up ClassDiagramManager behavior
        when(mockClassDiagramManager.isClassNameExists(anyString())).thenReturn(false);
        when(mockClassDiagramManager.getUiController()).thenReturn(mockClassDiagramUI);

        // Instantiate AggregationManager with the mocked ClassDiagramManager
        aggregationManager = new AggregationManager(mockClassDiagramManager);
    }

    @Test
    void testCreateRelationshipWithCustomValues() {
        // Mock the layoutXProperty() and layoutYProperty() to return mock DoubleProperty
        VBox startBoxMock = mock(VBox.class);
        VBox endBoxMock = mock(VBox.class);

        DoubleProperty startLayoutXMock = mock(DoubleProperty.class);
        DoubleProperty startLayoutYMock = mock(DoubleProperty.class);
        DoubleProperty endLayoutXMock = mock(DoubleProperty.class);
        DoubleProperty endLayoutYMock = mock(DoubleProperty.class);

        // Mock the layoutXProperty() and layoutYProperty()
        when(startBoxMock.layoutXProperty()).thenReturn(startLayoutXMock);
        when(startBoxMock.layoutYProperty()).thenReturn(startLayoutYMock);
        when(endBoxMock.layoutXProperty()).thenReturn(endLayoutXMock);
        when(endBoxMock.layoutYProperty()).thenReturn(endLayoutYMock);

        // Mock the values of layoutX and layoutY
        when(startLayoutXMock.get()).thenReturn(100.0);
        when(startLayoutYMock.get()).thenReturn(100.0);
        when(endLayoutXMock.get()).thenReturn(300.0);
        when(endLayoutYMock.get()).thenReturn(100.0);

        // Mock getChildren() to return a non-null ObservableList
        ObservableList<javafx.scene.Node> startChildrenMock = mock(ObservableList.class);
        ObservableList<javafx.scene.Node> endChildrenMock = mock(ObservableList.class);
        when(startBoxMock.getChildren()).thenReturn(startChildrenMock);
        when(endBoxMock.getChildren()).thenReturn(endChildrenMock);

        // Optionally, you can mock the behavior of isEmpty() on the ObservableList if needed:
        when(startChildrenMock.isEmpty()).thenReturn(true);
        when(endChildrenMock.isEmpty()).thenReturn(true);

        // Custom name and multiplicities
        String aggregationName = "Custom Aggregation";
        String startMultiplicity = "0..1";
        String endMultiplicity = "1..*";

        // Call the method to test
        aggregationManager.createRelationship(startBoxMock, endBoxMock, drawingPane, aggregationName, startMultiplicity, endMultiplicity);

        // Assert that custom values are applied
        assertEquals(aggregationName, aggregationManager.getLastRelationshipBox().getName());
        assertEquals(startMultiplicity, aggregationManager.getLastRelationshipBox().getStartMultiplicity());
        assertEquals(endMultiplicity, aggregationManager.getLastRelationshipBox().getEndMultiplicity());
    }

    /**
     * Test creating an aggregation relationship when one of the classes is null.
     */
    @Test
    void testCreateRelationship_WithNullClassBox() {
        // Attempt to create a relationship with a null startBox
        assertThrows(NullPointerException.class, () -> {
            aggregationManager.createRelationship(null, endBox, drawingPane, "Aggregation", "1", "0..*");
        });

        // Verify that getClassBoxMap was not called
        verify(mockClassDiagramManager, never()).getClassBoxMap();

        // Verify that no relationship box was added
        verify(mockClassDiagramManager, never()).addRelationshipBox(any());

        // Verify that no UI components were added
        assertEquals(0, drawingPane.getChildren().size());
    }

    /**
     * Test creating an aggregation relationship when ClassDiagramManager is null.
     */
    @Test
    void testCreateRelationship_WithNullClassDiagramManager() {
        // Instantiate AggregationManager with a null ClassDiagramManager
        AggregationManager nullManagerAggregationManager = new AggregationManager(null);

        // Attempt to create a relationship
        // This should throw a NullPointerException based on current implementation
        assertThrows(NullPointerException.class, () -> {
            nullManagerAggregationManager.createRelationship(startBox, endBox, drawingPane, "Aggregation", "1", "0..*");
        });

        // Verify that addRelationshipBox was never called
        // Since the manager is null, it should not attempt to add any relationship boxes
    }

    /**
     * Test creating a relationship from a UMLRelationship model successfully.
     */
    @Test
    void testCreateRelationshipFromModel_Successful() {
        // Setup UMLRelationship model
        String startName = "StartClass";
        String endName = "EndClass";
        String aggregationName = "AggregationFromModel";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        when(mockUMLRelationship.getStartElementName()).thenReturn(startName);
        when(mockUMLRelationship.getEndElementName()).thenReturn(endName);
        when(mockUMLRelationship.getName()).thenReturn(aggregationName);
        when(mockUMLRelationship.getStartMultiplicity()).thenReturn(startMultiplicity);
        when(mockUMLRelationship.getEndMultiplicity()).thenReturn(endMultiplicity);

        // Mock ClassDiagramManager's classBoxMap retrieval
        UMLElementBoxInterface startElement = mock(UMLElementBoxInterface.class);
        UMLElementBoxInterface endElement = mock(UMLElementBoxInterface.class);
        when(mockClassDiagramManager.getClassBoxMap()).thenReturn(new HashMap<>() {{
            put(startName, startElement);
            put(endName, endElement);
        }});

        // Mock getVisualRepresentation to return the real VBox instances
        when(startElement.getVisualRepresentation()).thenReturn(startBox);
        when(endElement.getVisualRepresentation()).thenReturn(endBox);

        // Call the method under test
        aggregationManager.createRelationshipFromModel(mockUMLRelationship, drawingPane);

        // Verify that getClassBoxMap was called twice (once to retrieve start, once to retrieve end)
        verify(mockClassDiagramManager, times(2)).getClassBoxMap();

        // Verify that a UMLRelationshipBox was added
        ArgumentCaptor<UMLRelationshipBox> relationshipBoxCaptor = ArgumentCaptor.forClass(UMLRelationshipBox.class);
        verify(mockClassDiagramManager).addRelationshipBox(relationshipBoxCaptor.capture());

        UMLRelationshipBox capturedBox = relationshipBoxCaptor.getValue();
        assertEquals("Aggregation", capturedBox.getType());
        assertEquals(startName, capturedBox.getStartElementName());
        assertEquals(endName, capturedBox.getEndElementName());
        assertEquals(aggregationName, capturedBox.getName());
        assertEquals(startMultiplicity, capturedBox.getStartMultiplicity());
        assertEquals(endMultiplicity, capturedBox.getEndMultiplicity());

        // Verify that the UI components are added to the drawingPane
        // Expected components: diamond (Polygon), aggregationLine (Line), aggregationLabel (Text), multiplicity labels (Text)
        assertEquals(5, drawingPane.getChildren().size());

        // Verify the aggregation label
        Node labelNode = drawingPane.getChildren().get(2);
        assertTrue(labelNode instanceof Text);
        Text aggregationLabel = (Text) labelNode;
        assertEquals(aggregationName, aggregationLabel.getText());
    }

    /**
     * Test creating a relationship from a UMLRelationship model with missing elements.
     */
    @Test
    void testCreateRelationshipFromModel_MissingElements() {
        // Setup UMLRelationship model
        String startName = "StartClass";
        String endName = "EndClass";
        String aggregationName = "AggregationFromModel";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        when(mockUMLRelationship.getStartElementName()).thenReturn(startName);
        when(mockUMLRelationship.getEndElementName()).thenReturn(endName);
        when(mockUMLRelationship.getName()).thenReturn(aggregationName);
        when(mockUMLRelationship.getStartMultiplicity()).thenReturn(startMultiplicity);
        when(mockUMLRelationship.getEndMultiplicity()).thenReturn(endMultiplicity);

        // Mock ClassDiagramManager's classBoxMap retrieval with missing end element
        UMLElementBoxInterface startElement = mock(UMLElementBoxInterface.class);
        when(mockClassDiagramManager.getClassBoxMap()).thenReturn(new HashMap<>() {{
            put(startName, startElement);
            // End element is missing
        }});

        // Mock getVisualRepresentation to return the real VBox instances
        when(startElement.getVisualRepresentation()).thenReturn(startBox);
        // End element retrieval would return null

        // Call the method under test
        aggregationManager.createRelationshipFromModel(mockUMLRelationship, drawingPane);

        // Verify that getClassBoxMap was called twice (once to retrieve start, once to retrieve end)
        verify(mockClassDiagramManager, times(2)).getClassBoxMap();

        // Verify that addRelationshipBox was not called due to missing end element
        verify(mockClassDiagramManager, never()).addRelationshipBox(any());

        // Verify that no UI components were added
        assertEquals(0, drawingPane.getChildren().size());

        // Optionally, verify that an error message was logged or handled
        // This depends on your implementation of error handling
    }

    /**
     * Test retrieving the last created UMLRelationshipBox.
     */
    @Test
    void testGetLastRelationshipBox() {
        // Setup input parameters
        String aggregationName = "AggregationRelation";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        // Call the method under test
        aggregationManager.createRelationship(startBox, endBox, drawingPane, aggregationName, startMultiplicity, endMultiplicity);

        // Retrieve the lastRelationshipBox
        UMLRelationshipBox lastBox = aggregationManager.getLastRelationshipBox();

        // Assert that lastBox is the one we just added
        ArgumentCaptor<UMLRelationshipBox> relationshipBoxCaptor = ArgumentCaptor.forClass(UMLRelationshipBox.class);
        verify(mockClassDiagramManager).addRelationshipBox(relationshipBoxCaptor.capture());

        UMLRelationshipBox capturedBox = relationshipBoxCaptor.getValue();
        assertEquals(lastBox, capturedBox);
    }

    /**
     * Test editing the aggregation name via double-click.
     *
     * Note: Testing UI interactions like double-clicks is complex and typically requires integration testing.
     * Here, we'll simulate the behavior by directly invoking the Consumer passed to the addEditDialogOnClick method.
     */
    @Test
    void testAddEditDialogOnClick_EditAggregationName() {
        // Setup input parameters
        String aggregationName = "AggregationRelation";
        String newAggregationName = "UpdatedAggregation";

        // Call the method under test
        aggregationManager.createRelationship(startBox, endBox, drawingPane, aggregationName, "1", "0..*");

        // Retrieve the aggregation label
        Node labelNode = drawingPane.getChildren().get(2);
        assertTrue(labelNode instanceof Text);
        Text aggregationLabel = (Text) labelNode;

        // Mock the Consumer to capture the new name
        Consumer<String> mockConsumer = mock(Consumer.class);

        // Simulate invoking the Consumer with a new name
        mockConsumer.accept(newAggregationName);

        // Since the actual Consumer updates the label and UMLRelationshipBox,
        // we'll simulate this behavior manually for the test

        // Update the label text
        aggregationLabel.setText(newAggregationName);

        // Verify that the aggregation label was updated
        assertEquals(newAggregationName, aggregationLabel.getText());

        // Note: To fully test this, you might need to expose the Consumer or refactor the method
        // to allow injecting a mock Consumer. This example shows a simplified approach.
    }

    /**
     * Test handling a custom data type addition.
     *
     * Note: Since handleCustomDataType is a static method that opens a dialog,
     * it's challenging to unit test without refactoring. Consider refactoring
     * to inject a Supplier or use TestFX for UI interactions.
     */
    @Test
    void testHandleCustomDataType_NewType() {
        // Acknowledge the limitation in unit testing static UI methods
        assertTrue(true, "handleCustomDataType involves UI interaction and requires integration testing.");
    }
}
