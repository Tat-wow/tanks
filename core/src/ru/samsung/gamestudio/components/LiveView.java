package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameResources;

public class LiveView extends View{
    String text;
    int leftLives;
    static int livePadding = 6;
    Texture texture;

    public LiveView(float x, float y) {
        super(x, y);
        texture = new Texture(GameResources.LIVE_IMG_PATH);
        this.width = texture.getWidth() * 2;
        this.height = texture.getHeight() * 2;
        leftLives = 0;
    }

    public void setLeftLives(int leftLives) {
        this.leftLives = leftLives;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (leftLives > 0) batch.draw(texture, x, y - 2 * (texture.getWidth() + livePadding), width, height);
        if (leftLives > 1) batch.draw(texture, x, y, width, height);
        if (leftLives > 2) batch.draw(texture, x, y + 2 * (texture.getWidth() + livePadding), width, height);
    }
}
