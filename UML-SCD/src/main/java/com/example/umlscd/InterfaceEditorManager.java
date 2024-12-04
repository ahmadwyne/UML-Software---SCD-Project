package com.example.umlscd;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <h1>Interface Editor Manager</h1>
 *
 * <p>The {@code InterfaceEditorManager} class is responsible for managing the data and logic behind the Interface Editor UI.
 * It handles the retrieval and update of the interface's name and methods, as well as managing the temporary parameters
 * while defining methods.</p>
 *
 * <p>It interacts with the UI components to modify and apply changes to the interface model, including adding methods,
 * parameters, and custom data types.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-04</p>
 */
public class InterfaceEditorManager {

    /**
     * The VBox representing the interface in the UI.
     *
     * <p>This VBox contains the interface's name and methods. It is used to update the interface's name and methods
     * when changes are applied.</p>
     */
    private VBox interfaceBox;

    /**
     * A list of parameters for the current method being defined.
     *
     * <p>This list temporarily stores the parameters for the method as the user adds them. After the method is finalized,
     * the list is cleared.</p>
     */
    private List<String> parameters = new ArrayList<>();

    /**
     * Reference to the {@code UMLInterfaceBox} model object.
     *
     * <p>This object represents the UML Interface in the model and contains data such as the Interface methods
     * It is updated based on user input from the editor UI.</p>
     */
    private UMLInterfaceBox umlInterfaceBox; // Reference to the UMLClassBox

    /**
     * Reference to the {@code ClassDiagramManager}.
     *
     * <p>The {@code ClassDiagramManager} is responsible for managing the overall class diagram,
     * including adding, updating, and removing classes, interfaces, and relationships. This reference
     * allows the {@code InterfaceEditorManager} to interact with the class diagram for model updates.</p>
     */
    private ClassDiagramManager classDiagramManager; // Reference to the manager

    /**
     * Default constructor.
     *
     * <p>Initializes the {@code InterfaceEditorManager}. No specific initialization is required for this class,
     * but this constructor ensures proper object creation.</p>
     */
    public InterfaceEditorManager() {
        // Initialization if necessary
    }

    /**
     * Sets the reference to the {@code ClassDiagramManager} instance.
     *
     * <p>This method allows the {@code InterfaceEditorManager} to interact with the {@code ClassDiagramManager},
     * enabling updates to the UML model and class diagram.</p>
     *
     * @param manager The {@code ClassDiagramManager} instance to be set.
     */
    public void setClassDiagramManager(ClassDiagramManager manager) {
        this.classDiagramManager = manager;
    }

    /**
     * Sets the VBox representing the interface.
     *
     * <p>This method sets the reference to the VBox that represents the interface. It is used to retrieve the
     * interface's name and methods, as well as update them when changes are applied.</p>
     *
     * @param interfaceBox The VBox containing the interface's name and methods.
     */
    public void setInterfaceBox(VBox interfaceBox, UMLInterfaceBox umlInterfaceBox) {
        this.interfaceBox = interfaceBox;
        this.umlInterfaceBox = umlInterfaceBox;
    }

    /**
     * Retrieves the name of the interface.
     *
     * <p>This method extracts the name of the interface from the {@code interfaceBox} by looking at the second child,
     * which is assumed to be a {@code Label} containing the interface name.</p>
     *
     * @return The name of the interface, or an empty string if the name is not set.
     */
    public String getInterfaceName() {
        if (interfaceBox != null && !interfaceBox.getChildren().isEmpty()) {
            Label interfaceNameLabel = (Label) interfaceBox.getChildren().get(1);
            return interfaceNameLabel.getText();
        }
        return "";
    }

    /**
     * Retrieves the methods of the interface as a string.
     *
     * <p>This method extracts all method names from the {@code interfaceBox} by iterating through its children.
     * The methods are assumed to be stored in a {@code VBox} located as the third child of the {@code interfaceBox}.
     * The method names are returned as a concatenated string, with each method on a new line.</p>
     *
     * @return A string containing all methods of the interface, separated by new lines.
     */
    public String getMethods() {
        if (interfaceBox != null && interfaceBox.getChildren().size() > 1) {
            VBox methodsBox = (VBox) interfaceBox.getChildren().get(2); // Methods Box is the 3rd child
            StringBuilder methods = new StringBuilder();
            for (var node : methodsBox.getChildren()) {
                if (node instanceof Label) {
                    methods.append(((Label) node).getText()).append("\n"); // Collect method names
                }
            }
            return methods.toString().trim();
        }
        return "";
    }

