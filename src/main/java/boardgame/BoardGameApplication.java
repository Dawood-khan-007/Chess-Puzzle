package boardgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BoardGameApplication extends Application {

    private final FXMLLoader fxmlLoader = new FXMLLoader();

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/InputName.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Mini Chess Game");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
