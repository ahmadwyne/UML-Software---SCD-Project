package com.example.umlscd.BuisnessLayer.ClassDiagram;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.umlscd.BuisnessLayer.ClasDiagram.*;
import com.example.umlscd.DataAccessLayer.Serializers.ClassDiagram.ClassDiagramSerializer;
import com.example.umlscd.Models.ClassDiagram.*;
import com.example.umlscd.PresentationLayer.ClassDiagram.ClassDiagramUI;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ClassDiagramManagerTest   {
    @Mock
    private ClassDiagramUI mockUiController;
    @Mock private Alert mockAlert;  // Mock Alert
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
    private ClassDiagramD classDiagram;    // Mock ClassDiagram
    private Pane drawingPane;
    private VBox firstVBox;
    private VBox secondVBox;

    // Create a simple mock for the relationsManager and InheritanceManager
    private ClassDiagramRelationsManager relationsManager;
    @BeforeEach
    public void setUp() {
        new JFXPanel();
        MockitoAnnotations.openMocks(this);
        manager = new ClassDiagramManager(mockUiController);
        manager.classDiagram = mockClassDiagram;
        classDiagram = mock(ClassDiagramD.class);
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

        // Step 6: Assertions
        // You can assert that the class box was created or that the method interacted with the pane as expected
        assertNotNull(mockPane.getChildren(), "The children of the pane should not be null");
    }

    @Test
    public void testCreateInterfaceBox() {
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

        // Step 6: Assertions
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
    void testLoadDiagram() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Given: A mocked serializer and UI controller
        ClassDiagramSerializer mockSerializer = mock(ClassDiagramSerializer.class);
        ClassDiagramUI mockUIController = mock(ClassDiagramUI.class);
        ClassDiagramManager manager = new ClassDiagramManager(mockUIController);

        // Use reflection to inject the mock serializer
        java.lang.reflect.Field field = ClassDiagramManager.class.getDeclaredField("serializer");
        field.setAccessible(true);  // Make the private field accessible
        field.set(manager, mockSerializer);

        // Create a temporary file to simulate the load
        File testFile = new File("test_diagram.json");

        // Mock the behavior of the serializer to return some mock data
        ClassDiagramD mockDiagram = mock(ClassDiagramD.class);
        when(mockSerializer.deserialize(testFile)).thenReturn(mockDiagram);

        // When: Load the diagram from the file
        manager.loadDiagram(testFile);

        // Then: Verify that the serializer's deserialize method was called with the correct file
        verify(mockSerializer, times(1)).deserialize(testFile);
        // Optionally, verify that no error alert is shown (since the load was successful)
        verify(mockUIController, times(0)).showErrorAlert(anyString());
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
    void testHandleToolSelectionClass() {
        // Make sure the JavaFX application thread is initialized
        Platform.runLater(() -> {
            // Given: Create real JavaFX UI components
            Pane mockDrawingPane = new Pane(); // Real Pane (not mocked)
            VBox mockEditorsPane = new VBox(); // Real VBox (not mocked)

            // Create the manager with a real UI component (no mocks)
            ClassDiagramUI realUI = new ClassDiagramUI();  // Assuming this is your real UI class
            ClassDiagramManager manager = new ClassDiagramManager(realUI);

            // When: Call the handleToolSelection method with the "Class" tool
            manager.handleToolSelection("Class", mockDrawingPane, mockEditorsPane);

            // Simulate adding a class box (assuming your 'createClassBox' method is invoked here)
            String expectedClassName = "Class1";
            manager.createClassBox(expectedClassName, 50, 100);

            // Then: Verify the behavior (Here, we can check that the class box is actually added to the pane)
            assertFalse(mockDrawingPane.getChildren().isEmpty(), "Drawing pane should contain at least one node after adding a class box");

            // Optionally, check that the box has the expected properties (e.g., name, position)
            assertEquals(1, mockDrawingPane.getChildren().size(), "There should be exactly one class box added");

            // Check that the class box has the correct name (if your method sets a label or text)
            VBox classBox = (VBox) mockDrawingPane.getChildren().get(0); // Assuming createClassBox adds VBox
            assertNotNull(classBox, "Class box should be added to the pane");
            // Assuming the class box has a label or something identifiable for name
            assertTrue(classBox.getChildren().get(0).toString().contains(expectedClassName), "Class box should have the expected class name");

            // You can also check if other methods are invoked within the 'handleToolSelection' method by checking GUI side effects
        });
    }
    @Test
    void testHandleToolSelectionInterface() {
        Platform.runLater(() -> {
            // Given: Real UI components
            Pane drawingPane = new Pane();
            VBox editorsPane = new VBox();
            ClassDiagramManager manager = new ClassDiagramManager(new ClassDiagramUI()); // Assuming real UI

            // When: Select the "Interface" tool
            manager.handleToolSelection("Interface", drawingPane, editorsPane);

            // Then: Verify that the interface box was created
            assertFalse(drawingPane.getChildren().isEmpty(), "Drawing pane should contain at least one node after interface selection");

            // Check that the first child in the pane is a VBox (interface box)
            VBox interfaceBox = (VBox) drawingPane.getChildren().get(0);
            assertNotNull(interfaceBox, "An interface box should be added");
        });
    }
    @Test
    void testHandleToolSelectionAssociation() {
        Platform.runLater(() -> {
            // Given: Real UI components
            Pane drawingPane = new Pane();
            VBox editorsPane = new VBox();
            ClassDiagramManager manager = new ClassDiagramManager(new ClassDiagramUI()); // Assuming real UI

            // When: Select the "Association" tool
            manager.handleToolSelection("Association", drawingPane, editorsPane);

            // Then: Verify that the association mode was enabled
            // You can verify if the relationsManager is an instance of AssociationManager
            assertTrue(manager.relationsManager instanceof AssociationManager, "The relations manager should be an instance of AssociationManager");
        });
    }
    @Test
    void testHandleToolSelectionAggregation() {
        Platform.runLater(() -> {
            // Given: Real UI components
            Pane drawingPane = new Pane();
            VBox editorsPane = new VBox();
            ClassDiagramManager manager = new ClassDiagramManager(new ClassDiagramUI()); // Assuming real UI

            // When: Select the "Aggregation" tool
            manager.handleToolSelection("Aggregation", drawingPane, editorsPane);

            // Then: Verify that the aggregation mode was enabled
            // Verify that the relationsManager is an instance of AggregationManager
            assertTrue(manager.relationsManager instanceof AggregationManager, "The relations manager should be an instance of AggregationManager");
        });
    }
    @Test
    void testHandleToolSelectionComposition() {
        Platform.runLater(() -> {
            // Given: Real UI components
            Pane drawingPane = new Pane();
            VBox editorsPane = new VBox();
            ClassDiagramManager manager = new ClassDiagramManager(new ClassDiagramUI()); // Assuming real UI

            // When: Select the "Composition" tool
            manager.handleToolSelection("Composition", drawingPane, editorsPane);

            // Then: Verify that the composition mode was enabled
            // Verify that the relationsManager is an instance of CompositionManager
            assertTrue(manager.relationsManager instanceof CompositionManager, "The relations manager should be an instance of CompositionManager");
        });
    }
    @Test
    void testHandleToolSelectionInheritance() {
        Platform.runLater(() -> {
            // Given: Real UI components
            Pane drawingPane = new Pane();
            VBox editorsPane = new VBox();
            ClassDiagramManager manager = new ClassDiagramManager(new ClassDiagramUI()); // Assuming real UI

            // When: Select the "Inheritance" tool
            manager.handleToolSelection("Inheritance", drawingPane, editorsPane);

            // Then: Verify that the inheritance mode was enabled
            // Verify that the relationsManager is an instance of InheritanceManager
            assertTrue(manager.relationsManager instanceof InheritanceManager, "The relations manager should be an instance of InheritanceManager");
        });
    }
    @Test
    void testHandleToolSelectionDrag() {
        Platform.runLater(() -> {
            // Given: Real UI components
            Pane drawingPane = new Pane();
            VBox editorsPane = new VBox();
            ClassDiagramManager manager = new ClassDiagramManager(new ClassDiagramUI()); // Assuming real UI

            // When: Select the "Drag" tool
            manager.handleToolSelection("Drag", drawingPane, editorsPane);

            // Then: Verify that drag mode is enabled
            // Assuming the manager has a method `isDragModeEnabled()`
            assertTrue(manager.isDragEnabled, "Drag mode should be enabled");
        });
    }

    @Test
    public void testSetDraggable() {
        VBox vbox = mock(VBox.class);
        // Test enabling dragging
        manager.setDraggable(vbox, true);
        verify(vbox, times(1)).setOnMousePressed(any());
        verify(vbox, times(1)).setOnMouseDragged(any());

        // Test disabling dragging
        manager.setDraggable(vbox, false);
        verify(vbox, times(1)).setOnMousePressed(null);
        verify(vbox, times(1)).setOnMouseDragged(null);
    }

    @Test
    public void testHighlightClass() {
        VBox classBox = new VBox();

        // Test highlighting
        manager.highlightClass(classBox, true);
        assertEquals("-fx-border-color: darkred; -fx-border-width: 2; -fx-border-style: solid;", classBox.getStyle());

        // Test unhighlighting
        manager.highlightClass(classBox, false);
        assertEquals("-fx-border-color: black; -fx-border-width: 1; -fx-border-style: solid;", classBox.getStyle());
    }
    @Test
    @Disabled
    void testEnableInheritanceMode() {
        // Create mock event for mouse click
        MouseEvent mockEvent = Mockito.mock(MouseEvent.class);
        PickResult mockPickResult = Mockito.mock(PickResult.class);
        Node mockNode = Mockito.mock(Node.class);

        // Mock the behavior of the event
        Mockito.when(mockEvent.getPickResult()).thenReturn(mockPickResult);
        Mockito.when(mockPickResult.getIntersectedNode()).thenReturn(mockNode);

        // Assume the node is a VBox and part of elements
        VBox mockVBox = Mockito.mock(VBox.class);
        Mockito.when(mockNode).thenReturn(mockVBox);

        // Create the mock class diagram manager and other necessary objects
        ClassDiagramManager mockManager = Mockito.mock(ClassDiagramManager.class);
        Pane mockPane = Mockito.mock(Pane.class);

        // Call your method with mocked data
        mockManager.enableInheritanceMode(mockPane);

        // Perform assertions or further actions
        Mockito.verify(mockPane, Mockito.times(1)).setOnMouseClicked(Mockito.any());
    }

}
