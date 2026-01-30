package ru.samsung.gamestudio;

import java.util.Random;

public class GameSession {
    public GameState state;
    Random r;

    private int score;


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

    public void updateScore() {
        score += 100;
    }

    public void endGame() {
        state = GameState.ENDED;
    }

    public void delScore() {
        score = 0;
    }
}