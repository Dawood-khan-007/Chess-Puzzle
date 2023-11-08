package boardgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class InputNameController {

    @FXML
    protected TextField playerName;

    private final FXMLLoader fxmlLoader = new FXMLLoader();

    @FXML
    public void handleStartButton(ActionEvent actionEvent) throws IOException {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        if(playerName.getText().isEmpty()) {
            alert.setContentText("Please Enter Player Name ");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            fxmlLoader.setLocation(getClass().getResource("/BoardGame.fxml"));
            BoardGameController.setPlayerName(playerName.getText());
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}