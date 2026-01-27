package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.ButtonView;
import ru.samsung.gamestudio.managers.ContactManager;
import ru.samsung.gamestudio.objects.BulletObject;
import ru.samsung.gamestudio.objects.Tank;
import ru.samsung.gamestudio.objects.WallObject;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen extends ScreenAdapter {
    ContactManager contactManager;

    MyGdxGame myGdxGame;
    Tank tank;
    WallObject wall;
    ButtonView button_up;
    ButtonView button_down;
    ButtonView button_right;
    ButtonView button_left;
    ButtonView button_shoot;

    ArrayList<BulletObject> bulletArray;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        contactManager = new ContactManager(myGdxGame.world);

        this.tank = new Tank(720, 720, 200, 160, GameResources.TANK_IMG_PATH, myGdxGame.world);
        wall = new WallObject(500, 300, 100, 100, GameResources.WALL_ING_PATH, myGdxGame.world);
        bulletArray = new ArrayList<>();
        button_left = new ButtonView(0, 0, 130, 130, GameResources.BUTTON_LEFT_PATH);
        button_right = new ButtonView(260, 0, 130, 130, GameResources.BUTTON_RIGHT_PATH);
        button_up = new ButtonView(130, 150, 130, 120, GameResources.BUTTON_UP_PATH);
        button_down = new ButtonView(130, 0, 130, 120, GameResources.BUTTON_DOWN_PATH);
        button_shoot = new ButtonView(1400, 150, 130, 120, GameResources.TARGET_PNG_PATH);
    }

    @Override
    public void render(float delta){
        handleInput();
        updateBullets();
        draw();
        myGdxGame.stepWorld();
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
        button_left.draw(myGdxGame.batch);
        button_down.draw(myGdxGame.batch);
        button_right.draw(myGdxGame.batch);
        button_up.draw(myGdxGame.batch);
        button_shoot.draw(myGdxGame.batch);
        wall.draw(myGdxGame.batch);
        tank.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }

}