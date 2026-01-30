package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static ru.samsung.gamestudio.GameSettings.SCALE;

public class GameObject {

    public int width, height;

    public boolean dynamic;

    public short cBits;

    public Body body;
    Texture texture;

    GameObject(String texturePath, int x, int y, int width, int height, short cBits, World world) {
        this.width = width;
        this.height = height;

        this.cBits = cBits;

        texture = new Texture(texturePath);
        this.dynamic = true;
        body = createBody(x, y, world);
    }

    GameObject(String texturePath, int x, int y, int width, int height, short cBits, World world, boolean dynamic) {
        this.width = width;
        this.height = height;

        this.cBits = cBits;
        this.dynamic = dynamic;
        texture = new Texture(texturePath);
        body = createBody(x, y, world);
    }

    public void hit() {

    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef();

        if (this.dynamic) {
            def.type = BodyDef.BodyType.DynamicBody;
        } else {
            def.type = BodyDef.BodyType.StaticBody;
        }
        def.fixedRotation = true;
        Body body = world.createBody(def);

        PolygonShape polyShape = new PolygonShape();

        float halfWidth = width * SCALE / 2f;
        float halfHeight = height * SCALE / 2f;
        float radius = Math.min(halfWidth, halfHeight) * 0.3f;
        Vector2[] vertices = new Vector2[8];

        // Левый верхний
        vertices[0] = new Vector2(-halfWidth + radius, halfHeight);
        vertices[1] = new Vector2(-halfWidth, halfHeight - radius);

        // Правый верхний
        vertices[2] = new Vector2(halfWidth - radius, halfHeight);
        vertices[3] = new Vector2(halfWidth, halfHeight - radius);

        // Правый нижний
        vertices[4] = new Vector2(halfWidth, -halfHeight + radius);
        vertices[5] = new Vector2(halfWidth - radius, -halfHeight);

        // Левый нижний
        vertices[6] = new Vector2(-halfWidth + radius, -halfHeight);
        vertices[7] = new Vector2(-halfWidth, -halfHeight + radius);

        polyShape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polyShape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = cBits;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        polyShape.dispose();

        body.setTransform(x * SCALE, y * SCALE, 0);
        return body;
    }


    public int getX() {
        return (int) (body.getPosition().x / SCALE);
    }

    public int getY() {
        return (int) (body.getPosition().y / SCALE);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setX(int x) {
        body.setTransform(x * SCALE, body.getPosition().y, 0);
    }

    public void setY(int y) {
        body.setTransform(body.getPosition().x, y * SCALE, 0);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - (width / 2f), getY() - (height / 2f), width, height);
    }

    public void dispose() {texture.dispose();}
}
