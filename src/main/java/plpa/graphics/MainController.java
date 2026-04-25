package plpa.graphics;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

/**
 * Links the GUI to Scala. Coursework: the program from the editor must be passed
 * directly to the Scala interpreter (getText()) and must not be modified.
 */
public class MainController implements Initializable {
    @FXML
    private Pane drawingCanvasPane;
    @FXML
    private TextArea errorBox;
    @FXML
    private TextArea programEditor;

    private DrawingCanvas drawingCanvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawingCanvas = new DrawingCanvas();
        drawingCanvas.setMinHeight(220);
        drawingCanvas.setStyle("-fx-background-color: #fafafa;");
        drawingCanvas.prefWidthProperty().bind(drawingCanvasPane.widthProperty());
        drawingCanvas.prefHeightProperty().bind(drawingCanvasPane.heightProperty());
        drawingCanvas.setMaxWidth(Double.MAX_VALUE);
        drawingCanvas.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(drawingCanvas, Priority.ALWAYS);
        drawingCanvasPane.getChildren().add(drawingCanvas);
    }

    /** Called by the "Run" button in main.fxml (onAction="#onRun"). */
    @FXML
    private void onRun() {
        runWithScale(1);
    }

    @FXML
    private void onPrettify() {
        runWithScale(100);
    }

    private void runWithScale(int scale) {
        drawingCanvas.setDrawingCoordinateDivisor(1.0);
        drawingCanvas.setHoverCoordinateDivisor(scale);
        String program = programEditor.getText();
        String result = invokeScalaInterpreter(program, scale);
        if (result.startsWith("ERR\n")) {
            errorBox.setText(result.substring(4));
            drawingCanvas.setDrawingData(null);
        } else if (result.startsWith("OK\n")) {
            errorBox.clear();
            drawingCanvas.setDrawingData(result.substring(3));
        } else {
            errorBox.setText("Unexpected interpreter output");
            drawingCanvas.setDrawingData(null);
        }
    }

    private String invokeScalaInterpreter(String program, int scale) {
        try {
            Path scalaOut = Path.of("target", "scala-3.3.7", "classes");
            if (!Files.isDirectory(scalaOut)) {
                return "ERR\nScala classes not found. Run `sbt compile` once, then retry.";
            }
            URL[] urls = new URL[] { scalaOut.toUri().toURL() };
            try (ProjectFirstClassLoader loader = new ProjectFirstClassLoader(urls, ClassLoader.getSystemClassLoader())) {
                return invokeInterpreterWithLoader(loader, program, scale);
            }
        } catch (Exception e) {
            return "ERR\n" + formatInterpreterException(e);
        }
    }

    private String invokeInterpreterWithLoader(ClassLoader loader, String program, int scale) throws Exception {
        Class<?> objectClass = Class.forName("plpa.graphics.GraphicsLogic$", true, loader);
        Field moduleField = objectClass.getField("MODULE$");
        Object module = moduleField.get(null);
        Method interpret = objectClass.getMethod("interpret", String.class, int.class);
        Object out = interpret.invoke(module, program, scale);
        return out != null ? out.toString() : "ERR\nInterpreter returned null";
    }

    private String formatInterpreterException(Exception e) {
        if (e instanceof InvocationTargetException ite && ite.getCause() != null) {
            Throwable cause = ite.getCause();
            return "Unable to run Scala interpreter: " + cause.getClass().getSimpleName()
                + (cause.getMessage() != null ? " - " + cause.getMessage() : "");
        }
        return "Unable to run Scala interpreter: " + e.getClass().getSimpleName()
            + (e.getMessage() != null ? " - " + e.getMessage() : "");
    }

    private static final class ProjectFirstClassLoader extends URLClassLoader {
        ProjectFirstClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            boolean projectClass = name.startsWith("plpa.graphics.") || name.startsWith("drawing.");
            if (!projectClass) {
                return super.loadClass(name, resolve);
            }
            synchronized (getClassLoadingLock(name)) {
                Class<?> loaded = findLoadedClass(name);
                if (loaded == null) {
                    try {
                        loaded = findClass(name);
                    } catch (ClassNotFoundException e) {
                        loaded = super.loadClass(name, false);
                    }
                }
                if (resolve) {
                    resolveClass(loaded);
                }
                return loaded;
            }
        }
    }

}
