package com.example.umlscd;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages the UML Class Diagram, including adding classes, interfaces, relationships,
 * and handling serialization/deserialization.
 */
public class ClassDiagramManager {
    private final ArrayList<Node> elements = new ArrayList<>();
    private VBox firstSelectedElement = null;
    private VBox secondSelectedElement = null;

    // Removed separate lists; using classDiagram's lists
    private final ClassDiagramSerializer serializer;
    private final ClassDiagramUI uiController;
    private ClassDiagramRelationsManager relationsManager;
    private ClassDiagramD classDiagram;
    private boolean isDragEnabled = false;

    /**
     * Constructs a ClassDiagramManager with the specified UI controller.
     *
     * @param uiController The UI controller associated with this manager.
     */
    public ClassDiagramManager(ClassDiagramUI uiController) {
        this.uiController = uiController;
        this.relationsManager = null; // Initialize as null; set appropriately during tool selection
        this.classDiagram = new ClassDiagramD();
        this.serializer = new ClassDiagramSerializer();
    }

    // Getters for serialization from classDiagram

    public List<UMLClassBox> getClasses() {
        return classDiagram.getClasses();
    }

    public List<UMLInterfaceBox> getInterfaces() {
        return classDiagram.getInterfaces();
    }

    public List<UMLRelationship> getRelationships() {
        return classDiagram.getRelationships();
    }

    /**
     * Clears all elements from the diagram.
     */
    public void clearDiagram() {
        uiController.getDrawingPane().getChildren().clear();
        classDiagram.getClasses().clear();
        classDiagram.getInterfaces().clear();
        classDiagram.getRelationships().clear();
        elements.clear();
    }

    /**
     * Creates a class box at the specified position and adds it to the diagram.
     *
     * @param name    The name of the class.
     * @param layoutX The X position.
     * @param layoutY The Y position.
     */
    public void createClassBox(String name, double layoutX, double layoutY) {
        VBox classBox = new VBox();
        classBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        classBox.setSpacing(0);

        Label classNameLabel = new Label(name);
        classNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
        classNameLabel.setAlignment(Pos.CENTER);
        classNameLabel.setMaxWidth(Double.MAX_VALUE);
        classNameLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-padding: 5;");

        VBox attributesBox = new VBox();
        attributesBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        classBox.getChildren().addAll(classNameLabel, attributesBox, methodsBox);
        classBox.setLayoutX(layoutX);
        classBox.setLayoutY(layoutY);

        classBox.setOnMouseClicked(event -> uiController.openClassEditor(classBox));
        uiController.getDrawingPane().getChildren().add(classBox);
        elements.add(classBox);
        setDraggable(classBox, isDragEnabled);

        // Update the ClassDiagram model
        UMLClassBox umlClassBox = new UMLClassBox(name, layoutX, layoutY, classBox);
        classDiagram.getClasses().add(umlClassBox);
    }

    /**
     * Creates an interface box at the specified position and adds it to the diagram.
     *
     * @param name    The name of the interface.
     * @param layoutX The X position.
     * @param layoutY The Y position.
     */
    public void createInterfaceBox(String name, double layoutX, double layoutY) {
        VBox interfaceBox = new VBox();
        interfaceBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        interfaceBox.setSpacing(0);

        // Create the "<<Interface>>" label
        Label interfaceLabel = new Label("<<Interface>>");
        interfaceLabel.setStyle("-fx-padding: 1;");
        interfaceLabel.setAlignment(Pos.CENTER);

        Label interfaceNameLabel = new Label(name);
        interfaceNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 1;");
        interfaceNameLabel.setAlignment(Pos.CENTER);
        interfaceNameLabel.setMaxWidth(Double.MAX_VALUE);
        interfaceNameLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-padding: 1;");

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        interfaceBox.getChildren().addAll(interfaceLabel, interfaceNameLabel, methodsBox);
        interfaceBox.setLayoutX(layoutX);
        interfaceBox.setLayoutY(layoutY);

        interfaceBox.setOnMouseClicked(event -> uiController.openInterfaceEditor(interfaceBox));
        uiController.getDrawingPane().getChildren().add(interfaceBox);
        elements.add(interfaceBox);
        setDraggable(interfaceBox, isDragEnabled);

        // Update the ClassDiagram model
        UMLInterfaceBox umlInterfaceBox = new UMLInterfaceBox(name, layoutX, layoutY, interfaceBox);
        classDiagram.getInterfaces().add(umlInterfaceBox);
    }

    /**
     * Creates a relationship based on serialized data.
     *
     * @param umlRelationship The UMLRelationship data.
     */
    public void createRelationshipFromSerialization(UMLRelationship umlRelationship) {
        String type = umlRelationship.getType();
        String startName = umlRelationship.getStartElementName();
        System.out.println("Start Element Name: " + startName);
        String endName = umlRelationship.getEndElementName();
        System.out.println("End Element Name: " + endName);
        String name = umlRelationship.getName();
        String startMultiplicity = umlRelationship.getStartMultiplicity();
        String endMultiplicity = umlRelationship.getEndMultiplicity();

        VBox startBox = findClassBoxByName(startName);
        if (startBox == null) startBox = findInterfaceBoxByName(startName);
        VBox endBox = findClassBoxByName(endName);
        if (endBox == null) endBox = findInterfaceBoxByName(endName);

        if (startBox == null || endBox == null) {
            System.err.println("Could not find elements for relationship: " + name);
            return;
        }

        // Depending on the relationship type, use the appropriate manager
        switch (type.toLowerCase()) {
            case "association":
                AssociationManager associationManager = new AssociationManager(this);
                associationManager.createRelationshipFromModel(umlRelationship, uiController.getDrawingPane());
                break;
            /*case "aggregation":
                AggregationManager aggregationManager = new AggregationManager(this);
                aggregationManager.createRelationshipFromModel(umlRelationship, uiController.getDrawingPane());
                break;
            case "composition":
                CompositionManager compositionManager = new CompositionManager(this);
                compositionManager.createRelationshipFromModel(umlRelationship, uiController.getDrawingPane());
                break;
            case "inheritance":
                InheritanceManager inheritanceManager = new InheritanceManager(this);
                inheritanceManager.createRelationshipFromModel(umlRelationship, uiController.getDrawingPane());
                break;*/
            /*default:
                System.err.println("Unknown relationship type: " + type);*/
        }
    }

