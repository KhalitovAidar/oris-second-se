package gui;

import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;

class CanvasPane extends Pane {

    private final Canvas canvas;

    public CanvasPane(int width, int height) {
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setWidth(width);
        this.setHeight(height);

        this.canvas = new Canvas(width, height);
        this.getChildren().add(canvas);

        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
