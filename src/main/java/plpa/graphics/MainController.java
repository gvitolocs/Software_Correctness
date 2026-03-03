package plpa.graphics;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Links the GUI to Scala. Coursework: the program from the editor must be passed
 * directly to the Scala interpreter (getText()) and must not be modified.
 */
public class MainController {

    @FXML
    private DrawingCanvas drawingCanvas;
    @FXML
    private TextArea errorBox;
    @FXML
    private TextArea programEditor;

    /** Called by the "Run" button in main.fxml (onAction="#onRun"). */
    @FXML
    private void onRun() {
        errorBox.setText("helloworld");
    }
}
