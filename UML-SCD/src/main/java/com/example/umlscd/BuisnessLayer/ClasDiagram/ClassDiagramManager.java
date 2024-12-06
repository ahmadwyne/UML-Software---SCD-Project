package com.example.umlscd.BuisnessLayer.ClasDiagram;

import com.example.umlscd.Models.ClassDiagram.*;
import com.example.umlscd.PresentationLayer.ClassDiagram.ClassDiagramUI;
import com.example.umlscd.DataAccessLayer.Serializers.ClassDiagram.ClassDiagramSerializer;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>Class Diagram Manager</h1>
 *
 * <p>The {@code ClassDiagramManager} class is responsible for managing the UML Class Diagram within the UML Editor application.
 * It handles the creation, manipulation, and serialization/deserialization of classes, interfaces, and their relationships.
 * The class interacts closely with the {@code ClassDiagramUI} to reflect changes in the user interface and ensure synchronization
 * between the model and the view.</p>
 *
 * <p>Key functionalities include:
 * <ul>
 *     <li>Adding and recreating class and interface boxes on the drawing pane.</li>
 *     <li>Managing relationships such as associations, aggregations, compositions, and inheritances.</li>
 *     <li>Handling serialization and deserialization of the class diagram to and from JSON files.</li>
 *     <li>Enabling and disabling drag functionality for moving diagram elements.</li>
 *     <li>Highlighting elements during user interactions for better visual feedback.</li>
 * </ul>
 * </p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class ClassDiagramManager {
    /**
     * A list of all drawable elements (e.g., classes, interfaces) present in the diagram.
     */
    private final ArrayList<Node> elements = new ArrayList<>();

    /**
     * The first selected element during relationship creation.
     */
    private VBox firstSelectedElement = null;

    /**
     * The second selected element during relationship creation.
     */
    private VBox secondSelectedElement = null;

    /**
     * Serializer for handling JSON serialization and deserialization of the class diagram.
     */
    private final ClassDiagramSerializer serializer;

    /**
     * Reference to the UI controller associated with this manager.
     */
    private final ClassDiagramUI uiController;

    /**
     * Manager for handling relationships within the class diagram.
     */
    private ClassDiagramRelationsManager relationsManager;

    /**
     * The underlying model representing the class diagram, including classes, interfaces, and relationships.
     */
    private ClassDiagramD classDiagram;

    /**
     * Flag indicating whether drag functionality is enabled for moving diagram elements.
     */
    private boolean isDragEnabled = false;

    /**
     * A mapping from element names to their corresponding UML element boxes (classes or interfaces).
     */
    private Map<String, UMLElementBoxInterface> classBoxMap = new HashMap<>();
    public ClassDiagramD getClassDiagram() {
        return classDiagram;
    }

    /**
     * Constructs a {@code ClassDiagramManager} with the specified UI controller.
     *
     * @param uiController The {@code ClassDiagramUI} instance associated with this manager.
     */
    public ClassDiagramManager(ClassDiagramUI uiController) {
        this.uiController = uiController;
        this.relationsManager = null; // Initialize as null; set appropriately during tool selection
        this.classDiagram = new ClassDiagramD();
        this.serializer = new ClassDiagramSerializer();
    }

    /**
     * Retrieves the list of UML class boxes in the diagram.
     *
     * @return A {@code List} of {@code UMLClassBox} instances.
     */
    public List<UMLClassBox> getClasses() {
        return classDiagram.getClasses();
    }

    /**
     * Retrieves the list of UML interface boxes in the diagram.
     *
     * @return A {@code List} of {@code UMLInterfaceBox} instances.
     */
    public List<UMLInterfaceBox> getInterfaces() {
        return classDiagram.getInterfaces();
    }

    /**
     * Retrieves the list of UML relationships in the diagram.
     *
     * @return A {@code List} of {@code UMLRelationship} instances.
     */
    public List<UMLRelationship> getRelationships() {
        return classDiagram.getRelationships();
    }

    /**
     * Clears all elements from the diagram, including classes, interfaces, and relationships.
     *
     * <p>This method removes all visual elements from the drawing pane and clears the underlying model.</p>
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
     * <p>This method initializes a new class box with the given name and position, adds it to the drawing pane,
     * and updates the underlying model accordingly.</p>
     *
     * @param name    The name of the class.
     * @param layoutX The X-coordinate position on the drawing pane.
     * @param layoutY The Y-coordinate position on the drawing pane.
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
     * <p>This method initializes a new interface box with the given name and position, adds it to the drawing pane,
     * and updates the underlying model accordingly.</p>
     *
     * @param name    The name of the interface.
     * @param layoutX The X-coordinate position on the drawing pane.
     * @param layoutY The Y-coordinate position on the drawing pane.
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
     * <p>This method searches through the list of classes in the model and returns the corresponding
     * {@code VBox} if a match is found.</p>
     *
     * @param name The name of the class to find.
     * @return The {@code VBox} representing the class, or {@code null} if not found.
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
     * <p>This method searches through the list of interfaces in the model and returns the corresponding
     * {@code VBox} if a match is found.</p>
     *
     * @param name The name of the interface to find.
     * @return The {@code VBox} representing the interface, or {@code null} if not found.
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
     * Saves the current UML diagram to a JSON file.
     *
     * <p>This method delegates the serialization process to the {@code ClassDiagramSerializer} and
     * provides user feedback upon successful or failed operations.</p>
     *
     * @param file The {@code File} object representing the destination JSON file.
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
     * <p>This method delegates the deserialization process to the {@code ClassDiagramSerializer}, updates
     * the internal model, and refreshes the UI to reflect the loaded diagram.</p>
     *
     * @param file The {@code File} object representing the source JSON file.
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
     * <p>This method determines the type of tool selected by the user (e.g., Class, Interface, Association)
     * and invokes the appropriate method to handle the creation or manipulation of diagram elements.</p>
     *
     * @param tool        The tool selected (e.g., "Class", "Interface", "Association", "Aggregation", "Composition", "Inheritance", "Drag").
     * @param drawingPane The {@code Pane} where elements are drawn.
     * @param editorsPane The {@code VBox} for displaying editors.
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
     * Enables association mode and handles user interactions for creating associations.
     *
     * <p>This method sets up mouse click events on the drawing pane to allow users to select two elements
     * between which an association will be created. Upon selecting both elements, a dialog prompts
     * the user to enter association details such as name and multiplicities.</p>
     *
     * @param drawingPane The {@code Pane} where elements are drawn.
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
                    relationsManager=null;
                }
            }
        });
    }

    /**
     * Enables aggregation mode and handles user interactions for creating aggregations.
     *
     * <p>This method sets up mouse click events on the drawing pane to allow users to select two elements
     * between which an aggregation will be created. Upon selecting both elements, a dialog prompts
     * the user to enter aggregation details such as name and multiplicities.</p>
     *
     * @param drawingPane The {@code Pane} where elements are drawn.
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
                    relationsManager=null;
                }
            }
        });
    }

    /**
     * Enables composition mode and handles user interactions for creating compositions.
     *
     * <p>This method sets up mouse click events on the drawing pane to allow users to select two elements
     * between which a composition will be created. Upon selecting both elements, a dialog prompts
     * the user to enter composition details such as name and multiplicities.</p>
     *
     * @param drawingPane The {@code Pane} where elements are drawn.
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
                    relationsManager=null;
                }
            }
        });
    }

    /**
     * Enables inheritance mode and handles user interactions for creating inheritances.
     *
     * <p>This method sets up mouse click events on the drawing pane to allow users to select two elements
     * between which an inheritance will be created. Upon selecting both elements, the inheritance relationship
     * is established without additional details.</p>
     *
     * @param drawingPane The {@code Pane} where elements are drawn.
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
                    relationsManager=null;
                }
            }
        });
    }

    /**
     * Retrieves the parent {@code VBox} of a given node.
     *
     * <p>This method traverses up the node hierarchy to find the nearest ancestor that is a {@code VBox}.
     * It is useful for identifying diagram elements based on mouse events.</p>
     *
     * @param node The {@code Node} to find the parent {@code VBox} for.
     * @return The parent {@code VBox}, or {@code null} if none is found.
     */
    private VBox getParentVBox(Node node) {
        while (node != null && !(node instanceof VBox)) {
            node = node.getParent();
        }
        return (VBox) node; // Will return null if no VBox is found
    }

    /**
     * Enables drag mode for all elements in the diagram.
     *
     * <p>This method sets the {@code isDragEnabled} flag to {@code true} and makes all {@code VBox} elements
     * draggable by attaching mouse event handlers.</p>
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
     * Disables drag mode for all elements in the diagram.
     *
     * <p>This method sets the {@code isDragEnabled} flag to {@code false} and removes mouse event handlers
     * from all {@code VBox} elements to prevent dragging.</p>
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
     * Sets a {@code VBox} as draggable or non-draggable.
     *
     * <p>This method attaches or removes mouse event handlers to enable or disable dragging of the specified
     * {@code VBox} element.</p>
     *
     * @param pane   The {@code VBox} to set as draggable or non-draggable.
     * @param enable {@code true} to enable dragging; {@code false} to disable.
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
     * <p>This method searches for the corresponding UML class or interface in the model and updates its
     * X and Y coordinates to match the new layout positions.</p>
     *
     * @param pane The {@code VBox} representing the UML element.
     * @param newX The new X-coordinate position.
     * @param newY The new Y-coordinate position.
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
     * <p>This method changes the visual styling of the specified {@code VBox} to indicate whether it is highlighted.
     * Highlighting is typically used during user interactions such as selecting elements for creating relationships.</p>
     *
     * @param classBox The {@code VBox} representing the class or interface to highlight.
     * @param highlight {@code true} to apply highlighting; {@code false} to remove it.
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

    /**
     * Retrieves a UML class box by its associated {@code VBox}.
     *
     * <p>This method searches through the list of UML classes in the model and returns the corresponding
     * {@code UMLClassBox} if a match is found based on the visual representation.</p>
     *
     * @param classBox The {@code VBox} representing the class in the UI.
     * @return The corresponding {@code UMLClassBox}, or {@code null} if not found.
     */
    public UMLClassBox getClassByVBox(VBox classBox) {
        for (UMLClassBox umlClass : classDiagram.getClasses()) {
            if (umlClass.getVisualRepresentation() == classBox) {
                return umlClass;
            }
        }
        return null;
    }

    /**
     * Retrieves a UML interface box by its associated {@code VBox}.
     *
     * <p>This method searches through the list of UML interfaces in the model and returns the corresponding
     * {@code UMLInterfaceBox} if a match is found based on the visual representation.</p>
     *
     * @param interfaceBox The {@code VBox} representing the interface in the UI.
     * @return The corresponding {@code UMLInterfaceBox}, or {@code null} if not found.
     */
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
     * <p>This method reconstructs a UML relationship (e.g., association, aggregation) from serialized
     * {@code UMLRelationship} data and adds it to the drawing pane.</p>
     *
     * @param umlRelationship The {@code UMLRelationship} data used to create the relationship.
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
     * Recreates a UML class box in the UI and updates the internal model.
     *
     * <p>This method is used during deserialization to reconstruct class boxes with their attributes and methods.</p>
     *
     * @param name       The name of the class.
     * @param layoutX    The X-coordinate position on the drawing pane.
     * @param layoutY    The Y-coordinate position on the drawing pane.
     * @param attributes The list of attributes for the class.
     * @param methods    The list of methods for the class.
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
     * Recreates a UML interface box in the UI and updates the internal model.
     *
     * <p>This method is used during deserialization to reconstruct interface boxes.</p>
     *
     * @param name    The name of the interface.
     * @param layoutX The X-coordinate position on the drawing pane.
     * @param layoutY The Y-coordinate position on the drawing pane.
     */
    public void reCreateInterfaceBox(String name, double layoutX, double layoutY, List<String> methods) {
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
        for (String method : methods) {
            methodsBox.getChildren().add(new Label(method));
        }

        interfaceBox.getChildren().addAll(interfaceLabel, interfaceNameLabel, methodsBox);
        interfaceBox.setLayoutX(layoutX);
        interfaceBox.setLayoutY(layoutY);

        interfaceBox.setOnMouseClicked(event -> uiController.openInterfaceEditor(interfaceBox));
        uiController.getDrawingPane().getChildren().add(interfaceBox);
        elements.add(interfaceBox);
        setDraggable(interfaceBox, isDragEnabled);

        // Update the ClassDiagram model
        UMLInterfaceBox umlInterfaceBox = new UMLInterfaceBox(name, layoutX, layoutY, interfaceBox);
        umlInterfaceBox.setVisualRepresentation(interfaceBox);
        classDiagram.getInterfaces().add(umlInterfaceBox);

        // Add to the mapping
        classBoxMap.put(name, umlInterfaceBox);

        // Debug Logging
        System.out.println("Added interface to classBoxMap: " + name + " -> " + umlInterfaceBox);
    }

    /**
     * Getter for the class-box mapping.
     *
     * @return The mapping from element names to their corresponding UML element boxes.
     */
    public Map<String, UMLElementBoxInterface> getClassBoxMap() {
        return classBoxMap;
    }

    /**
     * Adds a {@code UMLRelationshipBox} to the internal model.
     *
     * <p>This method registers a new relationship in the class diagram's relationship list.</p>
     *
     * @param relationshipBox The {@code UMLRelationshipBox} instance representing the relationship.
     */
    public void addRelationshipBox(UMLRelationshipBox relationshipBox) {
        classDiagram.getRelationships().add(relationshipBox.getUmlRelationship());
    }

    /**
     * Updates all relationships that reference a renamed class.
     *
     * <p>This method ensures that any relationships involving a class or interface that has been renamed
     * are updated to reflect the new name, maintaining consistency within the diagram.</p>
     *
     * @param oldName The old name of the class or interface.
     * @param newName The new name of the class or interface.
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

    /**
     * Retrieves the UI controller associated with this manager.
     *
     * @return The {@code ClassDiagramUI} instance.
     */
    public ClassDiagramUI getUiController() {
        return uiController;
    }

    /**
     * Checks if a class or interface name already exists in the diagram.
     *
     * <p>This method prevents duplicate names within the class diagram, ensuring that each class or interface has a unique identifier.</p>
     *
     * @param name The name to check for existence.
     * @return {@code true} if the name exists; {@code false} otherwise.
     */
    public boolean isClassNameExists(String name) {
        return classBoxMap.containsKey(name);
    }

    /**
     * Exports the current class diagram as an image file.
     *
     * <p>This method captures a snapshot of the drawing pane, converts it to a {@code BufferedImage},
     * and writes it to the specified file in the appropriate image format (PNG or JPEG).</p>
     *
     * @param file The {@code File} object representing the destination image file.
     * @throws IOException If an error occurs during writing the image.
     */
    public void exportAsImage(File file) throws IOException {
        // Retrieve the drawing pane from the UI controller
        Pane drawingPane = uiController.getDrawingPane();

        // Ensure the drawingPane is not null
        if (drawingPane == null) {
            throw new IllegalStateException("Drawing pane is not initialized.");
        }

        // Apply CSS and layout to ensure the snapshot is accurate
        drawingPane.applyCss();
        drawingPane.layout();

        // Create a WritableImage with the size of the drawing pane
        WritableImage image = new WritableImage((int) drawingPane.getWidth(), (int) drawingPane.getHeight());

        // Take a snapshot of the drawing pane
        SnapshotParameters params = new SnapshotParameters();
        // Optionally, set a background color if needed
        // params.setFill(Color.TRANSPARENT);
        image = drawingPane.snapshot(params, image);

        // Convert WritableImage to BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        // Determine the image format based on the file extension
        String fileName = file.getName().toLowerCase();
        String format;
        if (fileName.endsWith(".png")) {
            format = "png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            format = "jpg";
        } else {
            // Default to PNG if the extension is unrecognized
            format = "png";
            // Append the default extension
            file = new File(file.getAbsolutePath() + ".png");
        }

        // Write the BufferedImage to the file
        ImageIO.write(bufferedImage, format, file);
    }


    // Method to delete the selected element (class) and any related relationships
    public void deleteSelectedElement(VBox selectedElement) {
        if (selectedElement != null) {
            String className = selectedElement.getId();  // Assuming each VBox has a unique ID (class name)

            // Remove the class from the drawing pane
            uiController.getDrawingPane().getChildren().remove(selectedElement);

            // Remove the class from the classBoxMap
            classBoxMap.remove(className);

            // Remove the class from the model (classDiagram) if it's part of the model
            classDiagram.removeRelationshipsByClassName(className);

            // Optionally, remove related relationships if any
            deleteRelatedRelationships(className);

            // You can add a confirmation or alert that the class has been deleted
            //showDeletionConfirmation(className);
        }

    }

    private void showDeletionConfirmation(String className) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Class " + className + " and its related relationships have been deleted.", ButtonType.OK);
        alert.showAndWait();
    }

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