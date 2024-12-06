package com.example.umlscd.PresentationLayer.UseCaseDiagram;

import com.example.umlscd.Models.UseCaseDiagram.Association;
import com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram.UseCaseDiagramDAO;
import com.example.umlscd.BuisnessLayer.UseCaseDiagram.UseCaseDiagramManager;
import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram.UseCaseDiagramSerializer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * <h1>Use Case Diagram Controller</h1>
 *
 * <p>The {@code UseCaseDiagram} class serves as the controller for the Use Case Diagram editor within the UML Editor application.
 * It manages the creation, manipulation, and visualization of use case diagrams, including actors, use cases, and their relationships
 * such as associations, includes, and extends. The class handles user interactions, rendering of diagram elements on the canvas,
 * and provides functionalities to save, load, and export diagrams in both serialized and JSON formats.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class UseCaseDiagram {

    @FXML
    private Button btnHome;

    @FXML
    private Button btnAddActor;

    @FXML
    private Button btnAddUseCase;

    @FXML
    private ToggleButton btnAssociation;

    @FXML
    private ToggleButton btnInclude;

    @FXML
    private ToggleButton btnExtend;

    @FXML
    private ToggleButton btnDrag;

    @FXML
    private ToggleButton btnDelete;

    @FXML
    private ToggleButton btnEdit;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField txtActorName;

    @FXML
    private TextField txtUseCaseName;

    @FXML
    private TextField txtSystemBoundaryName;  // New TextField for the system boundary name
    @FXML
    private Button btnEnterSystemBoundary;

    @FXML
    private TreeView<String> objectExplorer; // TreeView for the object explorer

    private TreeItem<String> rootItem; // Root item for the explorer

    @FXML
    private Button btnSaveDiagram, btnLoadDiagram, btnSaveJson, btnLoadJson, btnExportImage;

    private ArrayList<UseCaseDiagramObject> objects;
    private ArrayList<Association> associations;
    private GraphicsContext gc;

    private UseCaseDiagramObject selectedObject1;
    private UseCaseDiagramObject selectedObject2;
    private UseCaseDiagramObject selectedObjectExplorer;
    private String associationType;
    private boolean isInDragMode = false;
    private UseCaseDiagramObject objectBeingDragged = null;
    private boolean isInEditMode = false; // To track if the user is in edit mode
    private String systemBoundaryName = "System";  // Default name for the system boundary

    /**
     * Initializes the Use Case Diagram editor by setting up UI components, event handlers, and default configurations.
     *
     * <p>This method is automatically called after the FXML file has been loaded. It configures the object explorer,
     * assigns actions to buttons, and sets up listeners for user interactions on the canvas and text fields.</p>
     */
    @FXML
    private void initialize() {
        objects = new ArrayList<>();
        associations = new ArrayList<>();
        gc = canvas.getGraphicsContext2D();

        // Initialize the object explorer
        rootItem = new TreeItem<>("Use Case Diagram");
        rootItem.setExpanded(true);
        objectExplorer.setRoot(rootItem);

        // Handle selection in the object explorer
        objectExplorer.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectObjectFromExplorer(newSelection.getValue());
            }
        });

        // Button actions
        btnAddActor.setOnAction(event -> addActor());
        btnAddUseCase.setOnAction(event -> addUseCase());

        // Association buttons actions
        btnAssociation.setOnAction(event -> associationType = "association");
        btnInclude.setOnAction(event -> associationType = "include");
        btnExtend.setOnAction(event -> associationType = "extend");

        // Drag button action
        btnDrag.setOnAction(event -> isInDragMode = btnDrag.isSelected());

        // Delete button action
        btnDelete.setOnAction(event -> {
            if (!btnDelete.isSelected()) {
                objectBeingDragged = null;
            }
        });

        // Edit button action
        btnEdit.setOnAction(event -> {
            isInEditMode = btnEdit.isSelected();
            if (!isInEditMode) {
                // Hide the text field when exiting edit mode
                if (selectedObjectExplorer != null) {
                    selectedObjectExplorer.hideNameField();
                }
                redrawCanvas();
            }
        });

        // Add listeners to text fields to handle name changes
        txtActorName.setOnAction(event -> onNameChange());
        txtUseCaseName.setOnAction(event -> onNameChange());
        btnEnterSystemBoundary.setOnAction(event -> onSystemBoundaryNameChange());  // Listener for system boundary name

        // Canvas interactions
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        // Home button
        btnHome.setOnAction(event -> goToHomePage());

        btnSaveDiagram.setOnAction(event -> saveDiagram());
        btnLoadDiagram.setOnAction(event -> loadDiagram());

        // JSON Save and Load buttons
        btnSaveJson.setOnAction(event -> saveDiagramToJson());
        btnLoadJson.setOnAction(event -> loadDiagramFromJson());

        // Set the action for the Export Image button
        btnExportImage.setOnAction(event -> exportDiagramToImage());

        // Set hover effects for association buttons
        btnAssociation.setOnAction(event -> {
            associationType = "association";
            toggleButtonColor(btnAssociation);
        });
        btnInclude.setOnAction(event -> {
            associationType = "include";
            toggleButtonColor(btnInclude);
        });
        btnExtend.setOnAction(event -> {
            associationType = "extend";
            toggleButtonColor(btnExtend);
        });
        btnDrag.setOnAction(event -> {
            isInDragMode = btnDrag.isSelected();
            toggleButtonColor(btnDrag);
        });
        btnDelete.setOnAction(event -> {
            if (!btnDelete.isSelected()) {
                objectBeingDragged = null;
            }
            toggleButtonColor(btnDelete);
        });
        btnEdit.setOnAction(event -> {
            isInEditMode = btnEdit.isSelected();
            if (!isInEditMode && selectedObjectExplorer != null) {
                selectedObjectExplorer.hideNameField();
                redrawCanvas();
            }
            toggleButtonColor(btnEdit);
        });
    }

    /**
     * Toggles the visual appearance of a {@code ToggleButton} based on its selection state.
     *
     * <p>If the button is selected, it applies a highlighted style; otherwise, it reverts to the default style.</p>
     *
     * @param button The {@code ToggleButton} whose style is to be toggled.
     */
    private void toggleButtonColor(ToggleButton button) {
        if (button.isSelected()) {
            button.setStyle("-fx-background-color: #8C8C8C; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
        } else {
            button.setStyle("-fx-font-size: 12px; -fx-font-family: 'Verdana';  -fx-font-weight: bold; -fx-background-color: #AFAFAF;  -fx-pref-width: 120px;");
        }
    }

    /**
     * Adds a new actor to the use case diagram based on the input provided in the actor name text field.
     *
     * <p>If the actor name is empty, it defaults to "Actor". The method creates a new {@code UseCaseDiagramObject},
     * adds it to the list of objects, updates the object explorer, and redraws the canvas to reflect the new actor.</p>
     */
    private void addActor() {
        String actorName = txtActorName.getText().trim();
        if (actorName.isEmpty()) {
            actorName = "Actor";
        }
        UseCaseDiagramObject actor = new UseCaseDiagramObject("actor", 150, 100, actorName);
        objects.add(actor);

        // Update the explorer
        TreeItem<String> actorItem = new TreeItem<>(actorName);
        rootItem.getChildren().add(actorItem);

        redrawCanvas();
        // Clear the actor name text field
        txtActorName.clear();
    }

    /**
     * Adds a new use case to the use case diagram based on the input provided in the use case name text field.
     *
     * <p>If the use case name is empty, it defaults to "Use Case". The method creates a new {@code UseCaseDiagramObject},
     * adds it to the list of objects, updates the object explorer, and redraws the canvas to reflect the new use case.</p>
     */
    private void addUseCase() {
        String useCaseName = txtUseCaseName.getText().trim();
        if (useCaseName.isEmpty()) {
            useCaseName = "Use Case";
        }
        UseCaseDiagramObject useCase = new UseCaseDiagramObject("usecase", 250, 150, useCaseName);
        objects.add(useCase);

        // Update the explorer
        TreeItem<String> useCaseItem = new TreeItem<>(useCaseName);
        rootItem.getChildren().add(useCaseItem);

        redrawCanvas();
        // Clear the use case name text field
        txtUseCaseName.clear();
    }

    /**
     * Selects an object (actor or use case) from the object explorer and highlights it on the canvas.
     *
     * <p>This method is invoked when a user selects an item from the object explorer tree view.
     * It identifies the corresponding {@code UseCaseDiagramObject} and redraws the canvas to highlight the selected object.</p>
     *
     * @param objectName The name of the object selected in the explorer.
     */
    private void selectObjectFromExplorer(String objectName) {
        for (UseCaseDiagramObject object : objects) {
            if (object.getName().equals(objectName)) {
                selectedObjectExplorer = object;
                redrawCanvas(); // Highlight the selected object
                break;
            }
        }
    }

    /**
     * Draws the system boundary on the canvas, including the boundary rectangle and its name.
     *
     * <p>The system boundary defines the scope of the system being modeled. It is drawn as a rectangle with the
     * specified system boundary name centered at the top.</p>
     */
    private void drawSystemBoundary() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(100, 50, canvas.getWidth() - 200, canvas.getHeight() - 100);

        // Draw the system boundary name at the top-center inside the rectangle
        gc.setFill(Color.BLACK);
        gc.fillText(systemBoundaryName, (canvas.getWidth() - systemBoundaryName.length() * 6) / 2, 50 + 20);
    }

    /**
     * Handles changes to the system boundary name based on user input in the corresponding text field.
     *
     * <p>This method updates the {@code systemBoundaryName} variable and redraws the canvas to reflect the new name.</p>
     */
    private void onSystemBoundaryNameChange() {
        //systemBoundaryName = txtSystemBoundaryName.getText().trim();
        //redrawCanvas();  // Redraw the canvas to reflect the updated name
        String newName = txtSystemBoundaryName.getText().trim();
        if (!newName.isEmpty()) {
            systemBoundaryName = newName;
            drawSystemBoundary(); // Redraw the boundary with the new name
            System.out.println("System boundary name updated to: " + systemBoundaryName);
        } else {
            System.out.println("System boundary name cannot be empty.");
        }
        redrawCanvas();

        // Clear the system boundary text field
        txtSystemBoundaryName.clear();
    }

    /**
     * Handles mouse press events on the canvas to facilitate object selection, association creation,
     * deletion, and editing based on the current mode (delete, edit, drag).
     *
     * @param event The {@code MouseEvent} triggered by the mouse press.
     */
    private void onMousePressed(MouseEvent event) {
        boolean isInDeleteMode = btnDelete.isSelected();
        boolean isInEditMode = btnEdit.isSelected();

        UseCaseDiagramObject clickedObjectO = getObjectAt(event.getX(), event.getY());
        if (clickedObjectO == null) {
            selectedObject1 = null;
            selectedObject2 = null;
            selectedObjectExplorer = null;
            redrawCanvas();
        }

        if (isInDeleteMode) {
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                objects.remove(clickedObject);
                associations.removeIf(assoc -> assoc.getObj1() == clickedObject || assoc.getObj2() == clickedObject);

                // Update the explorer by removing the corresponding TreeItem
                for (TreeItem<String> child : rootItem.getChildren()) {
                    if (child.getValue().equals(clickedObject.getName())) {
                        rootItem.getChildren().remove(child);
                        break; // Exit after removing the item
                    }
                }

                redrawCanvas();
                return;
            }

            for (Association assoc : associations) {
                if (isNearAssociation(assoc, event.getX(), event.getY())) {
                    associations.remove(assoc);
                    redrawCanvas();
                    return;
                }
            }
        } else if (isInEditMode) {
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                selectedObjectExplorer = clickedObject;
                clickedObject.showNameField();
                // Set the text fields with current names
                if ("actor".equals(clickedObject.getType())) {
                    txtActorName.setText(clickedObject.getName());
                    redrawCanvas();
                } else {
                    txtUseCaseName.setText(clickedObject.getName());
                    redrawCanvas();
                }
            }
        }

        if (isInDragMode) {
            objectBeingDragged = getObjectAt(event.getX(), event.getY());
        } else if (btnAssociation.isSelected()){
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                if (selectedObject1 == null) {
                    selectedObject1 = clickedObject;
                    clickedObject.showNameField();

                    gc.setStroke(Color.RED);
                        gc.setLineWidth(2);
                        if ("actor".equals(selectedObject1.getType())) {
                            gc.strokeOval(selectedObject1.getX() - 20, selectedObject1.getY() - 20, 40, 40);
                        } else if ("usecase".equals(selectedObject1.getType())) {
                            gc.strokeRect(selectedObject1.getX() - 55, selectedObject1.getY() - 30, 110, 60);
                        }

                } else if (selectedObject2 == null && clickedObject != selectedObject1) {
                    selectedObject2 = clickedObject;
                    drawAssociation(selectedObject1, selectedObject2, associationType);
                    associations.add(new Association(selectedObject1, selectedObject2, associationType));
                    selectedObject1 = null;
                    selectedObject2 = null;
                    redrawCanvas();
                }
            } else {
                selectedObject1 = null;
                selectedObject2 = null;
                redrawCanvas();
            }
        } else if (btnInclude.isSelected()){
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                if (selectedObject1 == null) {
                    selectedObject1 = clickedObject;
                    clickedObject.showNameField();

                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                    if ("actor".equals(selectedObject1.getType())) {
                        gc.strokeOval(selectedObject1.getX() - 20, selectedObject1.getY() - 20, 40, 40);
                    } else if ("usecase".equals(selectedObject1.getType())) {
                        gc.strokeRect(selectedObject1.getX() - 55, selectedObject1.getY() - 30, 110, 60);
                    }

                } else if (selectedObject2 == null && clickedObject != selectedObject1) {
                    selectedObject2 = clickedObject;
                    drawAssociation(selectedObject1, selectedObject2, associationType);
                    associations.add(new Association(selectedObject1, selectedObject2, associationType));
                    selectedObject1 = null;
                    selectedObject2 = null;
                    redrawCanvas();
                }
            } else {
                selectedObject1 = null;
                selectedObject2 = null;
                redrawCanvas();
            }
        } else if (btnExtend.isSelected()){
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                if (selectedObject1 == null) {
                    selectedObject1 = clickedObject;
                    clickedObject.showNameField();

                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                    if ("actor".equals(selectedObject1.getType())) {
                        gc.strokeOval(selectedObject1.getX() - 20, selectedObject1.getY() - 20, 40, 40);
                    } else if ("usecase".equals(selectedObject1.getType())) {
                        gc.strokeRect(selectedObject1.getX() - 55, selectedObject1.getY() - 30, 110, 60);
                    }

                } else if (selectedObject2 == null && clickedObject != selectedObject1) {
                    selectedObject2 = clickedObject;
                    drawAssociation(selectedObject1, selectedObject2, associationType);
                    associations.add(new Association(selectedObject1, selectedObject2, associationType));
                    selectedObject1 = null;
                    selectedObject2 = null;
                    redrawCanvas();
                }
            } else {
                selectedObject1 = null;
                selectedObject2 = null;
                redrawCanvas();
            }
        }

    }

    /**
     * Handles changes to the name of a selected object based on user input in the corresponding text field.
     *
     * <p>This method updates the name of the selected {@code UseCaseDiagramObject}, hides the name editing field,
     * and redraws the canvas to reflect the updated name.</p>
     */
    @FXML
    private void onNameChange() {
        if (selectedObjectExplorer != null) {
            String newName;
            if ("actor".equals(selectedObjectExplorer.getType())) {
                newName = txtActorName.getText().trim();
            } else {
                newName = txtUseCaseName.getText().trim();
            }

            // If the name is empty, revert to default
            if (newName.isEmpty()) {
                newName = selectedObjectExplorer.getType().equals("actor") ? "Actor" : "Use Case";
            }

            // Update the TreeView
            boolean updated = false;  // Flag to check if the update was successful

            for (TreeItem<String> child : rootItem.getChildren()) {
                System.out.println("I am updated");
                if (child.getValue().equals(selectedObjectExplorer.getName())) {
                    // Update the name in the TreeView
                    child.setValue(newName);
                    updated = true;  // Indicate the update was successful
                    break;
                }
            }

            // Update the object name
            selectedObjectExplorer.setName(newName);
            selectedObjectExplorer.hideNameField();  // Hide the name field after updating
            redrawCanvas();  // Redraw the canvas to reflect changes
        }
    }

    /**
     * Handles mouse drag events on the canvas to facilitate dragging of diagram objects.
     *
     * <p>If dragging mode is enabled and an object is being dragged, this method updates the object's position
     * based on the current mouse coordinates and redraws the canvas to reflect the movement.</p>
     *
     * @param event The {@code MouseEvent} triggered by the mouse drag.
     */
    private void onMouseDragged(MouseEvent event) {
        if (isInDragMode && objectBeingDragged != null) {
            objectBeingDragged.setX(event.getX());
            objectBeingDragged.setY(event.getY());
            redrawCanvas();
        }
    }

    /**
     * Handles mouse release events on the canvas to finalize dragging of diagram objects.
     *
     * <p>This method resets the {@code objectBeingDragged} reference when dragging mode is disabled,
     * ensuring that objects are no longer moved after the mouse is released.</p>
     *
     * @param event The {@code MouseEvent} triggered by the mouse release.
     */
    private void onMouseReleased(MouseEvent event) {
        if (isInDragMode) {
            if (objectBeingDragged != null) {
                objectBeingDragged.hideNameField();
            }
            objectBeingDragged = null;
        }
    }

    /**
     * Retrieves the {@code UseCaseDiagramObject} located at the specified canvas coordinates.
     *
     * <p>This method iterates through all objects in the diagram to determine if any contain the given (x, y) point.</p>
     *
     * @param x The X-coordinate on the canvas.
     * @param y The Y-coordinate on the canvas.
     * @return The {@code UseCaseDiagramObject} at the specified location, or {@code null} if none found.
     */
    private UseCaseDiagramObject getObjectAt(double x, double y) {
        for (UseCaseDiagramObject object : objects) {
            if (object.contains(x, y)) {
                return object;
            }
        }
        return null;
    }

    /**
     * Draws an association (relationship) between two use case diagram objects on the canvas.
     *
     * <p>The association can be of type "association", "include", or "extend". Depending on the type,
     * the association line is styled differently, and appropriate labels or arrowheads are added.</p>
     *
     * @param obj1 The first {@code UseCaseDiagramObject} involved in the association.
     * @param obj2 The second {@code UseCaseDiagramObject} involved in the association.
     * @param type The type of association ("association", "include", or "extend").
     */
    private void drawAssociation(UseCaseDiagramObject obj1, UseCaseDiagramObject obj2, String type) {
        double[] obj1End = getObjectEdge(obj1, obj2.getX(), obj2.getY());
        double[] obj2End = getObjectEdge(obj2, obj1.getX(), obj1.getY());

        double x1 = obj1End[0];
        double y1 = obj1End[1];
        double x2 = obj2End[0];
        double y2 = obj2End[1];

        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        if ("association".equals(type)) {
            gc.strokeLine(x1, y1, x2, y2);
        } else {
            gc.setLineDashes(10, 10);
            gc.strokeLine(x1, y1, x2, y2);
            gc.setLineDashes(0);

            String associationText = "include".equals(type) ? "<<include>>" : "<<extend>>";
            gc.setFill(Color.BLACK);
            gc.fillText(associationText, (x1 + x2) / 2, (y1 + y2) / 2 - 10);
        }

        if ("include".equals(type) || "extend".equals(type)) {
            drawArrowhead(x1, y1, x2, y2);
        }
    }

    /**
     * Determines the edge point of an object closest to a given coordinate, facilitating accurate association line drawing.
     *
     * <p>This method calculates the intersection point on the object's boundary in the direction of another point.</p>
     *
     * @param object The {@code UseCaseDiagramObject} whose edge is to be determined.
     * @param x      The X-coordinate towards which the edge point is calculated.
     * @param y      The Y-coordinate towards which the edge point is calculated.
     * @return An array containing the X and Y coordinates of the edge point.
     */
    private double[] getObjectEdge(UseCaseDiagramObject object, double x, double y) {
        double objX = object.getX();
        double objY = object.getY();

        if ("actor".equals(object.getType())) {
            double angle = Math.atan2(y - objY, x - objX);
            double radius = 50;
            double edgeX = objX + radius * Math.cos(angle);
            double edgeY = objY + radius * Math.sin(angle);
            return new double[]{edgeX, edgeY};
        }

        if ("usecase".equals(object.getType())) {
            double angle = Math.atan2(y - objY, x - objX);
            double width = 100;
            double height = 50;
            double edgeX = objX + width * Math.cos(angle) / 2;
            double edgeY = objY + height * Math.sin(angle) / 2;
            return new double[]{edgeX, edgeY};
        }

        return new double[]{objX, objY};
    }

    /**
     * Determines whether a given point is near an association line, facilitating association deletion.
     *
     * <p>This method calculates the perpendicular distance from the point to the association line and
     * checks if it falls within a specified tolerance.</p>
     *
     * @param association The {@code Association} to check against.
     * @param x           The X-coordinate of the point.
     * @param y           The Y-coordinate of the point.
     * @return {@code true} if the point is near the association line; {@code false} otherwise.
     */
    private boolean isNearAssociation(Association association, double x, double y) {
        double x1 = association.getObj1().getX();
        double y1 = association.getObj1().getY();
        double x2 = association.getObj2().getX();
        double y2 = association.getObj2().getY();

        double tolerance = 10;
        double distToLine = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1) /
                Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
        return distToLine < tolerance;
    }

    /**
     * Draws an arrowhead at the end of an association line to indicate directionality.
     *
     * <p>This method calculates the position and orientation of the arrowhead based on the
     * start and end coordinates of the association line.</p>
     *
     * @param x1 The X-coordinate of the start point of the association line.
     * @param y1 The Y-coordinate of the start point of the association line.
     * @param x2 The X-coordinate of the end point of the association line.
     * @param y2 The Y-coordinate of the end point of the association line.
     */
    private void drawArrowhead(double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1); // Get the angle of the line
        double arrowheadLength = 10; // Length of the arrowhead
        double arrowAngle = Math.PI / 6; // Angle for the arrowhead sides (30 degrees)

        // Calculate the points for the open arrowhead
        double x3 = x2 - arrowheadLength * Math.cos(angle + arrowAngle);
        double y3 = y2 - arrowheadLength * Math.sin(angle + arrowAngle);

        double x4 = x2 - arrowheadLength * Math.cos(angle - arrowAngle);
        double y4 = y2 - arrowheadLength * Math.sin(angle - arrowAngle);

        // Draw the open (unfilled) arrowhead
        gc.setStroke(Color.BLACK); // Set stroke color for the arrowhead
        gc.setLineWidth(2); // Set the line width for the arrowhead

        gc.strokeLine(x2, y2, x3, y3); // Line from the end to the first side of the arrowhead
        gc.strokeLine(x2, y2, x4, y4); // Line from the end to the second side of the arrowhead
    }

    /**
     * Redraws the entire use case diagram on the canvas, including system boundary, objects, and associations.
     *
     * <p>This method clears the current canvas, redraws the system boundary, iterates through all objects to draw them,
     * iterates through all associations to draw them, and highlights the selected object if any.</p>
     */
    public void redrawCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawSystemBoundary();
        for (UseCaseDiagramObject object : objects) {
            object.draw(gc);
        }
        for (Association association : associations) {
            drawAssociation(association.getObj1(), association.getObj2(), association.getType());
        }

        // Highlight the selected object
        if (selectedObjectExplorer != null) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            if ("actor".equals(selectedObjectExplorer.getType())) {
                gc.strokeOval(selectedObjectExplorer.getX() - 20, selectedObjectExplorer.getY() - 20, 40, 40);
            } else if ("usecase".equals(selectedObjectExplorer.getType())) {
                gc.strokeRect(selectedObjectExplorer.getX() - 55, selectedObjectExplorer.getY() - 30, 110, 60);
            }
        }
    }

    /**
     * Navigates the user back to the home page of the UML Editor application.
     *
     * <p>This method prompts the user with a confirmation dialog to save the current diagram before exiting.
     * Based on the user's choice, it either saves the diagram and navigates home, exits without saving,
     * or cancels the navigation.</p>
     */
    private void navigateToHome() {
        /*// Create a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Diagram");
        alert.setHeaderText("Do you want to save the diagram before exiting?");
        alert.setContentText("Choose your option:");

        // Add buttons to the alert
        ButtonType saveAndExit = new ButtonType("Save and Exit");
        ButtonType exitWithoutSaving = new ButtonType("Exit Without Saving");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveAndExit, exitWithoutSaving, cancel);

        // Show the alert and wait for user input
        alert.showAndWait().ifPresent(response -> {
            if (response == saveAndExit) {
                // Trigger save button's action event to save the diagram
                btnSaveDiagram.fire();
                goToHomePage(); // Navigate to the homepage
            } else if (response == exitWithoutSaving) {
                goToHomePage(); // Navigate to the homepage without saving
            }
            // If "Cancel" is clicked, do nothing and stay on the page
        });*/
        goToHomePage();
    }

    /**
     * Navigates the user to the home page by loading the welcome FXML layout.
     *
     * <p>This method is called internally after confirming navigation actions. It loads the
     * {@code welcome.fxml} file, sets up the scene with specified dimensions, and updates the stage.</p>
     */
    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml")); // Update with your FXML file path
            System.out.println("Loaded welcomepage");
            // Get the FXML file location
            URL fxmlLocation = getClass().getResource("/com/example/umlscd/welcome.fxml");
            // Debugging: check if the file is found
            if (fxmlLocation == null) {
                System.err.println("FXML file 'welcome.fxml' not found!");
                return;
            }
            Parent root = loader.load();
            Stage stage = (Stage) btnHome.getScene().getWindow();
            System.out.println("Loaded welcomepage");


            // Create a scene with specific size
            Scene scene = new Scene(root, 1366, 768);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current use case diagram by serializing it to a file with a {@code .ser} extension.
     *
     * <p>This method opens a file chooser dialog, allowing the user to select the destination file.
     * It ensures that the file has the correct extension and delegates the saving process
     * to the {@code UseCaseDiagramDAO} class.</p>
     */
    private void saveDiagram() {
        // Open a file chooser dialog for saving
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Use Case Diagram");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files (*.ser)", "*.ser"));

        // Show save dialog
        File selectedFile = fileChooser.showSaveDialog((Stage) btnSaveDiagram.getScene().getWindow());

        if (selectedFile != null) {
            // Ensure the file ends with ".ser"
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.endsWith(".ser")) {
                filePath += ".ser";
            }

            // Save the diagram to the chosen file
            UseCaseDiagramManager manager = new UseCaseDiagramManager(objects, associations, systemBoundaryName);
            UseCaseDiagramDAO.saveDiagram(manager, filePath);
        } else {
            System.out.println("Save operation was cancelled.");
        }
    }

    /**
     * Loads a previously saved use case diagram by deserializing it from a {@code .ser} file.
     *
     * <p>This method opens a file chooser dialog, allowing the user to select the file to load.
     * It delegates the loading process to the {@code UseCaseDiagramDAO} class and updates
     * the internal state of the diagram accordingly.</p>
     */
    private void loadDiagram() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Use Case Diagram");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files (*.ser)", "*.ser"));

        File selectedFile = fileChooser.showOpenDialog((Stage) btnLoadDiagram.getScene().getWindow());

        if (selectedFile != null) {
            UseCaseDiagramManager manager = UseCaseDiagramDAO.loadDiagram(selectedFile.getAbsolutePath());
            if (manager != null) {
                objects = manager.getObjects();
                associations = manager.getAssociations();
                systemBoundaryName = manager.getSystemBoundaryName(); // Load the system boundary name
                redrawCanvas();
            } else {
                System.err.println("Failed to load diagram from the selected file.");
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    /**
     * Saves the current use case diagram to a JSON file, facilitating interoperability and ease of sharing.
     *
     * <p>This method opens a file chooser dialog, allowing the user to select the destination JSON file.
     * It creates a {@code UseCaseDiagramManager}, populates it with current objects and associations,
     * and delegates the serialization process to the {@code UseCaseDiagramSerializer} class.</p>
     */
    private void saveDiagramToJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                // Create a UseCaseDiagramManager and populate it with objects and associations
                UseCaseDiagramManager diagramManager = new UseCaseDiagramManager();
                for (UseCaseDiagramObject object : objects) {
                    diagramManager.addObject(object); // Add objects to diagram manager
                }
                for (Association association : associations) {
                    diagramManager.addAssociation(association); // Add associations to diagram manager
                }

                // Set system boundary name if necessary
                diagramManager.setSystemBoundaryName(systemBoundaryName);

                // Save the diagram manager to a JSON file
                UseCaseDiagramSerializer.saveDiagram(diagramManager, file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to save diagram to JSON.");
            }
        }
    }

    /**
     * Loads a use case diagram from a JSON file, restoring objects, associations, and system boundary details.
     *
     * <p>This method opens a file chooser dialog, allowing the user to select the JSON file to load.
     * It delegates the deserialization process to the {@code UseCaseDiagramSerializer} class and updates
     * the internal state of the diagram accordingly.</p>
     */
    private void loadDiagramFromJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try {
                // Load the diagram manager from the JSON file
                UseCaseDiagramManager diagramManager = UseCaseDiagramSerializer.loadDiagram(file.getAbsolutePath());

                // Update objects and associations from the loaded diagram
                objects.clear();
                associations.clear();
                objects.addAll(diagramManager.getObjects());
                associations.addAll(diagramManager.getAssociations());

                // Step 2: Correctly associate the associations
                for (Association assoc : diagramManager.getAssociations()) {
                    // Find the objects by their names (or unique identifiers)
                    UseCaseDiagramObject obj1 = findObjectByName(assoc.getObj1().getName());
                    UseCaseDiagramObject obj2 = findObjectByName(assoc.getObj2().getName());

                    // Link the association to the correct objects
                    if (obj1 != null && obj2 != null) {
                        assoc.setObj1(obj1);
                        assoc.setObj2(obj2);
                        associations.add(assoc);
                    }
                }

                // Update the system boundary name
                systemBoundaryName = diagramManager.getSystemBoundaryName();

                // Redraw the canvas with the loaded data
                redrawCanvas();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load diagram from JSON.");
            }
        }
    }

    /**
     * Finds and retrieves a {@code UseCaseDiagramObject} by its name.
     *
     * <p>This helper method iterates through the list of objects to locate one with the matching name.</p>
     *
     * @param name The name of the object to find.
     * @return The {@code UseCaseDiagramObject} with the specified name, or {@code null} if not found.
     */
    private UseCaseDiagramObject findObjectByName(String name) {
        for (UseCaseDiagramObject obj : objects) {
            if (obj.getName().equals(name)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * <p>This helper method is used to notify users of errors during save and load operations.</p>
     *
     * @param title   The title of the alert dialog.
     * @param message The message content of the alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Exports the current use case diagram as an image file based on the current state of the canvas.
     *
     * <p>This method creates a {@code UseCaseDiagramManager} instance, populates it with current objects
     * and associations, sets the system boundary name, and delegates the export process to the manager.</p>
     */
    private void exportDiagramToImage() {
        // Create a UseCaseDiagramManager instance and set the current state of the diagram
        UseCaseDiagramManager manager = new UseCaseDiagramManager();
        manager.setObjects(new ArrayList<>(objects));  // Add objects
        manager.setAssociations(new ArrayList<>(associations));  // Add associations
        manager.setSystemBoundaryName(systemBoundaryName);  // Set system boundary name

        // Call the export method in the manager
        manager.exportDiagramToImage(canvas);
    }

    /**
     * Applies a hover effect to a {@code Button} when the mouse enters its area.
     *
     * <p>This method changes the button's background color, font size, and scaling to indicate a hover state.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered by the mouse entering the button area.
     */
    public void applyHoverEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #C0C0C0; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    /**
     * Removes the hover effect from a {@code Button} when the mouse exits its area.
     *
     * <p>This method reverts the button's background color, font size, and scaling to its default state.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered by the mouse exiting the button area.
     */
    public void removeHoverEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #AFAFAF; -fx-font-size: 12px;  -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    /**
     * Applies a click effect to a {@code Button} when it is clicked.
     *
     * <p>This method changes the button's background color and ensures its scaling is reset, indicating a clicked state.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered by the button click.
     */
    public void applyClickEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #8C8C8C; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

}
