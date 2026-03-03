package plpa.graphics;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
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
        errorBox.setText("helloworld");
    }
}
