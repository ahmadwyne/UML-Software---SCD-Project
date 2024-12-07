package com.example.umlscd.BuisnessLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.AssociationManager;
import com.example.umlscd.Models.ClassDiagram.UMLElementBoxInterface;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.example.umlscd.Models.ClassDiagram.UMLRelationshipBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Map;

import static org.mockito.Mockito.*;

class AssociationManagerTest {

    @Mock
    private ClassDiagramManager mockClassDiagramManager;

    @Mock
    private Pane mockDrawingPane;

    @Mock
    private VBox mockStartBox;

    @Mock
    private VBox mockEndBox;

    @Mock
    private UMLElementBoxInterface mockStartClass;

    @Mock
    private UMLElementBoxInterface mockEndClass;

    @Mock
    private DoubleProperty mockLayoutXProperty;

    @Mock
    private DoubleProperty mockLayoutYProperty;

    private AssociationManager associationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking ClassDiagramManager and its behavior
        when(mockClassDiagramManager.getClassBoxMap()).thenReturn(FXCollections.observableHashMap());

        // Creating the AssociationManager instance
        associationManager = new AssociationManager(mockClassDiagramManager);

        // Setting up mock behaviors for layout properties
        when(mockStartBox.layoutXProperty()).thenReturn(mockLayoutXProperty);
        when(mockEndBox.layoutXProperty()).thenReturn(mockLayoutXProperty);
        when(mockStartBox.layoutYProperty()).thenReturn(mockLayoutYProperty);
        when(mockEndBox.layoutYProperty()).thenReturn(mockLayoutYProperty);

