package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        BodyDef def = new BodyDef(); // def - defenition (определение) это объект, который содержит все данные, необходимые для посторения тела

        if (this.dynamic){
            def.type = BodyDef.BodyType.DynamicBody; // тип тела, который имеет массу и может быть подвинут под действием сил
        } else {
            def.type = BodyDef.BodyType.StaticBody;
        }
        def.fixedRotation = true; // запрещаем телу вращаться вокруг своей оси
        Body body = world.createBody(def); // создаём в мире world объект по описанному нами определению

        CircleShape circleShape = new CircleShape(); // задаём коллайдер в форме круга
        circleShape.setRadius(Math.max(width, height) * SCALE / 2f); // определяем радиус круга коллайдера

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape; // устанавливаем коллайдер
        fixtureDef.density = 0.1f; // устанавливаем плотность тела
        fixtureDef.friction = 1f; // устанвливаем коэффициент трения
        fixtureDef.filter.categoryBits = cBits;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        circleShape.dispose(); // так как коллайдер уже скопирован в fixutre, то circleShape может быть отчищена, чтобы не забивать оперативную память.

        body.setTransform(x * SCALE, y * SCALE, 0); // устанавливаем позицию тела по координатным осям и угол поворота
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
