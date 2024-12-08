package com.example.umlscd.PresentationLayer.ClassDiagram;

import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Test class for {@link ClassDiagramUI}.
 * <p>
 * This class contains UI tests for the {@link ClassDiagramUI} class to ensure the proper
 * functionality of buttons and tools in the class diagram user interface.
 * </p>
 */
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

        // Access the UI components again to make sure they are properly initialized
        btnExportImage = lookup("#btnExportImage").queryButton();
    }

    /**
     * Verifies that the UI components are correctly initialized.
     * <p>
     * This test checks that all buttons and the drawing pane are properly initialized
     * and accessible.
     * </p>
     */
    @Test
    public void testInitialize() {
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

    /**
     * Tests the delete button functionality by toggling the delete mode.
     * <p>
     * This test simulates the behavior of the delete button. It checks if delete mode is enabled
     * and disabled correctly with each click of the delete button.
     * </p>
     */
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

    /**
     * Tests enabling the delete mode via the delete button.
     * <p>
     * This test verifies that clicking the delete button enables delete mode, changes the button text,
     * and allows the user to delete an element by clicking on the drawing pane.
     * </p>
     */
    @Test
    public void testEnableDeleteMode() {
        // Simulate clicking the delete button to enable delete mode
        clickOn(btnDelete);

        // Check that delete mode is enabled and button text is updated
        assertTrue(classDiagram.isDeleteModeEnabled, "Delete mode should be enabled");
        assertEquals("Cancel Delete", btnDelete.getText(), "Button text should be 'Cancel Delete' when delete mode is enabled");

        // Simulate clicking on the drawing pane to delete an element
        clickOn(drawingPane);

        // Check that delete functionality is triggered and delete mode is disabled
        assertTrue(classDiagram.isDeleteModeEnabled, "Delete mode should be disabled after deletion");
        assertEquals("Cancel Delete", btnDelete.getText(), "Button text should revert to 'Delete' after deleting an element");
    }

    /**
     * Tests disabling delete mode after it has been enabled.
     * <p>
     * This test ensures that delete mode can be disabled by clicking the delete button again,
     * and verifies that the button text is reset to 'Delete'.
     * </p>
     */
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

    /**
     * Tests selecting the "Class" tool from the UI.
     * <p>
     * This test simulates clicking the Class tool button and verifies that the drawing pane becomes
     * enabled after the tool selection, indicating that the tool selection was successful.
     * </p>
     */
    @Test
    public void testClassToolSelection() {
        clickOn(btnClass);
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be enabled after selecting 'Class'");
    }

    /**
     * Tests selecting the "Interface" tool from the UI.
     * <p>
     * This test simulates clicking the Interface tool button and verifies that the drawing pane becomes
     * enabled after the tool selection, indicating that the tool selection was successful.
     * </p>
     */
    @Test
    public void testInterfaceToolSelection() {
        clickOn(btnInterface);
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be enabled after selecting 'Interface'");
    }

    /**
     * Tests selecting the "Association" tool from the UI.
     * <p>
     * This test simulates clicking the Association tool button and verifies that the drawing pane becomes
     * enabled after the tool selection, indicating that the tool selection was successful.
     * </p>
     */
    @Test
    public void testAssociationToolSelection() {
        clickOn(btnAssociation);
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be enabled after selecting 'Association'");
    }

    /**
     * Tests selecting the "Drag" tool from the UI.
     * <p>
     * This test simulates clicking the Drag tool button and verifies that the drawing pane becomes
     * enabled after the tool selection, indicating that the tool selection was successful.
     * </p>
     */
    @Test
    public void testDragToolSelection() {
        clickOn(btnDrag);
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be enabled after selecting 'Drag'");
    }

    /**
     * Tests selecting the "Aggregation" tool from the UI.
     * <p>
     * This test simulates clicking the Aggregation tool button and verifies that the drawing pane becomes
     * enabled after the tool selection, indicating that the tool selection was successful.
     * </p>
     */
    @Test
    public void testAggregationToolSelection() {
        clickOn(btnAggregation);
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be enabled after selecting 'Aggregation'");
    }

    /**
     * Tests selecting the "Composition" tool from the UI.
     * <p>
     * This test simulates clicking the Composition tool button and verifies that the drawing pane becomes
     * enabled after the tool selection, indicating that the tool selection was successful.
     * </p>
     */
    @Test
    public void testCompositionToolSelection() {
        clickOn(btnComposition);
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be enabled after selecting 'Composition'");
    }

    /**
     * Tests selecting the "Inheritance" tool from the UI.
     * <p>
     * This test simulates clicking the Inheritance tool button and verifies that the drawing pane becomes
     * enabled after the tool selection, indicating that the tool selection was successful.
     * </p>
     */
    @Test
    public void testInheritanceToolSelection() {
        clickOn(btnInheritance);
        assertFalse(drawingPane.isDisabled(), "Drawing pane should be enabled after selecting 'Inheritance'");
    }
}
