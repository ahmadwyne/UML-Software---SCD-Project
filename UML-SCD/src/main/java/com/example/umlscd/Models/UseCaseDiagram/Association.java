package com.example.umlscd.Models.UseCaseDiagram;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <h1>Association</h1>
 *
 * <p>The {@code Association} class represents a relationship between two {@code UseCaseDiagramObject} instances
 * within a use case diagram. It encapsulates the two objects involved in the association and the type of
 * association (e.g., "association", "include", "extend"). This class facilitates the management and
 * rendering of relationships between actors and use cases in the UML Editor application.</p>
 *
 * <p>This class implements {@code Serializable} to allow instances to be serialized and deserialized,
 * enabling the saving and loading of diagrams. Additionally, it utilizes Jackson annotations to support
 * JSON serialization, ensuring compatibility with modern data interchange formats.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class Association implements Serializable {
    /**
     * Serial version UID for serialization compatibility.
     */
    private static final long serialVersionUID = 1L; // Add a serial version UID

    /**
     * The first {@code UseCaseDiagramObject} involved in the association.
     *
     * <p>Annotated with {@code @JsonProperty} to include it in JSON serialization and deserialization.</p>
     */
    @JsonProperty
    private UseCaseDiagramObject obj1;

    /**
     * The second {@code UseCaseDiagramObject} involved in the association.
     *
     * <p>Annotated with {@code @JsonProperty} to include it in JSON serialization and deserialization.</p>
     */
    @JsonProperty
    private UseCaseDiagramObject obj2;

    /**
     * The type of association between the two objects (e.g., "association", "include", "extend").
     *
     * <p>Annotated with {@code @JsonProperty} to include it in JSON serialization and deserialization.</p>
     */
    @JsonProperty
    private String type;

    /**
     * Constructs an {@code Association} instance with the specified objects and association type.
     *
     * <p>This constructor is annotated with {@code @JsonCreator} to facilitate JSON deserialization.</p>
     *
     * @param obj1 The first {@code UseCaseDiagramObject} involved in the association.
     * @param obj2 The second {@code UseCaseDiagramObject} involved in the association.
     * @param type The type of association (e.g., "association", "include", "extend").
     */
    @JsonCreator
    public Association(@JsonProperty("obj1") UseCaseDiagramObject obj1,
                       @JsonProperty("obj2") UseCaseDiagramObject obj2,
                       @JsonProperty("type") String type) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.type = type;
    }

    /**
     * Retrieves the first {@code UseCaseDiagramObject} involved in the association.
     *
     * @return The first {@code UseCaseDiagramObject}.
     */
    @JsonProperty
    public UseCaseDiagramObject getObj1() {
        return obj1;
    }

    /**
     * Sets the first {@code UseCaseDiagramObject} involved in the association.
     *
     * @param obj1 The first {@code UseCaseDiagramObject} to set.
     */
    @JsonProperty
    public void setObj1(UseCaseDiagramObject obj1) {
        this.obj1 = obj1;
    }

    /**
     * Retrieves the second {@code UseCaseDiagramObject} involved in the association.
     *
     * @return The second {@code UseCaseDiagramObject}.
     */
    @JsonProperty
    public UseCaseDiagramObject getObj2() {
        return obj2;
    }

    /**
     * Sets the second {@code UseCaseDiagramObject} involved in the association.
     *
     * @param obj2 The second {@code UseCaseDiagramObject} to set.
     */
    @JsonProperty
    public void setObj2(UseCaseDiagramObject obj2) {
        this.obj2 = obj2;
    }

    /**
     * Retrieves the type of association between the two objects.
     *
     * @return A {@code String} representing the association type (e.g., "association", "include", "extend").
     */
    @JsonProperty
    public String getType() {
        return type;
    }

    /**
     * Sets the type of association between the two objects.
     *
     * @param type A {@code String} representing the association type (e.g., "association", "include", "extend").
     */
    @JsonProperty
    public void setType(String type) {
        this.type = type;
    }
}