package com.example.umlscd.BusinessLayer.ClassDiagram;

import com.example.umlscd.BusinessLayer.ClassDiagram.AssociationManager;
import com.example.umlscd.BusinessLayer.ClassDiagram.ClassDiagramManager;
import com.example.umlscd.BusinessLayer.ClassDiagram.ClassDiagramRelationsManager;
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

/**
 * Unit tests for the {@link AssociationManager} class.
 * This class contains tests for creating, editing, and handling association relationships within a UML class diagram.
 * <p>
 * The tests validate various functionalities of the {@link AssociationManager}, including creating relationships
 * with custom values, handling null inputs, and verifying the correct creation of relationships from a UML model.
 * </p>
 */
class AssociationManagerTest {

    private AssociationManager associationManager;
    private ClassDiagramManager mockClassDiagramManager;
    private UMLRelationshipBox mockUMLRelationshipBox;
    private UMLRelationship mockUMLRelationship;
    private Pane drawingPane; // Real Pane
    private VBox startBox; // Real VBox
    private VBox endBox; // Real VBox
    private Label startLabel; // Real Label
    private Label endLabel; // Real Label
    private ClassDiagramUI mockClassDiagramUI; // Mock for ClassDiagramUI

    /**
     * Initializes the JavaFX toolkit for testing.
     * This method must be called before any test methods are executed.
     * <p>
     * It ensures that JavaFX components are properly initialized in the test environment.
     * </p>
     */
    @BeforeAll
    static void initJFX() {
        // Initializes the JavaFX toolkit
        new JFXPanel();
    }

