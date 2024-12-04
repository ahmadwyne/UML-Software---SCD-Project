package com.example.umlscd;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private Map<String, UMLElementBoxInterface> classBoxMap = new HashMap<>();

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

        // Debug Logging: Current classBoxMap Keys
        System.out.println("Current classBoxMap keys: " + classBoxMap.keySet());

        // Lookup the class boxes
        UMLElementBoxInterface startClass = classBoxMap.get(startName);
        if (startClass == null) {
            System.out.println("ClassBox not found for: " + startName);
        }
        UMLElementBoxInterface endClass = classBoxMap.get(endName);
        if (endClass == null) {
            System.out.println("ClassBox not found for: " + endName);
        }

        if (startClass == null || endClass == null) {
            System.err.println("Could not find elements for relationship: " + name);
            return;
        }

        // Depending on the relationship type, use the appropriate manager
        switch (type.toLowerCase()) {
            case "association":
                AssociationManager associationManager = new AssociationManager(this);
                associationManager.createRelationshipFromModel(umlRelationship, uiController.getDrawingPane());
                break;
            // Uncomment and implement other relationship types as needed
            case "aggregation":
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
                break;
            default:
                System.err.println("Unknown relationship type: " + type);
        }
    }

    /**
     * Recreates a UML Class Box in the UI and updates the internal model.
     *
     * @param name       The name of the class.
     * @param layoutX    The X position.
     * @param layoutY    The Y position.
     * @param attributes The list of attributes.
     * @param methods    The list of methods.
     */
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

        // Use the corrected constructor
        UMLClassBox umlClassBox = new UMLClassBox(name, layoutX, layoutY, attributes, methods);
        umlClassBox.setVisualRepresentation(classBox);
        classDiagram.getClasses().add(umlClassBox);

        // Add to the mapping
        classBoxMap.put(name, umlClassBox);

        // Debug Logging
        System.out.println("Added class to classBoxMap: " + name + " -> " + umlClassBox);
    }

    /**
     * Recreates a UML Interface Box in the UI and updates the internal model.
     *
     * @param name    The name of the interface.
     * @param layoutX The X position.
     * @param layoutY The Y position.
     */
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

        // Add to the mapping
        classBoxMap.put(name, umlInterfaceBox);

        // Debug Logging
        System.out.println("Added interface to classBoxMap: " + name + " -> " + umlInterfaceBox);
    }

    /**
     * Getter for classBoxMap.
     *
     * @return The classBoxMap.
     */
    public Map<String, UMLElementBoxInterface> getClassBoxMap() {
        return classBoxMap;
    }

    /**
     * Adds a UMLRelationshipBox to the internal model.
     *
     * @param relationshipBox The UMLRelationshipBox to add.
     */
    public void addRelationshipBox(UMLRelationshipBox relationshipBox) {
        classDiagram.getRelationships().add(relationshipBox.getUmlRelationship());
    }

    /**
     * Updates all relationships that reference a renamed class.
     *
     * @param oldName The old name of the class.
     * @param newName The new name of the class.
     */
    public void updateRelationshipsForRenamedClass(String oldName, String newName) {
        for (UMLRelationship relationship : classDiagram.getRelationships()) {
            boolean updated = false;
            if (relationship.getStartElementName().equals(oldName)) {
                relationship.setStartElementName(newName);
                updated = true;
            }
            if (relationship.getEndElementName().equals(oldName)) {
                relationship.setEndElementName(newName);
                updated = true;
            }
            if (updated) {
                // Optionally, update the visual representation if necessary
                // For example, find the corresponding UMLRelationshipBox and update labels
                // This depends on your implementation
                // You might need to iterate over relationship boxes and update them
            }
        }
    }

    public ClassDiagramUI getUiController() {
        return uiController;
    }

    /**
     * Checks if a class or interface name already exists in the diagram.
     *
     * @param name The name to check.
     * @return True if the name exists, false otherwise.
     */
    public boolean isClassNameExists(String name) {
        return classBoxMap.containsKey(name);
    }


   /* // Method to delete the selected element (class)
    public void deleteSelectedElement(VBox selectedElement) {
        if (selectedElement != null) {
            String className = selectedElement.getId();  // Assuming each VBox has a unique ID (class name)

            // Remove the element from the drawing pane
            uiController.getDrawingPane().getChildren().remove(selectedElement);

            // Remove the element from the classBoxMap
            classBoxMap.remove(className);

            // Optionally, remove related relationships if any
           // deleteRelatedRelationships(className);
        }
    }*/

    // Method to delete the selected element (class) and any related relationships
    public void deleteSelectedElement(VBox selectedElement) {
        if (selectedElement != null) {
            String className = selectedElement.getId();  // Assuming each VBox has a unique ID (class name)

            // Remove the class from the drawing pane
            uiController.getDrawingPane().getChildren().remove(selectedElement);

            for(UMLRelationship rel : classDiagram.getRelationships()){

            }

            // Remove the class from the classBoxMap
            classBoxMap.remove(className);

            // Remove the class from the model (classDiagram) if it's part of the model
            classDiagram.removeRelationshipsByClassName(className);

            // Optionally, remove related relationships if any
            deleteRelatedRelationships(className);

            // You can add a confirmation or alert that the class has been deleted
            showDeletionConfirmation(className);
        }
    }

    /*// Helper method to delete related relationships of the class
    private void deleteRelatedRelationships(String className) {
        // Find and delete any relationships that involve this class
        List<UMLRelationship> relationshipsToRemove = new ArrayList<>();

        // Iterate over all the relationships in the class diagram
        for (UMLRelationship relationship : classDiagram.getRelationships()) {
            System.out.println(relationship.getStartElementName());
            System.out.println(relationship.getEndElementName());

            // Check if the relationship involves the class being deleted
            if (relationship.getStartElementName().equals(className) || relationship.getEndElementName().equals(className)) {
                relationshipsToRemove.add(relationship);
                System.out.println(relationship.getType());
            }
        }

        // Remove the relationships from the diagram
        for (UMLRelationship relationship : relationshipsToRemove) {
            // Remove from the drawing pane (if the relationship is represented visually)
            uiController.getDrawingPane().getChildren().remove(relationship.getType());

            // Remove from the class diagram model
            classDiagram.getRelationships().remove(relationship);  // This removes the relationship from the model
        }
    }*/


    private void deleteRelatedRelationships(String className) {
        // Collect relationships to remove
        List<UMLRelationship> relationshipsToRemove = classDiagram.getRelationships().stream()
                .filter(relationship -> relationship.getStartElementName().equals(className)
                        || relationship.getEndElementName().equals(className))
                .collect(Collectors.toList());

        // Remove each relationship from the diagram and UI
        for (UMLRelationship relationship : relationshipsToRemove) {
            // Remove the visual representation
            String visualElement = relationship.getType(); // Assuming getType() returns the Line/Node for the UI.
            if (visualElement != null) {
                uiController.getDrawingPane().getChildren().remove(visualElement);
            }

            // Remove the relationship from the data model
            classDiagram.getRelationships().remove(relationship);
        }
    }


    // Helper method to show a confirmation alert after deletion (optional)
    private void showDeletionConfirmation(String className) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Class " + className + " and its related relationships have been deleted.", ButtonType.OK);
        alert.showAndWait();
    }






    /*// Method to delete the selected relationship
    public void deleteSelectedRelationship(Node selectedRelationship) {
        if (selectedRelationship != null) {
            // Remove the relationship from the drawing pane
            uiController.getDrawingPane().getChildren().remove(selectedRelationship);

            // Remove the relationship from the model (ClassDiagramD)
            if (selectedRelationship instanceof Line) {
                // Find the corresponding UMLRelationship object in the model and remove it
                for (UMLRelationship relationship : classDiagram.getRelationships()) {
                    if (relationship.getType().equals(selectedRelationship)) {
                        classDiagram.getRelationships().remove(relationship);
                        break;
                    }
                }
            }
        }
    }*/

    public void deleteSelectedRelationship(Line line) {
        System.out.println("Attempting to delete line: " + line);
        uiController.getDrawingPane().getChildren().remove(line); // Remove from UI
        // Additional cleanup logic for labels or relationships
    }

    public void deleteSelectedRelationship(Node selectedRelationship) {
        if (selectedRelationship == null) {
            return; // No relationship selected
        }

        // Find and remove the corresponding UMLRelationship
        UMLRelationship toRemove = null;
        for (UMLRelationship relationship : classDiagram.getRelationships()) {
            if (relationship.getType().equals(selectedRelationship)) {
                toRemove = relationship;
                break;
            }
        }

        if (toRemove != null) {
            // Remove the visual representation
            uiController.getDrawingPane().getChildren().remove(selectedRelationship);

            // Remove from the data model
            classDiagram.getRelationships().remove(toRemove);

            // Optional: Show confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Relationship deleted successfully.", ButtonType.OK);
            alert.showAndWait();
        }
    }


}




