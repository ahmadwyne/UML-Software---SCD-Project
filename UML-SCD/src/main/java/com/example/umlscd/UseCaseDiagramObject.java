package com.example.umlscd;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UseCaseDiagramObject {
    private String type;
    private double x, y;

    public UseCaseDiagramObject(String type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getType(){
        return type;
    }

    public void draw(GraphicsContext gc) {
        if ("actor".equals(type)) {
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 15, y - 15, 30, 30); // Head
            gc.strokeLine(x, y + 15, x, y + 50); // Body
            gc.strokeLine(x, y + 25, x - 15, y + 40); // Left Arm
            gc.strokeLine(x, y + 25, x + 15, y + 40); // Right Arm
            gc.strokeLine(x, y + 50, x - 10, y + 70); // Left Leg
            gc.strokeLine(x, y + 50, x + 10, y + 70); // Right Leg
        } else if ("usecase".equals(type)) {
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 50, y - 25, 100, 50);
        }
    }

    public boolean contains(double px, double py) {
        if ("actor".equals(type)) {
            return Math.abs(px - x) < 15 && Math.abs(py - (y + 25)) < 40; // Adjust for actor bounds
        } else if ("usecase".equals(type)) {
            return px > x - 50 && px < x + 50 && py > y - 25 && py < y + 25;
        }
        return false;
    }
}
