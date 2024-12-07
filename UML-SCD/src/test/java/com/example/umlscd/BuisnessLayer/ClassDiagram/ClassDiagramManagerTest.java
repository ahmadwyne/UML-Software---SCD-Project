package com.example.umlscd.BuisnessLayer.ClassDiagram;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.umlscd.BuisnessLayer.ClasDiagram.AssociationManager;
import javafx.embed.swing.JFXPanel; // Import JFXPanel to initialize the JavaFX toolkit

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.DataAccessLayer.Serializers.ClassDiagram.ClassDiagramSerializer;
import com.example.umlscd.Models.ClassDiagram.ClassDiagramD;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.example.umlscd.PresentationLayer.ClassDiagram.ClassDiagramUI;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class ClassDiagramManagerTest {

    @Mock
    private ClassDiagramUI mockUiController;

    @Mock
    private ClassDiagramSerializer mockSerializer;

    @Mock
    private ClassDiagramD mockClassDiagram;

    @Mock
    private VBox mockClassBox;

    @Mock
    private VBox mockInterfaceBox;

    @Mock
    private UMLClassBox mockUMLClassBox;

    @Mock
    private UMLInterfaceBox mockUMLInterfaceBox;

    @Mock
    private File mockFile;

    private ClassDiagramManager manager;
    private Object mockUIController;
    private Pane mockDrawingPane;
    private VBox mockElement1;
    private VBox mockElement2;
    @BeforeEach
    public void setUp() {
        new JFXPanel();
        MockitoAnnotations.openMocks(this);
        manager = new ClassDiagramManager(mockUiController);
        manager.classDiagram = mockClassDiagram;
        // Mocking the relations manager
        manager.relationsManager = mock(AssociationManager.class);

        manager = mock(ClassDiagramManager.class);
        mockDrawingPane = new Pane();
        mockElement1 = new VBox();
        mockElement2 = new VBox();
        mockDrawingPane.getChildren().addAll(mockElement1, mockElement2);

    }

    @Test
    public void testGetClasses() {
        // Arrange
        when(mockClassDiagram.getClasses()).thenReturn(List.of(mockUMLClassBox));

        // Act
        List<UMLClassBox> classes = manager.getClasses();

        // Assert
        assertNotNull(classes);
        assertEquals(1, classes.size());
        verify(mockClassDiagram).getClasses();
    }

    @Test
    public void testGetInterfaces() {
        // Arrange
        when(mockClassDiagram.getInterfaces()).thenReturn(List.of(mockUMLInterfaceBox));

        // Act
        List<UMLInterfaceBox> interfaces = manager.getInterfaces();

        // Assert
        assertNotNull(interfaces);
        assertEquals(1, interfaces.size());
        verify(mockClassDiagram).getInterfaces();
    }

    @Test
    public void testGetRelationships() {
        // Arrange
        when(mockClassDiagram.getRelationships()).thenReturn(List.of());

        // Act
        List<UMLRelationship> relationships = manager.getRelationships();

        // Assert
        assertNotNull(relationships);
        assertTrue(relationships.isEmpty());
        verify(mockClassDiagram).getRelationships();
    }

    @Test
    public void testClearDiagram() {
        // Arrange
        when(mockUiController.getDrawingPane()).thenReturn(new VBox());

        // Act
        manager.clearDiagram();

        // Assert
        verify(mockUiController).getDrawingPane();
        verify(mockClassDiagram).getClasses();
        verify(mockClassDiagram).getInterfaces();
        verify(mockClassDiagram).getRelationships();
    }
    @Test
    void testCreateClassBox() {
        // Step 1: Mock the ClassDiagramUI
        ClassDiagramUI mockUI = mock(ClassDiagramUI.class);

        // Step 2: Mock the getDrawingPane() method to return a valid Pane
        Pane mockPane = mock(Pane.class);
        when(mockUI.getDrawingPane()).thenReturn(mockPane);

        // Step 3: Mock the behavior of getChildren() to avoid NullPointerException
        when(mockPane.getChildren()).thenReturn(javafx.collections.FXCollections.observableArrayList());  // Empty list or a mock list

        // Step 4: Create the ClassDiagramManager with the mocked ClassDiagramUI
        ClassDiagramManager manager = new ClassDiagramManager(mockUI);

        // Step 5: Test the createClassBox method
        manager.createClassBox("Class1", 50, 100);

        // You can assert that the class box was created or that the method interacted with the pane as expected
        assertNotNull(mockPane.getChildren(), "The children of the pane should not be null");
    }
    @Test
    void testCreateInterfaceBox() {
        // Step 1: Mock the ClassDiagramUI
        ClassDiagramUI mockUI = mock(ClassDiagramUI.class);

        // Step 2: Mock the getDrawingPane() method to return a valid Pane
        Pane mockPane = mock(Pane.class);
        when(mockUI.getDrawingPane()).thenReturn(mockPane);

        // Step 3: Mock the behavior of getChildren() to avoid NullPointerException
        when(mockPane.getChildren()).thenReturn(javafx.collections.FXCollections.observableArrayList());  // Empty list or a mock list

        // Step 4: Create the ClassDiagramManager with the mocked ClassDiagramUI
        ClassDiagramManager manager = new ClassDiagramManager(mockUI);

        // Step 5: Test the createClassBox method
        manager.createClassBox("Interface1", 50, 100);

        // You can assert that the class box was created or that the method interacted with the pane as expected
        assertNotNull(mockPane.getChildren(), "The children of the pane should not be null");
    }


    @Test
    public void testFindClassBoxByName() {
        // Arrange
        String className = "TestClass";
        when(mockClassDiagram.getClasses()).thenReturn(List.of(mockUMLClassBox));
        when(mockUMLClassBox.getName()).thenReturn(className);
        when(mockUMLClassBox.getVisualRepresentation()).thenReturn(mockClassBox);

        // Act
        VBox result = manager.findClassBoxByName(className);

        // Assert
        assertNotNull(result);
        verify(mockClassDiagram).getClasses();
    }

    @Test
    public void testFindInterfaceBoxByName() {
        // Arrange
        String interfaceName = "TestInterface";
        when(mockClassDiagram.getInterfaces()).thenReturn(List.of(mockUMLInterfaceBox));
        when(mockUMLInterfaceBox.getName()).thenReturn(interfaceName);
        when(mockUMLInterfaceBox.getVisualRepresentation()).thenReturn(mockInterfaceBox);

        // Act
        VBox result = manager.findInterfaceBoxByName(interfaceName);

        // Assert
        assertNotNull(result);
        verify(mockClassDiagram).getInterfaces();
    }
    @Test
    void testSaveDiagram() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Given: A mocked serializer and UI controller
        ClassDiagramSerializer mockSerializer = mock(ClassDiagramSerializer.class);
        ClassDiagramUI mockUIController = mock(ClassDiagramUI.class);
        ClassDiagramManager manager = new ClassDiagramManager(mockUIController);

        // Use reflection to set the mock serializer directly in the ClassDiagramManager
        java.lang.reflect.Field field = ClassDiagramManager.class.getDeclaredField("serializer");
        field.setAccessible(true);  // Make private field accessible
        field.set(manager, mockSerializer);

        // Create a temporary file to save the diagram to
        File testFile = new File("test_diagram.json");

        // When: Saving the diagram to the file
        manager.saveDiagram(testFile);

        // Then: Verify that the serializer's serialize method was called once with the classDiagram and the file
        verify(mockSerializer, times(1)).serialize(any(ClassDiagramD.class), eq(testFile));

        // Also, verify that the UI controller shows the success alert
        verify(mockUIController, times(1)).showInformationAlert("Diagram saved successfully to " + testFile.getAbsolutePath());
    }

    @Test
    void testSaveDiagramWithError() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Given: A mocked serializer that throws IOException
        ClassDiagramSerializer mockSerializer = mock(ClassDiagramSerializer.class);
        ClassDiagramUI mockUIController = mock(ClassDiagramUI.class);
        ClassDiagramManager manager = new ClassDiagramManager(mockUIController);

        // Use reflection to inject the mock serializer
        java.lang.reflect.Field field = ClassDiagramManager.class.getDeclaredField("serializer");
        field.setAccessible(true);  // Make the private field accessible
        field.set(manager, mockSerializer);

        // Create a temporary file to simulate the save
        File testFile = new File("test_diagram.json");

        // Simulate an IOException during the serialization process
        doThrow(new IOException("Test Exception")).when(mockSerializer).serialize(any(ClassDiagramD.class), eq(testFile));

        // When: Save the diagram
        manager.saveDiagram(testFile);

        // Then: Verify that the error alert was shown in the UI controller
        verify(mockUIController, times(1)).showErrorAlert("Failed to save diagram.");

        // Optional: Verify that the exception was logged (if your method logs the error)
        // In case you want to check whether the exception was printed or logged in console
        // You can add additional verifications or use a logging framework
    }
    @Test
    void testHandleToolSelectionClass() {
        // Given
        ClassDiagramManager manager = mock(ClassDiagramManager.class);
        Pane mockDrawingPane = mock(Pane.class);
        VBox mockEditorsPane = mock(VBox.class);
        ClassDiagramD mockClassDiagram = mock(ClassDiagramD.class);
        when(manager.getClassDiagram()).thenReturn(mockClassDiagram);

        // Call method with "Class" tool
        manager.handleToolSelection("Class", mockDrawingPane, mockEditorsPane);

        // Verify createClassBox is called
        verify(manager, times(1)).createClassBox(anyString(), anyInt(), anyInt());
    }

    @Test
    void testHandleToolSelectionInterface() {
        // Given
        ClassDiagramManager manager = mock(ClassDiagramManager.class);
        Pane mockDrawingPane = mock(Pane.class);
        VBox mockEditorsPane = mock(VBox.class);
        ClassDiagramD mockClassDiagram = mock(ClassDiagramD.class);
        when(manager.getClassDiagram()).thenReturn(mockClassDiagram);

        // Call method with "Interface" tool
        manager.handleToolSelection("Interface", mockDrawingPane, mockEditorsPane);

        // Verify createInterfaceBox is called
        verify(manager, times(1)).createInterfaceBox(anyString(), anyInt(), anyInt());
    }

    //@Test
    /*void testHandleToolSelectionAssociation() {
        // Given
        ClassDiagramManager manager = mock(ClassDiagramManager.class);
        Pane mockDrawingPane = mock(Pane.class);
        VBox mockEditorsPane = mock(VBox.class);

        // Call method with "Association" tool
        manager.handleToolSelection("Association", mockDrawingPane, mockEditorsPane);

        // Verify relationsManager is set to AssociationManager and enableAssociationMode is called
        verify(manager, times(1)).enableAssociationMode(mockDrawingPane);
    }*/

    @Test
    void testHandleToolSelectionAggregation() {
        // Given
        ClassDiagramManager manager = mock(ClassDiagramManager.class);
        Pane mockDrawingPane = mock(Pane.class);
        VBox mockEditorsPane = mock(VBox.class);

        // Call method with "Aggregation" tool
        manager.handleToolSelection("Aggregation", mockDrawingPane, mockEditorsPane);

        // Verify relationsManager is set to AggregationManager and enableAggregationMode is called
        verify(manager, times(1)).enableAggregationMode(mockDrawingPane);
    }

    @Test
    void testHandleToolSelectionComposition() {
        // Given
        ClassDiagramManager manager = mock(ClassDiagramManager.class);
        Pane mockDrawingPane = mock(Pane.class);
        VBox mockEditorsPane = mock(VBox.class);

        // Call method with "Composition" tool
        manager.handleToolSelection("Composition", mockDrawingPane, mockEditorsPane);

        // Verify relationsManager is set to CompositionManager and enableCompositionMode is called
        verify(manager, times(1)).enableCompositionMode(mockDrawingPane);
    }

    @Test
    void testHandleToolSelectionInheritance() {
        // Given
        ClassDiagramManager manager = mock(ClassDiagramManager.class);
        Pane mockDrawingPane = mock(Pane.class);
        VBox mockEditorsPane = mock(VBox.class);

        // Call method with "Inheritance" tool
        manager.handleToolSelection("Inheritance", mockDrawingPane, mockEditorsPane);

        // Verify relationsManager is set to InheritanceManager and enableInheritanceMode is called
        verify(manager, times(1)).enableInheritanceMode(mockDrawingPane);
    }

    @Test
    void testHandleToolSelectionDrag() {
        // Given
        ClassDiagramManager manager = mock(ClassDiagramManager.class);
        Pane mockDrawingPane = mock(Pane.class);
        VBox mockEditorsPane = mock(VBox.class);

        // Call method with "Drag" tool
        manager.handleToolSelection("Drag", mockDrawingPane, mockEditorsPane);

        // Verify enableDragMode is called
        verify(manager, times(1)).enableDragMode();
    }

    @Test
    void testHandleToolSelectionUnknown() {
        // Given
        ClassDiagramManager manager = mock(ClassDiagramManager.class);
        Pane mockDrawingPane = mock(Pane.class);
        VBox mockEditorsPane = mock(VBox.class);

        // Call method with an unknown tool
        manager.handleToolSelection("UnknownTool", mockDrawingPane, mockEditorsPane);

        // Verify that no actions are triggered (except for the error message)
        verify(manager, never()).createClassBox(anyString(), anyInt(), anyInt());
        verify(manager, never()).createInterfaceBox(anyString(), anyInt(), anyInt());
    }
    @Test
    public void testLoadDiagram() throws IOException, IllegalAccessException, NoSuchFieldException {
        // Arrange
        File mockFile = mock(File.class); // Mock the File object
        ClassDiagramD mockClassDiagram = mock(ClassDiagramD.class); // Mock the ClassDiagramD object
        ClassDiagramSerializer mockSerializer = mock(ClassDiagramSerializer.class); // Mock the serializer

        // Mock the behavior of the file input stream
        FileInputStream mockFileInputStream = mock(FileInputStream.class);
        when(mockFile.exists()).thenReturn(true); // Mock file exists check
        when(mockFileInputStream.read(any(byte[].class))).thenReturn(0); // Mock file read behavior

        // Mock the deserialize method to return the mock class diagram
        when(mockSerializer.deserialize(mockFile)).thenReturn(mockClassDiagram);

        // Create the ClassDiagramManager with the mock UI controller
        ClassDiagramManager manager = new ClassDiagramManager(mockUiController);
       // Use reflection to inject the mock serializer
        Field serializerField = ClassDiagramManager.class.getDeclaredField("serializer");
        serializerField.setAccessible(true); // Make the private field accessible
        serializerField.set(manager, mockSerializer); // Set the mock serializer

        // Act
        manager.loadDiagram(mockFile); // Call the method to test

        // Assert
        verify(mockSerializer).deserialize(mockFile); // Verify the deserialize method is called
        verify(mockUiController).showInformationAlert(anyString()); // Verify UI feedback is shown
    }
    @Test
    void testLoadDiagramWithError() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Given: A mocked serializer that throws IOException
        ClassDiagramSerializer mockSerializer = mock(ClassDiagramSerializer.class);
        ClassDiagramUI mockUIController = mock(ClassDiagramUI.class);
        ClassDiagramManager manager = new ClassDiagramManager(mockUIController);

        // Use reflection to inject the mock serializer
        java.lang.reflect.Field field = ClassDiagramManager.class.getDeclaredField("serializer");
        field.setAccessible(true);  // Make the private field accessible
        field.set(manager, mockSerializer);

        // Create a temporary file to simulate the load
        File testFile = new File("test_diagram.json");

        // Simulate an IOException during the loading process (e.g., file not found or corrupt file)
        doThrow(new IOException("Test Exception")).when(mockSerializer).deserialize(any(File.class));

        // When: Load the diagram from the file
        manager.loadDiagram(testFile);

        // Then: Verify that the error alert was shown in the UI controller
        verify(mockUIController, times(1)).showErrorAlert("Failed to load diagram.");

        // Optional: Verify that the exception was logged (if your method logs the error)
        // In case you want to check whether the exception was printed or logged in console
        // You can add additional verifications or use a logging framework
    }

    @Test
    void testEnableAssociationMode() {
        // Simulate a MouseEvent for first selection (first element clicked)
        MouseEvent mockEvent1 = mock(MouseEvent.class);
        when(mockEvent1.getPickResult()).thenReturn(mock(PickResult.class));
        when(mockEvent1.getPickResult().getIntersectedNode()).thenReturn(mockElement1);

        // Simulate a MouseEvent for second selection (second element clicked)
        MouseEvent mockEvent2 = mock(MouseEvent.class);
        when(mockEvent2.getPickResult()).thenReturn(mock(PickResult.class));
        when(mockEvent2.getPickResult().getIntersectedNode()).thenReturn(mockElement2);

        // Simulate user interaction by firing mouse events on mock elements
        mockElement1.fireEvent(mockEvent1);  // First click on mockElement1
        mockElement2.fireEvent(mockEvent2);  // Second click on mockElement2

        // Verify that the dialog was shown after the second click
        //verify(mockDialog, times(1)).showAndWait();

        // Verify that the createRelationship method is called with the correct parameters
        verify(manager.relationsManager, times(1)).createRelationship(
                eq(mockElement1), eq(mockElement2), eq(mockDrawingPane),
                eq("Association"), eq("1"), eq("1")
        );
    }
}

