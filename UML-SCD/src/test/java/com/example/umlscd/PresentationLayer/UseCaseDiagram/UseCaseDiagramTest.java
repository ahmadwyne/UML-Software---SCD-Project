package com.example.umlscd.PresentationLayer.UseCaseDiagram;

import com.example.umlscd.Models.UseCaseDiagram.Association;
import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import com.example.umlscd.BuisnessLayer.UseCaseDiagram.UseCaseDiagramManager;
import com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram.UseCaseDiagramDAO;
import com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram.UseCaseDiagramSerializer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;

public class UseCaseDiagramTest extends ApplicationTest {

    private UseCaseDiagram controller;
    private FileChooser mockFileChooser;
    private File mockSaveFile;
    private File mockLoadFile;
    private File mockExportFile;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize Mockito mocks
        mockFileChooser = mock(FileChooser.class);
        mockSaveFile = File.createTempFile("diagram", ".ser");
        mockLoadFile = File.createTempFile("diagram", ".ser");
        mockExportFile = File.createTempFile("diagram", ".png");

        // Ensure temporary files are deleted on exit
        mockSaveFile.deleteOnExit();
        mockLoadFile.deleteOnExit();
        mockExportFile.deleteOnExit();

        // Define default behavior for FileChooser
        when(mockFileChooser.showSaveDialog(any(Stage.class))).thenReturn(mockSaveFile);
        when(mockFileChooser.showOpenDialog(any(Stage.class))).thenReturn(mockLoadFile);
        // Specific stubbing for tests requiring different return values should be done within those tests.
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clear the TreeView and objects list after each test to prevent state leakage
        Platform.runLater(() -> {
            if (controller.getObjectExplorer().getRoot() != null) {
                controller.getObjectExplorer().getRoot().getChildren().clear();
            }
            controller.getObjects().clear();
            controller.getAssociations().clear();
            controller.systemBoundaryName = "System";
            // Clear the canvas
            controller.redrawCanvas();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML without modifying the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/UseCaseDiagram.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        // Inject the mocked FileChooser via reflection
        try {
            Field chooserField = UseCaseDiagram.class.getDeclaredField("chooser"); // Replace "chooser" with actual field name
            chooserField.setAccessible(true);
            chooserField.set(controller, mockFileChooser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to inject mock FileChooser into controller.", e);
        }

        // Set up the scene and show the stage
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

        // Wait for UI to initialize
        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * Helper method to check if TreeView contains a specific text
     */
    private boolean treeViewContainsText(String text) {
        javafx.scene.control.TreeView<String> treeView = lookup("#objectExplorer").query();
        return traverseTree(treeView.getRoot(), text);
    }

    /**
     * Recursive method to traverse TreeItems
     */
    private boolean traverseTree(javafx.scene.control.TreeItem<String> item, String text) {
        if (item.getValue() != null && item.getValue().equals(text)) {
            return true;
        }
        for (javafx.scene.control.TreeItem<String> child : item.getChildren()) {
            if (traverseTree(child, text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to select a TreeView item programmatically
     */
    private void selectTreeViewItem(String text) {
        javafx.scene.control.TreeView<String> treeView = lookup("#objectExplorer").query();
        javafx.scene.control.TreeItem<String> targetItem = findTreeItem(treeView.getRoot(), text);

        if (targetItem != null) {
            // Programmatically select the item on the FX thread
            Platform.runLater(() -> {
                treeView.scrollTo(treeView.getRow(targetItem));
                treeView.getSelectionModel().select(targetItem);
            });
            WaitForAsyncUtils.waitForFxEvents();
        } else {
            fail("TreeView does not contain item: " + text);
        }
    }

    /**
     * Recursive method to find a TreeItem by text
     */
    private javafx.scene.control.TreeItem<String> findTreeItem(javafx.scene.control.TreeItem<String> root, String text) {
        if (root == null) return null;
        if (root.getValue() != null && root.getValue().equals(text)) return root;
        for (javafx.scene.control.TreeItem<String> child : root.getChildren()) {
            javafx.scene.control.TreeItem<String> found = findTreeItem(child, text);
            if (found != null) return found;
        }
        return null;
    }

    /**
     * Test that the Add Actor button exists and has the correct label
     */
    @Test
    public void testAddActorButtonExists() {
        // Verify that the Add Actor button has the correct text
        verifyThat("#btnAddActor", LabeledMatchers.hasText("Add Actor"));
    }

    /**
     * Test adding an actor with a specified name
     */
    @Test
    public void testAddActorWithName() {
        // Generate a unique actor name
        String uniqueActorName = "Admin_" + UUID.randomUUID();

        // Enter the actor name in the txtActorName TextField
        clickOn("#txtActorName").write(uniqueActorName);
        // Click on the Add Actor button
        clickOn("#btnAddActor");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the actor is added to the controller's objects list
        assertTrue(controller.getObjects().stream()
                        .anyMatch(obj -> obj.getName().equals(uniqueActorName) && obj.getType().equals("actor")),
                "Actor '" + uniqueActorName + "' should be present in the objects list");

        // Additionally, verify that a node with the unique text exists in the TreeView
        assertTrue(treeViewContainsText(uniqueActorName), "TreeView should contain '" + uniqueActorName + "'");
    }

    /**
     * Test adding an actor without specifying a name (should use default)
     */
    @Test
    public void testAddActorWithoutName() {
        // Click on the Add Actor button without entering a name
        clickOn("#btnAddActor");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the actor is added with the default name "Actor"
        assertTrue(controller.getObjects().stream()
                        .anyMatch(obj -> obj.getName().equals("Actor") && obj.getType().equals("actor")),
                "Default actor 'Actor' should be present in the objects list");

        // Additionally, verify that a node with text "Actor" exists in the TreeView
        assertTrue(treeViewContainsText("Actor"), "TreeView should contain 'Actor'");
    }

    /**
     * Test adding a use case with a specified name
     */
    @Test
    public void testAddUseCaseWithName() {
        // Generate a unique use case name
        String uniqueUseCaseName = "ApproveRequest_" + UUID.randomUUID();

        // Enter the use case name in the txtUseCaseName TextField
        clickOn("#txtUseCaseName").write(uniqueUseCaseName);
        // Click on the Add Use Case button
        clickOn("#btnAddUseCase");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the use case is added to the controller's objects list
        assertTrue(controller.getObjects().stream()
                        .anyMatch(obj -> obj.getName().equals(uniqueUseCaseName) && obj.getType().equals("usecase")),
                "Use Case '" + uniqueUseCaseName + "' should be present in the objects list");

        // Additionally, verify that a node with the unique text exists in the TreeView
        assertTrue(treeViewContainsText(uniqueUseCaseName), "TreeView should contain '" + uniqueUseCaseName + "'");
    }

    /**
     * Test selecting an object from the explorer
     */
    @Test
    public void testSelectObjectFromExplorer() {
        // Add an actor first
        String uniqueActorName = "User_" + UUID.randomUUID();
        clickOn("#txtActorName").write(uniqueActorName);
        clickOn("#btnAddActor");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that a node with the unique text exists in the TreeView
        assertTrue(treeViewContainsText(uniqueActorName), "TreeView should contain '" + uniqueActorName + "'");

        // Select the actor from the TreeView by clicking on its text
        selectTreeViewItem(uniqueActorName);

        // Verify that the controller has the correct selected object
        assertNotNull(controller.getSelectedObjectExplorer(), "Selected object should not be null");
        assertEquals(uniqueActorName, controller.getSelectedObjectExplorer().getName(),
                "Selected object should be '" + uniqueActorName + "'");
    }

    /**
     * Test editing an object's name
     */
    @Test
    public void testEditObjectName() {
        // Add an actor
        String originalActorName = "Guest_" + UUID.randomUUID();
        clickOn("#txtActorName").write(originalActorName);
        clickOn("#btnAddActor");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that a node with the unique text exists in the TreeView
        assertTrue(treeViewContainsText(originalActorName), "TreeView should contain '" + originalActorName + "'");

        // Select the object from the TreeView
        selectTreeViewItem(originalActorName);

        // Wait for selection to register
        WaitForAsyncUtils.waitForFxEvents();

        // Click the Edit button to activate edit mode
        clickOn("#btnEdit");

        // Enter a new name in the appropriate TextField
        String newActorName = "Visitor_" + UUID.randomUUID();
        clickOn("#txtActorName").eraseText(originalActorName.length()).write(newActorName);

        // Optionally, press Enter if required by the application
        // push(KeyCode.ENTER);

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the controller's objects list is updated
        assertTrue(controller.getObjects().stream()
                        .anyMatch(obj -> obj.getName().equals(newActorName) && obj.getType().equals("actor")),
                "Actor '" + newActorName + "' should be present in the objects list");
        assertFalse(controller.getObjects().stream()
                        .anyMatch(obj -> obj.getName().equals(originalActorName) && obj.getType().equals("actor")),
                "Actor '" + originalActorName + "' should no longer be present in the objects list");

        // Additionally, verify that the TreeView is updated
        assertTrue(treeViewContainsText(newActorName), "TreeView should contain '" + newActorName + "'");
        assertFalse(treeViewContainsText(originalActorName), "TreeView should not contain '" + originalActorName + "'");
    }

    /**
     * Test deleting an object via the delete button
     */
    @Test
    public void testDeleteObject() {
        // Add a use case
        String useCaseName = "Developer_" + UUID.randomUUID();
        clickOn("#txtUseCaseName").write(useCaseName);
        clickOn("#btnAddUseCase");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that a node with the unique text exists in the TreeView
        assertTrue(treeViewContainsText(useCaseName), "TreeView should contain '" + useCaseName + "'");

        // Select the use case from the TreeView
        selectTreeViewItem(useCaseName);

        // Verify that the controller has the correct selected object
        assertNotNull(controller.getSelectedObjectExplorer(), "Selected object should not be null");
        assertEquals(useCaseName, controller.getSelectedObjectExplorer().getName(),
                "Selected object should be '" + useCaseName + "'");

        // Click the Delete button to activate delete mode
        clickOn("#btnDelete");

        // Perform drag-and-drop on the canvas to delete (assuming deletion is done via drag)
        // Note: Since graphical elements are drawn on a Canvas, we simulate dragging at specific coordinates
        // You may need to adjust coordinates based on where the use case is drawn
        // For this test, assuming default position (250, 150)
        moveTo("#canvas").press(MouseButton.PRIMARY)
                .moveBy(250, 150) // Move to (250, 150) relative to the canvas's top-left corner
                .release(MouseButton.PRIMARY);

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the use case is removed from the controller's objects list
        assertFalse(controller.getObjects().stream()
                        .anyMatch(obj -> obj.getName().equals(useCaseName) && obj.getType().equals("usecase")),
                "Use case '" + useCaseName + "' should be removed from the objects list");

        // Verify that a node with the unique text no longer exists in the TreeView
        assertFalse(treeViewContainsText(useCaseName), "TreeView should not contain '" + useCaseName + "'");
    }

    /**
     * Test dragging an object
     */
    @Test
    public void testDragObject() {
        // Add an actor
        String actorName = "Tester_" + UUID.randomUUID();
        clickOn("#txtActorName").write(actorName);
        clickOn("#btnAddActor");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Select the actor from the TreeView
        assertTrue(treeViewContainsText(actorName), "TreeView should contain '" + actorName + "'");
        selectTreeViewItem(actorName);

        // Wait for selection to register
        WaitForAsyncUtils.waitForFxEvents();

        // Click the Drag button to activate drag mode
        clickOn("#btnDrag");

        // Perform drag-and-drop on the canvas to move the actor
        // Assuming default position (150, 100)
        moveTo("#canvas").press(MouseButton.PRIMARY)
                .moveBy(150, 100) // Move to (150, 100) relative to the canvas's top-left corner
                .moveBy(150, 100) // Move to (300, 200) relative to the current position
                .release(MouseButton.PRIMARY);

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the actor's position is updated in the controller's state
        UseCaseDiagramObject draggedActor = controller.getObjects().stream()
                .filter(obj -> obj.getName().equals(actorName) && obj.getType().equals("actor"))
                .findFirst()
                .orElse(null);

        assertNotNull(draggedActor, "Dragged actor should exist");
        assertEquals(300, draggedActor.getX(), 0.1, "Actor's X position should be updated to 300");
        assertEquals(200, draggedActor.getY(), 0.1, "Actor's Y position should be updated to 200");
    }

    /**
     * Test saving the diagram using the mock FileChooser and verifying the DAO call
     */
    @Test
    public void testSaveDiagramStaticMethod() {
        // Arrange: Stub the FileChooser to return mockSaveFile
        when(mockFileChooser.showSaveDialog(any(Stage.class))).thenReturn(mockSaveFile);

        // Use MockedStatic to mock and verify static method calls
        try (MockedStatic<UseCaseDiagramDAO> mockedDAO = Mockito.mockStatic(UseCaseDiagramDAO.class)) {

            // Add an actor and a use case
            String actorName = "Manager_" + UUID.randomUUID();
            clickOn("#txtActorName").write(actorName);
            clickOn("#btnAddActor");

            String useCaseName = "ApproveRequest_" + UUID.randomUUID();
            clickOn("#txtUseCaseName").write(useCaseName);
            clickOn("#btnAddUseCase");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Act: Click the Save Diagram button
            clickOn("#btnSaveDiagram");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Assert: Verify that the FileChooser was invoked
            verify(mockFileChooser, times(1)).showSaveDialog(any(Stage.class));

            // Verify that saveDiagram was called with correct parameters
            mockedDAO.verify(() -> UseCaseDiagramDAO.saveDiagram(any(UseCaseDiagramManager.class), eq(mockSaveFile.getAbsolutePath())), times(1));

            // Optionally, capture the arguments if needed
            ArgumentCaptor<UseCaseDiagramManager> managerCaptor = ArgumentCaptor.forClass(UseCaseDiagramManager.class);
            ArgumentCaptor<String> filePathCaptor = ArgumentCaptor.forClass(String.class);
            mockedDAO.verify(() -> UseCaseDiagramDAO.saveDiagram(managerCaptor.capture(), filePathCaptor.capture()), times(1));

            // Further assertions on captured arguments if necessary
            assertEquals(mockSaveFile.getAbsolutePath(), filePathCaptor.getValue(), "Diagram should be saved to the correct file path");
            assertNotNull(managerCaptor.getValue(), "UseCaseDiagramManager should not be null");
        }
    }

    /**
     * Test loading the diagram using the mock FileChooser and verifying the DAO call
     */
    @Test
    public void testLoadDiagramStaticMethod() throws IOException {
        // Arrange: Stub the FileChooser to return mockLoadFile
        when(mockFileChooser.showOpenDialog(any(Stage.class))).thenReturn(mockLoadFile);

        // Use MockedStatic to mock and verify static method calls
        try (MockedStatic<UseCaseDiagramDAO> mockedDAO = Mockito.mockStatic(UseCaseDiagramDAO.class)) {

            // Optionally, define behavior for loadDiagram if it returns a specific manager
            UseCaseDiagramManager mockManager = new UseCaseDiagramManager();
            // Add mock objects and associations to mockManager as needed
            mockedDAO.when(() -> UseCaseDiagramDAO.loadDiagram(eq(mockLoadFile.getAbsolutePath()))).thenReturn(mockManager);

            // Act: Click the Load Diagram button
            clickOn("#btnLoadDiagram");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Assert: Verify that the FileChooser was invoked
            verify(mockFileChooser, times(1)).showOpenDialog(any(Stage.class));

            // Verify that loadDiagram was called with correct parameters
            mockedDAO.verify(() -> UseCaseDiagramDAO.loadDiagram(eq(mockLoadFile.getAbsolutePath())), times(1));

            // Verify that the controller's state was updated with the loaded manager
            assertEquals(mockManager, controller.getUseCaseDiagramManager(), "Diagram manager should be updated with loaded data");
        }
    }

    /**
     * Test exporting the diagram to an image using the mock FileChooser and verifying the export
     */
    @Test
    public void testExportDiagramToImage() throws IOException {
        // Arrange: Stub the FileChooser to return mockExportFile
        when(mockFileChooser.showSaveDialog(any(Stage.class))).thenReturn(mockExportFile);

        // Use MockedStatic to mock any static export methods if they exist
        try (MockedStatic<UseCaseDiagramManager> mockedExport = Mockito.mockStatic(UseCaseDiagramManager.class)) {

            // Optionally, define behavior for exportDiagramToImage if it's a static method
            // For example:
            // mockedExport.when(() -> UseCaseDiagramManager.exportDiagramToImage(any(Canvas.class), eq(mockExportFile.getAbsolutePath()))).thenReturn(true);

            // Add some objects
            String actorName = "CEO_" + UUID.randomUUID();
            clickOn("#txtActorName").write(actorName);
            clickOn("#btnAddActor");

            String useCaseName = "Strategize_" + UUID.randomUUID();
            clickOn("#txtUseCaseName").write(useCaseName);
            clickOn("#btnAddUseCase");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Act: Click the Export Image button
            clickOn("#btnExportImage");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Assert: Verify that the FileChooser was invoked
            verify(mockFileChooser, times(1)).showSaveDialog(any(Stage.class));

            // Optionally, verify that the export logic was executed
            // For example, if exportDiagramToImage is a static method:
            // mockedExport.verify(() -> UseCaseDiagramManager.exportDiagramToImage(any(Canvas.class), eq(mockExportFile.getAbsolutePath())), times(1));

            // Additionally, verify that the image file was created
            assertTrue(mockExportFile.exists(), "Image file should exist after exporting");
            assertTrue(mockExportFile.length() > 0, "Image file should not be empty");
        }
    }

    /**
     * Test editing the system boundary name
     */
    @Test
    public void testEditSystemBoundaryName() {
        // Enter a new system boundary name
        String newBoundaryName = "EnterpriseSystem_" + UUID.randomUUID();
        clickOn("#txtSystemBoundaryName").write(newBoundaryName);

        // Click the Enter System Boundary button
        clickOn("#btnEnterSystemBoundary");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the system boundary name is updated in the controller's state
        assertEquals(newBoundaryName, controller.getSystemBoundaryName(),
                "System boundary name should be updated to '" + newBoundaryName + "'");

        // Optionally, verify that the canvas has the new system boundary name
        // This requires accessing the canvas's GraphicsContext or using visual assertions
    }

    /**
     * Test adding an association between two objects
     */
    @Test
    public void testAddAssociation() {
        // Generate unique names using UUID
        String actor1Name = "Actor1_" + UUID.randomUUID();
        String useCase1Name = "UseCase1_" + UUID.randomUUID();

        // Enter names and add actors and use cases
        clickOn("#txtActorName").write(actor1Name);
        clickOn("#btnAddActor");

        clickOn("#txtUseCaseName").write(useCase1Name);
        clickOn("#btnAddUseCase");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Activate association mode (assuming there's an Association toggle button)
        clickOn("#btnAssociation");

        // Select the first actor's head circle
        // Assuming that head circles have IDs like "actorHeadCircle_Admin_UUID"
        // Modify this based on how you implement graphical element identifiers
        // For this example, we'll assume actors have corresponding head circles with unique fx:id
        String actorHeadCircleId = "actorHeadCircle_" + actor1Name.replaceAll("\\s+", "_");
        clickOn("#" + actorHeadCircleId);

        // Select the second use case from the TreeView
        selectTreeViewItem(useCase1Name);

        // Wait for association to be added
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the association is added in the controller's state
        assertTrue(controller.getAssociations().stream()
                        .anyMatch(assoc -> assoc.getObj1().getName().equals(actor1Name) &&
                                assoc.getObj2().getName().equals(useCase1Name) &&
                                assoc.getType().equals("association")),
                "Association between '" + actor1Name + "' and '" + useCase1Name + "' should exist");

        // Optionally, verify that the association is drawn on the canvas
        // This might involve checking the canvas's pixels or verifying graphical nodes, which can be complex
    }

    /**
     * Test adding an include association between two objects
     */
    @Test
    public void testAddIncludeAssociation() {
        // Generate unique names using UUID
        String actorName = "Actor_" + UUID.randomUUID();
        String useCaseInclude = "IncludeUseCase_" + UUID.randomUUID();
        String useCaseBase = "BaseUseCase_" + UUID.randomUUID();

        // Add an actor
        clickOn("#txtActorName").write(actorName);
        clickOn("#btnAddActor");

        // Add include use case
        clickOn("#txtUseCaseName").write(useCaseInclude);
        clickOn("#btnAddUseCase");

        // Add base use case
        clickOn("#txtUseCaseName").write(useCaseBase);
        clickOn("#btnAddUseCase");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Activate include association mode
        clickOn("#btnInclude");

        // Select the base use case's TreeView item
        selectTreeViewItem(useCaseBase);

        // Click on the include use case's TreeView item to create the association
        selectTreeViewItem(useCaseInclude);

        // Wait for association to be added
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the association is added in the controller's state
        assertTrue(controller.getAssociations().stream()
                        .anyMatch(assoc -> assoc.getObj1().getName().equals(useCaseBase) &&
                                assoc.getObj2().getName().equals(useCaseInclude) &&
                                assoc.getType().equals("include")),
                "Include association between '" + useCaseBase + "' and '" + useCaseInclude + "' should exist");
    }

    /**
     * Test adding an extend association between two objects
     */
    @Test
    public void testAddExtendAssociation() {
        // Generate unique names using UUID
        String actorName = "Actor_" + UUID.randomUUID();
        String useCaseExtend = "ExtendUseCase_" + UUID.randomUUID();
        String useCaseBase = "BaseUseCase_" + UUID.randomUUID();

        // Add an actor
        clickOn("#txtActorName").write(actorName);
        clickOn("#btnAddActor");

        // Add extend use case
        clickOn("#txtUseCaseName").write(useCaseExtend);
        clickOn("#btnAddUseCase");

        // Add base use case
        clickOn("#txtUseCaseName").write(useCaseBase);
        clickOn("#btnAddUseCase");

        // Wait for UI to update
        WaitForAsyncUtils.waitForFxEvents();

        // Activate extend association mode
        clickOn("#btnExtend");

        // Select the base use case's TreeView item
        selectTreeViewItem(useCaseBase);

        // Click on the extend use case's TreeView item to create the association
        selectTreeViewItem(useCaseExtend);

        // Wait for association to be added
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the association is added in the controller's state
        assertTrue(controller.getAssociations().stream()
                        .anyMatch(assoc -> assoc.getObj1().getName().equals(useCaseBase) &&
                                assoc.getObj2().getName().equals(useCaseExtend) &&
                                assoc.getType().equals("extend")),
                "Extend association between '" + useCaseBase + "' and '" + useCaseExtend + "' should exist");
    }

    /**
     * Test saving the diagram to JSON using the mock FileChooser
     */
    @Test
    public void testSaveDiagramToJson() throws IOException {
        // Arrange: Stub the FileChooser to return a mock JSON file
        File mockJsonFile = File.createTempFile("diagram", ".json");
        mockJsonFile.deleteOnExit();
        when(mockFileChooser.showSaveDialog(any(Stage.class))).thenReturn(mockJsonFile);

        // Use MockedStatic to mock and verify static method calls
        try (MockedStatic<UseCaseDiagramSerializer> mockedSerializer = Mockito.mockStatic(UseCaseDiagramSerializer.class)) {

            // Add an actor and a use case
            String actorName = "Manager_" + UUID.randomUUID();
            clickOn("#txtActorName").write(actorName);
            clickOn("#btnAddActor");

            String useCaseName = "ApproveRequest_" + UUID.randomUUID();
            clickOn("#txtUseCaseName").write(useCaseName);
            clickOn("#btnAddUseCase");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Act: Click the Save JSON button
            clickOn("#btnSaveJson");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Assert: Verify that the FileChooser was invoked
            verify(mockFileChooser, times(1)).showSaveDialog(any(Stage.class));

            // Verify that saveDiagram was called with correct parameters
            mockedSerializer.verify(() -> UseCaseDiagramSerializer.saveDiagram(any(UseCaseDiagramManager.class), eq(mockJsonFile.getAbsolutePath())), times(1));

            // Optionally, capture the arguments if needed
            ArgumentCaptor<UseCaseDiagramManager> managerCaptor = ArgumentCaptor.forClass(UseCaseDiagramManager.class);
            ArgumentCaptor<String> filePathCaptor = ArgumentCaptor.forClass(String.class);
            mockedSerializer.verify(() -> UseCaseDiagramSerializer.saveDiagram(managerCaptor.capture(), filePathCaptor.capture()), times(1));

            // Further assertions on captured arguments if necessary
            assertEquals(mockJsonFile.getAbsolutePath(), filePathCaptor.getValue(), "Diagram should be saved to the correct JSON file path");
            assertNotNull(managerCaptor.getValue(), "UseCaseDiagramManager should not be null");
        }
    }

    /**
     * Test loading the diagram from JSON using the mock FileChooser
     */
    @Test
    public void testLoadDiagramFromJson() throws IOException {
        // Arrange: Stub the FileChooser to return a mock JSON file
        File mockJsonFile = File.createTempFile("diagram", ".json");
        mockJsonFile.deleteOnExit();

        // Use MockedStatic to mock and verify static method calls
        try (MockedStatic<UseCaseDiagramSerializer> mockedSerializer = Mockito.mockStatic(UseCaseDiagramSerializer.class)) {

            // Prepare a UseCaseDiagramManager and stub the loadDiagram method
            UseCaseDiagramManager mockManager = new UseCaseDiagramManager();
            UseCaseDiagramObject actor = new UseCaseDiagramObject("actor", 150, 100, "Tester");
            UseCaseDiagramObject useCase = new UseCaseDiagramObject("usecase", 250, 150, "TestUseCase");
            mockManager.addObject(actor);
            mockManager.addObject(useCase);
            mockManager.setSystemBoundaryName("TestSystem");

            mockedSerializer.when(() -> UseCaseDiagramSerializer.loadDiagram(eq(mockJsonFile.getAbsolutePath()))).thenReturn(mockManager);

            // Stub the FileChooser to return the mock JSON file
            when(mockFileChooser.showOpenDialog(any(Stage.class))).thenReturn(mockJsonFile);

            // Act: Click the Load JSON button
            clickOn("#btnLoadJson");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Assert: Verify that the FileChooser was invoked
            verify(mockFileChooser, times(1)).showOpenDialog(any(Stage.class));

            // Verify that loadDiagram was called with correct parameters
            mockedSerializer.verify(() -> UseCaseDiagramSerializer.loadDiagram(eq(mockJsonFile.getAbsolutePath())), times(1));

            // Verify that the controller's state is updated with loadedManager
            assertEquals(mockManager, controller.getUseCaseDiagramManager(), "Diagram manager should be updated with loaded data");

            // Additionally, verify that UI components reflect the loaded data
            assertTrue(treeViewContainsText("Tester"), "TreeView should contain 'Tester'");
            assertTrue(treeViewContainsText("TestUseCase"), "TreeView should contain 'TestUseCase'");
            assertEquals("TestSystem", controller.getSystemBoundaryName(), "System boundary name should be 'TestSystem'");
        }
    }

    /**
     * Test exporting the diagram to an image using the mock FileChooser
     */
    /*@Test
    public void testExportDiagramToImage() throws IOException {
        // Arrange: Stub the FileChooser to return mockExportFile
        File mockImageFile = File.createTempFile("diagram", ".png");
        mockImageFile.deleteOnExit();
        when(mockFileChooser.showSaveDialog(any(Stage.class))).thenReturn(mockImageFile);

        // Use MockedStatic to mock and verify static method calls if any
        try (MockedStatic<UseCaseDiagramManager> mockedExport = Mockito.mockStatic(UseCaseDiagramManager.class)) {

            // Optionally, define behavior for exportDiagramToImage if it's a static method
            // For example:
            // mockedExport.when(() -> UseCaseDiagramManager.exportDiagramToImage(any(Canvas.class), eq(mockExportFile.getAbsolutePath()))).thenReturn(true);

            // Add some objects
            String actorName = "CEO_" + UUID.randomUUID();
            clickOn("#txtActorName").write(actorName);
            clickOn("#btnAddActor");

            String useCaseName = "Strategize_" + UUID.randomUUID();
            clickOn("#txtUseCaseName").write(useCaseName);
            clickOn("#btnAddUseCase");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Act: Click the Export Image button
            clickOn("#btnExportImage");

            // Wait for UI to update
            WaitForAsyncUtils.waitForFxEvents();

            // Assert: Verify that the FileChooser was invoked
            verify(mockFileChooser, times(1)).showSaveDialog(any(Stage.class));

            // Verify that the export logic was executed if applicable
            // For example, if there is a static method exportDiagramToImage, you can verify it
            // mockedExport.verify(() -> UseCaseDiagramManager.exportDiagramToImage(any(Canvas.class), eq(mockExportFile.getAbsolutePath())), times(1));

            // Additionally, verify that the image file was created
            assertTrue(mockImageFile.exists(), "Image file should exist after exporting");
            assertTrue(mockImageFile.length() > 0, "Image file should not be empty");
        }
    }*/

    // ... [Other test methods remain unchanged]

}
