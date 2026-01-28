package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.gamestudio.GameSettings;

public class NexusObject extends GameObject {
    boolean wasHit;

    public NexusObject(int y, int x, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.NEXUS_BIT, world, false);
        wasHit = false;
    }

    public boolean getHit() {
        return wasHit;
    }

    @Override
    public void hit() {
        wasHit = true;
    }
}
