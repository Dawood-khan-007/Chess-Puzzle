package boardgame;

import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import boardgame.model.Square;
import boardgame.result.GameResult;
import boardgame.util.BoardGameMoveSelector;
import boardgame.util.ImageStorage;
import boardgame.util.OrdinalImageStorage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.time.Duration;

import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.*;
import static javafx.util.Duration.seconds;

public class BoardGameController {

    @FXML
    private GridPane board;
     @FXML
    private TextField numberOfMovesField;
    @FXML
    private Button FinishGameButton;
    @FXML
    private TextField timeElapsedField;

    @FXML
    private Button GiveUpButton;
    private final BoardGameModel model = new BoardGameModel();
    private final BoardGameMoveSelector selector = new BoardGameMoveSelector(model);
    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);
    private Timeline timeline;
    private Duration elapsedTime = ZERO;
    private final ImageStorage<Integer> imageStorage = new OrdinalImageStorage("/images",
            "", "king.png", "rook.png", "bishop.png");
    private static String playerName;
    private Instant gameStartedAt;
    private Instant gameFinishedAt;
    private ZonedDateTime gameFinishTime;
    private ZonedDateTime gameStartTime;

    @FXML
    private void initialize() {
        createControlBindings();
        initializeTimeline();
        timeline.play();
        fillBoard();
    }

    private void createControlBindings() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    private void fillBoard() {
        Square[][] initialState = {
                {Square.KING, Square.BISHOP, Square.BISHOP},
                {Square.ROOK, Square.ROOK, Square.NONE}
        };

        Square[][] goalState = {
                {Square.BISHOP, Square.BISHOP, Square.NONE},
                {Square.ROOK, Square.ROOK, Square.KING}
        };

        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j, initialState[i][j], goalState[i][j]);
                board.add(square, j, i);
            }
        }
    }

    @FXML
    private void handleGiveUpButton() {
        handleGameOver();
    }

    private void initializeTimeline() {
        timeline = new Timeline(
                new KeyFrame(seconds(1), event -> {
                    elapsedTime = elapsedTime.plus(ofSeconds(1));
                    updateElapsedTimeText();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        gameStartedAt = Instant.now();
        gameStartTime = ZonedDateTime.now();
    }

    private void updateElapsedTimeText() {
        long seconds = elapsedTime.getSeconds();
        long minutes = seconds / 60;
        seconds %= 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        timeElapsedField.setText(formattedTime);
    }

    private StackPane createSquare(int row, int col, Square initialSquare, Square goalSquare) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.getStyleClass().add((row + col) % 2 == 0 ? "light" : "dark");

        var pieceView = new ImageView();
        pieceView.setFitHeight(100);
        pieceView.setFitWidth(75);
        pieceView.setSmooth(true);

        pieceView.imageProperty().bind(
                new ObjectBinding<>() {
                    {
                        super.bind(model.squareProperty(row, col));
                    }

                    @Override
                    protected Image computeValue() {
                        int ordinal = model.squareProperty(row, col).get().ordinal();
                        return imageStorage.get(ordinal);
                    }
                }
        );

        square.getChildren().add(pieceView);
        square.setOnMouseClicked(this::handleMouseClick);


        if (model.squareProperty(row, col).get() == initialSquare) {
            square.getStyleClass().add("initial-state");
        } else if (model.squareProperty(row, col).get() == goalSquare) {
            square.getStyleClass().add("goal-state");
        }

        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Click on square ({},{})", row, col);
        var position = new Position(row, col);

        if (selector.getPhase() == BoardGameMoveSelector.Phase.SELECT_FROM) {
            if (!model.isEmpty(position)) {
                selector.select(position);
                if (model.isValidSourcePosition(position)) {
                    var emptyPosition = model.findEmptyPosition();
                    if (emptyPosition != null) {
                        if (model.canMove(position, emptyPosition)) {
                            model.move(position, emptyPosition);
                            numberOfMoves.set(numberOfMoves.get() + 1);
                            if (isGoalStateReached()) {
                                handleGameOver();
                            }
                            selector.reset();
                        } else {

                            selector.reset();
                        }
                    }
                }
            }
        }
    }

    public static void setPlayerName(String name) {
        playerName = name;
    }

    private boolean isGoalStateReached() {
        Square[][] goalState = {
                {Square.BISHOP, Square.BISHOP, Square.NONE},
                {Square.ROOK, Square.ROOK, Square.KING}
        };

        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                if (model.squareProperty(i, j).get() != goalState[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void handleGameOver() {
        gameFinishedAt = Instant.now();
        gameFinishTime = ZonedDateTime.now();
        board.setDisable(true);
        timeline.stop();
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Over");

        if (isGoalStateReached()) {
            alert.setContentText("Congratulations, you have solved the puzzle!");
        }
        else {
            alert.setContentText("You gave up.");
        }
        alert.showAndWait();
        FinishGameButton.setDisable(false);
        saveGameResult();
        GiveUpButton.setDisable(true);

    }
    @FXML
    private void handleFinishGameButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        HighScoreController highScoreController = new HighScoreController();
        highScoreController.setScoreData(numberOfMoves.get(), elapsedTime);
        fxmlLoader.setLocation(getClass().getResource("/HighScoreController.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Mini Chess  Game");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void saveGameResult() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File("results.json");

        // Read existing game results from the file, if any
        List<GameResult> gameResultList;
        if (file.exists()) {
            try {
                gameResultList = mapper.readValue(file, new TypeReference<List<GameResult>>() {});

            } catch (IOException e){
                e.printStackTrace();
                return;
            }
        } else {
            gameResultList = new ArrayList<>();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String playerName = BoardGameController.playerName;
        boolean solved = isGoalStateReached();
        int numberOfSteps = numberOfMoves.get();

        // Calculate the duration in seconds and minutes
        long seconds = elapsedTime.getSeconds();
        long minutes = seconds / 60;
        seconds %= 60;
        String duration = String.format("%02d:%02d", minutes, seconds);

        double highScore = numberOfMoves.get() / (Duration.between(gameStartedAt, gameFinishedAt).toMillis() / 1000.0);  // Calculate high score
        GameResult gameResult = new GameResult(playerName, solved, numberOfSteps, duration, gameStartTime.format(formatter), highScore, gameFinishTime.format(formatter));
        gameResultList.add(gameResult);

        try {
            mapper.writeValue(file, gameResultList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}