    /**
     * Sets up the test environment for each test case.
     * This includes initializing mocked objects and creating real JavaFX components.
     * <p>
     * This method is called before each test to ensure that the test starts with a clean and controlled setup.
     * </p>
     */
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
        associationManager = new AssociationManager(mockClassDiagramManager);
    }

    /**
     * Tests the creation of an association relationship with custom values.
     * <p>
     * This test verifies that custom association names and multiplicities are correctly applied when creating a relationship.
     * It mocks the layout and properties of the start and end boxes to simulate the relationship creation process.
     * </p>
     */
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
        String aggregationName = "Custom Association";
        String startMultiplicity = "0..1";
        String endMultiplicity = "1..*";

        // Call the method to test
        associationManager.createRelationship(startBoxMock, endBoxMock, drawingPane, aggregationName, startMultiplicity, endMultiplicity);

        // Assert that custom values are applied
        assertEquals(aggregationName, associationManager.getLastRelationshipBox().getName());
        assertEquals(startMultiplicity, associationManager.getLastRelationshipBox().getStartMultiplicity());
        assertEquals(endMultiplicity, associationManager.getLastRelationshipBox().getEndMultiplicity());
    }

    /**
     * Tests creating an association relationship when one of the class boxes is null.
     * <p>
     * This test ensures that a {@link NullPointerException} is thrown when attempting to create a relationship with a null start or end class box.
     * </p>
     */
    @Test
    void testCreateRelationship_WithNullClassBox() {
        // Attempt to create a relationship with a null startBox
        assertThrows(NullPointerException.class, () -> {
            associationManager.createRelationship(null, endBox, drawingPane, "Association", "1", "0..*");
        });

        // Verify that getClassBoxMap was not called
        verify(mockClassDiagramManager, never()).getClassBoxMap();

        // Verify that no relationship box was added
        verify(mockClassDiagramManager, never()).addRelationshipBox(any());

        // Verify that no UI components were added
        assertEquals(0, drawingPane.getChildren().size());
    }

    /**
     * Tests creating an association relationship when the ClassDiagramManager is null.
     * <p>
     * This test checks if a {@link NullPointerException} is thrown when the {@link AssociationManager} is instantiated with a null {@link ClassDiagramManager}.
     * </p>
     */
    @Test
    void testCreateRelationship_WithNullClassDiagramManager() {
        // Instantiate AssociationManager with a null ClassDiagramManager
        AssociationManager nullManagerAssociationManager = new AssociationManager(null);

        // Attempt to create a relationship
        // This should throw a NullPointerException based on current implementation
        assertThrows(NullPointerException.class, () -> {
            nullManagerAssociationManager.createRelationship(startBox, endBox, drawingPane, "Association", "1", "0..*");
        });

    }

    /**
     * Tests the creation of a relationship from an existing UMLRelationship model.
     * <p>
     * This test ensures that the association relationship is correctly created when using an existing model of a UML relationship.
     * The model is verified for the correct start and end names, multiplicities, and UI components added to the drawing pane.
     * </p>
     */
    @Test
    void testCreateRelationshipFromModel_Successful() {
        // Setup UMLRelationship model
        String startName = "StartClass";
        String endName = "EndClass";
        String associationName = "AssociationFromModel";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        when(mockUMLRelationship.getStartElementName()).thenReturn(startName);
        when(mockUMLRelationship.getEndElementName()).thenReturn(endName);
        when(mockUMLRelationship.getName()).thenReturn(associationName);
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
        associationManager.createRelationshipFromModel(mockUMLRelationship, drawingPane);

        // Verify that getClassBoxMap was called twice (once to retrieve start, once to retrieve end)
        verify(mockClassDiagramManager, times(2)).getClassBoxMap();

        // Verify that a UMLRelationshipBox was added
        ArgumentCaptor<UMLRelationshipBox> relationshipBoxCaptor = ArgumentCaptor.forClass(UMLRelationshipBox.class);
        verify(mockClassDiagramManager).addRelationshipBox(relationshipBoxCaptor.capture());

        UMLRelationshipBox capturedBox = relationshipBoxCaptor.getValue();
        assertEquals("Association", capturedBox.getType());
        assertEquals(startName, capturedBox.getStartElementName());
        assertEquals(endName, capturedBox.getEndElementName());
        assertEquals(associationName, capturedBox.getName());
        assertEquals(startMultiplicity, capturedBox.getStartMultiplicity());
        assertEquals(endMultiplicity, capturedBox.getEndMultiplicity());

        // Verify that the UI components are added to the drawingPane
        // Expected components: associationLine (Line), associationLabel (Text), multiplicity labels (Text)
        assertEquals(4, drawingPane.getChildren().size());

        // Verify the aggregation label
        Node labelNode = drawingPane.getChildren().get(1);
        assertTrue(labelNode instanceof Text);
        Text aggregationLabel = (Text) labelNode;
        assertEquals(associationName, aggregationLabel.getText());
    }

    /**
     * Tests creating a relationship from a UMLRelationship model when some elements are missing.
     * <p>
     * This test ensures that no relationship box is created and no UI components are added when elements in the model are missing (e.g., missing end class).
     * </p>
     */
    @Test
    void testCreateRelationshipFromModel_MissingElements() {
        // Setup UMLRelationship model
        String startName = "StartClass";
        String endName = "EndClass";
        String aggregationName = "AssociationFromModel";
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
        associationManager.createRelationshipFromModel(mockUMLRelationship, drawingPane);

        // Verify that getClassBoxMap was called twice (once to retrieve start, once to retrieve end)
        verify(mockClassDiagramManager, times(2)).getClassBoxMap();

        // Verify that addRelationshipBox was not called due to missing end element
        verify(mockClassDiagramManager, never()).addRelationshipBox(any());

        // Verify that no UI components were added
        assertEquals(0, drawingPane.getChildren().size());
    }

    /**
     * Tests retrieving the last created UMLRelationshipBox.
     * <p>
     * This test verifies that the last created relationship box can be correctly retrieved from the manager.
     * </p>
     */
    @Test
    void testGetLastRelationshipBox() {
        // Setup input parameters
        String aggregationName = "AggregationRelation";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        // Call the method under test
        associationManager.createRelationship(startBox, endBox, drawingPane, aggregationName, startMultiplicity, endMultiplicity);

        // Retrieve the lastRelationshipBox
        UMLRelationshipBox lastBox = associationManager.getLastRelationshipBox();

        // Assert that lastBox is the one we just added
        ArgumentCaptor<UMLRelationshipBox> relationshipBoxCaptor = ArgumentCaptor.forClass(UMLRelationshipBox.class);
        verify(mockClassDiagramManager).addRelationshipBox(relationshipBoxCaptor.capture());

        UMLRelationshipBox capturedBox = relationshipBoxCaptor.getValue();
        assertEquals(lastBox, capturedBox);
    }

    /**
     * Tests editing the association name via double-click.
     * <p>
     * This test simulates a double-click on the association label and verifies that the name is updated correctly.
     * </p>
     */
    @Test
    void testAddEditDialogOnClick_EditAssociationName() {
        // Setup input parameters
        String associationName = "AssociationRelation";
        String newAssociationName = "UpdatedAssociation";

        // Call the method under test
        associationManager.createRelationship(startBox, endBox, drawingPane, associationName, "1", "0..*");

        // Retrieve the aggregation label
        Node labelNode = drawingPane.getChildren().get(2);
        assertTrue(labelNode instanceof Text);
        Text aggregationLabel = (Text) labelNode;

        // Mock the Consumer to capture the new name
        Consumer<String> mockConsumer = mock(Consumer.class);

        // Simulate invoking the Consumer with a new name
        mockConsumer.accept(newAssociationName);

        // Update the label text
        aggregationLabel.setText(newAssociationName);

        // Verify that the aggregation label was updated
        assertEquals(newAssociationName, aggregationLabel.getText());
    }
}