    /**
     * Finds a class box by its name.
     *
     * @param name The name of the class.
     * @return The VBox representing the class, or null if not found.
     */
    public VBox findClassBoxByName(String name) {
        for (UMLClassBox classBox : classDiagram.getClasses()) {
            if (classBox.getName().equals(name)) {
                System.out.println("Class names:" + classBox.getName());
                if(classBox.getVisualRepresentation() == null)
                    System.out.println("No visual representation is available.");
                return classBox.getVisualRepresentation();
            }
        }
        return null;
    }

    /**
     * Finds an interface box by its name.
     *
     * @param name The name of the interface.
     * @return The VBox representing the interface, or null if not found.
     */
    public VBox findInterfaceBoxByName(String name) {
        for (UMLInterfaceBox interfaceBox : classDiagram.getInterfaces()) {
            if (interfaceBox.getName().equals(name)) {
                System.out.println("Interface names:" + interfaceBox.getName());
                if(interfaceBox.getVisualRepresentation() == null)
                    System.out.println("No visual representation is available.");
                return interfaceBox.getVisualRepresentation();
            }
        }
        return null;
    }

    /**
     * Adds a relationship to the model and UI.
     *
     * @param relationshipBox The UMLRelationshipBox to add.
     */
    public void addRelationshipBox(UMLRelationshipBox relationshipBox) {
        if (relationshipBox != null) {
            classDiagram.getRelationships().add(relationshipBox.getUmlRelationship());
            // No separate relationships list, so no need to add to it
        }
    }

    /**
     * Saves the current UML diagram to a JSON file.
     *
     * @param file The file to save the JSON to.
     */
    public void saveDiagram(File file) {
        try {
            serializer.serialize(classDiagram, file);
            uiController.showInformationAlert("Diagram saved successfully to " + file.getAbsolutePath());
            System.out.println("Diagram saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            uiController.showErrorAlert("Failed to save diagram.");
        }
    }

    /**
     * Loads a UML diagram from a JSON file.
     *
     * @param file The JSON file to load.
     */
    public void loadDiagram(File file) {
        try {
            System.out.println("Loading diagram from file: " + file.getAbsolutePath());
            ClassDiagramD loadedDiagram = serializer.deserialize(file);
            if (loadedDiagram == null) {
                throw new IOException("Deserialized diagram is null.");
            }
            System.out.println("Diagram deserialized successfully.");
            this.classDiagram = loadedDiagram;

            // Log restore process
            System.out.println("Restoring diagram...");
            serializer.restoreDiagram(loadedDiagram, this);
            uiController.showInformationAlert("Diagram loaded successfully from " + file.getAbsolutePath());
            System.out.println("Diagram loaded successfully from " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            uiController.showErrorAlert("Failed to load diagram.");
        }
    }


    /**
     * Handles tool selection and adds corresponding UML elements to the diagram.
     *
     * @param tool        The tool selected (e.g., Class, Interface, Association).
     * @param drawingPane The pane where elements are drawn.
     * @param editorsPane The pane for editors.
     */
    public void handleToolSelection(String tool, Pane drawingPane, VBox editorsPane) {
        switch (tool) {
            case "Class":
                disableDrag();
                createClassBox("Class" + (classDiagram.getClasses().size() + 1), 100 + classDiagram.getClasses().size() * 50, 100);
                break;
            case "Interface":
                disableDrag();
                createInterfaceBox("Interface" + (classDiagram.getInterfaces().size() + 1), 200 + classDiagram.getInterfaces().size() * 50, 100);
                break;
            case "Association":
                disableDrag();
                relationsManager = new AssociationManager(this);
                enableAssociationMode(drawingPane);
                break;
            case "Aggregation":
                disableDrag();
                relationsManager = new AggregationManager(this);
                enableAggregationMode(drawingPane);
                break;
            case "Composition":
                disableDrag();
                relationsManager = new CompositionManager(this);
                enableCompositionMode(drawingPane);
                break;
            case "Inheritance":
                disableDrag();
                relationsManager = new InheritanceManager(this);
                enableInheritanceMode(drawingPane);
                break;
            case "Drag":
                enableDragMode();
                break;
            default:
                System.err.println("Unknown tool selected: " + tool);
        }
    }

    /**
     * Enables association mode and handles user interactions.
     *
     * @param drawingPane The pane where elements are drawn.
     */
    private void enableAssociationMode(Pane drawingPane) {
        relationsManager.enableAssociationMode();

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());
            if (target instanceof VBox && elements.contains(target)) {
                if (!(relationsManager instanceof AssociationManager)) {
                    return; // Skip if association mode is not enabled
                }

                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);
                } else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);

                    // Show dialog for association details
                    Dialog<List<String>> dialog = new Dialog<>();
                    dialog.setTitle("Set Association Details");
                    dialog.setHeaderText("Enter Association Name and Multiplicities");

                    TextField associationNameField = new TextField();
                    associationNameField.setPromptText("Association Name");
                    TextField startMultiplicityField = new TextField();
                    startMultiplicityField.setPromptText("Start Multiplicity");
                    TextField endMultiplicityField = new TextField();
                    endMultiplicityField.setPromptText("End Multiplicity");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.add(new Label("Association Name:"), 0, 0);
                    grid.add(associationNameField, 1, 0);
                    grid.add(new Label("Start Multiplicity:"), 0, 1);
                    grid.add(startMultiplicityField, 1, 1);
                    grid.add(new Label("End Multiplicity:"), 0, 2);
                    grid.add(endMultiplicityField, 1, 2);