    /**
     * Applies changes to the interface.
     *
     * <p>This method updates the interface's name and methods in the {@code interfaceBox}. It clears the existing methods
     * and adds the new methods as {@code Label} nodes styled in italic.</p>
     *
     * @param interfaceName The new name of the interface.
     * @param methodsText The new method signatures as a string.
     */
    public void applyChanges(String interfaceName, String methodsText) {
        if (umlInterfaceBox != null) {
            String oldName = umlInterfaceBox.getName();
            String newName = interfaceName.trim();

            if (newName.isEmpty()) {
                // Optionally, enforce non-empty names
                classDiagramManager.getUiController().showErrorAlert("Class name cannot be empty.");
                return;
            }

            // Check for unique class name
            if (!oldName.equals(newName) && classDiagramManager.isClassNameExists(newName)) {
                // Notify the user about duplicate class names
                classDiagramManager.getUiController().showErrorAlert("An Interface with this name already exists.");
                return;
            }

            // Update the model
            umlInterfaceBox.setName(newName);
            classDiagramManager.getClassBoxMap().remove(oldName);
            classDiagramManager.getClassBoxMap().put(newName, umlInterfaceBox);

            // Update methods
            List<String> methods = new ArrayList<>();
            if (!methodsText.isEmpty()) {
                methods = Arrays.asList(methodsText.split("\\n"));
            }
            umlInterfaceBox.setMethods(methods);

            // Update the UI
            Label interfaceNameLabel = (Label) interfaceBox.getChildren().get(1); // Assuming the first label is the name
            interfaceNameLabel.setText(newName);

            VBox methodsBox = (VBox) interfaceBox.getChildren().get(2);
            methodsBox.getChildren().clear();
            for (String line : methodsText.split("\\n")) {
                Label method = new Label(line);
                methodsBox.getChildren().add(method);
            }

            // Update relationships in the ClassDiagramManager
            classDiagramManager.updateRelationshipsForRenamedClass(oldName, newName);
        }
    }

    /**
     * Handles adding a custom data type to a ComboBox.
     *
     * <p>This method opens a dialog that prompts the user to input a custom data type. If the user provides a valid
     * custom data type, it is added to the ComboBox and selected as the current value.</p>
     *
     * @param dropdown The ComboBox to which the custom data type should be added.
     */
    public void handleCustomDataType(ComboBox<String> dropdown) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Custom Data Type");
        dialog.setHeaderText("Enter Custom Data Type:");
        dialog.setContentText("Data Type:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(customType -> {
            if (!dropdown.getItems().contains(customType)) {
                dropdown.getItems().add(customType); // Add the custom type to the dropdown
            }
            dropdown.setValue(customType); // Set the custom type as the selected value
        });
    }

    /**
     * Adds a parameter to the temporary list of parameters.
     *
     * <p>This method adds a parameter to the list of parameters for the current method being defined. It is used
     * when the user specifies method parameters.</p>
     *
     * @param parameter The parameter to add to the current method.
     */
    public void addParameterToMethod(String parameter) {
        parameters.add(parameter);
    }

    /**
     * Finalizes the method signature with the parameters.
     *
     * <p>This method constructs the full method signature by combining the method name, return type, visibility,
     * and parameters. The parameters are formatted as "name: type" and inserted into the signature.</p>
     *
     * @param methodName The name of the method.
     * @param returnType The return type of the method.
     * @param visibility The visibility of the method (e.g., public, private).
     * @return The final method signature as a string.
     */
    public String finalizeMethod(String methodName, String returnType, String visibility) {
        StringBuilder methodSignature = new StringBuilder(visibility.charAt(0) + methodName + "(");
        for (int i = 0; i < parameters.size(); i++) {
            methodSignature.append(parameters.get(i));
            if (i < parameters.size() - 1) {
                methodSignature.append(", ");
            }
        }
        methodSignature.append(") : ").append(returnType);
        return methodSignature.toString();
    }

    /**
     * Resets the list of parameters after a method is finalized.
     *
     * <p>This method clears the temporary list of parameters to prepare for the next method definition.</p>
     */
    public void resetParameters() {
        parameters.clear();
    }
}