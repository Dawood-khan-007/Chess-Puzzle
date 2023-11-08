package boardgame;

import boardgame.result.GameResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public class HighScoreController {
    @FXML
    private TableView<GameResult> highScoreTable;
    @FXML
    private TableColumn<GameResult, String> playerColumn;
    @FXML
    private TableColumn<GameResult, Integer> stepsColumn;
    @FXML
    private TableColumn<GameResult, String> durationColumn;
    @FXML
    private TableColumn<GameResult, String> createdColumn;
    private int numberOfMoves;
    private Duration elapsedTime;

    public void initialize() {
        initializeTableColumns();
        loadHighScores();
    }

    private void initializeTableColumns() {
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        stepsColumn.setCellValueFactory(new PropertyValueFactory<>("steps"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
    }

    public void setScoreData(int  numberOfMoves, Duration elapsedTime) {
        this.numberOfMoves = numberOfMoves;
        this.elapsedTime = elapsedTime;
    }

    private void loadHighScores() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("results.json");

        try {
            List<GameResult> results = objectMapper.readValue(file, new TypeReference<>() {
            });
            ObservableList<GameResult> observableList = FXCollections.observableArrayList();
            observableList.addAll(getBestScores(results, 10));
            highScoreTable.setItems(observableList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<GameResult> getBestScores(List<GameResult> scores, int n) {
        return scores.stream()
                .filter(GameResult::isSolved)
                .sorted(Comparator.comparing(GameResult::getHighScore).reversed())
                .limit(n)
                .toList();
    }

    @FXML
    private void handleRestartButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        fxmlLoader.setLocation(getClass().getResource("/InputName.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Mini Chess Game");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
