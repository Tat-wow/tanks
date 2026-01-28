package ru.samsung.gamestudio;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Tanks2026");
        config.setWindowedMode(GameSettings.SCR_WIDTH / 2, GameSettings.SCR_HEIGHT / 2);
        new Lwjgl3Application(new MyGdxGame(), config);
    }
}