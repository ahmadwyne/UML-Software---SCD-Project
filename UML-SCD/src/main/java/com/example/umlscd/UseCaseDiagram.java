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
    private ToggleButton btnDelete; // Delete button to toggle delete mode

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

        // Delete button action (toggle delete mode)
        btnDelete.setOnAction(event -> {
            boolean isInDeleteMode = btnDelete.isSelected();
            if (!isInDeleteMode) {
                objectBeingDragged = null; // Reset object when exiting delete mode
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
        boolean isInDeleteMode = btnDelete.isSelected();
        if (isInDeleteMode) {
            // Check for object deletion
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                // Remove object from the list and its associations
                objects.remove(clickedObject);
                associations.removeIf(assoc -> assoc.getObj1() == clickedObject || assoc.getObj2() == clickedObject);
                redrawCanvas();
                return; // Prevent further processing
            }

            // Check for association deletion (if clicked near an association)
            for (Association assoc : associations) {
                if (isNearAssociation(assoc, event.getX(), event.getY())) {
                    associations.remove(assoc);
                    redrawCanvas();
                    return; // Prevent further processing
                }
            }
        }

        // Rest of the logic for selecting objects for associations or dragging...
        if (isInDragMode) {
            objectBeingDragged = getObjectAt(event.getX(), event.getY());
        } else {
            UseCaseDiagramObject clickedObject = getObjectAt(event.getX(), event.getY());
            if (clickedObject != null) {
                if (selectedObject1 == null) {
                    selectedObject1 = clickedObject;
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

    // Updated method to draw associations, including boundary connections
    private void drawAssociation(UseCaseDiagramObject obj1, UseCaseDiagramObject obj2, String type) {
        // Get edge points for the objects
        double[] obj1End = getObjectEdge(obj1, obj2.getX(), obj2.getY()); // Get boundary point for obj1
        double[] obj2End = getObjectEdge(obj2, obj1.getX(), obj1.getY()); // Get boundary point for obj2

        double x1 = obj1End[0];
        double y1 = obj1End[1];
        double x2 = obj2End[0];
        double y2 = obj2End[1];

        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK); // Always black for association lines

        // Draw the association line
        if ("association".equals(type)) {
            gc.strokeLine(x1, y1, x2, y2);
        } else {
            // Draw dashed lines for include and extend
            gc.setLineDashes(10, 10);
            gc.strokeLine(x1, y1, x2, y2);
            gc.setLineDashes(0); // Reset line dashes

            // Draw the appropriate text (<<include>> or <<extend>>)
            String associationText = type.equals("include") ? "<<include>>" : "<<extend>>";

            // Set text color to black
            gc.setFill(Color.BLACK);

            // Position text above the line
            gc.fillText(associationText, (x1 + x2) / 2, (y1 + y2) / 2 - 10); // Adjust text position if needed
        }

        // Draw arrowhead for include and extend associations
        if ("include".equals(type) || "extend".equals(type)) {
            drawArrowhead(x1, y1, x2, y2);
        }
    }

    // Method to calculate the closest edge point on the object boundary
    private double[] getObjectEdge(UseCaseDiagramObject object, double x, double y) {
        double objX = object.getX();
        double objY = object.getY();

        // For "actor" type object
        if ("actor".equals(object.getType())) {
            // Calculate the boundary point based on the actor's body (circular shape)
            double angle = Math.atan2(y - objY, x - objX); // Angle between the object and the other object
            double radius = 50; // Distance from the center to the edge of the actor (body radius)

            // Calculate the boundary point using the angle
            double edgeX = objX + radius * Math.cos(angle);
            double edgeY = objY + radius * Math.sin(angle);

            return new double[] { edgeX, edgeY };
        }

        // For "usecase" type object (ellipse shape)
        if ("usecase".equals(object.getType())) {
            // Usecase object is elliptical, find the closest edge
            double angle = Math.atan2(y - objY, x - objX);
            double width = 100; // Width of the use case
            double height = 50; // Height of the use case

            // Use parametric equation for ellipse boundary
            double edgeX = objX + width * Math.cos(angle) / 2;
            double edgeY = objY + height * Math.sin(angle) / 2;

            return new double[] { edgeX, edgeY };
        }

        return new double[] { objX, objY }; // Default fallback (center)
    }

    private boolean isNearAssociation(Association association, double x, double y) {
        double x1 = association.getObj1().getX();
        double y1 = association.getObj1().getY();
        double x2 = association.getObj2().getX();
        double y2 = association.getObj2().getY();

        // A simple check if the point is near the center of the line (adjust tolerance as needed)
        double tolerance = 10;
        double distToLine = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1) / Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
        return distToLine < tolerance;
    }

    // Method to draw an arrowhead at the end of the line
    private void drawArrowhead(double x1, double y1, double x2, double y2) {
        // Calculate the direction of the line
        double angle = Math.atan2(y2 - y1, x2 - x1);

        // Calculate the points for the arrowhead
        double arrowheadLength = 10;
        double arrowheadAngle = Math.PI / 6;

        double xArrow1 = x2 - arrowheadLength * Math.cos(angle - arrowheadAngle);
        double yArrow1 = y2 - arrowheadLength * Math.sin(angle - arrowheadAngle);

        double xArrow2 = x2 - arrowheadLength * Math.cos(angle + arrowheadAngle);
        double yArrow2 = y2 - arrowheadLength * Math.sin(angle + arrowheadAngle);

        // Draw the arrowhead as a triangle
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(x2, y2, xArrow1, yArrow1);
        gc.strokeLine(x2, y2, xArrow2, yArrow2);
    }

    private void redrawCanvas() {
        drawSystemBoundary();
        for (UseCaseDiagramObject object : objects) {
            object.draw(gc);
        }
        // Redraw all associations after objects or associations are deleted
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