package com.example.umlscd.PresentationLayer.UseCaseDiagram;

import com.example.umlscd.BuisnessLayer.UseCaseDiagram.UseCaseDiagramManager;
import com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram.UseCaseDiagramDAO;
import com.example.umlscd.Models.UseCaseDiagram.Association;
import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class UseCaseDiagramTest extends ApplicationTest {

    private UseCaseDiagram useCaseDiagram;
    private Button btnAddActor;
    private Canvas canvas;
    private TreeView<String> objectExplorer;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/useCaseDiagram.fxml"));
        BorderPane root = loader.load();
        useCaseDiagram = loader.getController();

        stage.setTitle("Test Stage");
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();

        // Access the UI components
        btnAddActor = lookup("#btnAddActor").queryButton();
        canvas = lookup("#canvas").queryAs(Canvas.class);
        objectExplorer = lookup("#objectExplorer").queryAs(TreeView.class);
    }

    @Test
    void testAddActor() throws InterruptedException {
        // Simulate clicking the "Add Actor" button
        clickOn(btnAddActor);
        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify that the actor was added
        assertEquals(1, useCaseDiagram.objects.size(), "An actor should be added to the objects list.");
        assertEquals("Actor", useCaseDiagram.objects.get(0).getName(), "The actor's default name should be 'Actor'.");
    }

    @Test
    void testCanvasInteraction() throws InterruptedException {
        // Simulate a click on the canvas
        clickOn(canvas);
        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify that the canvas interaction is handled correctly
        assertNotNull(useCaseDiagram.gc, "GraphicsContext should not be null after interacting with the canvas.");
    }

    @Test
    void testTreeViewInteraction() throws InterruptedException {
        // Add a new TreeItem to the object explorer
        interact(() -> {
            TreeItem<String> newItem = new TreeItem<>("Test Item");
            useCaseDiagram.rootItem.getChildren().add(newItem);
        });

        // Select the new item
        TreeItem<String> testItem = useCaseDiagram.rootItem.getChildren().get(0);
        interact(() -> objectExplorer.getSelectionModel().select(testItem));

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify the selection
        assertEquals("Test Item", objectExplorer.getSelectionModel().getSelectedItem().getValue(),
                "The selected item's value should match the test item's value.");
    }

    @Test
    void testInitialize() {
        // Verify initial state
        assertNotNull(useCaseDiagram.objects, "Objects list should be initialized.");
        assertTrue(useCaseDiagram.objects.isEmpty(), "Objects list should be empty initially.");
        assertNotNull(useCaseDiagram.associations, "Associations list should be initialized.");
        assertTrue(useCaseDiagram.associations.isEmpty(), "Associations list should be empty initially.");

        // Verify TreeView initialization
        assertNotNull(useCaseDiagram.objectExplorer, "TreeView should be initialized.");
        assertEquals(useCaseDiagram.rootItem, objectExplorer.getRoot(), "TreeView root should match the initialized root item.");
    }

    @Test
    void testAddUseCase() throws InterruptedException {
        // Simulate entering a use case name and clicking the "Add Use Case" button
        interact(() -> useCaseDiagram.txtUseCaseName.setText("Test Use Case"));
        interact(() -> useCaseDiagram.addUseCase());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify the use case was added
        assertEquals(1, useCaseDiagram.objects.size(), "One use case should be added to the objects list.");
        assertEquals("Test Use Case", useCaseDiagram.objects.get(0).getName(), "The use case name should match the input.");

        // Verify the TreeView was updated
        assertEquals(1, useCaseDiagram.rootItem.getChildren().size(), "One TreeItem should be added to the root.");
        assertEquals("Test Use Case", useCaseDiagram.rootItem.getChildren().get(0).getValue(),
                "The TreeItem's value should match the use case name.");
    }

    @Test
    void testDrawSystemBoundary() throws InterruptedException {
        // Set a system boundary name and draw the boundary
        interact(() -> useCaseDiagram.systemBoundaryName = "Test Boundary");
        interact(() -> useCaseDiagram.drawSystemBoundary());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify the canvas has been drawn with the system boundary name
        assertNotNull(useCaseDiagram.gc, "GraphicsContext should not be null.");
        assertTrue(useCaseDiagram.canvas.getWidth() > 0 && useCaseDiagram.canvas.getHeight() > 0,
                "Canvas dimensions should be greater than 0.");
        // Additional checks can be done using pixel-by-pixel comparison or mocks
    }

    @Test
    void testOnSystemBoundaryNameChange() throws InterruptedException {
        // Simulate changing the system boundary name
        interact(() -> useCaseDiagram.txtSystemBoundaryName.setText("Updated Boundary"));
        interact(() -> useCaseDiagram.onSystemBoundaryNameChange());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify the system boundary name was updated
        assertEquals("Updated Boundary", useCaseDiagram.systemBoundaryName,
                "System boundary name should be updated to the new value.");

        // Verify the canvas was redrawn
        assertNotNull(useCaseDiagram.gc, "GraphicsContext should not be null.");
        // Additional validation of canvas redraw can be implemented based on requirements
    }

    @Test
    void testAddActorWithCustomName() throws InterruptedException {
        // Simulate entering a custom actor name and clicking the "Add Actor" button
        interact(() -> useCaseDiagram.txtActorName.setText("Custom Actor"));
        interact(() -> useCaseDiagram.addActor());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify the actor was added
        assertEquals(1, useCaseDiagram.objects.size(), "One actor should be added to the objects list.");
        assertEquals("Custom Actor", useCaseDiagram.objects.get(0).getName(), "The actor's name should match the input.");

        // Verify the TreeView was updated with the custom actor name
        assertEquals(1, useCaseDiagram.rootItem.getChildren().size(), "One TreeItem should be added to the root.");
        assertEquals("Custom Actor", useCaseDiagram.rootItem.getChildren().get(0).getValue(),
                "The TreeItem's value should match the actor name.");
    }

    @Test
    void testAddUseCaseWithCustomName() throws InterruptedException {
        // Simulate entering a custom use case name and clicking the "Add Use Case" button
        interact(() -> useCaseDiagram.txtUseCaseName.setText("Custom Use Case"));
        interact(() -> useCaseDiagram.addUseCase());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify the use case was added
        assertEquals(1, useCaseDiagram.objects.size(), "One use case should be added to the objects list.");
        assertEquals("Custom Use Case", useCaseDiagram.objects.get(0).getName(), "The use case name should match the input.");

        // Verify the TreeView was updated with the custom use case name
        assertEquals(1, useCaseDiagram.rootItem.getChildren().size(), "One TreeItem should be added to the root.");
        assertEquals("Custom Use Case", useCaseDiagram.rootItem.getChildren().get(0).getValue(),
                "The TreeItem's value should match the use case name.");
    }

    @Test
    void testOnSystemBoundaryNameChangeWithCustomName() throws InterruptedException {
        // Simulate changing the system boundary name to a custom name
        interact(() -> useCaseDiagram.txtSystemBoundaryName.setText("Custom System Boundary"));
        interact(() -> useCaseDiagram.onSystemBoundaryNameChange());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)


        // Verify the system boundary name was updated to the custom value
        assertEquals("Custom System Boundary", useCaseDiagram.systemBoundaryName,
                "System boundary name should be updated to the new custom value.");

        // Verify the canvas was redrawn with the new boundary name
        assertNotNull(useCaseDiagram.gc, "GraphicsContext should not be null.");
        // Additional validation of canvas redraw can be implemented based on requirements
    }


    @Test
    void testSelectObjectFromExplorer() throws InterruptedException {
        // Add a test object to the list and the explorer
        interact(() -> {
            useCaseDiagram.objects.add(new UseCaseDiagramObject("usecase", 250, 150, "Test Use Case"));
            TreeItem<String> useCaseItem = new TreeItem<>("Test Use Case");
            useCaseDiagram.rootItem.getChildren().add(useCaseItem);
        });

        // Simulate selecting an object from the explorer
        interact(() -> useCaseDiagram.selectObjectFromExplorer("Test Use Case"));

        // Delay to slow down the execution for better observation
        Thread.sleep(500);

        // Verify that the object is selected and the canvas is redrawn
        assertNotNull(useCaseDiagram.selectedObjectExplorer, "The selected object should be set.");
        assertEquals("Test Use Case", useCaseDiagram.selectedObjectExplorer.getName(), "The selected object's name should match.");
        // You can validate the canvas being redrawn further based on your implementation, e.g., checking specific canvas state.
    }

    @Test
    void testOnNameChangeWithActor() throws InterruptedException {
        // Add a test actor to the list and the explorer
        interact(() -> {
            useCaseDiagram.objects.add(new UseCaseDiagramObject("actor", 250, 150, "Test Actor"));
            TreeItem<String> actorItem = new TreeItem<>("Test Actor");
            useCaseDiagram.rootItem.getChildren().add(actorItem);
            useCaseDiagram.selectObjectFromExplorer("Test Actor");  // Select the actor for renaming
        });

        // Simulate changing the name of the actor
        interact(() -> useCaseDiagram.txtActorName.setText("Updated Actor"));
        interact(() -> useCaseDiagram.onNameChange());

        // Delay to slow down the execution for better observation
        Thread.sleep(500);

        // Verify that the actor's name has been updated
        assertEquals("Updated Actor", useCaseDiagram.selectedObjectExplorer.getName(), "The actor's name should be updated.");

        // Verify the TreeView has been updated with the new name
        assertEquals("Updated Actor", useCaseDiagram.rootItem.getChildren().get(0).getValue(),
                "The TreeView should reflect the updated actor name.");
    }

    @Test
    void testOnNameChangeWithUseCase() throws InterruptedException {
        // Add a test use case to the list and the explorer
        interact(() -> {
            useCaseDiagram.objects.add(new UseCaseDiagramObject("usecase", 250, 150, "Test Use Case"));
            TreeItem<String> useCaseItem = new TreeItem<>("Test Use Case");
            useCaseDiagram.rootItem.getChildren().add(useCaseItem);
            useCaseDiagram.selectObjectFromExplorer("Test Use Case");  // Select the use case for renaming
        });

        // Simulate changing the name of the use case
        interact(() -> useCaseDiagram.txtUseCaseName.setText("Updated Use Case"));
        interact(() -> useCaseDiagram.onNameChange());

        // Delay to slow down the execution for better observation
        Thread.sleep(500);

        // Verify that the use case's name has been updated
        assertEquals("Updated Use Case", useCaseDiagram.selectedObjectExplorer.getName(), "The use case's name should be updated.");

        // Verify the TreeView has been updated with the new name
        assertEquals("Updated Use Case", useCaseDiagram.rootItem.getChildren().get(0).getValue(),
                "The TreeView should reflect the updated use case name.");
    }

    /*@Test
    void testOnMouseDragged() throws InterruptedException {
        // Add a test object to the list and the explorer
        interact(() -> {
            useCaseDiagram.objects.add(new UseCaseDiagramObject("usecase", 250, 150, "Test Use Case"));
            TreeItem<String> useCaseItem = new TreeItem<>("Test Use Case");
            useCaseDiagram.rootItem.getChildren().add(useCaseItem);
            useCaseDiagram.selectObjectFromExplorer("Test Use Case");  // Select the use case for dragging
        });

        // Create a MouseEvent for dragging the object (we simulate dragging the object to 300, 200)
        MouseEvent dragEvent = new MouseEvent(
                MouseEvent.MOUSE_DRAGGED,   // Event type (MOUSE_DRAGGED)
                300,                       // X coordinate
                200,                       // Y coordinate
                300,                       // screenX
                200,                       // screenY
                MouseButton.PRIMARY,       // MouseButton
                1,                         // clickCount
                false,                     // Alt down
                false,                     // Shift down
                false,                     // Control down
                false,                     // Meta down
                false,                     // Primary button down
                false,                     // Middle button down
                false,                     // Secondary button down
                false                      // Synthetic event
        );

        // Simulate dragging the object
        interact(() -> useCaseDiagram.onMouseDragged(dragEvent));

        // Delay to slow down the execution for better observation
        Thread.sleep(500);

        // Verify that the object's position has been updated
        assertEquals(300, useCaseDiagram.selectedObjectExplorer.getX(), "The object's X position should be updated.");
        assertEquals(200, useCaseDiagram.selectedObjectExplorer.getY(), "The object's Y position should be updated.");
    }
    @Test
    void testOnMouseReleased() throws InterruptedException {
        // Add a test object to the list and the explorer
        interact(() -> {
            useCaseDiagram.objects.add(new UseCaseDiagramObject("usecase", 250, 150, "Test Use Case"));
            TreeItem<String> useCaseItem = new TreeItem<>("Test Use Case");
            useCaseDiagram.rootItem.getChildren().add(useCaseItem);
            useCaseDiagram.selectObjectFromExplorer("Test Use Case");  // Select the use case for releasing the drag
        });

        // Simulate mouse drag event
        javafx.scene.input.MouseEvent dragEvent = new MouseEvent(MouseEvent.MOUSE_DRAGGED, 300, 200, 0, 0, null, 0, false, false, false, false, false, false, null);
        interact(() -> useCaseDiagram.onMouseDragged(dragEvent));

        // Simulate mouse release event
        MouseEvent releaseEvent = new MouseEvent(MouseEvent.MOUSE_RELEASED, 300, 200, 0, 0, null, 0, false, false, false, false, false, false, null);
        interact(() -> useCaseDiagram.onMouseReleased(releaseEvent));

        // Delay to slow down the execution for better observation
        Thread.sleep(500);

        // Verify that the object is no longer being dragged
        assertNull(useCaseDiagram.objectBeingDragged, "The object being dragged should be null after release.");
    }

   /* @Test
    void testOnMouseReleased() throws InterruptedException {
        // Add a test object to the list and the explorer
        interact(() -> {
            useCaseDiagram.objects.add(new UseCaseDiagramObject("usecase", 250, 150, "Test Use Case"));
            TreeItem<String> useCaseItem = new TreeItem<>("Test Use Case");
            useCaseDiagram.rootItem.getChildren().add(useCaseItem);
            useCaseDiagram.selectObjectFromExplorer("Test Use Case");  // Select the use case for releasing the drag
        });

        // Simulate mouse drag event
        MouseEvent dragEvent = new MouseEvent(
                MouseEvent.MOUSE_DRAGGED,  // Event type
                300,                       // sceneX
                200,                       // sceneY
                300,                       // screenX
                200,                       // screenY
                MouseButton.PRIMARY,       // MouseButton
                1,                         // clickCount
                false,                     // shiftDown
                false,                     // controlDown
                false,                     // altDown
                false,                     // metaDown
                false,                     // primaryButtonDown
                false,                     // middleButtonDown
                false,                     // secondaryButtonDown
                null                        // eventTarget
        );
        interact(() -> useCaseDiagram.onMouseDragged(dragEvent));  // Call onMouseDragged with the simulated event

        // Simulate mouse release event
        MouseEvent releaseEvent = new MouseEvent(
                MouseEvent.MOUSE_RELEASED,  // Event type
                300,                        // sceneX
                200,                        // sceneY
                300,                        // screenX
                200,                        // screenY
                MouseButton.PRIMARY,        // MouseButton
                1,                          // clickCount
                false,                      // shiftDown
                false,                      // controlDown
                false,                      // altDown
                false,                      // metaDown
                false,                      // primaryButtonDown
                false,                      // middleButtonDown
                false,                      // secondaryButtonDown
                null                        // eventTarget
        );

        interact(() -> useCaseDiagram.onMouseReleased(releaseEvent));  // Call onMouseReleased with the simulated event

        // Delay to slow down the execution for better observation
        Thread.sleep(500);

        // Verify that the object is no longer being dragged
        assertNull(useCaseDiagram.objectBeingDragged, "The object being dragged should be null after release.");
    }
*/
    @Test
    void testDrawAssociation() {
        UseCaseDiagramObject obj1 = new UseCaseDiagramObject("actor", 100, 100, "Actor");
        UseCaseDiagramObject obj2 = new UseCaseDiagramObject("usecase", 200, 200, "UseCase");

        // Simulate drawing an association between the two objects
        useCaseDiagram.drawAssociation(obj1, obj2, "association");

        // Verify that the graphics context is being used (e.g., line is drawn)
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Add indirect checks (like line drawing) based on the behavior
        assertTrue(gc.getFill().equals(Color.BLACK), "The stroke color should be black for an association.");
        // You can add other indirect validations here (e.g., verifying line positions)
    }

    @Test
    void testDrawArrowhead() {
        // Define positions for testing
        double x1 = 100, y1 = 100, x2 = 200, y2 = 200;

        // Call the drawArrowhead method directly
        useCaseDiagram.drawArrowhead(x1, y1, x2, y2);

        // Verify that the arrowhead is drawn at the correct position
        // This test checks whether the arrowhead is drawn based on the provided coordinates
        GraphicsContext gc = canvas.getGraphicsContext2D();
        assertTrue(gc.getLineWidth() == 2, "Line width should be 2 for arrowhead.");
        assertTrue(gc.getFill().equals(Color.BLACK), "Arrowhead should be black.");
        // Additional validation can be done by indirectly checking the graphical state or using UI-based checks.
    }

    @Test
    void testDrawInclude() {
        UseCaseDiagramObject obj1 = new UseCaseDiagramObject("actor", 100, 100, "Actor");
        UseCaseDiagramObject obj2 = new UseCaseDiagramObject("usecase", 200, 200, "UseCase");

        // Simulate drawing an 'include' association between the two objects
        useCaseDiagram.drawAssociation(obj1, obj2, "include");

        // Verify that the include text is drawn
        GraphicsContext gc = canvas.getGraphicsContext2D();
        assertTrue(gc.getFill().equals(Color.BLACK), "The stroke color should be black for 'include'.");
        // Further checks can be added here, like verifying the drawing of text on the canvas
    }

    @Test
    void testDrawExtend() {
        UseCaseDiagramObject obj1 = new UseCaseDiagramObject("actor", 100, 100, "Actor");
        UseCaseDiagramObject obj2 = new UseCaseDiagramObject("usecase", 200, 200, "UseCase");

        // Simulate drawing an 'extend' association between the two objects
        useCaseDiagram.drawAssociation(obj1, obj2, "extend");

        // Verify that the extend text is drawn
        GraphicsContext gc = canvas.getGraphicsContext2D();
        assertTrue(gc.getFill().equals(Color.BLACK), "The stroke color should be black for 'extend'.");
        // Add further validation for '<<extend>>' text drawing
    }

    /*@Test
    void testRedrawCanvas() throws InterruptedException {
        // Set up a test object and association for drawing
        useCaseDiagram.objects.add(new UseCaseDiagramObject("actor", 250, 150, "Actor"));
        useCaseDiagram.objects.add(new UseCaseDiagramObject("usecase", 350, 250, "Test Use Case"));
        useCaseDiagram.associations.add(new Association(useCaseDiagram.objects.get(0), useCaseDiagram.objects.get(1), "association"));

        // Simulate selecting the actor object
        useCaseDiagram.selectedObjectExplorer = useCaseDiagram.objects.get(0);

        // Trigger the redrawCanvas method
        interact(() -> useCaseDiagram.redrawCanvas());

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // 500ms delay (adjust as needed)

        // Verify that the canvas has been cleared
        assertEquals(0, useCaseDiagram.canvas.getGraphicsContext2D().getPixelWriter().getWidth(),
                "Canvas should be cleared before redrawing.");

        // Verify that objects are drawn (this will depend on your actual drawing logic, this is a placeholder)
        assertTrue(useCaseDiagram.canvas.getGraphicsContext2D().getLineWidth() > 0, "Object should be drawn on the canvas.");

        // Verify that the selected object is highlighted (this can be done by checking if a red oval or rectangle is drawn)
        // Depending on the actual implementation, you may want to mock or check canvas state at specific points
    }*/

    @Test
    void testGoToHomePage() {
        // Simulate clicking the home button
        clickOn(useCaseDiagram.btnHome);

        // Verify that the FXML is loaded (i.e., the scene is set correctly)
        // Since FXMLLoader can throw an exception, let's catch it to ensure proper loading
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml"));
            assertNotNull(loader.getLocation(), "FXML location should not be null.");
        } catch (Exception e) {
            fail("FXML loading failed: " + e.getMessage());
        }

        // Verify the stage is switched correctly (you can check the scene or stage title)
        Stage stage = (Stage) useCaseDiagram.btnHome.getScene().getWindow();
       // assertEquals("Home Page", stage.getTitle(), "The stage should show the correct title after switching to home page.");
    }


    @Test
    void testSaveDiagram() throws InterruptedException {
        // Prepare data for saving
        interact(() -> {
            useCaseDiagram.objects.add(new UseCaseDiagramObject("actor", 250, 150, "Test Actor"));
            useCaseDiagram.systemBoundaryName = "Test Boundary";
        });

        // Simulate clicking the "Save Diagram" button
        clickOn("#btnSaveDiagram");

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // Adjust delay as needed

        // Verify that the diagram has been saved (you can check file creation or mock the saving behavior)
        // Here we just verify if the save method is invoked correctly (you can mock the file saving)
        // Ideally, you would mock the UseCaseDiagramDAO.saveDiagram method in this case
        assertTrue(new File("path_to_saved_file.ser").exists(), "The diagram file should be saved.");
    }

    @Test
    void testLoadDiagram() throws InterruptedException {
        // Simulate the saving of a diagram first to ensure there's something to load
        String testFilePath = "path_to_saved_file.ser";  // Adjust this to the mock location or temporary file

        interact(() -> {
            useCaseDiagram.objects.add(new UseCaseDiagramObject("actor", 250, 150, "Test Actor"));
            useCaseDiagram.systemBoundaryName = "Test Boundary";
            UseCaseDiagramManager manager = new UseCaseDiagramManager(useCaseDiagram.objects, useCaseDiagram.associations, useCaseDiagram.systemBoundaryName);
            UseCaseDiagramDAO.saveDiagram(manager, testFilePath);
        });

        // Simulate clicking the "Load Diagram" button
        clickOn("#btnLoadDiagram");

        // Delay to slow down the execution for better observation
        Thread.sleep(500); // Adjust delay as needed

        // Verify that the diagram has been loaded correctly (check if the data was restored)
        // You would ideally mock the file loading in the test and check the state of useCaseDiagram's objects
        assertFalse(useCaseDiagram.objects.isEmpty(), "The objects list should not be empty after loading.");
        assertEquals("Test Actor", useCaseDiagram.objects.get(0).getName(), "The actor name should match the loaded data.");
        assertEquals("Test Boundary", useCaseDiagram.systemBoundaryName, "The system boundary name should match the loaded data.");
    }


    @Test
    void testSaveDiagramToJson() throws InterruptedException {
        // Simulate adding an actor to the diagram
        interact(() -> {
            useCaseDiagram.addActor(); // Add actor
            useCaseDiagram.systemBoundaryName = "Test Boundary"; // Set system boundary name
        });

        // Simulate saving the diagram to JSON
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/testDiagram.json");
        interact(() -> {
            // Set the file chooser to simulate saving
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("java.io.tmpdir")));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            fileChooser.showSaveDialog(new Stage()); // This triggers the save action
        });

        Thread.sleep(500); // Add delay to ensure file operations complete

        // Verify the file was created
        Path path = Paths.get(tempFile.getAbsolutePath());
        assertTrue(Files.exists(path), "The JSON file should exist after saving.");

        // Clean up
        tempFile.delete();
    }



    @Test
    void testLoadDiagramFromJson() throws InterruptedException {
        // Create a temporary JSON file
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/testDiagram.json");

        // Simulate saving a diagram to the file
        interact(() -> {
            useCaseDiagram.addActor(); // Add actor
            useCaseDiagram.systemBoundaryName = "Test Boundary"; // Set system boundary name
            useCaseDiagram.saveDiagramToJson(); // Call the save method
        });

        Thread.sleep(500); // Wait for save operation to complete

        // Now load the diagram from the file
        interact(() -> {
            useCaseDiagram.loadDiagramFromJson(); // Call the load method
        });

        Thread.sleep(500); // Wait for load operation to complete

        // Verify that the diagram was loaded successfully
        assertEquals(1, useCaseDiagram.objects.size(), "The objects list should contain one object.");
        assertEquals("Actor", useCaseDiagram.objects.get(0).getName(), "The loaded object should be an Actor.");
        assertEquals("Test Boundary", useCaseDiagram.systemBoundaryName, "The system boundary name should be loaded correctly.");

        // Clean up
        tempFile.delete();
    }





}
