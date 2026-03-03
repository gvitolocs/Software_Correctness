package plpa.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 * The drawing area on the left side of the IDE.
 * Java must not store any drawing state (bounding box, colours, etc.). It only displays
 * what the Scala interpreter sends. You must draw a grid on this canvas.
 */
public class DrawingCanvas extends Pane {

    private final Canvas canvas;
    private final GraphicsContext gc;

    public DrawingCanvas() {
        canvas = new Canvas(0, 0);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        widthProperty().addListener((o, a, b) -> redraw());
        heightProperty().addListener((o, a, b) -> redraw());
    }

    /**
     * Update the drawing using the string that Scala returns (the part after "OK\n").
     * Your Scala code decides the format: for example first line "BOX x1 y1 x2 y2", then
     * lines like "LINE x1 y1 x2 y2", "CIRCLE x y r", "TEXT x y content", etc.
     * If data is null or empty, clear the canvas (e.g. when there is an error).
     * You can split by newline, parse the first line for the box, then parse each shape line
     * and store them in fields so redraw() can draw them. Do not keep box or colours
     * permanent state—only for this update; Scala sends fresh data each time.
     */
    public void setDrawingData(String data) {
        // TODO: parse data, store shapes/bounding box, call redraw()
    }

    /**
     * Convert a program x-coordinate to a pixel x position.
     * The program uses coordinates with (0,0) at bottom-left. Use: margin + (x - boxX1) * scale.
     * Scale is computed so the bounding box fits inside the panel (from box size and panel size).
     */
    private double toPixelX(double x) {
        // TODO: implement (need boxX1, scale, margin as fields or from current data)
        return 0;
    }

    /**
     * Convert a program y-coordinate to a pixel y position.
     * In JavaFX, y increases downward. So we flip: getHeight() - margin - (y - boxY1) * scale.
     */
    private double toPixelY(double y) {
        // TODO: implement
        return 0;
    }

    /**
     * Clear the canvas and redraw: grid, bounding box, then all shapes in order.
     * Draw lines with Bresenham’s algorithm, circles with midpoint circle algorithm.
     * Use gc.fillText (or similar) for text. Clip anything outside the bounding box.
     * Highlight the object that is currently being drawn (see coursework PDF).
     * To compute scale: use the box width and height and the panel size (minus margins);
     * take the minimum of the horizontal and vertical scale so the aspect ratio is kept.
     */
    private void redraw() {
        // TODO: clear, compute scale from bounding box and pane size, draw grid, draw box, draw shapes
    }
}
