package com.example.umlscd.BusinessLayer.ClassDiagram;

import com.example.umlscd.Models.ClassDiagram.UMLElementBoxInterface;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.example.umlscd.Models.ClassDiagram.UMLRelationshipBox;
import com.example.umlscd.PresentationLayer.ClassDiagram.ClassDiagramUI;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link InheritanceManager} class.
 * This class contains tests for creating inheritance relationships, handling null class boxes,
 * and managing UML relationship models in a class diagram.
 * <p>
 * The tests ensure that the {@link InheritanceManager} behaves as expected in scenarios such as successful relationship creation,
 * dealing with null elements, and working with a UML model of a relationship.
 * </p>
 */
class InheritanceManagerTest {

    private InheritanceManager inheritanceManager;
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

        // Instantiate AggregationManager with the mocked ClassDiagramManager
        inheritanceManager = new InheritanceManager(mockClassDiagramManager);
    }

    /**
     * Tests the creation of an inheritance relationship.
     * <p>
     * This test ensures that an inheritance relationship is correctly created between two class boxes.
     * The test verifies the correct type of the relationship ("Inheritance") and checks the number of components added to the drawing pane.
     * </p>
     */
    @Test
    void testCreateRelationship() {
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

        // Call the method to test
        inheritanceManager.createRelationship(startBoxMock, endBoxMock, drawingPane, "", "", "");

        // Assert that Inheritance is Created
        assertEquals("Inheritance", inheritanceManager.getLastRelationshipBox().getType());
        assertEquals(2, drawingPane.getChildren().size());
    }

    /**
     * Tests creating an inheritance relationship when one of the class boxes is null.
     * <p>
     * This test ensures that a {@link NullPointerException} is thrown when attempting to create a relationship with a null class box.
     * </p>
     */
    @Test
    void testCreateRelationship_WithNullClassBox() {
        // Attempt to create a relationship with a null startBox
        assertThrows(NullPointerException.class, () -> {
            inheritanceManager.createRelationship(null, endBox, drawingPane, "", "", "");
        });

        // Verify that getClassBoxMap was not called
        verify(mockClassDiagramManager, never()).getClassBoxMap();

        // Verify that no relationship box was added
        verify(mockClassDiagramManager, never()).addRelationshipBox(any());

        // Verify that no UI components were added
        assertEquals(0, drawingPane.getChildren().size());
    }

    /**
     * Tests creating an inheritance relationship when the ClassDiagramManager is null.
     * <p>
     * This test ensures that a {@link NullPointerException} is thrown when the {@link InheritanceManager} is instantiated with a null {@link ClassDiagramManager}.
     * </p>
     */
    @Test
    void testCreateRelationship_WithNullClassDiagramManager() {
        // Instantiate AggregationManager with a null ClassDiagramManager
        InheritanceManager nullManagerCompositionManager = new InheritanceManager(null);

        // Attempt to create a relationship
        // This should throw a NullPointerException based on current implementation
        assertThrows(NullPointerException.class, () -> {
            nullManagerCompositionManager.createRelationship(startBox, endBox, drawingPane, "", "", "");
        });
    }

    /**
     * Tests creating an inheritance relationship from a UMLRelationship model.
     * <p>
     * This test ensures that an inheritance relationship is successfully created from a given UML model.
     * The test verifies that the correct start and end class names are retrieved, and the UI components are correctly added to the drawing pane.
     * </p>
     */
    @Test
    void testCreateRelationshipFromModel_Successful() {
        // Setup UMLRelationship model
        String startName = "StartClass";
        String endName = "EndClass";
        String inheritanceName = "";
        String startMultiplicity = "";
        String endMultiplicity = "";

        when(mockUMLRelationship.getStartElementName()).thenReturn(startName);
        when(mockUMLRelationship.getEndElementName()).thenReturn(endName);
        when(mockUMLRelationship.getName()).thenReturn(inheritanceName);
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
        inheritanceManager.createRelationshipFromModel(mockUMLRelationship, drawingPane);

        // Verify that getClassBoxMap was called twice (once to retrieve start, once to retrieve end)
        verify(mockClassDiagramManager, times(2)).getClassBoxMap();

        // Verify that a UMLRelationshipBox was added
        ArgumentCaptor<UMLRelationshipBox> relationshipBoxCaptor = ArgumentCaptor.forClass(UMLRelationshipBox.class);
        verify(mockClassDiagramManager).addRelationshipBox(relationshipBoxCaptor.capture());

        UMLRelationshipBox capturedBox = relationshipBoxCaptor.getValue();
        assertEquals("Inheritance", capturedBox.getType());
        assertEquals(startName, capturedBox.getStartElementName());
        assertEquals(endName, capturedBox.getEndElementName());

        // Verify that the UI components are added to the drawingPane
        // Expected components: diamond (Polygon), inheritanceLine (Line)
        assertEquals(3, drawingPane.getChildren().size());
    }

    /**
     * Tests retrieving the last created UMLRelationshipBox.
     * <p>
     * This test ensures that the last relationship box created is correctly retrieved from the InheritanceManager.
     * </p>
     */
    @Test
    void testGetLastRelationshipBox() {
        // Setup input parameters
        String compositionName = "";
        String startMultiplicity = "";
        String endMultiplicity = "";

        // Call the method under test
        inheritanceManager.createRelationship(startBox, endBox, drawingPane, compositionName, startMultiplicity, endMultiplicity);

        // Retrieve the lastRelationshipBox
        UMLRelationshipBox lastBox = inheritanceManager.getLastRelationshipBox();

        // Assert that lastBox is the one we just added
        ArgumentCaptor<UMLRelationshipBox> relationshipBoxCaptor = ArgumentCaptor.forClass(UMLRelationshipBox.class);
        verify(mockClassDiagramManager).addRelationshipBox(relationshipBoxCaptor.capture());

        UMLRelationshipBox capturedBox = relationshipBoxCaptor.getValue();
        assertEquals(lastBox, capturedBox);
    }
}
