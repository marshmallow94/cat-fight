package objects;

import com.badlogic.gdx.graphics.Texture;

public class Fireball {

    public static final int SPEED = 500;
    private static Texture texture;
    float x, y;
    private Player player;
    public boolean remove = false;


    public Fireball(Player player, float x, float y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }



}
