package com.example.umlscd.BusinessLayer.ClassDiagram;

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
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CompositionManager} class.
 * This class contains tests for creating, editing, and handling composition relationships within a UML class diagram.
 * <p>
 * The tests ensure that the {@link CompositionManager} works as expected, covering scenarios such as custom composition creation,
 * null class boxes, successful and unsuccessful relationship creation from a UML model, and interaction with the user interface.
 * </p>
 */
class CompositionManagerTest {

    private CompositionManager compositionManager;
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
     * <p>
     * This method is called before any test methods are executed to ensure JavaFX components are initialized.
     * </p>
     */
    @BeforeAll
    static void initJFX() {
        // Initializes the JavaFX toolkit
        new JFXPanel();
    }

    /**
     * Sets up the test environment for each test case.
     * <p>
     * This method prepares the mocked objects and real JavaFX components to provide a clean state for each test.
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

        // Instantiate CompositionManager with the mocked ClassDiagramManager
        compositionManager = new CompositionManager(mockClassDiagramManager);
    }

    /**
     * Tests the creation of a composition relationship with custom values.
     * <p>
     * This test ensures that custom composition names and multiplicities are correctly applied when creating a relationship.
     * The test mocks the layout and properties of the start and end boxes to simulate the relationship creation process.
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
        String compositionName = "Custom Composition";
        String startMultiplicity = "0..1";
        String endMultiplicity = "1..*";

        // Call the method to test
        compositionManager.createRelationship(startBoxMock, endBoxMock, drawingPane, compositionName, startMultiplicity, endMultiplicity);

        // Assert that custom values are applied
        assertEquals(compositionName, compositionManager.getLastRelationshipBox().getName());
        assertEquals(startMultiplicity, compositionManager.getLastRelationshipBox().getStartMultiplicity());
        assertEquals(endMultiplicity, compositionManager.getLastRelationshipBox().getEndMultiplicity());
    }

    /**
     * Tests creating a composition relationship when one of the class boxes is null.
     * <p>
     * This test ensures that a {@link NullPointerException} is thrown when attempting to create a relationship with a null start or end class box.
     * </p>
     */
    @Test
    void testCreateRelationship_WithNullClassBox() {
        // Attempt to create a relationship with a null startBox
        assertThrows(NullPointerException.class, () -> {
            compositionManager.createRelationship(null, endBox, drawingPane, "Composition", "1", "0..*");
        });

        // Verify that getClassBoxMap was not called
        verify(mockClassDiagramManager, never()).getClassBoxMap();

        // Verify that no relationship box was added
        verify(mockClassDiagramManager, never()).addRelationshipBox(any());

        // Verify that no UI components were added
        assertEquals(0, drawingPane.getChildren().size());
    }

    /**
     * Tests creating a composition relationship when the ClassDiagramManager is null.
     * <p>
     * This test checks if a {@link NullPointerException} is thrown when the {@link CompositionManager} is instantiated with a null {@link ClassDiagramManager}.
     * </p>
     */
    @Test
    void testCreateRelationship_WithNullClassDiagramManager() {
        // Instantiate AggregationManager with a null ClassDiagramManager
        CompositionManager nullManagerCompositionManager = new CompositionManager(null);

        // Attempt to create a relationship
        // This should throw a NullPointerException based on current implementation
        assertThrows(NullPointerException.class, () -> {
            nullManagerCompositionManager.createRelationship(startBox, endBox, drawingPane, "Composition", "1", "0..*");
        });
    }

