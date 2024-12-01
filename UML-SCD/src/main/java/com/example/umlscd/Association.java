package com.example.umlscd;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Association implements Serializable {
    private static final long serialVersionUID = 1L; // Add a serial version UID

    @JsonProperty
    private UseCaseDiagramObject obj1;
    @JsonProperty
    private UseCaseDiagramObject obj2;
    @JsonProperty
    private String type;

    @JsonCreator
    public Association(@JsonProperty("obj1") UseCaseDiagramObject obj1,
                       @JsonProperty("obj2") UseCaseDiagramObject obj2,
                       @JsonProperty("type") String type) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.type = type;
    }

    @JsonProperty
    public UseCaseDiagramObject getObj1() {
        return obj1;
    }

    @JsonProperty
    public void setObj1(UseCaseDiagramObject obj1) {
        this.obj1 = obj1;
    }

    @JsonProperty
    public UseCaseDiagramObject getObj2() {
        return obj2;
    }

    @JsonProperty
    public void setObj2(UseCaseDiagramObject obj2) {
        this.obj2 = obj2;
    }
    @JsonProperty
    public String getType() {
        return type;
    }

    @JsonProperty
    public void setType(String type) {
        this.type = type;
    }

}