        // Mocking the addition of elements to the Pane (drawing pane)
        ObservableList<Node> mockChildren = FXCollections.observableArrayList();
        when(mockDrawingPane.getChildren()).thenReturn(mockChildren);
    }

    @Test
    void testCreateRelationship_successful() {
        // Arrange
        when(mockStartBox.getLayoutX()).thenReturn(100.0);
        when(mockStartBox.getLayoutY()).thenReturn(200.0);
        when(mockEndBox.getLayoutX()).thenReturn(300.0);
        when(mockEndBox.getLayoutY()).thenReturn(400.0);

        // Act
        associationManager.createRelationship(mockStartBox, mockEndBox, mockDrawingPane, "Association", "1", "0..*");

        // Assert: Verifying the drawing pane interactions
        verify(mockDrawingPane, times(1)).getChildren();
        verify(mockDrawingPane.getChildren(), times(1)).add(any(Line.class));
        verify(mockDrawingPane.getChildren(), times(2)).add(any(Text.class)); // 1 for association, 1 for multiplicity
        verify(mockStartBox.layoutXProperty(), times(1)).addListener((ChangeListener<? super Number>) any());
        verify(mockStartBox.layoutYProperty(), times(1)).addListener((ChangeListener<? super Number>) any());
    }

    @Test
    void testCreateRelationship_invalidInput() {
        // Arrange: Provide null or empty values for relationship name and multiplicity
        when(mockStartBox.getLayoutX()).thenReturn(100.0);
        when(mockStartBox.getLayoutY()).thenReturn(200.0);
        when(mockEndBox.getLayoutX()).thenReturn(300.0);
        when(mockEndBox.getLayoutY()).thenReturn(400.0);

        // Act: Call createRelationship with empty associationName and multiplicities
        associationManager.createRelationship(mockStartBox, mockEndBox, mockDrawingPane, "", "", "");

        // Assert: Verify that the default values are used (associationName = "Association", multiplicity = "1")
        verify(mockDrawingPane.getChildren(), times(1)).add(any(Line.class));
        verify(mockDrawingPane.getChildren(), times(1)).add(any(Text.class)); // Association label
        verify(mockDrawingPane.getChildren(), times(2)).add(any(Text.class)); // Multiplicity labels
    }

    /*@Test
    void testAddEditDialog_onAssociationLabelClick() {
        // Arrange
        Text associationLabel = new Text("Association");

        // Mock the label click behavior
        associationManager.addEditDialogOnClick(associationLabel, "Edit Association Name", newName -> {
            associationLabel.setText(newName);
        });

        // Act: Simulate a double-click event on the label
        MouseEvent mouseEvent = new MouseEvent(
                MouseEvent.MOUSE_CLICKED,    // Event Type
                0, 0,                        // X and Y position
                0, 0,                        // Screen coordinates
                2,                            // Click count (double-click)
                MouseButton.PRIMARY,         // Mouse button (left button)
                1,                            // Click count (1 = single click)
                true, true, true, true,       // Various flags (Shift, Control, etc.)
                false, false,                // Modifiers (alt, etc.)
                null                          // Source (optional)
        );

        // Simulate the double-click event on the association label
        associationLabel.fireEvent(mouseEvent);

        // Assert: Verify the dialog opens and the name is updated
        TextInputDialog dialog = new TextInputDialog("Association");
        dialog.setTitle("Edit Association Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new value:");
        dialog.showAndWait().ifPresent(newValue -> associationLabel.setText(newValue));

        // Verify the label's text is updated
        assert associationLabel.getText().equals("New Association Name");
    }*/


    @Test
    void testCreateRelationshipFromModel_successful() {
        // Arrange: Mock UMLRelationship
        UMLRelationship mockRelationship = mock(UMLRelationship.class);
        when(mockRelationship.getStartElementName()).thenReturn("Class1");
        when(mockRelationship.getEndElementName()).thenReturn("Class2");
        when(mockRelationship.getName()).thenReturn("Association");
        when(mockRelationship.getStartMultiplicity()).thenReturn("1");
        when(mockRelationship.getEndMultiplicity()).thenReturn("0..*");

        UMLElementBoxInterface mockStartClass = mock(UMLElementBoxInterface.class);
        UMLElementBoxInterface mockEndClass = mock(UMLElementBoxInterface.class);
        VBox mockStartBox = mock(VBox.class);
        VBox mockEndBox = mock(VBox.class);

        // Simulate that the class diagram manager has the required elements
        when(mockClassDiagramManager.getClassBoxMap()).thenReturn(FXCollections.observableMap(Map.of("Class1", mockStartClass, "Class2", mockEndClass)));
        when(mockStartClass.getVisualRepresentation()).thenReturn(mockStartBox);
        when(mockEndClass.getVisualRepresentation()).thenReturn(mockEndBox);

        // Act: Create the relationship using data from UMLRelationship
        associationManager.createRelationshipFromModel(mockRelationship, mockDrawingPane);

        // Assert: Verify elements were added to the drawing pane
        verify(mockDrawingPane.getChildren(), times(1)).add(any(Line.class));
        verify(mockDrawingPane.getChildren(), times(3)).add(any(Text.class)); // 1 for label, 2 for multiplicity
    }

    @Test
    void testDynamicUpdateListener_updatesPosition() {
        // Arrange
        Line line = mock(Line.class);
        Text associationLabel = mock(Text.class);
        Text startMultiplicityText = mock(Text.class);
        Text endMultiplicityText = mock(Text.class);

        // Create mock boxes
        VBox startBox = mock(VBox.class);
        VBox endBox = mock(VBox.class);

        when(startBox.layoutXProperty()).thenReturn(mockLayoutXProperty);
        when(endBox.layoutXProperty()).thenReturn(mockLayoutXProperty);

        // Act: Add listeners for dynamic updates
        associationManager.addDynamicUpdateListener(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText);

        // Simulate movement of boxes
        mockLayoutXProperty.set(150.0);
        mockLayoutYProperty.set(250.0);

        // Assert: Verify that the update listener updated the line position
        verify(line, times(1)).setStartX(anyDouble());
        verify(line, times(1)).setStartY(anyDouble());
        verify(line, times(1)).setEndX(anyDouble());
        verify(line, times(1)).setEndY(anyDouble());
    }
}