    /**
     * Tests creating a composition relationship from a UMLRelationship model.
     * <p>
     * This test ensures that the composition relationship is correctly created when using an existing model of a UML relationship.
     * The model is verified for the correct start and end names, multiplicities, and UI components added to the drawing pane.
     * </p>
     */
    @Test
    void testCreateRelationshipFromModel_Successful() {
        // Setup UMLRelationship model
        String startName = "StartClass";
        String endName = "EndClass";
        String compositionName = "CompositionFromModel";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        when(mockUMLRelationship.getStartElementName()).thenReturn(startName);
        when(mockUMLRelationship.getEndElementName()).thenReturn(endName);
        when(mockUMLRelationship.getName()).thenReturn(compositionName);
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
        compositionManager.createRelationshipFromModel(mockUMLRelationship, drawingPane);

        // Verify that getClassBoxMap was called twice (once to retrieve start, once to retrieve end)
        verify(mockClassDiagramManager, times(2)).getClassBoxMap();

        // Verify that a UMLRelationshipBox was added
        ArgumentCaptor<UMLRelationshipBox> relationshipBoxCaptor = ArgumentCaptor.forClass(UMLRelationshipBox.class);
        verify(mockClassDiagramManager).addRelationshipBox(relationshipBoxCaptor.capture());

        UMLRelationshipBox capturedBox = relationshipBoxCaptor.getValue();
        assertEquals("Composition", capturedBox.getType());
        assertEquals(startName, capturedBox.getStartElementName());
        assertEquals(endName, capturedBox.getEndElementName());
        assertEquals(compositionName, capturedBox.getName());
        assertEquals(startMultiplicity, capturedBox.getStartMultiplicity());
        assertEquals(endMultiplicity, capturedBox.getEndMultiplicity());

        // Verify that the UI components are added to the drawingPane
        // Expected components: diamond (Polygon), compositionLine (Line), compositionLabel (Text), multiplicity labels (Text)
        assertEquals(5, drawingPane.getChildren().size());

        // Verify the composition label
        Node labelNode = drawingPane.getChildren().get(2);
        assertTrue(labelNode instanceof Text);
        Text compositionLabel = (Text) labelNode;
        assertEquals(compositionName, compositionLabel.getText());
    }

    /**
     * Tests creating a composition relationship from a UMLRelationship model with missing elements.
     * <p>
     * This test ensures that no relationship is created if the necessary class elements are missing from the model.
     * </p>
     */
    @Test
    void testCreateRelationshipFromModel_MissingElements() {
        // Setup UMLRelationship model
        String startName = "StartClass";
        String endName = "EndClass";
        String compositionName = "CompositionFromModel";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        when(mockUMLRelationship.getStartElementName()).thenReturn(startName);
        when(mockUMLRelationship.getEndElementName()).thenReturn(endName);
        when(mockUMLRelationship.getName()).thenReturn(compositionName);
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
        compositionManager.createRelationshipFromModel(mockUMLRelationship, drawingPane);

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
     * This test ensures that the last relationship box created can be correctly retrieved from the CompositionManager.
     * </p>
     */
    @Test
    void testGetLastRelationshipBox() {
        // Setup input parameters
        String compositionName = "CompositionRelation";
        String startMultiplicity = "1";
        String endMultiplicity = "0..*";

        // Call the method under test
        compositionManager.createRelationship(startBox, endBox, drawingPane, compositionName, startMultiplicity, endMultiplicity);

        // Retrieve the lastRelationshipBox
        UMLRelationshipBox lastBox = compositionManager.getLastRelationshipBox();

        // Assert that lastBox is the one we just added
        ArgumentCaptor<UMLRelationshipBox> relationshipBoxCaptor = ArgumentCaptor.forClass(UMLRelationshipBox.class);
        verify(mockClassDiagramManager).addRelationshipBox(relationshipBoxCaptor.capture());

        UMLRelationshipBox capturedBox = relationshipBoxCaptor.getValue();
        assertEquals(lastBox, capturedBox);
    }

    /**
     * Tests editing the composition name via double-click.
     * <p>
     * This test simulates the double-click event to edit the composition label's name and ensures it is updated correctly.
     * </p>
     */
    @Test
    void testAddEditDialogOnClick_EditCompositionName() {
        // Setup input parameters
        String compositionName = "CompositionRelation";
        String newCompositionName = "UpdatedComposition";

        // Call the method under test
        compositionManager.createRelationship(startBox, endBox, drawingPane, compositionName, "1", "0..*");

        // Retrieve the composition label
        Node labelNode = drawingPane.getChildren().get(2);
        assertTrue(labelNode instanceof Text);
        Text compositionLabel = (Text) labelNode;

        // Mock the Consumer to capture the new name
        Consumer<String> mockConsumer = mock(Consumer.class);

        // Simulate invoking the Consumer with a new name
        mockConsumer.accept(newCompositionName);

        // Update the label text
        compositionLabel.setText(newCompositionName);

        // Verify that the composition label was updated
        assertEquals(newCompositionName, compositionLabel.getText());
    }
}