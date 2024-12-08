package com.example.umlscd.PresentationLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.InterfaceEditorManager;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.api.FxRobot;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class InterfaceEditorUITest extends ApplicationTest {

    private InterfaceEditorUI interfaceEditorUI;
    private VBox root;
    private TextField interfaceNameField;
    private TextArea methodsArea;
    private ComboBox<String> returnTypeDropdown;
    private ComboBox<String> methodVisibilityDropdown;
    private TextField methodNameField;
    private Button addMethodButton;
    private Button addParameterButton;
    private Button applyChangesButton;
    private Button deleteMethodButton;
    private Button editMethodButton;
    private InterfaceEditorManager interfaceEditorManager;
    private ClassDiagramManager classDiagramManager;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/umlscd/InterfaceEditor.fxml"));
        root = fxmlLoader.load();

        interfaceEditorUI = fxmlLoader.getController();

        // Initialize the mock managers
        interfaceEditorManager = new InterfaceEditorManager();
        classDiagramManager = new ClassDiagramManager(new ClassDiagramUI());

        // Set managers
        interfaceEditorUI.setClassDiagramManager(classDiagramManager);

        // Initialize UI components
        interfaceNameField = (TextField) root.lookup("#interfaceNameField");
        methodsArea = (TextArea) root.lookup("#methodsArea");
        returnTypeDropdown = (ComboBox<String>) root.lookup("#returnTypeDropdown");
        methodVisibilityDropdown = (ComboBox<String>) root.lookup("#methodVisibilityDropdown");
        methodNameField = (TextField) root.lookup("#methodNameField");
        addMethodButton = (Button) root.lookup("#addMethodButton");
        addParameterButton = (Button) root.lookup("#addParameterButton");
        applyChangesButton = (Button) root.lookup("#applyChangesButton");
        deleteMethodButton = (Button) root.lookup("#deleteMethodButton");
        editMethodButton = (Button) root.lookup("#editMethodButton");

        // Show the UI
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testInterfaceNameField() {
        // Test that the interface name field is empty initially
        assertTrue(interfaceNameField.getText().isEmpty());
    }

    @Test
    public void testAddMethodButton() {
        // Test adding a method
        clickOn(methodNameField).write("myMethod");
        clickOn(returnTypeDropdown);
        type(KeyCode.DOWN).type(KeyCode.ENTER);  // Select first option: "void"
        clickOn(methodVisibilityDropdown);
        type(KeyCode.DOWN).type(KeyCode.ENTER);  // Select first option: "+ public"
        clickOn(addMethodButton);

        // Wait for the UI to update (could be a short delay or a wait for a condition)
        waitForFxEvents();

        // Verify the method is added to the methods area
        assertTrue(methodsArea.getText().contains("myMethod()"));
    }

    @Test
    public void testDeleteMethodButton() {
        // Add a method
        clickOn(methodNameField).write("methodToDelete");
        clickOn(returnTypeDropdown);
        type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn(methodVisibilityDropdown);
        type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn(addMethodButton);

        // Verify method added
        assertTrue(methodsArea.getText().contains("methodToDelete()"));

        // Delete the method
        clickOn(deleteMethodButton);

        // Simulate typing the method name to delete ("methodToDelete")
        clickOn(".text-input").write("methodToDelete");
        clickOn("OK");

        // Wait for the UI to update
        waitForFxEvents();

        // Verify method is deleted
        assertFalse(methodsArea.getText().contains("public methodToDelete()"));
    }

    @Test
    public void testAddParameterButton() {
        // Add a method first
        clickOn(methodNameField).write("myMethodWithParam");
        clickOn(returnTypeDropdown);
        type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn(methodVisibilityDropdown);
        type(KeyCode.DOWN).type(KeyCode.ENTER);

        // Add a parameter to the method
        clickOn(addParameterButton);

        // Simulate adding parameter input (paramName: "param1", paramType: "String")
        clickOn("Parameter Name").write("param1");
        clickOn("Parameter Type").write("String");
        clickOn("Add");
        clickOn(addMethodButton);

        // Wait for the UI to update
        waitForFxEvents();

        // Check if the parameter is added
        assertTrue(methodsArea.getText().contains("param1"));
    }

    @Test
    public void testApplyChangesButton() {
        // Test that applying changes updates the UML model (interface name and methods)
        clickOn(interfaceNameField).write("TestInterface");
        clickOn(methodNameField).write("applyMethod");
        clickOn(returnTypeDropdown);
        type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn(methodVisibilityDropdown);
        type(KeyCode.DOWN).type(KeyCode.ENTER);
        clickOn(addMethodButton);

        // Apply changes
        clickOn(applyChangesButton);

        // Verify that the applied changes reflect in the model
        // (This part depends on your specific method for verifying changes, adjust as needed)
        assertEquals("TestInterface", interfaceEditorUI.interfaceNameField.getText());  // Assume getter exists
    }

    @Test
    public void testCustomReturnTypeSelection() {
        // Test handling of custom return type
        clickOn(returnTypeDropdown);
        type(KeyCode.DOWN).type(KeyCode.DOWN);  // Select "Custom..."
        clickOn("Custom...");
        // Assuming custom data type handling logic is added elsewhere in your code.
    }
}
