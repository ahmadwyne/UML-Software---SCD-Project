package com.example.umlscd;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDiagramManager {
    private final ArrayList<Node> elements = new ArrayList<>();
    private VBox firstSelectedElement = null;
    private VBox secondSelectedElement = null;

    private final ClassDiagramUI uiController;
    private ClassDiagramRelationsManager relationsManager;
    private boolean isDragEnabled = false;

    public ClassDiagramManager(ClassDiagramUI uiController) {
        this.uiController = uiController;
        this.relationsManager = new AssociationManager(); // Set the correct manager
        this.relationsManager= new AggregationManager();

       this.relationsManager= new CompositionManager();
        this.relationsManager= new InheritanceManager();

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
    private void enableInheritanceMode(Pane drawingPane) {
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
    private VBox getParentVBox(Node node) {
        while (node != null && !(node instanceof VBox)) {
            node = node.getParent();
        }
        return (VBox) node; // Will return null if no VBox is found
    }

/*    private VBox getParentVBox(Node node) {
        while (node != null && !(node instanceof VBox)) {
            node = node.getParent();
        }
        return (VBox) node;
    }
*/

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
}
