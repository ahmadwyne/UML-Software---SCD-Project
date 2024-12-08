package com.example.umlscd.BusinessLayer.ClassDiagram;

import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <h1>Class Editor Manager</h1>
 *
 * <p>The {@code ClassEditorManager} class is responsible for managing the logic related to class editing
 * in the UML Class Diagram Editor. It interacts with the {@code ClassDiagramManager} to update the class
 * diagram model with changes to the class name, attributes, and methods.</p>
 *
 * <p>This class provides functionality for adding custom data types, managing method parameters, and
 * applying changes from the editor UI to the UML model.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class ClassEditorManager {

    /**
     * The {@code VBox} representing the class box in the UI.
     *
     * <p>This UI component displays the class name, attributes, and methods. It is used to synchronize
     * the editor's state with the visual representation of the class in the diagram.</p>
     */
    private VBox classBox;

    /**
     * Temporary storage for method parameters.
     *
     * <p>This list holds parameters being added to a method before the method is finalized and added to the UML model.</p>
     */
    public List<String> parameters = new ArrayList<>();

    /**
     * Reference to the {@code UMLClassBox} model object.
     *
     * <p>This object represents the UML class in the model and contains data such as the class name, attributes,
     * and methods. It is updated based on user input from the editor UI.</p>
     */
    private UMLClassBox umlClassBox; // Reference to the UMLClassBox

    /**
     * Reference to the {@code ClassDiagramManager}.
     *
     * <p>The {@code ClassDiagramManager} is responsible for managing the overall class diagram,
     * including adding, updating, and removing classes, interfaces, and relationships. This reference
     * allows the {@code ClassEditorManager} to interact with the class diagram for model updates.</p>
     */
    public ClassDiagramManager classDiagramManager; // Reference to the manager

    /**
     * Default constructor.
     *
     * <p>Initializes the {@code ClassEditorManager}. No specific initialization is required for this class,
     * but this constructor ensures proper object creation.</p>
     */
    public ClassEditorManager() {
        // Initialization if necessary
    }

    /**
     * Sets the reference to the {@code ClassDiagramManager} instance.
     *
     * <p>This method allows the {@code ClassEditorManager} to interact with the {@code ClassDiagramManager},
     * enabling updates to the UML model and class diagram.</p>
     *
     * @param manager The {@code ClassDiagramManager} instance to be set.
     */
    public void setClassDiagramManager(ClassDiagramManager manager) {
        this.classDiagramManager = manager;
    }

    /**
     * Sets the class box and UML class box references.
     *
     * <p>This method sets the references for the class box UI component and the associated {@code UMLClassBox} model.
     * It ensures that the editor can interact with the correct class and its corresponding box in the diagram.</p>
     *
     * @param classBox    The {@code VBox} representing the class box in the UI.
     * @param umlClassBox The {@code UMLClassBox} representing the class model.
     */
    public void setClassBox(VBox classBox, UMLClassBox umlClassBox) {
        this.classBox = classBox;
        this.umlClassBox = umlClassBox;
    }

    /**
     * Retrieves the name of the class from the UI.
     *
     * <p>This method extracts the class name from the class box UI. It assumes that the class name is displayed
     * in the first label of the class box.</p>
     *
     * @return The name of the class.
     */
    public String getClassName() {
        if (classBox != null && !classBox.getChildren().isEmpty()) {
            Label classNameLabel = (Label) classBox.getChildren().get(0);
            return classNameLabel.getText();
        }
        return "";
    }

    /**
     * Retrieves the attributes of the class from the UI.
     *
     * <p>This method extracts the class attributes from the class box UI and returns them as a formatted string,
     * with each attribute on a new line.</p>
     *
     * @return The attributes of the class as a string.
     */
    public String getAttributes() {
        if (classBox != null && classBox.getChildren().size() > 1) {
            VBox attributesBox = (VBox) classBox.getChildren().get(1);
            StringBuilder attributes = new StringBuilder();
            for (var node : attributesBox.getChildren()) {
                if (node instanceof Label) {
                    attributes.append(((Label) node).getText()).append("\n");
                }
            }
            return attributes.toString().trim();
        }
        return "";
    }

    /**
     * Retrieves the methods of the class from the UI.
     *
     * <p>This method extracts the methods of the class from the class box UI and returns them as a formatted string,
     * with each method on a new line.</p>
     *
     * @return The methods of the class as a string.
     */
    public String getMethods() {
        if (classBox != null && classBox.getChildren().size() > 2) {
            VBox methodsBox = (VBox) classBox.getChildren().get(2);
            StringBuilder methods = new StringBuilder();
            for (var node : methodsBox.getChildren()) {
                if (node instanceof Label) {
                    methods.append(((Label) node).getText()).append("\n");
                }
            }
            return methods.toString().trim();
        }
        return "";
    }

    /**
     * Applies the changes made in the editor to the model and updates relationships in the diagram.
     *
     * <p>This method takes the updated class name, attributes, and methods from the editor and applies them to the
     * corresponding {@code UMLClassBox} and {@code ClassDiagramManager}. It also ensures that the relationships
     * involving the renamed class are updated accordingly.</p>
     *
     * @param className      The new class name.
     * @param attributesText The updated attributes text.
     * @param methodsText    The updated methods text.
     */
    public void applyChanges(String className, String attributesText, String methodsText) {
        if (umlClassBox != null) {
            String oldName = umlClassBox.getName();
            String newName = className.trim();

            if (newName.isEmpty()) {
                // Optionally, enforce non-empty names
                classDiagramManager.getUiController().showErrorAlert("Class name cannot be empty.");
                return;
            }

            // Check for unique class name
            if (!oldName.equals(newName) && classDiagramManager.isClassNameExists(newName)) {
                // Notify the user about duplicate class names
                classDiagramManager.getUiController().showErrorAlert("A class with this name already exists.");
                return;
            }

            // Update the model
            umlClassBox.setName(newName);
            classDiagramManager.getClassBoxMap().remove(oldName);
            classDiagramManager.getClassBoxMap().put(newName, umlClassBox);

            // Update attributes
            List<String> attributes = new ArrayList<>();
            if (!attributesText.isEmpty()) {
                attributes = Arrays.asList(attributesText.split("\\n"));
            }
            umlClassBox.setAttributes(attributes);

            // Update methods
            List<String> methods = new ArrayList<>();
            if (!methodsText.isEmpty()) {
                methods = Arrays.asList(methodsText.split("\\n"));
            }
            umlClassBox.setMethods(methods);

            // Update the UI
            Label classNameLabel = (Label) classBox.getChildren().get(0); // Assuming the first label is the name
            classNameLabel.setText(newName);

            VBox attributesBox = (VBox) classBox.getChildren().get(1);
            attributesBox.getChildren().clear();
            for (String line : attributesText.split("\\n")) {
                Label attribute = new Label(line);
                attributesBox.getChildren().add(attribute);
            }

            VBox methodsBox = (VBox) classBox.getChildren().get(2);
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
     * Handles the addition of a custom data type to a dropdown list.
     *
     * <p>This method displays a dialog to the user, prompting for a custom data type. If a valid custom type is entered,
     * it is added to the provided dropdown list, allowing the user to select it as a data type option.</p>
     *
     * @param dropdown The {@code ComboBox<String>} where the custom data type will be added.
     */
    public void handleCustomDataType(ComboBox<String> dropdown) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Custom Data Type");
        dialog.setHeaderText("Enter Custom Data Type:");
        dialog.setContentText("Data Type:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(customType -> {
            customType = customType.trim();
            if (!customType.isEmpty() && !dropdown.getItems().contains(customType)) {
                dropdown.getItems().add(customType);
            }
            dropdown.setValue(customType); // Set the custom type as the selected value
        });
    }

    /**
     * Adds a parameter to the temporary list of parameters for the current method.
     *
     * <p>This method allows the user to add method parameters one by one. The parameters are temporarily stored
     * in a list until the method signature is finalized and added to the UML model.</p>
     *
     * @param parameter The parameter to be added.
     */
    public void addParameterToMethod(String parameter) {
        parameters.add(parameter);
    }

    /**
     * Finalizes the method signature with parameters.
     *
     * <p>This method constructs the complete method signature by combining the method name, parameters, return type,
     * and visibility. It formats the signature in the standard UML method notation.</p>
     *
     * @param methodName The name of the method.
     * @param returnType The return type of the method.
     * @param visibility The visibility modifier of the method (e.g., +, -, #).
     * @return A {@code String} representing the finalized method signature.
     */
    public String finalizeMethod(String methodName, String returnType, String visibility) {
        StringBuilder methodSignature = new StringBuilder();
        methodSignature.append(visibility.charAt(0))
                .append(methodName)
                .append("(");
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
     * Resets the temporary list of parameters after adding the method.
     *
     * <p>This method clears the list of parameters, ensuring that previous parameters do not carry over to new methods.</p>
     */
    public void resetParameters() {
        parameters.clear();
    }
}