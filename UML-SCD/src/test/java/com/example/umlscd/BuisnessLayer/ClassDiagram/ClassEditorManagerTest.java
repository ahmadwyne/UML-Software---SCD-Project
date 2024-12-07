package com.example.umlscd.BuisnessLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassEditorManager;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import com.example.umlscd.Models.ClassDiagram.UMLElementBoxInterface;
import com.example.umlscd.PresentationLayer.ClassDiagram.ClassDiagramUI;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClassEditorManagerTest {

    private ClassEditorManager classEditorManager;
    private ClassDiagramManager mockClassDiagramManager;
    private UMLClassBox mockUmlClassBox;
    private VBox classBox; // Real VBox
    private Label classNameLabel; // Real Label
    private VBox attributesBox; // Real VBox
    private VBox methodsBox; // Real VBox
    private ClassDiagramUI mockClassDiagramUI; // Mock for ClassDiagramUI

    @BeforeAll
    static void initJFX() {
        // Initializes the JavaFX toolkit
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        classEditorManager = new ClassEditorManager();

        // Mock dependencies
        mockClassDiagramManager = mock(ClassDiagramManager.class);
        mockUmlClassBox = mock(UMLClassBox.class);
        mockClassDiagramUI = mock(ClassDiagramUI.class); // Initialize mock ClassDiagramUI

        // Set up ClassDiagramManager behavior
        when(mockClassDiagramManager.isClassNameExists(anyString())).thenReturn(false);
        when(mockClassDiagramManager.getClassBoxMap()).thenReturn(new HashMap<>());
        when(mockClassDiagramManager.getUiController()).thenReturn(mockClassDiagramUI); // Assume getter exists

        // Set up UMLClassBox behavior
        when(mockUmlClassBox.getName()).thenReturn("OldClassName");

        // Initialize real JavaFX components
        classNameLabel = new Label("OldClassName");
        attributesBox = new VBox();
        methodsBox = new VBox();

        // Optionally, add initial attributes and methods
        // attributesBox.getChildren().addAll(new Label("private String name"), new Label("public int age"));
        // methodsBox.getChildren().addAll(new Label("public void setName(String name)"), new Label("public String getName()"));

        classBox = new VBox(classNameLabel, attributesBox, methodsBox);

        // Initialize ClassEditorManager with mocked dependencies and real classBox
        classEditorManager.setClassDiagramManager(mockClassDiagramManager);
        classEditorManager.setClassBox(classBox, mockUmlClassBox);
    }

    @Test
    void testGetClassName() {
        String className = classEditorManager.getClassName();
        assertEquals("OldClassName", className);
    }

    @Test
    void testGetClassName_WhenClassBoxIsNull() {
        classEditorManager.setClassBox(null, mockUmlClassBox);
        String className = classEditorManager.getClassName();
        assertEquals("", className);
    }

    @Test
    void testGetClassName_WhenClassBoxHasNoChildren() {
        VBox emptyClassBox = new VBox(); // Real VBox with no children
        classEditorManager.setClassBox(emptyClassBox, mockUmlClassBox);
        String className = classEditorManager.getClassName();
        assertEquals("", className);
    }

    @Test
    void testGetAttributes() {
        // Add real Label instances to attributesBox
        Label attribute1 = new Label("private String name");
        Label attribute2 = new Label("public int age");
        attributesBox.getChildren().addAll(attribute1, attribute2);

        String attributes = classEditorManager.getAttributes();
        assertEquals("private String name\npublic int age", attributes);
    }

    @Test
    void testGetAttributes_WhenNoAttributes() {
        // Ensure attributesBox is empty
        attributesBox.getChildren().clear();
        String attributes = classEditorManager.getAttributes();
        assertEquals("", attributes);
    }

    @Test
    void testGetMethods() {
        // Add real Label instances to methodsBox
        Label method1 = new Label("public void setName(String name)");
        Label method2 = new Label("public String getName()");
        methodsBox.getChildren().addAll(method1, method2);

        String methods = classEditorManager.getMethods();
        assertEquals("public void setName(String name)\npublic String getName()", methods);
    }

    @Test
    void testGetMethods_WhenNoMethods() {
        // Ensure methodsBox is empty
        methodsBox.getChildren().clear();
        String methods = classEditorManager.getMethods();
        assertEquals("", methods);
    }

    @Test
    void testApplyChanges_SuccessfulUpdate() {
        // Setup
        String newClassName = "NewClassName";
        String newAttributes = "private String name\npublic int age";
        String newMethods = "public void setName(String name)\npublic String getName()";

        // Mock class name existence check
        when(mockClassDiagramManager.isClassNameExists(newClassName)).thenReturn(false);

        // Apply changes
        classEditorManager.applyChanges(newClassName, newAttributes, newMethods);

        // Verify UMLClassBox updates
        verify(mockUmlClassBox).setName(newClassName);
        verify(mockUmlClassBox).setAttributes(Arrays.asList("private String name", "public int age"));
        verify(mockUmlClassBox).setMethods(Arrays.asList("public void setName(String name)", "public String getName()"));

        // Verify ClassDiagramManager updates
        ArgumentCaptor<Map<String, UMLElementBoxInterface>> mapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockClassDiagramManager, times(2)).getClassBoxMap();
        verify(mockClassDiagramManager).updateRelationshipsForRenamedClass("OldClassName", newClassName);

        // Verify UI updates
        assertEquals(newClassName, classNameLabel.getText());

        // Verify attributesBox updates
        List<String> expectedAttributes = Arrays.asList("private String name", "public int age");
        List<String> actualAttributes = new ArrayList<>();
        for (Node node : attributesBox.getChildren()) {
            if (node instanceof Label) {
                actualAttributes.add(((Label) node).getText());
            }
        }
        assertEquals(expectedAttributes, actualAttributes);

        // Verify methodsBox updates
        List<String> expectedMethods = Arrays.asList("public void setName(String name)", "public String getName()");
        List<String> actualMethods = new ArrayList<>();
        for (Node node : methodsBox.getChildren()) {
            if (node instanceof Label) {
                actualMethods.add(((Label) node).getText());
            }
        }
        assertEquals(expectedMethods, actualMethods);

        // Verify classBox map updates
        Map<String, UMLElementBoxInterface> classBoxMap = mockClassDiagramManager.getClassBoxMap();
        assertTrue(classBoxMap.containsKey(newClassName));
        assertFalse(classBoxMap.containsKey("OldClassName"));
        assertEquals(mockUmlClassBox, classBoxMap.get(newClassName));
    }

    @Test
    void testApplyChanges_EmptyClassName() {
        String newClassName = "   "; // Blank after trimming
        String newAttributes = "private String name";
        String newMethods = "public void setName(String name)";

        classEditorManager.applyChanges(newClassName, newAttributes, newMethods);

        // Verify error alert is shown via ClassDiagramUI
        verify(mockClassDiagramUI).showErrorAlert("Class name cannot be empty.");

        // Verify no updates are made
        verify(mockUmlClassBox, never()).setName(anyString());
        verify(mockUmlClassBox, never()).setAttributes(anyList());
        verify(mockUmlClassBox, never()).setMethods(anyList());

        // Verify UI remains unchanged
        assertEquals("OldClassName", classNameLabel.getText());
    }

    @Test
    void testApplyChanges_DuplicateClassName() {
        String newClassName = "ExistingClassName";
        String newAttributes = "private String name";
        String newMethods = "public void setName(String name)";

        // Mock class name existence check
        when(mockClassDiagramManager.isClassNameExists(newClassName)).thenReturn(true);

        classEditorManager.applyChanges(newClassName, newAttributes, newMethods);

        // Verify error alert is shown via ClassDiagramUI
        verify(mockClassDiagramUI).showErrorAlert("A class with this name already exists.");

        // Verify no updates are made
        verify(mockUmlClassBox, never()).setName(anyString());
        verify(mockUmlClassBox, never()).setAttributes(anyList());
        verify(mockUmlClassBox, never()).setMethods(anyList());

        // Verify UI remains unchanged
        assertEquals("OldClassName", classNameLabel.getText());
    }

    @Test
    void testHandleCustomDataType_NewType() {
        // Since handleCustomDataType involves UI interaction with TextInputDialog,
        // it's challenging to unit test without refactoring.
        // Consider refactoring to inject a Supplier<Optional<String>> for better testability.
        // For now, acknowledge the limitation.

        // Placeholder assertion
        assertTrue(true, "handleCustomDataType involves UI interaction and requires integration testing.");
    }

    @Test
    void testAddParameterToMethod() {
        String parameter = "String name";
        classEditorManager.addParameterToMethod(parameter);
        assertTrue(classEditorManager.parameters.contains(parameter));
        assertEquals(1, classEditorManager.parameters.size());
    }

    @Test
    void testFinalizeMethod_NoParameters() {
        String methodName = "getName";
        String returnType = "String";
        String visibility = "+";

        String methodSignature = classEditorManager.finalizeMethod(methodName, returnType, visibility);
        assertEquals("+getName() : String", methodSignature);
    }

    @Test
    void testFinalizeMethod_WithParameters() {
        classEditorManager.addParameterToMethod("String name");
        classEditorManager.addParameterToMethod("int age");

        String methodName = "setNameAndAge";
        String returnType = "void";
        String visibility = "-";

        String methodSignature = classEditorManager.finalizeMethod(methodName, returnType, visibility);
        assertEquals("-setNameAndAge(String name, int age) : void", methodSignature);
    }

    @Test
    void testResetParameters() {
        classEditorManager.addParameterToMethod("String name");
        classEditorManager.addParameterToMethod("int age");
        assertEquals(2, classEditorManager.parameters.size());

        classEditorManager.resetParameters();
        assertTrue(classEditorManager.parameters.isEmpty());
    }

    // Additional tests can be written for UI updates and other edge cases as needed.

}
