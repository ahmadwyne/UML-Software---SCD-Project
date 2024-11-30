package com.example.umlscd;


import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InterfaceEditorManager {

    private VBox interfaceBox;
    private List<String> parameters = new ArrayList<>(); // Temporary storage for parameters

    public void setInterfaceBox(VBox interfaceBox) {
        this.interfaceBox = interfaceBox;
    }

    public String getInterfaceName() {
        if (interfaceBox != null && !interfaceBox.getChildren().isEmpty()) {
            Label interfaceNameLabel = (Label) interfaceBox.getChildren().get(1);
            return interfaceNameLabel.getText();
        }
        return "";
    }

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

    // This method is used when applying changes to the interface, so we add methods in the normal style
    public void applyChanges(String interfaceName, String methodsText) {
        if (interfaceBox != null && !interfaceBox.getChildren().isEmpty()) {
            Label interfaceNameLabel = (Label) interfaceBox.getChildren().get(1);
            interfaceNameLabel.setText(interfaceName); // Update interface name

            VBox methodsBox = (VBox) interfaceBox.getChildren().get(2); // Get methods VBox
            methodsBox.getChildren().clear(); // Clear existing methods

            for (String line : methodsText.split("\\n")) {
                // Add method to VBox as normal text
                Label methodLabel = new Label(line);
                methodLabel.setStyle("-fx-font-style: italic;");
                methodsBox.getChildren().add(methodLabel);
            }
        }
    }



    // Method to handle adding a custom data type to a dropdown
    public void handleCustomDataType(ComboBox<String> dropdown) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Custom Data Type");
        dialog.setHeaderText("Enter Custom Data Type:");
        dialog.setContentText("Data Type:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(customType -> {
            if (!dropdown.getItems().contains(customType)) {
                dropdown.getItems().add(customType);
            }
            dropdown.setValue(customType); // Set the custom type as the selected value
        });
    }

    // Add parameter to the temporary list of parameters for the current method
    public void addParameterToMethod(String parameter) {
        parameters.add(parameter);
    }

    // Finalize the method signature with parameters
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

    // Reset the temporary parameters after adding the method
    public void resetParameters() {
        parameters.clear();
    }
}
