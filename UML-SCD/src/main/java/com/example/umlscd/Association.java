package com.example.umlscd;

public class Association {
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
