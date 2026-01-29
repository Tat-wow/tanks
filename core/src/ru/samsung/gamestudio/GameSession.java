package ru.samsung.gamestudio;

import com.badlogic.gdx.utils.TimeUtils;

public class GameSession {

    public GameState state;


    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
    }
}




