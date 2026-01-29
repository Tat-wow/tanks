package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;


import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.MapMaker;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.ButtonView;
import ru.samsung.gamestudio.managers.ContactManager;
import ru.samsung.gamestudio.objects.*;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen extends ScreenAdapter {
    ContactManager contactManager;

    MyGdxGame myGdxGame;
    Tank tank;
    WallObject wall;
    EnemyTank enemy;
    NexusObject nexus;
    ButtonView button_up;
    ButtonView button_down;
    ButtonView button_right;
    ButtonView button_left;
    ButtonView button_shoot;
    MapMaker mapMaker;
    ArrayList<WallObject> wallArray;

    ArrayList<BulletObject> bulletArray;
    ArrayList<EnemyTank> enemyArray;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        contactManager = new ContactManager(myGdxGame.world);

        tank = new Tank(720, 720, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world);
        wallArray = new ArrayList<>();
        mapMaker = new MapMaker();
        String mapString = mapMaker.makeMap();
        createWallsFromMap(mapString);
        enemy = new EnemyTank(720, 1000, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world);
        enemyArray = new ArrayList<>();

        //добавляем танки
        enemyArray.add(new EnemyTank(720, 1000, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world));
        enemyArray.add(new EnemyTank(500, 800, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world));
        enemyArray.add(new EnemyTank(300, 600, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world));

        bulletArray = new ArrayList<>();
        button_left = new ButtonView(0, 0, 130, 130, GameResources.BUTTON_LEFT_PATH);
        button_right = new ButtonView(260, 0, 130, 130, GameResources.BUTTON_RIGHT_PATH);
        button_up = new ButtonView(130, 150, 130, 120, GameResources.BUTTON_UP_PATH);
        button_down = new ButtonView(130, 0, 130, 120, GameResources.BUTTON_DOWN_PATH);
        button_shoot = new ButtonView(1400, 150, 130, 120, GameResources.TARGET_PNG_PATH);
    }

    public void restart_game() {
        enemyArray.add(new EnemyTank(720, 1000, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world));
        enemyArray.add(new EnemyTank(500, 800, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world));
        enemyArray.add(new EnemyTank(300, 600, 65, 65, GameResources.TANK_IMG_PATH, myGdxGame.world));
        bulletArray.clear();
        enemyArray.clear();

    }

    @Override
    public void render(float delta){
        handleInput();
        updateBullets();

        updateEnemies();

        if (nexus.getHit()) {
            restart_game();
            myGdxGame.setScreen(myGdxGame.menuScreen);
        }

        if (enemyArray.isEmpty()) {
            restart_game();
            myGdxGame.setScreen(myGdxGame.menuScreen);
        }

        draw();
        myGdxGame.stepWorld();
    }

    private void updateEnemies() {
        Iterator<EnemyTank> iterator = enemyArray.iterator();
        while (iterator.hasNext()) {
            EnemyTank enemy = iterator.next();

            if (enemy.isDestroyed()) {
                enemy.dispose();
                myGdxGame.world.destroyBody(enemy.body);
                iterator.remove();
                continue;
            }

            enemy.move();

            if (enemy.needToShoot()) {
                BulletObject bullet = enemy.shoot();
                if (bullet != null) {
                    bulletArray.add(bullet);
                }
            }
        }
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
                }
            }
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            boolean anyKeyPressed = false;
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (button_up.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {tank.move(3);anyKeyPressed = true;}
            if (button_down.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {tank.move(4);anyKeyPressed = true;}
            if (button_right.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {tank.move(1);anyKeyPressed = true;}
            if (button_left.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {tank.move(2);anyKeyPressed = true;}
            if (button_shoot.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                BulletObject bullet = tank.needToShoot();
                if (bullet != null) {
                    bulletArray.add(bullet);
                }
            }
            if (!anyKeyPressed && !Gdx.input.isTouched()) {
                tank.move(0);
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.UP))  {
            tank.move(3);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  {
            tank.move(4);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))  {
            tank.move(1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  {
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
        for (EnemyTank enemy : enemyArray) {
            enemy.draw(myGdxGame.batch);
        }
        nexus.draw(myGdxGame.batch);
        tank.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }

}