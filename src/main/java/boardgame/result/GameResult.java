package boardgame.result;

import lombok.Data;

@Data
public class GameResult {

    private String playerName;
    private boolean solved;
    private int steps;
    private double highScore;
    private String duration;
    private String created;
    private String finishTime;

    public GameResult() {}

    public GameResult(String playerName, boolean solved, int steps, String duration, String created, double highScore, String finishTime) {
        this.playerName = playerName;
        this.solved = solved;
        this.steps = steps;
        this.duration = duration;
        this.created = created;
        this.highScore = highScore;
        this.finishTime = finishTime;
    }
}