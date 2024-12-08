package com.example.umlscd.PresentationLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassEditorManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;

public class ClassEditorUITest extends ApplicationTest {

    private ClassEditorUI classEditorUI;
    private VBox root;
    private TextField classNameField;
    private TextArea attributesArea;
    private TextField attributeNameField;
    private ComboBox<String> dataTypeDropdown;
    private ComboBox<String> visibilityDropdown;
    private Button addAttributeButton;
    private Button addMethodButton;
    private Button addParameterButton;
    private Button applyChangesButton;
    private List<String> attributeList;
    private List<String> methodList;
    private List<String> parameterList;
    private ClassEditorUI controller;
    private ClassEditorManager classEditorManager;  // This is the manager that applies changes
    private Stage stage;

    @Override
    public void start(Stage stage) {
        // Mock classEditorManager
        classEditorManager = mock(ClassEditorManager.class);

        // Initialize UI elements
        attributeNameField = new TextField();
        dataTypeDropdown = new ComboBox<>();
        visibilityDropdown = new ComboBox<>();
        attributesArea = new TextArea();
        addAttributeButton = new Button("Add Attribute");
        addMethodButton = new Button("Add Method");
        addParameterButton = new Button("Add Parameter");
        applyChangesButton = new Button("Apply Changes");

        // Initialize Lists
        attributeList = new ArrayList<>();
        methodList = new ArrayList<>();
        parameterList = new ArrayList<>();

        // Load FXML and set up the scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/classEditor.fxml"));
            root = loader.load();
            controller = loader.getController();

            // Inject the mocked classEditorManager into the controller
            controller.classEditorManager = classEditorManager;

            // Set the scene for the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            this.stage = stage;
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setUp() {
        // Initialize UI components on the JavaFX thread
        Platform.runLater(() -> {
            // Set the initial values for UI components
            controller.attributeNameField.setText("Attribute1");
            controller.dataTypeDropdown.getItems().addAll("int", "String");
            controller.dataTypeDropdown.setValue("int");
            controller.visibilityDropdown.getItems().addAll("public", "private", "protected");
            controller.visibilityDropdown.setValue("public");

            // Ensure the UI is shown before performing tests
            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    @Test
    public void testAddAttribute() {
        // Ensure UI is ready before interacting
        Platform.runLater(() -> {
            // Simulate clicking the addAttributeButton
            clickOn(controller.addAttributeButton);
        });

        // Wait for async updates to complete
        WaitForAsyncUtils.waitForFxEvents();

        // Debugging log to check the content of the attributes area
        System.out.println("Attributes Area Text: " + controller.attributesArea.getText());

        // Assert that the attributesArea contains the expected attribute
        String actualText = controller.attributesArea.getText();
        assertTrue(actualText.contains("pAttribute1 : int"),
                "Expected 'pAttribute1 : int' to be in the attributes area but got: " + actualText);
    }

    @Test
    public void testAddMethod() {
        // Simulate adding a method
        Platform.runLater(() -> {
            controller.methodNameField.setText("Method1");
            controller.returnTypeDropdown.getItems().addAll("int", "String");
            controller.returnTypeDropdown.setValue("int");
            controller.visibilityDropdown.getItems().addAll("public", "private", "protected");
            controller.visibilityDropdown.setValue("public");
            clickOn(controller.addMethodButton);  // Corrected to click on the button in the controller
        });

        // Wait for async updates
        WaitForAsyncUtils.waitForFxEvents();

        // Assert that the method is added correctly
        String actualText = controller.methodsArea.getText();
        assertTrue(actualText.contains("Method1"),
                "Expected 'Method1' to be in the methods area but got: " + actualText);
    }

    @Test
    public void testApplyChanges() {
        // Simulate user input
        Platform.runLater(() -> {
            // Setting some values in the input fields and areas
            controller.classNameField.setText("MyClass");
            controller.attributesArea.setText("pAttribute1 : int");
            controller.methodsArea.setText("method1()");

            // Triggering the applyChanges method
            clickOn(controller.applyChangesButton);
        });

        // Wait for async updates
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the interaction with classEditorManager
        verify(classEditorManager).applyChanges("MyClass", "pAttribute1 : int", "method1()");

        // You can also check whether the UI reflects the changes after applying them.
        assertEquals("MyClass", controller.classNameField.getText());
        assertEquals("pAttribute1 : int", controller.attributesArea.getText());
        assertEquals("method1()", controller.methodsArea.getText());
    }

    @Test
    public void testDeleteAttribute() {
        // Simulate user input for deleting an attribute
        Platform.runLater(() -> {
            clickOn(controller.addAttributeButton);
            // Trigger the deleteAttribute method (usually this would be triggered by a button click)
            clickOn(controller.deleteAttributeButton);  // Assuming there's a button to delete attributes
        });

        // Wait for async updates to complete
        WaitForAsyncUtils.waitForFxEvents();

        // Mock user input for the attribute name to be deleted
        String attributeNameToDelete = "pAttribute1";
        clickOn("Attribute Name");
        write(attributeNameToDelete);
        clickOn("OK");

        // Wait for async updates
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the attribute was deleted
        String actualText = controller.attributesArea.getText();
        assertFalse(actualText.contains(attributeNameToDelete),
                "Expected attribute '" + attributeNameToDelete + "' to be deleted but it was still found in the attributes area: " + actualText);
    }

    @Test
    public void testDeleteMethod() {
        // Simulate user input for deleting a method
        Platform.runLater(() -> {
            controller.methodNameField.setText("Method1");
            controller.returnTypeDropdown.getItems().addAll("int", "String");
            controller.returnTypeDropdown.setValue("int");
            controller.visibilityDropdown.getItems().addAll("public", "private", "protected");
            controller.visibilityDropdown.setValue("public");
            clickOn(controller.addMethodButton);
            // Trigger the deleteMethod method (usually this would be triggered by a button click)
            clickOn(controller.deleteMethodButton);  // Assuming there's a button to delete methods
        });

        // Wait for async updates to complete
        WaitForAsyncUtils.waitForFxEvents();

        // Mock user input for the method name to be deleted
        String methodNameToDelete = "method1()";
        clickOn("Method Name");
        write(methodNameToDelete);

        clickOn("OK");

        // Wait for async updates
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the method was deleted
        String actualText = controller.methodsArea.getText();
        assertFalse(actualText.contains(methodNameToDelete),
                "Expected method '" + methodNameToDelete + "' to be deleted but it was still found in the methods area: " + actualText);
    }
}
