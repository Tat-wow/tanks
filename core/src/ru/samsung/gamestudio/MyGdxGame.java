package ru.samsung.gamestudio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.gamestudio.screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import static ru.samsung.gamestudio.GameSettings.POSITION_ITERATIONS;
import static ru.samsung.gamestudio.GameSettings.STEP_TIME;
import static ru.samsung.gamestudio.GameSettings.VELOCITY_ITERATIONS;

public class MyGdxGame extends Game {

	public SpriteBatch batch;
	public OrthographicCamera camera;
    public Vector3 touch;

	public GameScreen gameScreen;
    public BitmapFont commonBlackFont;
    public World world;

    float accumulator = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameSettings.SCR_WIDTH, GameSettings.SCR_HEIGHT);
        commonBlackFont = FontBuilder.generate(24, Color.BLACK, GameResources.FONT_PATH);

		gameScreen = new GameScreen(this);

		setScreen(gameScreen);
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