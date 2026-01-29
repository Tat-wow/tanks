package ru.samsung.gamestudio;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

import ru.samsung.gamestudio.managers.MemoryManager;

public class GameSession {
    long sessionStartTime;
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
        score = (int) (TimeUtils.millis() - sessionStartTime) / 100 + destructedTrashNumber * 100;
        if (score == battleCount * 500 + r.nextInt(500) + 500) {
            System.out.println("pom");
        }
    }

    public void endGame() {
        updateScore();
        state = GameState.ENDED;
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if (recordsTable == null) {
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++) {
            if (recordsTable.get(foundIdx) < getScore()) break;
        }
        recordsTable.add(foundIdx, getScore());
        MemoryManager.saveTableOfRecords(recordsTable);
    }
}