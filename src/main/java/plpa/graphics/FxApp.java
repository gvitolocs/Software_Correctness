package plpa.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FxApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        stage.setScene(new Scene(root, 820, 520));
        stage.setTitle("PLPA Graphics IDE");
        stage.setMinWidth(820);
        stage.setMinHeight(520);
        stage.show();
    }
}
