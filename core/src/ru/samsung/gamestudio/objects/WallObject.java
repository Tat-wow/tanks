package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.physics.box2d.World;

import ru.samsung.gamestudio.GameSettings;

public class WallObject extends GameObject{
    boolean wasGetHit;

    public WallObject(int y, int x, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.WALL_BIT, world, false);
        wasGetHit = false;
    }

    public boolean hasToBeDestroyed() {
        return wasGetHit;
    }

    @Override
    public void hit() {
        wasGetHit = true;
    }
}
