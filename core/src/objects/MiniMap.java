package objects;

import com.badlogic.drop.Cat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class MiniMap extends GameObject {
    private final int WIDTH = 320;
    private final int HEIGHT = 1000;
    private ShapeRenderer sr;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    Texture map;
    Texture p1;
    Texture p2;

    Rectangle r1;
    Rectangle r2;

    public MiniMap() {
        map = new Texture(Gdx.files.internal("Texture/map.png"));
        p1 = new Texture(Gdx.files.internal("Texture/p1.png"));
        p2 = new Texture(Gdx.files.internal("Texture/p2.png"));
        r1 = new Rectangle();
        r2 = new Rectangle();
    }

    public Rectangle getR1() {
        return r1;
    }
    public Rectangle getR2() {
        return r2;
    }

    public Texture getP1() {
        return p1;
    }

    public Texture getP2() {
        return p2;
    }

    public void update(Cat cat) {
        cat.batch.draw(map, 416, 0, 128,98);

    }




}
