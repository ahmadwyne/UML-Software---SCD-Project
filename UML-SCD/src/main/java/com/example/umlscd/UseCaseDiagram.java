package com.example.umlscd;

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

import java.io.File;
import java.util.ArrayList;

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
    private TreeView<String> objectExplorer; // TreeView for the object explorer

    private TreeItem<String> rootItem; // Root item for the explorer

    @FXML
    private Button btnSaveDiagram, btnLoadDiagram;


    private ArrayList<UseCaseDiagramObject> objects;
    private ArrayList<Association> associations;
    private GraphicsContext gc;

    private UseCaseDiagramObject selectedObject1;
    private UseCaseDiagramObject selectedObject2;
    private String associationType = "association";
    private boolean isInDragMode = false;
    private UseCaseDiagramObject objectBeingDragged = null;
    private boolean isInEditMode = false; // To track if the user is in edit mode
    private String systemBoundaryName = "System";  // Default name for the system boundary

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
                if (selectedObject1 != null) {
                    selectedObject1.hideNameField();
                }
                redrawCanvas();
            }
        });

        // Add listeners to text fields to handle name changes
        txtActorName.setOnAction(event -> onNameChange());
        txtUseCaseName.setOnAction(event -> onNameChange());
        txtSystemBoundaryName.setOnAction(event -> onSystemBoundaryNameChange());  // Listener for system boundary name

        // Canvas interactions
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        // Home button
        btnHome.setOnAction(event -> navigateToHome());

        btnSaveDiagram.setOnAction(event -> saveDiagram());
        btnLoadDiagram.setOnAction(event -> loadDiagram());



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
            if (!isInEditMode && selectedObject1 != null) {
                selectedObject1.hideNameField();
                redrawCanvas();
            }
            toggleButtonColor(btnEdit);
        });
    }

    private void toggleButtonColor(ToggleButton button) {
        if (button.isSelected()) {
            button.setStyle("-fx-font-size: 14px; -fx-font-family: 'Verdana'; -fx-background-color: #434343; -fx-background-radius: 10px; -fx-pref-width: 150px; -fx-pref-height: 40px;");
        } else {
            button.setStyle("-fx-font-size: 14px; -fx-font-family: 'Verdana'; -fx-background-color: #AFAFAF; -fx-background-radius: 10px; -fx-pref-width: 150px; -fx-pref-height: 40px;");
        }
    }



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
    }

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
    }

    // Select an object from the explorer
    private void selectObjectFromExplorer(String objectName) {
        for (UseCaseDiagramObject object : objects) {
            if (object.getName().equals(objectName)) {
                selectedObject1 = object;
                redrawCanvas(); // Highlight the selected object
                break;
            }
        }
    }

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

    private void onSystemBoundaryNameChange() {
        systemBoundaryName = txtSystemBoundaryName.getText().trim();
        redrawCanvas();  // Redraw the canvas to reflect the updated name
    }

    private void onMousePressed(MouseEvent event) {
        boolean isInDeleteMode = btnDelete.isSelected();
        boolean isInEditMode = btnEdit.isSelected();

        if (isInDeleteMode) {
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                objects.remove(clickedObject);
                associations.removeIf(assoc -> assoc.getObj1() == clickedObject || assoc.getObj2() == clickedObject);
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
                selectedObject1 = clickedObject;
                clickedObject.showNameField();
                // Set the text fields with current names
                if ("actor".equals(clickedObject.getType())) {
                    txtActorName.setText(clickedObject.getName());
                } else {
                    txtUseCaseName.setText(clickedObject.getName());
                }
            }
        }

        if (isInDragMode) {
            objectBeingDragged = getObjectAt(event.getX(), event.getY());
        } else {
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                if (selectedObject1 == null) {
                    selectedObject1 = clickedObject;
                    clickedObject.showNameField();
                } else if (selectedObject2 == null && clickedObject != selectedObject1) {
                    selectedObject2 = clickedObject;
                    drawAssociation(selectedObject1, selectedObject2, associationType);
                    associations.add(new Association(selectedObject1, selectedObject2, associationType));
                    selectedObject1 = null;
                    selectedObject2 = null;
                }
            }
        }
    }

    @FXML
    private void onNameChange() {
        if (selectedObject1 != null) {
            String newName;
            if ("actor".equals(selectedObject1.getType())) {
                newName = txtActorName.getText().trim();
            } else {
                newName = txtUseCaseName.getText().trim();
            }

            // If the name is empty, revert to default
            if (newName.isEmpty()) {
                newName = selectedObject1.getType().equals("actor") ? "Actor" : "Use Case";
            }

            // Update the object name
            selectedObject1.setName(newName);
            selectedObject1.hideNameField();  // Hide the name field after updating
            redrawCanvas();  // Redraw the canvas to reflect changes
        }
    }

    private void onMouseDragged(MouseEvent event) {
        if (isInDragMode && objectBeingDragged != null) {
            objectBeingDragged.setX(event.getX());
            objectBeingDragged.setY(event.getY());
            redrawCanvas();
        }
    }

    private void onMouseReleased(MouseEvent event) {
        if (isInDragMode) {
            if (objectBeingDragged != null) {
                objectBeingDragged.hideNameField();
            }
            objectBeingDragged = null;
        }
    }

    private UseCaseDiagramObject getObjectAt(double x, double y) {
        for (UseCaseDiagramObject object : objects) {
            if (object.contains(x, y)) {
                return object;
            }
        }
        return null;
    }

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


    // Redraw the canvas
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
        if (selectedObject1 != null) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            if ("actor".equals(selectedObject1.getType())) {
                gc.strokeOval(selectedObject1.getX() - 20, selectedObject1.getY() - 20, 40, 40);
            } else if ("usecase".equals(selectedObject1.getType())) {
                gc.strokeRect(selectedObject1.getX() - 55, selectedObject1.getY() - 30, 110, 60);
            }
        }
    }

    private void navigateToHome() {
        // Create a confirmation dialog
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
        });
    }

    // Method to navigate to the homepage
    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml")); // Update with your FXML file path
            Parent root = loader.load();
            Stage stage = (Stage) btnHome.getScene().getWindow();

            // Create a scene with specific size
            Scene scene = new Scene(root, 1366, 768);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




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


}