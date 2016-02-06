package csim.csimemotions.log;


import java.io.Serializable;

import csim.csimemotions.Emotions;
import csim.csimemotions.States;

/**
 * Created by jorge on 26/01/16.
 */
public class LogStage implements Serializable {
    private long timestamp;
    private States game;
    private int difficulty;
    private Emotions correctAnswer;
    private Emotions userAnswer;
    private int stage;

    public LogStage(Emotions correctAnswer, int difficulty, States game, long timestamp, Emotions userAnswer, int stage) {
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
        this.game = game;
        this.timestamp = timestamp;
        this.userAnswer = userAnswer;
        this.stage = stage;
    }

    public Emotions getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Emotions correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Emotions getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(Emotions userAnswer) {
        this.userAnswer = userAnswer;
    }
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public States getGame() {
        return game;
    }

    public void setGame(States game) {
        this.game = game;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String[] getStrings() {
        String s[] = new String[6];
        s[0] = this.game.name();
        s[1] = Integer.toString(this.stage);
        s[2] = Integer.toString(this.difficulty);
        s[3] = this.correctAnswer.name();
        s[4] = this.userAnswer.name();
        s[5] = Long.toString(this.timestamp);
        return s;
    }
}
