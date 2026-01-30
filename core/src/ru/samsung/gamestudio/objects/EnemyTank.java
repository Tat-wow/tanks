package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSettings;

import java.util.Random;

public class EnemyTank extends GameObject {
    int rotation;
    long lastShotTime;
    long lastDirectionChangeTime;
    int livesLeft;
    TextureRegion textureRegion;
    int currentDirection;
    Random random;
    private World world;
    boolean isDestroyed;

    public EnemyTank(int y, int x, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.ENEMY_TANK_BIT, world);
        this.world = world;
        rotation = 0;
        textureRegion = new TextureRegion(getTexture());
        lastShotTime = TimeUtils.millis();
        lastDirectionChangeTime = TimeUtils.millis();
        random = new Random();
        currentDirection = 4; // начальное направление - стоп
        livesLeft = 1;
    }

    public boolean needToTurn() {
        return TimeUtils.millis() - lastDirectionChangeTime >= 2000;
    }

    public boolean needToShoot() {
        return TimeUtils.millis() - lastShotTime >= GameSettings.SHOOTING_COOL_DOWN;
    }

    public BulletObject shoot() {
        if (needToShoot()) {
            lastShotTime = TimeUtils.millis();

            int bulletX = getX();
            int bulletY = getY();

            switch (rotation) {
                case 0: // вверх
                    bulletY += height / 2 + 40;
                    break;
                case 90: // влево
                    bulletX -= width / 2 + 40;
                    break;
                case 180: // вниз
                    bulletY -= height / 2 + 40;
                    break;
                case 270: // вправо
                    bulletX += width / 2 + 40;
                    break;
            }

            BulletObject bullet = new BulletObject(
                    GameResources.BULLET_IMG_PATH,
                    bulletX, bulletY,
                    GameSettings.BULLET_WIDTH,
                    GameSettings.BULLET_HEIGHT,
                    world,
                    rotation
            );

            return bullet;
        }
        return null;
    }

    public void move() {

        if (isDestroyed) {
            return;
        }

        if (needToTurn()) {
            currentDirection = random.nextInt(5);
            lastDirectionChangeTime = TimeUtils.millis();
        }

        switch (currentDirection) {
            case 0: // стоп
                body.setLinearVelocity(0, 0);
                break;
            case 1: // вправо
                body.setLinearVelocity(GameSettings.TANK_SPEED, 0);
                rotation = 270;
                break;
            case 2: // влево
                body.setLinearVelocity(-GameSettings.TANK_SPEED, 0);
                rotation = 90;
                break;
            case 3: // вверх
                body.setLinearVelocity(0, GameSettings.TANK_SPEED);
                rotation = 0;
                break;
            case 4: // вниз
                body.setLinearVelocity(0, -GameSettings.TANK_SPEED);
                rotation = 180;
                break;
        }
    }

    @Override
    public void hit() {
        livesLeft--;
        if (livesLeft <= 0) {
            isDestroyed = true;
            // Можно сразу уничтожить тело здесь или пометить для удаления
        }
    }

    public void draw(SpriteBatch batch) {
        if (isDestroyed) {return;}

        batch.draw(textureRegion,
                getX() - (width / 2f),
                getY() - (height / 2f),
                width / 2f,
                height / 2f,
                width,
                height,
                1, 1,
                rotation);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

}