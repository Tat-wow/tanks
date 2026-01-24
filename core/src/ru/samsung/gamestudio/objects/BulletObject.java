package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.gamestudio.GameSettings;

public class BulletObject extends GameObject {

    boolean wasHit;
    int direction;
    TextureRegion textureRegion;

    public BulletObject(String texturePath, int x, int y, int width, int height, World world, int direction) {
        super(texturePath, x, y, width, height, GameSettings.BULLET_BIT, world);
        this.direction = direction;
        this.textureRegion = new TextureRegion(getTexture());

        Vector2 velocity = new Vector2();
        switch (direction) {
            case 0: // вверх
                velocity.set(0, GameSettings.BULLET_VELOCITY);
                break;
            case 90: // влево
                velocity.set(-GameSettings.BULLET_VELOCITY, 0);
                break;
            case 180: // вниз
                velocity.set(0, -GameSettings.BULLET_VELOCITY);
                break;
            case 270: // вправо
                velocity.set(GameSettings.BULLET_VELOCITY, 0);
                break;
        }

        body.setLinearVelocity(velocity);
        body.setBullet(true);
        wasHit = false;
    }

    public boolean hasToBeDestroyed() {
        if (wasHit) return true;

        int x = getX();
        int y = getY();

        return x < 0 || x > GameSettings.SCR_WIDTH ||
                y < 0 || y > GameSettings.SCR_HEIGHT;
    }

    @Override
    public void hit() {
        wasHit = true;
    }

    public void draw(SpriteBatch batch) {
        // Рисуем пулю с вращением, как танк
        batch.draw(textureRegion,
                getX() - (width / 2f),
                getY() - (height / 2f),
                width / 2f,  // точка вращения X (центр)
                height / 2f, // точка вращения Y (центр)
                width,
                height,
                1, 1,
                direction);
    }
}