                    dialog.getDialogPane().setContent(grid);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            List<String> result = new ArrayList<>();
                            result.add(associationNameField.getText());
                            result.add(startMultiplicityField.getText());
                            result.add(endMultiplicityField.getText());
                            return result;
                        }
                        return null;
                    });

                    Optional<List<String>> result = dialog.showAndWait();
                    result.ifPresent(values -> {
                        String associationName = values.get(0).isEmpty() ? "associationName" : values.get(0);
                        String startMultiplicity = values.get(1).isEmpty() ? "1" : values.get(1);
                        String endMultiplicity = values.get(2).isEmpty() ? "1" : values.get(2);

                        // Use AssociationManager to create the relationship
                        ((AssociationManager) relationsManager).createRelationship(
                                firstSelectedElement,
                                secondSelectedElement,
                                drawingPane,
                                associationName,
                                startMultiplicity,
                                endMultiplicity
                        );
                    });

                    // Reset the selected elements
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }

    /**
     * Enables aggregation mode and handles user interactions.
     *
     * @param drawingPane The pane where elements are drawn.
     */
    private void enableAggregationMode(Pane drawingPane) {
        relationsManager.enableAggregationMode();

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());
            if (target instanceof VBox && elements.contains(target)) {
                if (!(relationsManager instanceof AggregationManager)) {
                    return; // Skip if aggregation mode is not enabled
                }

                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);
                } else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);

                    // Show dialog for aggregation details
                    Dialog<List<String>> dialog = new Dialog<>();
                    dialog.setTitle("Set Aggregation Details");
                    dialog.setHeaderText("Enter Aggregation Name and Multiplicities");

                    TextField aggregationNameField = new TextField();
                    aggregationNameField.setPromptText("Aggregation Name");
                    TextField startMultiplicityField = new TextField();
                    startMultiplicityField.setPromptText("Start Multiplicity");
                    TextField endMultiplicityField = new TextField();
                    endMultiplicityField.setPromptText("End Multiplicity");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.add(new Label("Aggregation Name:"), 0, 0);
                    grid.add(aggregationNameField, 1, 0);
                    grid.add(new Label("Start Multiplicity:"), 0, 1);
                    grid.add(startMultiplicityField, 1, 1);
                    grid.add(new Label("End Multiplicity:"), 0, 2);
                    grid.add(endMultiplicityField, 1, 2);

                    dialog.getDialogPane().setContent(grid);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            List<String> result = new ArrayList<>();
                            result.add(aggregationNameField.getText());
                            result.add(startMultiplicityField.getText());
                            result.add(endMultiplicityField.getText());
                            return result;
                        }
                        return null;
                    });

                    Optional<List<String>> result = dialog.showAndWait();
                    result.ifPresent(values -> {
                        String aggregationName = values.get(0).isEmpty() ? "aggregationName" : values.get(0);
                        String startMultiplicity = values.get(1).isEmpty() ? "1" : values.get(1);
                        String endMultiplicity = values.get(2).isEmpty() ? "1" : values.get(2);

                        ((AggregationManager) relationsManager).createRelationship(
                                firstSelectedElement,
                                secondSelectedElement,
                                drawingPane,
                                aggregationName,
                                startMultiplicity,
                                endMultiplicity
                        );
                    });

                    // Reset the selected elements
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }

    /**
     * Enables composition mode and handles user interactions.
     *
     * @param drawingPane The pane where elements are drawn.
     */
    private void enableCompositionMode(Pane drawingPane) {
        relationsManager.enableCompositionMode();

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());

            if (target == null || !(target instanceof VBox)) {
                return; // Exit if no VBox was clicked
            }

            if (elements.contains(target)) {
                if (!(relationsManager instanceof CompositionManager)) {
                    return; // Skip if composition mode is not enabled
                }

                // Handle first selection
                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);  // Highlight the first selected element
                }
                // Handle second selection
                else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);  // Highlight the second selected element

                    // Show dialog for composition details
                    Dialog<List<String>> dialog = new Dialog<>();
                    dialog.setTitle("Set Composition Details");
                    dialog.setHeaderText("Enter Composition Name and Multiplicities");

                    TextField compositionNameField = new TextField();
                    compositionNameField.setPromptText("Composition Name");
                    TextField startMultiplicityField = new TextField();
                    startMultiplicityField.setPromptText("Start Multiplicity");
                    TextField endMultiplicityField = new TextField();
                    endMultiplicityField.setPromptText("End Multiplicity");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.add(new Label("Composition Name:"), 0, 0);
                    grid.add(compositionNameField, 1, 0);
                    grid.add(new Label("Start Multiplicity:"), 0, 1);
                    grid.add(startMultiplicityField, 1, 1);
                    grid.add(new Label("End Multiplicity:"), 0, 2);
                    grid.add(endMultiplicityField, 1, 2);

                    dialog.getDialogPane().setContent(grid);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            List<String> result = new ArrayList<>();
                            result.add(compositionNameField.getText());
                            result.add(startMultiplicityField.getText());
                            result.add(endMultiplicityField.getText());
                            return result;
                        }
                        return null;
                    });

                    Optional<List<String>> result = dialog.showAndWait();
                    result.ifPresent(values -> {
                        String compositionName = values.get(0).isEmpty() ? "compositionName" : values.get(0);
                        String startMultiplicity = values.get(1).isEmpty() ? "1" : values.get(1);
                        String endMultiplicity = values.get(2).isEmpty() ? "1" : values.get(2);

                        ((CompositionManager) relationsManager).createRelationship(
                                firstSelectedElement,
                                secondSelectedElement,
                                drawingPane,
                                compositionName,
                                startMultiplicity,
                                endMultiplicity
                        );
                    });

                    // Reset the selected elements
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }

    /**
     * Enables inheritance mode and handles user interactions.
     *
     * @param drawingPane The pane where elements are drawn.
     */
    private void enableInheritanceMode(Pane drawingPane) {
        relationsManager.enableInheritanceMode();

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());

            if (target == null || !(target instanceof VBox)) {
                return; // Exit if no VBox was clicked
            }

            if (elements.contains(target)) {
                if (!(relationsManager instanceof InheritanceManager)) {
                    return; // Skip if inheritance mode is not enabled
                }

                // Handle first selection
                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);  // Highlight the first selected element
                }
                // Handle second selection
                else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);  // Highlight the second selected element

                    // Create the inheritance relationship
                    ((InheritanceManager) relationsManager).createRelationship(
                            firstSelectedElement,
                            secondSelectedElement,
                            drawingPane,
                            null, null, null
                    );

                    // Reset the selected elements
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }

    /**
     * Retrieves the parent VBox of a given node.
     *
     * @param node The node to find the parent VBox for.
     * @return The parent VBox, or null if not found.
     */
    private VBox getParentVBox(Node node) {
        while (node != null && !(node instanceof VBox)) {
            node = node.getParent();
        }
        return (VBox) node; // Will return null if no VBox is found
    }

    /**
     * Enables drag mode for all elements.
     */
    private void enableDragMode() {
        isDragEnabled = true;
        for (Node element : elements) {
            if (element instanceof VBox) {
                setDraggable((VBox) element, true);
            }
        }
    }

    /**
     * Disables drag mode for all elements.
     */
    private void disableDrag() {
        isDragEnabled = false;
        for (Node element : elements) {
            if (element instanceof VBox) {
                setDraggable((VBox) element, false);
            }
        }
    }

    /**
     * Sets a VBox as draggable or not.
     *
     * @param pane   The VBox to set as draggable.
     * @param enable Whether to enable dragging.
     */
    private void setDraggable(VBox pane, boolean enable) {
        if (enable) {
            pane.setOnMousePressed(event -> pane.setUserData(new double[]{event.getSceneX(), event.getSceneY(), pane.getLayoutX(), pane.getLayoutY()}));
            pane.setOnMouseDragged(event -> {
                double[] data = (double[]) pane.getUserData();
                double offsetX = event.getSceneX() - data[0];
                double offsetY = event.getSceneY() - data[1];
                pane.setLayoutX(data[2] + offsetX);
                pane.setLayoutY(data[3] + offsetY);

                // Update model coordinates
                updateElementCoordinates(pane, data[2] + offsetX, data[3] + offsetY);
            });
        } else {
            pane.setOnMousePressed(null);
            pane.setOnMouseDragged(null);
        }
    }

    /**
     * Updates the coordinates of a UML element in the model based on its new position.
     *
     * @param pane The VBox representing the UML element.
     * @param newX The new X coordinate.
     * @param newY The new Y coordinate.
     */
    private void updateElementCoordinates(VBox pane, double newX, double newY) {
        for (UMLClassBox umlClass : classDiagram.getClasses()) {
            if (umlClass.getVisualRepresentation() == pane) {
                umlClass.setX(newX);
                umlClass.setY(newY);
                return;
            }
        }
        for (UMLInterfaceBox umlInterface : classDiagram.getInterfaces()) {
            if (umlInterface.getVisualRepresentation() == pane) {
                umlInterface.setX(newX);
                umlInterface.setY(newY);
                return;
            }
        }
        // Handle relationships if necessary
    }

    /**
     * Highlights or unhighlights a class/interface box.
     *
     * @param classBox The VBox representing the class/interface.
     * @param highlight Whether to highlight.
     */
    private void highlightClass(VBox classBox, boolean highlight) {
        if (classBox == null) {
            System.out.println("Attempting to highlight a null classBox!");
            return;
        }

        if (highlight) {
            classBox.setStyle("-fx-border-color: darkred; -fx-border-width: 2; -fx-border-style: solid;");
        } else {
            // Reset to default styles based on whether it's a class or interface
            // You might need additional logic to determine the type
            classBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-style: solid;");
        }
    }

    public UMLClassBox getClassByVBox(VBox classBox) {
        for (UMLClassBox umlClass : classDiagram.getClasses()) {
            if (umlClass.getVisualRepresentation() == classBox) {
                return umlClass;
            }
        }
        return null;
    }

    public UMLInterfaceBox getInterfaceByVBox(VBox interfaceBox) {
        for (UMLInterfaceBox umlInterface : classDiagram.getInterfaces()) {
            if (umlInterface.getVisualRepresentation() == interfaceBox) {
                return umlInterface;
            }
        }
        return null;
    }

    public void reCreateClassBox(String name, double layoutX, double layoutY, List<String> attributes, List<String> methods) {
        VBox classBox = new VBox();
        classBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        classBox.setSpacing(0);

        Label classNameLabel = new Label(name);
        classNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
        classNameLabel.setAlignment(Pos.CENTER);
        classNameLabel.setMaxWidth(Double.MAX_VALUE);

        VBox attributesBox = new VBox();
        attributesBox.setStyle("-fx-border-color: black; -fx-padding: 5;");
        for (String attribute : attributes) {
            attributesBox.getChildren().add(new Label(attribute));
        }

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");
        for (String method : methods) {
            methodsBox.getChildren().add(new Label(method));
        }

        classBox.getChildren().addAll(classNameLabel, attributesBox, methodsBox);
        classBox.setLayoutX(layoutX);
        classBox.setLayoutY(layoutY);

        classBox.setOnMouseClicked(event -> uiController.openClassEditor(classBox));
        uiController.getDrawingPane().getChildren().add(classBox);
        elements.add(classBox);
        setDraggable(classBox, isDragEnabled);

        // Update the ClassDiagram model
        UMLClassBox umlClassBox = new UMLClassBox(name, layoutX, layoutY, classBox);
        umlClassBox.setVisualRepresentation(classBox);
        classDiagram.getClasses().add(umlClassBox);
    }

    public void reCreateInterfaceBox(String name, double layoutX, double layoutY) {
        VBox interfaceBox = new VBox();
        interfaceBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        interfaceBox.setSpacing(0);

        Label interfaceLabel = new Label("<<Interface>>");
        interfaceLabel.setStyle("-fx-padding: 1;");
        interfaceLabel.setAlignment(Pos.CENTER);

        Label interfaceNameLabel = new Label(name);
        interfaceNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 1;");
        interfaceNameLabel.setAlignment(Pos.CENTER);
        interfaceNameLabel.setMaxWidth(Double.MAX_VALUE);

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        interfaceBox.getChildren().addAll(interfaceLabel, interfaceNameLabel, methodsBox);
        interfaceBox.setLayoutX(layoutX);
        interfaceBox.setLayoutY(layoutY);

        interfaceBox.setOnMouseClicked(event -> uiController.openInterfaceEditor(interfaceBox));
        uiController.getDrawingPane().getChildren().add(interfaceBox);
        elements.add(interfaceBox);
        setDraggable(interfaceBox, isDragEnabled);

        // Update the ClassDiagram model
        UMLInterfaceBox umlInterfaceBox = new UMLInterfaceBox(name, layoutX, layoutY, interfaceBox);
        classDiagram.getInterfaces().add(umlInterfaceBox);
    }

}




