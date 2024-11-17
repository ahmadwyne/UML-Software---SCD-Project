package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

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
    private ToggleButton btnDrag; // New Drag Button

    @FXML
    private Canvas canvas;

    private ArrayList<UseCaseDiagramObject> objects;
    private ArrayList<Association> associations; // Store associations
    private GraphicsContext gc;

    private UseCaseDiagramObject selectedObject1;
    private UseCaseDiagramObject selectedObject2;
    private String associationType = "association"; // Default type
    private boolean isDragging = false; // Flag to track drag mode
    private UseCaseDiagramObject objectBeingDragged = null; // The object being dragged
    private boolean isInDragMode = false; // Flag to check if drag mode is active

    @FXML
    private void initialize() {
        objects = new ArrayList<>();
        associations = new ArrayList<>();
        gc = canvas.getGraphicsContext2D();

        // Draw initial system boundary
        drawSystemBoundary();

        // Button actions
        btnAddActor.setOnAction(event -> addActor());
        btnAddUseCase.setOnAction(event -> addUseCase());

        // Association buttons actions
        btnAssociation.setOnAction(event -> associationType = "association");
        btnInclude.setOnAction(event -> associationType = "include");
        btnExtend.setOnAction(event -> associationType = "extend");

        // Drag button action (toggling drag mode)
        btnDrag.setOnAction(event -> {
            isInDragMode = btnDrag.isSelected();
            if (!isInDragMode) {
                objectBeingDragged = null; // Reset drag object when exiting drag mode
            }
        });

        // Canvas interactions
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        // Home button
        btnHome.setOnAction(event -> navigateToHome());
    }

    private void drawSystemBoundary() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(100, 50, canvas.getWidth() - 200, canvas.getHeight() - 100); // Narrower and taller
    }

    private void addActor() {
        UseCaseDiagramObject actor = new UseCaseDiagramObject("actor", 150, 100);
        objects.add(actor);
        redrawCanvas();
    }

    private void addUseCase() {
        UseCaseDiagramObject useCase = new UseCaseDiagramObject("usecase", 250, 150);
        objects.add(useCase);
        redrawCanvas();
    }

    private void onMousePressed(MouseEvent event) {
        if (isInDragMode) {
            // Only allow drag if in drag mode
            objectBeingDragged = getObjectAt(event.getX(), event.getY());
        } else {
            // Otherwise, select objects for associations
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                // If no object is selected, select the first one
                if (selectedObject1 == null) {
                    selectedObject1 = clickedObject;
                }
                // If the first object is selected, select the second one and draw the association
                else if (selectedObject2 == null && clickedObject != selectedObject1) {
                    selectedObject2 = clickedObject;
                    drawAssociation(selectedObject1, selectedObject2, associationType);
                    // Add association to the list
                    associations.add(new Association(selectedObject1, selectedObject2, associationType));
                    selectedObject1 = null; // Reset selection
                    selectedObject2 = null; // Reset selection
                }
            }
        }
    }

    private void onMouseDragged(MouseEvent event) {
        if (isInDragMode && objectBeingDragged != null) {
            // Move the object being dragged based on mouse position
            objectBeingDragged.setX(event.getX());
            objectBeingDragged.setY(event.getY());
            redrawCanvas();
        }
    }

    private void onMouseReleased(MouseEvent event) {
        if (isInDragMode) {
            // Release the object being dragged
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
        double x1 = obj1.getX();
        double y1 = obj1.getY();
        double x2 = obj2.getX();
        double y2 = obj2.getY();

        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK); // Always black for association lines

        if ("association".equals(type)) {
            gc.strokeLine(x1, y1, x2, y2);
        } else if ("include".equals(type)) {
            gc.setLineDashes(10, 10);
            gc.strokeLine(x1, y1, x2, y2);
            gc.setLineDashes(0);
            gc.fillText("<<include>>", (x1 + x2) / 2, (y1 + y2) / 2 - 10);
        } else if ("extend".equals(type)) {
            gc.setLineDashes(10, 10);
            gc.strokeLine(x1, y1, x2, y2);
            gc.setLineDashes(0);
            gc.fillText("<<extend>>", (x1 + x2) / 2, (y1 + y2) / 2 - 10);
        }
    }

    private void redrawCanvas() {
        drawSystemBoundary();
        for (UseCaseDiagramObject object : objects) {
            object.draw(gc);
        }
        // Redraw all associations after objects are moved
        for (Association association : associations) {
            drawAssociation(association.getObj1(), association.getObj2(), association.getType());
        }
    }

    private void navigateToHome() {
        try {
            Stage stage = (Stage) btnHome.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml"));
            Parent root = loader.load();
            Scene welcomeScene = new Scene(root, 800, 600);
            stage.setScene(welcomeScene);
            stage.setTitle("Welcome to UML Editor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper class to store associations
    public static class Association {
        private UseCaseDiagramObject obj1;
        private UseCaseDiagramObject obj2;
        private String type;

        public Association(UseCaseDiagramObject obj1, UseCaseDiagramObject obj2, String type) {
            this.obj1 = obj1;
            this.obj2 = obj2;
            this.type = type;
        }

        public UseCaseDiagramObject getObj1() {
            return obj1;
        }

        public UseCaseDiagramObject getObj2() {
            return obj2;
        }

        public String getType() {
            return type;
        }
    }
}
