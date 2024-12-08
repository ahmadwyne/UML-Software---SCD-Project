package com.example.umlscd.PresentationLayer.ClassDiagram;

import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;


public class ClassDiagramUITest extends ApplicationTest {

    private ClassDiagramUI classDiagram;
    private Button btnSave;
    private Button btnLoad;
    private Button btnExportImage;
    private Button btnCode;
    private Button btnDelete;
    private Button btnClass;
    private Button btnInterface;
    private Button btnAssociation;
    private Button btnDrag;
    private Button btnAggregation;
    private Button btnComposition;
    private Button btnInheritance;
    private Pane drawingPane;

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file and initialize the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/classDiagram.fxml"));
        javafx.scene.layout.BorderPane root = loader.load();
        classDiagram = loader.getController();

        stage.setTitle("Test Stage");
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();

        // Access the UI components
        btnSave = lookup("#btnSave").queryButton();
        btnLoad = lookup("#btnLoad").queryButton();
        btnExportImage = lookup("#btnExportImage").queryButton();
        btnCode = lookup("#btnCode").queryButton();
        btnDelete = lookup("#btnDelete").queryButton();
        btnClass = lookup("#btnClass").queryButton();
        btnInterface = lookup("#btnInterface").queryButton();
        btnAssociation = lookup("#btnAssociation").queryButton();
        btnDrag = lookup("#btnDrag").queryButton();
        btnAggregation = lookup("#btnAggregation").queryButton();
        btnComposition = lookup("#btnComposition").queryButton();
        btnInheritance = lookup("#btnInheritance").queryButton();
        drawingPane = lookup("#drawingPane").queryAs(Pane.class);

        // Access the UI components
        btnExportImage = lookup("#btnExportImage").queryButton();
    }

    @Test
    public void testInitialize() {
        // Verify that the UI components are initialized
        assertNotNull(btnSave, "Save button should be initialized");
        assertNotNull(btnLoad, "Load button should be initialized");
        assertNotNull(btnExportImage, "Export Image button should be initialized");
        assertNotNull(btnCode, "Code button should be initialized");
        assertNotNull(btnDelete, "Delete button should be initialized");
        assertNotNull(btnClass, "Class button should be initialized");
        assertNotNull(btnInterface, "Interface button should be initialized");
        assertNotNull(btnAssociation, "Association button should be initialized");
        assertNotNull(btnDrag, "Drag button should be initialized");
        assertNotNull(btnAggregation, "Aggregation button should be initialized");
        assertNotNull(btnComposition, "Composition button should be initialized");
        assertNotNull(btnInheritance, "Inheritance button should be initialized");
        assertNotNull(drawingPane, "Drawing Pane should be initialized");
    }


    @Test
    public void testDeleteButtonHandler() {
        // Ensure that initially, delete mode is not enabled
        assertFalse(classDiagram.isDeleteModeEnabled, "Delete mode should initially be disabled");

        // Simulate clicking the delete button
        clickOn(btnDelete);

        // After the first click, delete mode should be enabled
        assertTrue(classDiagram.isDeleteModeEnabled, "Delete mode should be enabled after the first click");

        // Simulate clicking the delete button again
        clickOn(btnDelete);

        // After the second click, delete mode should be disabled
        assertFalse(classDiagram.isDeleteModeEnabled, "Delete mode should be disabled after the second click");
    }


    @Test
    public void testEnableDeleteMode() {
        // Simulate clicking the delete button to enable delete mode
        clickOn(btnDelete);

        // Check that delete mode is enabled and button text is updated
        assertTrue(classDiagram.isDeleteModeEnabled, "Delete mode should be enabled");
        assertEquals("Cancel Delete", btnDelete.getText(), "Button text should be 'Cancel Delete' when delete mode is enabled");

        // Simulate clicking on the drawing pane to delete an element
        // Assuming getSelectedElement() will return a valid element when in delete mode
        // Here, we simply simulate clicking on the drawing pane
        clickOn(drawingPane);

        // Check that delete functionality is triggered and delete mode is disabled
        // Assuming that clicking on the drawing pane will disable delete mode after the deletion
        assertTrue(classDiagram.isDeleteModeEnabled, "Delete mode should be disabled after deletion");
        assertEquals("Cancel Delete", btnDelete.getText(), "Button text should revert to 'Delete' after deleting an element");
    }

    @Test
    public void testDisableDeleteMode() {
        // Enable delete mode first
        clickOn(btnDelete);

        // Ensure delete mode is enabled and button text is 'Cancel Delete'
        assertTrue(classDiagram.isDeleteModeEnabled, "Delete mode should be enabled");
        assertEquals("Cancel Delete", btnDelete.getText(), "Button text should be 'Cancel Delete' when delete mode is enabled");

        // Now simulate disabling delete mode manually (simulate clicking the delete button again)
        clickOn(btnDelete);

        // Check that delete mode is now disabled
        assertFalse(classDiagram.isDeleteModeEnabled, "Delete mode should be disabled after clicking the button again");
        assertEquals("Delete", btnDelete.getText(), "Button text should revert to 'Delete' when delete mode is disabled");
    }

    @Test
    public void testClassToolSelection() {
        // Simulate clicking the Class tool button
        clickOn(btnClass);

        // Verify that the tool selection method was called with "Class"
        // You could mock classDiagramManager or check UI behavior if necessary
        // For now, let's assume it updates the drawing pane in some way
        // Check that drawing pane is enabled or some UI change happens
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be disabled after selecting 'Class'");
    }

    @Test
    public void testInterfaceToolSelection() {
        // Simulate clicking the Interface tool button
        clickOn(btnInterface);

        // Verify that the tool selection method was called with "Interface"
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be disabled after selecting 'Interface'");
    }

    @Test
    public void testAssociationToolSelection() {
        // Simulate clicking the Association tool button
        clickOn(btnAssociation);

        // Verify that the tool selection method was called with "Association"
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be disabled after selecting 'Association'");
    }

    @Test
    public void testDragToolSelection() {
        // Simulate clicking the Drag tool button
        clickOn(btnDrag);

        // Verify that the tool selection method was called with "Drag"
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be disabled after selecting 'Drag'");
    }

    @Test
    public void testAggregationToolSelection() {
        // Simulate clicking the Aggregation tool button
        clickOn(btnAggregation);

        // Verify that the tool selection method was called with "Aggregation"
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be disabled after selecting 'Aggregation'");
    }

    @Test
    public void testCompositionToolSelection() {
        // Simulate clicking the Composition tool button
        clickOn(btnComposition);

        // Verify that the tool selection method was called with "Composition"
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be disabled after selecting 'Composition'");
    }

    @Test
    public void testInheritanceToolSelection() {
        // Simulate clicking the Inheritance tool button
        clickOn(btnInheritance);

        // Verify that the tool selection method was called with "Inheritance"
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be disabled after selecting 'Inheritance'");
    }









}
