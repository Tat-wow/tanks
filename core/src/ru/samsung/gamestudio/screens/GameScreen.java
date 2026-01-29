package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSession;
import ru.samsung.gamestudio.GameState;
import ru.samsung.gamestudio.MapMaker;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.ButtonView;
import ru.samsung.gamestudio.components.ImageView;
import ru.samsung.gamestudio.components.LiveView;
import ru.samsung.gamestudio.components.TextView;
import ru.samsung.gamestudio.managers.ContactManager;
import ru.samsung.gamestudio.objects.*;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen extends ScreenAdapter {
    ContactManager contactManager;

    MyGdxGame myGdxGame;
    Tank tank;
    GameSession gameSession;
    EnemyTank enemy;
    NexusObject nexus;
    ButtonView button_up;
    ButtonView button_down;
    ButtonView button_right;
    ButtonView button_left;
    ButtonView button_shoot;
    MapMaker mapMaker;
    ImageView fullWhiteoutView;
    ArrayList<WallObject> wallArray;
    LiveView liveView;
    TextView pauseTextView;
    ButtonView pauseButton;
    ButtonView homeButton;
    ButtonView continueButton;

    ArrayList<BulletObject> bulletArray;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();

        contactManager = new ContactManager(myGdxGame.world);

        tank = new Tank(100, 725, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world);
        liveView = new LiveView(100, 500);
        wallArray = new ArrayList<>();
        mapMaker = new MapMaker();
        String mapString = mapMaker.makeMap();
        createWallsFromMap(mapString);
        enemy = new EnemyTank(800, 1010, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world);
        bulletArray = new ArrayList<>();
        button_left = new ButtonView(0, 0, 130, 130, GameResources.BUTTON_LEFT_PATH);
        button_right = new ButtonView(260, 0, 130, 130, GameResources.BUTTON_RIGHT_PATH);
        button_up = new ButtonView(130, 150, 130, 120, GameResources.BUTTON_UP_PATH);
        button_down = new ButtonView(130, 0, 130, 120, GameResources.BUTTON_DOWN_PATH);
        button_shoot = new ButtonView(1400, 150, 130, 120, GameResources.TARGET_PNG_PATH);
        pauseButton = new ButtonView(1400, 842, 150, 150, GameResources.PAUSE_IMAGE_PATH);
        homeButton = new ButtonView(700, 542, 300, 300, GameResources.HOME_BUTTON_IMAGE_PATH);
        continueButton = new ButtonView(700, 242, 300, 300, GameResources.PLAY_BUTTON_IMAGE_PATH);
        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 700, 842, "Pause");
        fullWhiteoutView = new ImageView(350, 0, GameResources.WHITEOUT_IMAGE_PATH);
    }

    @Override
    public void render(float delta){
        handleInput();
        if (gameSession.state == GameState.PLAYING) {
            updateBullets();
            updateWalls();

            enemy.move();

            if (enemy.needToShoot()) {
                BulletObject bullet = enemy.shoot();
                if (bullet != null) {
                    bulletArray.add(bullet);
                }
            }
            liveView.setLeftLives(tank.livesLeft());

            if (nexus.getHit()) {
                // проигрышь
            }

            myGdxGame.stepWorld();
        }
        draw();
    }

    @Override
    public void show() {
        restartGame();
    }

    private void createWallsFromMap(String mapString) {
        // Очищаем старые стены
        wallArray.clear();

        // Разбиваем на строки
        String[] rows = mapString.split("\n");

        // Проходим по каждой строке карты
        for (int y = 0; y < rows.length; y++) {
            String row = rows[y];
            // Проходим по каждому символу в строке
            for (int x = 0; x < row.length(); x++) {
                char cell = row.charAt(x);

                // Если символ '#', создаем стену
                if (cell == '#') {
                    WallObject wall = new WallObject(
                            (rows.length - y - 1) * 70 - 25,
                            x * 70 + 380,
                            70,                        // ширина
                            70,                        // высота
                            GameResources.WALL_ING_PATH,
                            myGdxGame.world,
                            false
                    );
                    wallArray.add(wall);
                } else if (cell == 'o'){
                    nexus = new NexusObject((rows.length - y - 1) * 70 - 25, x * 70 + 380,
                            70, 70,
                            GameResources.NEXUS_IMG_PATH, myGdxGame.world);
                } else if (cell == '$'){
                    WallObject wall = new WallObject(
                            (rows.length - y - 1) * 70 - 25,
                            x * 70 + 380,
                            70,                        // ширина
                            70,                        // высота
                            GameResources.UNBREAK_WALL_ING_PATH,
                            myGdxGame.world,
                            true
                    );
                    wallArray.add(wall);
                }
            }
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            boolean anyKeyPressed = false;
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    if (button_up.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        tank.move(3);
                        anyKeyPressed = true;
                    }
                    if (button_down.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        tank.move(4);
                        anyKeyPressed = true;
                    }
                    if (button_right.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        tank.move(1);
                        anyKeyPressed = true;
                    }
                    if (button_left.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        tank.move(2);
                        anyKeyPressed = true;
                    }
                    if (button_shoot.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        BulletObject bullet = tank.needToShoot();
                        if (bullet != null) {
                            bulletArray.add(bullet);
                            if (myGdxGame.audioManager.isSoundOn)
                                myGdxGame.audioManager.shootSound.play(0.04f);
                        }
                    }
                    if (!anyKeyPressed && !Gdx.input.isTouched()) {
                        tank.move(0);
                    }
                    break;
                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                        restartGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }
        }


            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                tank.move(3);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                tank.move(4);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                tank.move(1);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                tank.move(2);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                BulletObject bullet = tank.needToShoot();
                if (bullet != null) {
                    bulletArray.add(bullet);
                }
            }

            if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) && !Gdx.input.isTouched()) {
                tank.move(0);
            }
    }

    private void updateBullets() {
        Iterator<BulletObject> iterator = bulletArray.iterator();
        while (iterator.hasNext()) {
            BulletObject bullet = iterator.next();
            if (bullet.hasToBeDestroyed()) {
                bullet.dispose();
                myGdxGame.world.destroyBody(bullet.body);
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.04f);
                iterator.remove();
            }
        }
    }

    private void updateWalls() {
        Iterator<WallObject> iterator = wallArray.iterator();
        while (iterator.hasNext()) {
            WallObject wall = iterator.next();
            if (wall.hasToBeDestroyed()) {
                wall.dispose();
                myGdxGame.world.destroyBody(wall.body);
                iterator.remove();
            }
        }
    }

    private void draw() {

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        for (WallObject wall : wallArray) wall.draw(myGdxGame.batch);
        button_left.draw(myGdxGame.batch);
        button_down.draw(myGdxGame.batch);
        button_right.draw(myGdxGame.batch);
        button_up.draw(myGdxGame.batch);
        button_shoot.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        enemy.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);
        nexus.draw(myGdxGame.batch);
        tank.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        if (gameSession.state == GameState.PAUSED) {
            fullWhiteoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        }
        myGdxGame.batch.end();
    }

    private void restartGame() {

        for (int i = 0; i < bulletArray.size(); i++) {
            myGdxGame.world.destroyBody(bulletArray.get(i).body);
            bulletArray.remove(i--);
        }

        for (int i = 0; i < wallArray.size(); i++) {
            myGdxGame.world.destroyBody(wallArray.get(i).body);
            wallArray.remove(i--);
        }

        wallArray = new ArrayList<>();
        mapMaker = new MapMaker();
        String mapString = mapMaker.makeMap();
        createWallsFromMap(mapString);

        if (tank != null) {
            myGdxGame.world.destroyBody(tank.body);
        }

        tank = new Tank(
                100, 725,
                65, 65,
                GameResources.TANK_IMG_PATH,
                myGdxGame.world
        );

        bulletArray.clear();
        gameSession.startGame();
    }

}