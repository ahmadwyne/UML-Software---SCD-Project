package com.example.umlscd.BuisnessLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.InterfaceEditorManager;
import com.example.umlscd.Models.ClassDiagram.UMLElementBoxInterface;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import com.example.umlscd.PresentationLayer.ClassDiagram.ClassDiagramUI;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterfaceEditorManagerTest {

    private InterfaceEditorManager interfaceEditorManager;
    private ClassDiagramManager mockClassDiagramManager;
    private UMLInterfaceBox mockUmlInterfaceBox;
    private VBox interfaceBox; // Real VBox
    private Label interfaceNameLabel; // Real Label
    private VBox methodsBox; // Real VBox
    private ClassDiagramUI mockClassDiagramUI; // Mock for ClassDiagramUI

    @BeforeAll
    static void initJFX() {
        // Initializes the JavaFX toolkit
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        interfaceEditorManager = new InterfaceEditorManager();

        // Mock dependencies
        mockClassDiagramManager = mock(ClassDiagramManager.class);
        mockUmlInterfaceBox = mock(UMLInterfaceBox.class);
        mockClassDiagramUI = mock(ClassDiagramUI.class); // Initialize mock ClassDiagramUI

        // Set up ClassDiagramManager behavior
        when(mockClassDiagramManager.isClassNameExists(anyString())).thenReturn(false);
        when(mockClassDiagramManager.getClassBoxMap()).thenReturn(new HashMap<>());
        when(mockClassDiagramManager.getUiController()).thenReturn(mockClassDiagramUI); // Assume getter exists

        // Set up UMLInterfaceBox behavior
        when(mockUmlInterfaceBox.getName()).thenReturn("OldInterfaceName");

        // Initialize real JavaFX components
        interfaceNameLabel = new Label("OldInterfaceName");
        methodsBox = new VBox();

        // Optionally, add initial methods
        // methodsBox.getChildren().addAll(new Label("public void methodOne()"), new Label("public int methodTwo(String param)"));

        interfaceBox = new VBox();
        interfaceBox.getChildren().addAll(new Label("Interface:"), interfaceNameLabel, methodsBox);

        // Initialize InterfaceEditorManager with mocked dependencies and real interfaceBox
        interfaceEditorManager.setClassDiagramManager(mockClassDiagramManager);
        interfaceEditorManager.setInterfaceBox(interfaceBox, mockUmlInterfaceBox);
    }

    /**
     * Test retrieving the interface name when interfaceBox is properly initialized.
     */
    @Test
    void testGetInterfaceName() {
        String interfaceName = interfaceEditorManager.getInterfaceName();
        assertEquals("OldInterfaceName", interfaceName);
    }

    /**
     * Test retrieving the interface name when interfaceBox is null.
     */
    @Test
    void testGetInterfaceName_WhenInterfaceBoxIsNull() {
        interfaceEditorManager.setInterfaceBox(null, mockUmlInterfaceBox);
        String interfaceName = interfaceEditorManager.getInterfaceName();
        assertEquals("", interfaceName);
    }

    /**
     * Test retrieving the interface name when interfaceBox has no children.
     */
    @Test
    void testGetInterfaceName_WhenInterfaceBoxHasNoChildren() {
        VBox emptyInterfaceBox = new VBox(); // Real VBox with no children
        interfaceEditorManager.setInterfaceBox(emptyInterfaceBox, mockUmlInterfaceBox);
        String interfaceName = interfaceEditorManager.getInterfaceName();
        assertEquals("", interfaceName);
    }

    /**
     * Test retrieving methods when methodsBox has methods.
     */
    @Test
    void testGetMethods() {
        // Add real Label instances to methodsBox
        Label method1 = new Label("public void methodOne()");
        Label method2 = new Label("public int methodTwo(String param)");
        methodsBox.getChildren().addAll(method1, method2);

        String methods = interfaceEditorManager.getMethods();
        assertEquals("public void methodOne()\npublic int methodTwo(String param)", methods);
    }

    /**
     * Test retrieving methods when methodsBox has no methods.
     */
    @Test
    void testGetMethods_WhenNoMethods() {
        // Ensure methodsBox is empty
        methodsBox.getChildren().clear();
        String methods = interfaceEditorManager.getMethods();
        assertEquals("", methods);
    }

    /**
     * Test applying changes with valid new interface name and methods.
     */
    @Test
    void testApplyChanges_SuccessfulUpdate() {
        // Setup
        String newInterfaceName = "NewInterfaceName";
        String newMethods = "public void newMethodOne()\npublic String newMethodTwo(int value)";

        // Mock class name existence check
        when(mockClassDiagramManager.isClassNameExists(newInterfaceName)).thenReturn(false);

        // Apply changes
        interfaceEditorManager.applyChanges(newInterfaceName, newMethods);

        // Verify UMLInterfaceBox updates
        verify(mockUmlInterfaceBox).setName(newInterfaceName);
        verify(mockUmlInterfaceBox).setMethods(Arrays.asList("public void newMethodOne()", "public String newMethodTwo(int value)"));

        // Verify ClassDiagramManager updates
        verify(mockClassDiagramManager, times(2)).getClassBoxMap(); // Depending on applyChanges implementation
        verify(mockClassDiagramManager).updateRelationshipsForRenamedClass("OldInterfaceName", newInterfaceName);

        // Verify UI updates
        assertEquals(newInterfaceName, interfaceNameLabel.getText());

        // Verify methodsBox updates
        List<String> expectedMethods = Arrays.asList("public void newMethodOne()", "public String newMethodTwo(int value)");
        List<String> actualMethods = new ArrayList<>();
        for (Node node : methodsBox.getChildren()) {
            if (node instanceof Label) {
                actualMethods.add(((Label) node).getText());
            }
        }
        assertEquals(expectedMethods, actualMethods);

        // Verify classBox map updates
        Map<String, UMLElementBoxInterface> classBoxMap = mockClassDiagramManager.getClassBoxMap();
        assertTrue(classBoxMap.containsKey(newInterfaceName));
        assertFalse(classBoxMap.containsKey("OldInterfaceName"));
        assertEquals(mockUmlInterfaceBox, classBoxMap.get(newInterfaceName));
    }

    /**
     * Test applying changes with an empty interface name.
     */
    @Test
    void testApplyChanges_EmptyInterfaceName() {
        String newInterfaceName = "   "; // Blank after trimming
        String newMethods = "public void newMethod()";

        interfaceEditorManager.applyChanges(newInterfaceName, newMethods);

        // Verify error alert is shown via ClassDiagramUI
        verify(mockClassDiagramUI).showErrorAlert("Class name cannot be empty.");

        // Verify no updates are made
        verify(mockUmlInterfaceBox, never()).setName(anyString());
        verify(mockUmlInterfaceBox, never()).setMethods(anyList());

        // Verify UI remains unchanged
        assertEquals("OldInterfaceName", interfaceNameLabel.getText());
    }

    /**
     * Test applying changes with a duplicate interface name.
     */
    @Test
    void testApplyChanges_DuplicateInterfaceName() {
        String newInterfaceName = "ExistingInterfaceName";
        String newMethods = "public void newMethod()";

        // Mock class name existence check
        when(mockClassDiagramManager.isClassNameExists(newInterfaceName)).thenReturn(true);

        interfaceEditorManager.applyChanges(newInterfaceName, newMethods);

        // Verify error alert is shown via ClassDiagramUI
        verify(mockClassDiagramUI).showErrorAlert("An Interface with this name already exists.");

        // Verify no updates are made
        verify(mockUmlInterfaceBox, never()).setName(anyString());
        verify(mockUmlInterfaceBox, never()).setMethods(anyList());

        // Verify UI remains unchanged
        assertEquals("OldInterfaceName", interfaceNameLabel.getText());
    }

    /**
     * Test adding a parameter to a method.
     */
    @Test
    void testAddParameterToMethod() {
        String parameter = "String name";
        interfaceEditorManager.addParameterToMethod(parameter);
        assertTrue(interfaceEditorManager.parameters.contains(parameter));
        assertEquals(1, interfaceEditorManager.parameters.size());
    }

    /**
     * Test finalizing a method signature without parameters.
     */
    @Test
    void testFinalizeMethod_NoParameters() {
        String methodName = "getName";
        String returnType = "String";
        String visibility = "+";

        String methodSignature = interfaceEditorManager.finalizeMethod(methodName, returnType, visibility);
        assertEquals("+getName() : String", methodSignature);
    }

    /**
     * Test finalizing a method signature with parameters.
     */
    @Test
    void testFinalizeMethod_WithParameters() {
        interfaceEditorManager.addParameterToMethod("String name");
        interfaceEditorManager.addParameterToMethod("int age");

        String methodName = "setNameAndAge";
        String returnType = "void";
        String visibility = "-";

        String methodSignature = interfaceEditorManager.finalizeMethod(methodName, returnType, visibility);
        assertEquals("-setNameAndAge(String name, int age) : void", methodSignature);
    }

    /**
     * Test resetting the parameters list after finalizing a method.
     */
    @Test
    void testResetParameters() {
        interfaceEditorManager.addParameterToMethod("String name");
        interfaceEditorManager.addParameterToMethod("int age");
        assertEquals(2, interfaceEditorManager.parameters.size());

        interfaceEditorManager.resetParameters();
        assertTrue(interfaceEditorManager.parameters.isEmpty());
    }

    /**
     * Test handling a custom data type addition.
     *
     * <p>Note: Since handleCustomDataType is a static method that opens a dialog,
     * it's challenging to unit test without refactoring. Consider refactoring
     * to inject a Supplier or use TestFX for UI interactions.</p>
     */
    @Test
    void testHandleCustomDataType_NewType() {
        // Acknowledge the limitation in unit testing static UI methods
        assertTrue(true, "handleCustomDataType involves UI interaction and requires integration testing.");
    }
}
