package com.example.umlscd.PresentationLayer.UseCaseDiagram;

import com.example.umlscd.PresentationLayer.UseCaseDiagram.UseCaseDiagram;
import com.example.umlscd.Models.UseCaseDiagram.Association;
import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UseCaseDiagramTest {

    private UseCaseDiagram useCaseDiagram;
    private TextField txtActorName;
    private TextField txtUseCaseName;
    private ToggleButton btnAssociation;
    private MouseEvent mouseEvent;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize UI components in Platform.runLater
        Platform.runLater(() -> {
            useCaseDiagram = new UseCaseDiagram();
            txtActorName = new TextField();
            txtUseCaseName = new TextField();
            btnAssociation = new ToggleButton();
            mouseEvent = mock(MouseEvent.class);
            useCaseDiagram.txtActorName = txtActorName;
            useCaseDiagram.txtUseCaseName = txtUseCaseName;
            useCaseDiagram.btnAssociation = btnAssociation;

            // Initialize the canvas, objects, etc.
            useCaseDiagram.objects = new ArrayList<>();
            useCaseDiagram.associations = new ArrayList<>();
        });
    }

    // Test Case 1: Initialize Diagram
    @Test
    void testInitialize() {
        useCaseDiagram.initialize();  // Now we can directly call it
        assertNotNull(useCaseDiagram);
    }

    // Test Case 2: Add Actor
    @Test
    void testAddActor() {
        String actorName = "Test Actor";
        txtActorName.setText(actorName);  // Set text to simulate user input

        useCaseDiagram.addActor();  // Simulate adding an actor

        assertTrue(useCaseDiagram.objects.stream().anyMatch(obj -> obj.getName().equals(actorName)));  // Assert actor added
    }

    // Test Case 3: Add Use Case
    @Test
    void testAddUseCase() {
        String useCaseName = "Test UseCase";
        txtUseCaseName.setText(useCaseName);  // Set text to simulate user input

        useCaseDiagram.addUseCase();  // Simulate adding a use case

        assertTrue(useCaseDiagram.objects.stream().anyMatch(obj -> obj.getName().equals(useCaseName)));  // Assert use case added
    }

    // Test Case 4: Toggle Association Button
    @Test
    void testToggleAssociationButton() {
        btnAssociation.setSelected(true);  // Simulate button press

        useCaseDiagram.toggleButtonColor(btnAssociation);  // Simulate action

        assertTrue(btnAssociation.isSelected());  // Assert button is toggled
    }

    // Test Case 5: Add Association Between Two Objects
    @Test
    void testAddAssociation() {
        UseCaseDiagramObject object1 = new UseCaseDiagramObject("actor", 100, 100, "Test Actor 1");
        UseCaseDiagramObject object2 = new UseCaseDiagramObject("usecase", 200, 200, "Test UseCase 1");

        useCaseDiagram.drawAssociation(object1, object2, "association");

        assertTrue(useCaseDiagram.associations.size() > 0);  // Assert association is added
    }

    // Test Case 6: Edit Object
    @Test
    void testEditObject() {
        UseCaseDiagramObject object = new UseCaseDiagramObject("actor", 100, 100, "Test Actor 1");
        useCaseDiagram.objects.add(object);

        String newName = "New Actor Name";
        txtActorName.setText(newName);  // Simulate new name input

        useCaseDiagram.onNameChange();  // Simulate name change

        assertEquals(newName, object.getName());  // Assert name is updated
    }

    // Test Case 7: Drag Object
    @Test
    void testDragObject() {
        UseCaseDiagramObject object = new UseCaseDiagramObject("actor", 100, 100, "Test Actor 1");
        useCaseDiagram.objects.add(object);

        useCaseDiagram.onMousePressed(mouseEvent);  // Simulate mouse press
        useCaseDiagram.onMouseDragged(mouseEvent);  // Simulate dragging
        useCaseDiagram.onMouseReleased(mouseEvent);  // Simulate mouse release

        assertNull(useCaseDiagram.objectBeingDragged);  // Object should be null after drag
    }

    // Test Case 8: Delete Object
    @Test
    void testDeleteObject() {
        UseCaseDiagramObject object = new UseCaseDiagramObject("actor", 100, 100, "Test Actor");
        useCaseDiagram.objects.add(object);

        useCaseDiagram.removeObjectFromDiagram(object);

        assertFalse(useCaseDiagram.objects.contains(object));  // Assert object is deleted
    }

    // Test Case 9: Save Diagram
    @Test
    void testSaveDiagram() {
        useCaseDiagram.saveDiagram();
        verify(useCaseDiagram).saveDiagram();  // Ensure save is called
    }

    // Test Case 10: Export Diagram to Image
    @Test
    void testExportDiagramToImage() {
        useCaseDiagram.exportDiagramToImage();
        verify(useCaseDiagram).exportDiagramToImage();  // Ensure export is called
    }

    // Test Case 11: Load Diagram from JSON
    @Test
    void testLoadDiagramFromJson() {
        useCaseDiagram.loadDiagramFromJson();
        verify(useCaseDiagram).loadDiagramFromJson();  // Ensure load from JSON is called
    }

    // Test Case 12: Verify Object Selection in Explorer
    @Test
    void testVerifyObjectSelectionInExplorer() {
        String objectName = "Actor1";
        useCaseDiagram.selectObjectFromExplorer(objectName);

        assertNotNull(useCaseDiagram.selectedObject1);  // Assert object selection is not null
    }
}
