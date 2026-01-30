package ru.samsung.gamestudio;

import java.util.Random;

public class GameSession {
    public GameState state;
    byte battleCount;
    Random r;

    private int score;
    int destructedTrashNumber;


    public GameSession() {
        r = new Random();
    }

    public void startGame() {
        state = GameState.PLAYING;
    }

    public void pauseGame() {
        state = GameState.PAUSED;
    }

    public void resumeGame() {
        state = GameState.PLAYING;
    }

    public int getScore() {
        return score;
    }

    public void destructionRegistration() {
        destructedTrashNumber += 1;
    }

    public void updateScore() {
        System.out.println("update");
    }

    public void endGame() {
        updateScore();
        state = GameState.ENDED;
    }
}