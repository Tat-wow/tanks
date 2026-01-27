package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSettings;

public class Tank extends GameObject {

    long lastShotTime;
    int livesLeft;
    int rotation;
    TextureRegion textureRegion;
    private World world;

    public Tank(int y, int x, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.SHIP_BIT, world);
        this.world = world;
        livesLeft = 3;
        textureRegion = new TextureRegion(getTexture());
        rotation = 0;
        lastShotTime = System.currentTimeMillis();
    }


    //эту функцию нужно будет вызывать в handleInput, где направление будет зависить от нажатой кнопки
    public void move(int dir) {
        switch (dir) {
            case 0: // стоп
                body.setLinearVelocity(0, 0);
                break;
            case 1: // право
                body.setLinearVelocity(GameSettings.TANK_SPEED, 0);
                rotation = 270;
                break;
            case 2: // лево
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


    public BulletObject needToShoot() {
        long currentTime = System.currentTimeMillis();
        // Проверяем задержку между выстрелами (например, 500 мс)
        if (currentTime - lastShotTime > 500) {
            lastShotTime = currentTime;

            // Вычисляем позицию для выстрела в зависимости от направления танка
            int bulletX = getX();
            int bulletY = getY();

            switch (rotation) {
                case 0: // вверх
                    bulletY += height / 2 + 50;
                    break;
                case 90: // влево
                    bulletX -= width / 2 + 50;
                    break;
                case 180: // вниз
                    bulletY -= height / 2 + 50;
                    break;
                case 270: // вправо
                    bulletX += width / 2 + 50;
                    break;
            }

            // Создаем пулю с правильным направлением
            BulletObject bullet = new BulletObject(
                    GameResources.BULLET_IMG_PATH,
                    bulletX, bulletY,
                    GameSettings.BULLET_WIDTH,
                    GameSettings.BULLET_HEIGHT,
                    world,
                    rotation // Передаем направление для пули
            );

            return bullet;
        }
        return null;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textureRegion,
                getX() - (width / 2f),
                getY() - (height / 2f),
                width / 2f,  // точка вращения X
                height / 2f, // точка вращения Y
                width,
                height,
                1, 1,
                rotation);
    }
}
