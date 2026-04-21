package plpa.graphics;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * The drawing area on the left side of the IDE.
 * Java must not store any drawing state (bounding box, colours, etc.). It only displays
 * what the Scala interpreter sends. You must draw a grid on this canvas.
 */
public class DrawingCanvas extends Pane {

    private final Canvas canvas;
    private final GraphicsContext gc;

    // Current data from Scala (only for this redraw; not permanent state)
    private double boxX1 = 0, boxY1 = 0, boxX2 = 1, boxY2 = 1;
    private final List<String> shapeLines = new ArrayList<>();
    private static final double MARGIN = 20;
    private static final double GRID_CELL = 10;

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
     * Format: first line "BOX x1 y1 x2 y2", then one line per drawable: "PIXEL x y color" or "TEXT x y text color".
     * Pixels come from DrawingEngine (including fill); each pixel has its own color.
     */
    public void setDrawingData(String data) {
        shapeLines.clear();
        if (data == null || data.isBlank()) {
            redraw();
            return;
        }
        String[] lines = data.trim().split("\n");
        if (lines.length == 0) {
            redraw();
            return;
        }
        // First line: BOX x1 y1 x2 y2
        String[] boxTokens = lines[0].trim().split("\\s+");
        if (boxTokens.length >= 5 && "BOX".equals(boxTokens[0])) {
            boxX1 = Double.parseDouble(boxTokens[1]);
            boxY1 = Double.parseDouble(boxTokens[2]);
            boxX2 = Double.parseDouble(boxTokens[3]);
            boxY2 = Double.parseDouble(boxTokens[4]);
        }
        for (int i = 1; i < lines.length; i++) {
            String trimmed = lines[i].trim();
            if (!trimmed.isEmpty()) {
                shapeLines.add(trimmed);
            }
        }
        Platform.runLater(this::redraw);
    }

    private double toPixelX(double x) {
        double scale = getScale();
        return MARGIN + (x - boxX1) * scale;
    }

    private double toPixelY(double y) {
        double scale = getScale();
        return getCanvasHeight() - MARGIN - (y - boxY1) * scale;
    }

    private double getCanvasWidth() {
        return canvas.getWidth() > 0 ? canvas.getWidth() : getWidth();
    }

    private double getCanvasHeight() {
        return canvas.getHeight() > 0 ? canvas.getHeight() : getHeight();
    }

    private double getScale() {
        double w = getCanvasWidth();
        double h = getCanvasHeight();
        if (w <= 2 * MARGIN || h <= 2 * MARGIN) return 1;
        double boxW = boxX2 - boxX1;
        double boxH = boxY2 - boxY1;
        if (boxW <= 0 || boxH <= 0) return 1;
        double scaleX = (w - 2 * MARGIN) / boxW;
        double scaleY = (h - 2 * MARGIN) / boxH;
        return Math.min(scaleX, scaleY);
    }

    private void redraw() {
        double w = getCanvasWidth();
        double h = getCanvasHeight();
        if (w <= 0 || h <= 0) return;
        gc.clearRect(0, 0, w, h);

        // Checkered grid background
        for (int row = 0; row < (int)(h / GRID_CELL) + 1; row++) {
            for (int col = 0; col < (int)(w / GRID_CELL) + 1; col++) {
                gc.setFill((row + col) % 2 == 0 ? Color.valueOf("#e8e8e8") : Color.valueOf("#ffffff"));
                gc.fillRect(col * GRID_CELL, row * GRID_CELL, GRID_CELL, GRID_CELL);
            }
        }

        double scale = getScale();
        double boxW = (boxX2 - boxX1) * scale;
        double boxH = (boxY2 - boxY1) * scale;
        double left = toPixelX(boxX1);
        double top = toPixelY(boxY2);

        // Clip to bounding box (in pixel coords)
        gc.save();
        gc.beginPath();
        gc.rect(left, top, boxW, boxH);
        gc.clip();

        // Draw bounding box outline
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        gc.strokeRect(left, top, boxW, boxH);

        double pixelSize = Math.max(1, scale);

        for (String line : shapeLines) {
            drawDrawableLine(line, pixelSize);
        }

        gc.restore();
    }

    private void drawDrawableLine(String line, double pixelSize) {
        String[] parts = line.split("\\s+");
        if (parts.length < 4) return;
        String kind = parts[0].toUpperCase();
        try {
            if ("PIXEL".equals(kind) && parts.length >= 4) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                Color c = parseColor(parts[3]);
                gc.setFill(c);
                gc.fillRect(toPixelX(x), toPixelY(y), pixelSize, pixelSize);
            } else if ("TEXT".equals(kind) && parts.length >= 5) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                Color c = parseColor(parts[parts.length - 1]);
                String text = String.join(" ", java.util.Arrays.copyOfRange(parts, 3, parts.length - 1));
                gc.setFill(c);
                gc.fillText(text, toPixelX(x), toPixelY(y));
            }
        } catch (NumberFormatException ignored) { }
    }

    private Color parseColor(String s) {
        return switch (s != null ? s.toLowerCase() : "") {
            case "red" -> Color.RED;
            case "green" -> Color.GREEN;
            case "blue" -> Color.BLUE;
            default -> Color.BLACK;
        };
    }
}