/*import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDiagramManager {
    private final ArrayList<Node> elements = new ArrayList<>();
    private VBox firstSelectedElement = null;
    private VBox secondSelectedElement = null;

    private final List<UMLClassBox> classes = new ArrayList<>();
    private final List<UMLInterfaceBox> interfaces = new ArrayList<>();
    private final List<UMLRelationshipBox> relationships = new ArrayList<>();

    private final ClassDiagramSerializer serializer;

    private final ClassDiagramUI uiController;
    private ClassDiagramRelationsManager relationsManager;
    private ClassDiagramD classDiagram;
    private boolean isDragEnabled = false;

    /*public ClassDiagramManager(ClassDiagramUI uiController) {
        this.uiController = uiController;
        this.relationsManager = new AssociationManager(this); // Set the correct manager
        this.relationsManager= new AggregationManager(this);

        this.relationsManager= new CompositionManager(this);
        this.relationsManager= new InheritanceManager(this);

        this.classDiagram = new ClassDiagramD();
        this.serializer = new ClassDiagramSerializer();
    }

    public ClassDiagramManager() {
        uiController = new ClassDiagramUI();
        serializer = new ClassDiagramSerializer();
        classDiagram = new ClassDiagramD();
    }*/

    /*public ClassDiagramManager(ClassDiagramUI uiController) {
        this.uiController = uiController;
        this.relationsManager = null; // Initialize as null; set appropriately during tool selection
        this.classDiagram = new ClassDiagramD();
        this.serializer = new ClassDiagramSerializer();
    }


    // Getters for serialization

    public List<UMLClassBox> getClasses() {
        return classes;
    }

    public List<UMLInterfaceBox> getInterfaces() {
        return interfaces;
    }

    public List<UMLRelationshipBox> getRelationships() {
        return relationships;
    }

    /**
     * Clears all elements from the diagram.
     */
    /*public void clearDiagram() {
        uiController.getDrawingPane().getChildren().clear();
        classes.clear();
        interfaces.clear();
        relationships.clear();
    }

    /**
     * Creates a class box at the specified position and adds it to the diagram.
     *
     * @param name    The name of the class.
     * @param layoutX The X position.
     * @param layoutY The Y position.
     */
    /*public void reCreateClassBox(String name, double layoutX, double layoutY) {
        VBox classBox = new VBox();
        classBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        classBox.setSpacing(0);

        Label classNameLabel = new Label(name);
        classNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
        classNameLabel.setAlignment(Pos.CENTER);
        classNameLabel.setMaxWidth(Double.MAX_VALUE);
        classNameLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-padding: 5;");

        VBox attributesBox = new VBox();
        attributesBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        classBox.getChildren().addAll(classNameLabel, attributesBox, methodsBox);
        classBox.setLayoutX(layoutX);
        classBox.setLayoutY(layoutY);

        classBox.setOnMouseClicked(event -> uiController.openClassEditor(classBox));
        uiController.getDrawingPane().getChildren().add(classBox);
        elements.add(classBox);
        setDraggable(classBox, false);

        // Update the ClassDiagram model
        UMLClassBox umlClassBox = new UMLClassBox(name, layoutX, layoutY, classBox);
        classes.add(umlClassBox);
        classDiagram.getClasses().add(umlClassBox);
    }

    /**
     * Creates an interface box at the specified position and adds it to the diagram.
     *
     * @param name    The name of the interface.
     * @param layoutX The X position.
     * @param layoutY The Y position.
     */
    /*public void reCreateInterfaceBox(String name, double layoutX, double layoutY) {
        VBox interfaceBox = new VBox();
        interfaceBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        interfaceBox.setSpacing(0);

        Label interfaceLabel = new Label("<<Interface>>");
        interfaceLabel.setStyle("-fx-padding: 1;");
        interfaceLabel.setAlignment(Pos.CENTER);

        Label interfaceNameLabel = new Label(name);
        interfaceNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 1;");
        interfaceNameLabel.setAlignment(Pos.CENTER);
        interfaceNameLabel.setMaxWidth(Double.MAX_VALUE);
        interfaceNameLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-padding: 1;");

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        interfaceBox.getChildren().addAll(interfaceLabel, interfaceNameLabel, methodsBox);
        interfaceBox.setLayoutX(layoutX);
        interfaceBox.setLayoutY(layoutY);

        interfaceBox.setOnMouseClicked(event -> uiController.openInterfaceEditor(interfaceBox));
        uiController.getDrawingPane().getChildren().add(interfaceBox);
        elements.add(interfaceBox);
        setDraggable(interfaceBox, false);

        // Update the ClassDiagram model
        UMLInterfaceBox umlInterfaceBox = new UMLInterfaceBox(name, layoutX, layoutY, interfaceBox);
        interfaces.add(umlInterfaceBox);
        classDiagram.getInterfaces().add(umlInterfaceBox);
    }


    /**
     * Creates a relationship based on serialized data.
     *
     * @param umlRelationship The UMLRelationship data.
     */
    /*public void createRelationshipFromSerialization(UMLRelationship umlRelationship) {
        String type = umlRelationship.getType();
        String startName = umlRelationship.getStartElementName();
        String endName = umlRelationship.getEndElementName();
        String name = umlRelationship.getName();
        String startMultiplicity = umlRelationship.getStartMultiplicity();
        String endMultiplicity = umlRelationship.getEndMultiplicity();

        VBox startBox = findClassBoxByName(startName);
        if (startBox == null) startBox = findInterfaceBoxByName(startName);
        VBox endBox = findClassBoxByName(endName);
        if (endBox == null) endBox = findInterfaceBoxByName(endName);

        if (startBox == null || endBox == null) {
            System.err.println("Could not find elements for relationship: " + name);
            return;
        }

        // Depending on the relationship type, use the appropriate manager
        switch (type.toLowerCase()) {
            case "association":
                AssociationManager associationManager = new AssociationManager(this);
                associationManager.createRelationship(startBox, endBox, uiController.getDrawingPane(), name, startMultiplicity, endMultiplicity);
                addRelationshipBox(associationManager.getLastRelationshipBox());
                break;
            case "aggregation":
                AggregationManager aggregationManager = new AggregationManager(this);
                aggregationManager.createRelationship(startBox, endBox, uiController.getDrawingPane(), name, startMultiplicity, endMultiplicity);
                addRelationshipBox(aggregationManager.getLastRelationshipBox());
                break;
            case "composition":
                CompositionManager compositionManager = new CompositionManager(this);
                compositionManager.createRelationship(startBox, endBox, uiController.getDrawingPane(), name, startMultiplicity, endMultiplicity);
                addRelationshipBox(compositionManager.getLastRelationshipBox());
                break;
            case "inheritance":
                InheritanceManager inheritanceManager = new InheritanceManager(this);
                inheritanceManager.createRelationship(startBox, endBox, uiController.getDrawingPane(), name, startMultiplicity, endMultiplicity);
                addRelationshipBox(inheritanceManager.getLastRelationshipBox());
                break;
            default:
                System.err.println("Unknown relationship type: " + type);
        }
    }


    /**
     * Finds a class box by its name.
     *
     * @param name The name of the class.
     * @return The VBox representing the class, or null if not found.
     */
    /*private VBox findClassBoxByName(String name) {
        for (UMLClassBox classBox : classes) {
            if (classBox.getName().equals(name)) {
                return classBox.getVBox();
            }
        }
        return null;
    }

    /**
     * Finds an interface box by its name.
     *
     * @param name The name of the interface.
     * @return The VBox representing the interface, or null if not found.
     */
    /*private VBox findInterfaceBoxByName(String name) {
        for (UMLInterfaceBox interfaceBox : interfaces) {
            if (interfaceBox.getName().equals(name)) {
                return interfaceBox.getVBox();
            }
        }
        return null;
    }

    /**
     * Adds a relationship to the relationships list.
     *
     * @param relationshipBox The UMLRelationshipBox to add.
     */
    /*public void addRelationshipBox(UMLRelationshipBox relationshipBox) {
        if (relationshipBox != null) {
            relationships.add(relationshipBox);
        }
    }

    /**
     * Saves the current UML diagram to a JSON file.
     *
     * @param file The file to save the JSON to.
     */
    /*public void saveDiagram(File file) {
        try {
            serializer.serialize(classDiagram, file);
            uiController.showInformationAlert("Diagram saved successfully to " + file.getAbsolutePath());
            System.out.println("Diagram saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            uiController.showErrorAlert("Failed to save diagram.");
        }
    }


    public void handleToolSelection(String tool, Pane drawingPane, VBox editorsPane) {
        if ("Class".equals(tool)) {
            disableDrag();
            createClassBox("Class" + (elements.size() + 1), drawingPane);
        } else if ("Interface".equals(tool)) {
            disableDrag();
            createInterfaceBox("Interface" + (elements.size() + 1), drawingPane);
        } else if ("Association".equals(tool)) {
            disableDrag();
            relationsManager = new AssociationManager();  // Assign AssociationManager
            enableAssociationMode(drawingPane); // Enable association mode
        } else if ("Drag".equals(tool)) {
            enableDragMode();
        } else if ("Aggregation".equals(tool)) {
            disableDrag();
            relationsManager = new AggregationManager();  // Assign AggregationManager
            enableAggregationMode(drawingPane);
        } else if ("Composition".equals(tool)) {
            disableDrag();
            relationsManager = new CompositionManager();  // Assign CompositionManager
            enableCompositionMode(drawingPane);
        } else if ("Inheritance".equals(tool)) {
            disableDrag();
            relationsManager = new InheritanceManager();  // Correctly assign InheritanceManager
            enableInheritanceMode(drawingPane); // Enable inheritance mode
        }

    }

    private void createClassBox(String name, Pane drawingPane) {
        VBox classBox = new VBox();
        classBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        classBox.setSpacing(0);

        Label classNameLabel = new Label(name);
        classNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
        classNameLabel.setAlignment(Pos.CENTER);

        // Ensure the label takes the full width of the VBox
        classNameLabel.setMaxWidth(Double.MAX_VALUE);
        classNameLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-padding: 5;");

        VBox attributesBox = new VBox();
        attributesBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        classBox.getChildren().addAll(classNameLabel, attributesBox, methodsBox);
        classBox.setLayoutX(100 + elements.size() * 50);
        classBox.setLayoutY(100);

        classBox.setOnMouseClicked(event -> uiController.openClassEditor(classBox));
        drawingPane.getChildren().add(classBox);
        elements.add(classBox);
        setDraggable(classBox, false);
    }

    // New method for creating Interface Box
    private void createInterfaceBox(String name, Pane drawingPane) {
        VBox interfaceBox = new VBox();
        interfaceBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        interfaceBox.setSpacing(0);

        // Create the "<<Interface>>" label
        Label interfaceLabel = new Label("<<Interface>>");
        interfaceLabel.setStyle(" -fx-padding: 1;");
        interfaceLabel.setAlignment(Pos.CENTER);

        Label interfaceNameLabel = new Label(name);
        interfaceNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 1;");
        interfaceNameLabel.setAlignment(Pos.CENTER);

        // Ensure the label takes the full width of the VBox
        interfaceNameLabel.setMaxWidth(Double.MAX_VALUE);
        interfaceNameLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-padding: 1;");

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        interfaceBox.getChildren().addAll(interfaceLabel, interfaceNameLabel, methodsBox);
        interfaceBox.setLayoutX(100 + elements.size() * 50);
        interfaceBox.setLayoutY(100);

        interfaceBox.setOnMouseClicked(event -> uiController.openInterfaceEditor(interfaceBox));
        drawingPane.getChildren().add(interfaceBox);
        elements.add(interfaceBox);
        setDraggable(interfaceBox, false);
    }

    private void enableAssociationMode(Pane drawingPane) {
        relationsManager.enableAssociationMode(); // Enable association mode

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());
            if (target instanceof VBox && elements.contains(target)) {
                if (!(relationsManager instanceof AssociationManager)) {
                    return; // Skip if association mode is not enabled
                }


                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);
                } else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);

                    // Show dialog for association details
                    Dialog<List<String>> dialog = new Dialog<>();
                    dialog.setTitle("Set Association Details");
                    dialog.setHeaderText("Enter Association Name and Multiplicities");

                    TextField associationNameField = new TextField();
                    associationNameField.setPromptText("Association Name");
                    TextField startMultiplicityField = new TextField();
                    startMultiplicityField.setPromptText("Start Multiplicity");
                    TextField endMultiplicityField = new TextField();
                    endMultiplicityField.setPromptText("End Multiplicity");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.add(new Label("Association Name:"), 0, 0);
                    grid.add(associationNameField, 1, 0);
                    grid.add(new Label("Start Multiplicity:"), 0, 1);
                    grid.add(startMultiplicityField, 1, 1);
                    grid.add(new Label("End Multiplicity:"), 0, 2);
                    grid.add(endMultiplicityField, 1, 2);

                    dialog.getDialogPane().setContent(grid);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            List<String> result = new ArrayList<>();
                            result.add(associationNameField.getText());
                            result.add(startMultiplicityField.getText());
                            result.add(endMultiplicityField.getText());
                            return result;
                        }
                        return null;
                    });

                    Optional<List<String>> result = dialog.showAndWait();
                    result.ifPresent(values -> {
                        String associationName = values.get(0).isEmpty() ? "associationName" : values.get(0);
                        String startMultiplicity = values.get(1).isEmpty() ? "1" : values.get(1);
                        String endMultiplicity = values.get(2).isEmpty() ? "1" : values.get(2);

                        // Use AssociationManager to create the relationship
                        ((AssociationManager) relationsManager).createRelationship(
                                firstSelectedElement,
                                secondSelectedElement,
                                drawingPane,
                                associationName,
                                startMultiplicity,
                                endMultiplicity
                        );
                    });

                    // Reset the selected elements
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }

    private void enableAggregationMode(Pane drawingPane) {
        relationsManager.enableAggregationMode(); // Enable aggregation mode

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());
            if (target instanceof VBox && elements.contains(target)) {
                if (!(relationsManager instanceof AggregationManager)) {
                    return; // Skip if association mode is not enabled
                }

                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);
                } else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);

                    // Show dialog for Aggregation details
                    Dialog<List<String>> dialog = new Dialog<>();
                    dialog.setTitle("Set Aggregation Details");
                    dialog.setHeaderText("Enter Aggregation Name and Multiplicities");

                    TextField aggregationNameField = new TextField();
                    aggregationNameField.setPromptText("Aggregation Name");
                    TextField startMultiplicityField = new TextField();
                    startMultiplicityField.setPromptText("Start Multiplicity");
                    TextField endMultiplicityField = new TextField();
                    endMultiplicityField.setPromptText("End Multiplicity");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.add(new Label("Aggregation Name:"), 0, 0);
                    grid.add(aggregationNameField, 1, 0);
                    grid.add(new Label("Start Multiplicity:"), 0, 1);
                    grid.add(startMultiplicityField, 1, 1);
                    grid.add(new Label("End Multiplicity:"), 0, 2);
                    grid.add(endMultiplicityField, 1, 2);

                    dialog.getDialogPane().setContent(grid);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            List<String> result = new ArrayList<>();
                            result.add(aggregationNameField.getText());
                            result.add(startMultiplicityField.getText());
                            result.add(endMultiplicityField.getText());
                            return result;
                        }
                        return null;
                    });

                    Optional<List<String>> result = dialog.showAndWait();
                    result.ifPresent(values -> {
                        String aggregationName = values.get(0).isEmpty() ? "aggregationName" : values.get(0);
                        String startMultiplicity = values.get(1).isEmpty() ? "1" : values.get(1);
                        String endMultiplicity = values.get(2).isEmpty() ? "1" : values.get(2);

                        ((AggregationManager) relationsManager).createRelationship(
                                firstSelectedElement,
                                secondSelectedElement,
                                drawingPane,
                                aggregationName,
                                startMultiplicity,
                                endMultiplicity
                        );
                    });

                    // Reset the selected elements
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }
    private void enableCompositionMode(Pane drawingPane) {
        relationsManager.enableCompositionMode(); // Enable aggregation mode

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());
            if (target instanceof VBox && elements.contains(target)) {
                if (!(relationsManager instanceof CompositionManager)) {
                    return; // Skip if association mode is not enabled
                }

                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);
                } else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);

                    // Show dialog for Aggregation details
                    Dialog<List<String>> dialog = new Dialog<>();
                    dialog.setTitle("Set Composition Details");
                    dialog.setHeaderText("Enter Composition Name and Multiplicities");

                    TextField aggregationNameField = new TextField();
                    aggregationNameField.setPromptText("Composition Name");
                    TextField startMultiplicityField = new TextField();
                    startMultiplicityField.setPromptText("Start Multiplicity");
                    TextField endMultiplicityField = new TextField();
                    endMultiplicityField.setPromptText("End Multiplicity");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.add(new Label("Composition Name:"), 0, 0);
                    grid.add(aggregationNameField, 1, 0);
                    grid.add(new Label("Start Multiplicity:"), 0, 1);
                    grid.add(startMultiplicityField, 1, 1);
                    grid.add(new Label("End Multiplicity:"), 0, 2);
                    grid.add(endMultiplicityField, 1, 2);

                    dialog.getDialogPane().setContent(grid);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            List<String> result = new ArrayList<>();
                            result.add(aggregationNameField.getText());
                            result.add(startMultiplicityField.getText());
                            result.add(endMultiplicityField.getText());
                            return result;
                        }
                        return null;
                    });

                    Optional<List<String>> result = dialog.showAndWait();
                    result.ifPresent(values -> {
                        String aggregationName = values.get(0).isEmpty() ? "compositionName" : values.get(0);
                        String startMultiplicity = values.get(1).isEmpty() ? "1" : values.get(1);
                        String endMultiplicity = values.get(2).isEmpty() ? "1" : values.get(2);

                        ((CompositionManager) relationsManager).createRelationship(
                                firstSelectedElement,
                                secondSelectedElement,
                                drawingPane,
                                aggregationName,
                                startMultiplicity,
                                endMultiplicity
                        );
                    });

                    // Reset the selected elements
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }

    /*private void enableInheritanceMode(Pane drawingPane) {
        relationsManager.enableInheritanceMode(); // Enable aggregation mode

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());
            if (target instanceof VBox && elements.contains(target)) {
                if (!(relationsManager instanceof InheritanceManager)) {
                    return; // Skip if association mode is not enabled
                }

                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);
                } else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);


                    ((InheritanceManager) relationsManager).createRelationship(
                            firstSelectedElement,
                            secondSelectedElement,
                            drawingPane, null, null, null

                    );
                }

                // Reset the selected elements
                highlightClass(firstSelectedElement, false);
                highlightClass(secondSelectedElement, false);
                firstSelectedElement = null;
                secondSelectedElement = null;
            }
        });
    }*/
    /*private void enableInheritanceMode(Pane drawingPane) {
        relationsManager.enableInheritanceMode(); // Enable inheritance mode

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());

            if (target == null || !(target instanceof VBox)) {
                return; // Exit if no VBox was clicked
            }

            if (elements.contains(target)) {
                if (!(relationsManager instanceof InheritanceManager)) {
                    return; // Skip if inheritance mode is not enabled
                }

                // Handle first selection
                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);  // Highlight the first selected element
                }
                // Handle second selection
                else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);  // Highlight the second selected element

                    // Create the inheritance relationship
                    ((InheritanceManager) relationsManager).createRelationship(
                            firstSelectedElement,
                            secondSelectedElement,
                            drawingPane, null, null, null
                    );
                }

                // Reset the selected elements after the relationship is created
                if (firstSelectedElement != null && secondSelectedElement != null) {
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }



    /*private Node getParentVBox(Node node) {
        while (node != null && !(node instanceof VBox)) {
            node = node.getParent();
        }
        return node;
    }*/
    /*private VBox getParentVBox(Node node) {
        while (node != null && !(node instanceof VBox)) {
            node = node.getParent();
        }
        return (VBox) node; // Will return null if no VBox is found
    }*/

