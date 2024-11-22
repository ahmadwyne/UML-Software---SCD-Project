package com.example.umlscd;

import java.io.Serializable;

public class Association implements Serializable {
    private static final long serialVersionUID = 1L; // Add a serial version UID

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
