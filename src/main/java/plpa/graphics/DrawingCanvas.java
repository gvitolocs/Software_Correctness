package plpa.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 * Graphics viewer (left side of IDE). Coursework: Java must NOT hold drawing state
 * (bounding box, colours, etc.); it only displays what Scala sends. Grid must be displayed.
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
     * Set the drawing from the result of GraphicsLogic.interpret (only the part after "OK\n").
     * Format is defined by your Scala output, e.g. "BOX x1 y1 x2 y2\n" then lines like
     * "LINE x1 y1 x2 y2 [colour]\n", "CIRCLE x y r [colour]\n", "TEXT x y content\n", etc.
     * If data is null or empty, clear the canvas (e.g. on error).
     * Tip: split by "\n", parse first line for BOX, then loop and parse each shape; store in
     * fields so redraw() can use them. Do not store bounding box/colours as “engine state”
     * long-term—only for this frame (data comes from Scala each time).
     */
    public void setDrawingData(String data) {
        // TODO: parse data, store shapes/bounding box, call redraw()
    }

    /**
     * Convert logical x (program coordinates) to pixel x.
     * Tip: Euclidean plane has (0,0) at bottom-left (coursework). So: margin + (x - boxX1) * scale.
     * Scale is computed from bounding box and pane size so the box fits inside the margins.
     */
    private double toPixelX(double x) {
        // TODO: implement (need boxX1, scale, margin as fields or from current data)
        return 0;
    }

    /**
     * Convert logical y (program coordinates) to pixel y.
     * Tip: JavaFX y grows downward, so flip: getHeight() - margin - (y - boxY1) * scale.
     */
    private double toPixelY(double y) {
        // TODO: implement
        return 0;
    }

    /**
     * Clear the canvas and redraw: grid, bounding box, then all shapes in order.
     * Coursework: lines with Bresenham’s algorithm, circles with Midpoint circle algorithm,
     * text with built-in rendering (e.g. gc.fillText). Drawing outside the bounding box
     * must be clipped (or only draw inside the box). Optionally highlight the “current”
     * object (e.g. last drawn) for the demo.
     * Tip: compute scale from (boxX2-boxX1), (boxY2-boxY1) and (getWidth()-2*margin),
     * (getHeight()-2*margin); use Math.min of the two ratios to keep aspect ratio.
     */
    private void redraw() {
        // TODO: clear, compute scale from bounding box and pane size, draw grid, draw box, draw shapes
    }
}