/*    private VBox getParentVBox(Node node) {
        while (node != null && !(node instanceof VBox)) {
            node = node.getParent();
        }
        return (VBox) node;
    }
*/
/*
private void enableDragMode() {
    isDragEnabled = true;
    for (Node element : elements) {
        if (element instanceof VBox) {
            setDraggable((VBox) element, true);
        }
    }
}

private void disableDrag() {
    isDragEnabled = false;
    for (Node element : elements) {
        if (element instanceof VBox) {
            setDraggable((VBox) element, false);
        }
    }
}

private void setDraggable(VBox pane, boolean enable) {
    if (enable) {
        pane.setOnMousePressed(event -> pane.setUserData(new double[]{event.getSceneX(), event.getSceneY(), pane.getLayoutX(), pane.getLayoutY()}));
        pane.setOnMouseDragged(event -> {
            double[] data = (double[]) pane.getUserData();
            double offsetX = event.getSceneX() - data[0];
            double offsetY = event.getSceneY() - data[1];
            pane.setLayoutX(data[2] + offsetX);
            pane.setLayoutY(data[3] + offsetY);
        });
    } else {
        pane.setOnMousePressed(null);
        pane.setOnMouseDragged(null);
    }
}

    private void highlightClass(VBox classBox, boolean highlight) {
        if (classBox == null) {
            System.out.println("Attempting to highlight a null classBox!");
            return;
        }

        if (highlight) {
            classBox.setStyle("-fx-border-color: darkred; -fx-border-width: 2; -fx-border-style: solid;");
        } else {
            classBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-style: solid;");
        }
    }
}*/
