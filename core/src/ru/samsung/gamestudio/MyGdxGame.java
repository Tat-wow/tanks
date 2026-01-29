package ru.samsung.gamestudio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

import ru.samsung.gamestudio.managers.AudioManager;
import ru.samsung.gamestudio.screens.GameScreen;
import ru.samsung.gamestudio.screens.MenuScreen;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import static ru.samsung.gamestudio.GameSettings.POSITION_ITERATIONS;
import static ru.samsung.gamestudio.GameSettings.STEP_TIME;
import static ru.samsung.gamestudio.GameSettings.VELOCITY_ITERATIONS;

public class MyGdxGame extends Game {

	public SpriteBatch batch;
	public OrthographicCamera camera;
    public Vector3 touch;

	public GameScreen gameScreen;
	public MenuScreen menuScreen;
    public BitmapFont commonBlackFont;
	public BitmapFont largeWhiteFont;
	public BitmapFont commonWhiteFont;
    public World world;
	public AudioManager audioManager;

	float accumulator = 0;

	@Override
	public void create() {
		Box2D.init();

		batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameSettings.SCR_WIDTH, GameSettings.SCR_HEIGHT);
        commonBlackFont = FontBuilder.generate(48, Color.BLACK, GameResources.FONT_PATH);
		largeWhiteFont = FontBuilder.generate(96, Color.WHITE, GameResources.FONT_PATH);
		commonWhiteFont = FontBuilder.generate(48, Color.WHITE, GameResources.FONT_PATH);
		commonBlackFont = FontBuilder.generate(48, Color.BLACK, GameResources.FONT_PATH);
		audioManager = new AudioManager();

		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);

		setScreen(menuScreen);
	}

    public void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += delta;

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

	@Override
	public void dispose() {
		batch.dispose();
	}
}