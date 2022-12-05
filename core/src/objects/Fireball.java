package objects;

import com.badlogic.gdx.graphics.Texture;

public class Fireball {

    public static final int SPEED = 500;
    private static Texture texture;
    float x, y;
    private Player player;
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int UP = 2;
    private final int DOWN = 3;
    public boolean remove = false;
    Texture[] imgs;

    public void loadTexture(Player p) {
        if (p.getPlayerNum() == 1) {
            imgs[LEFT] = new Texture("Texture/attack/b1_L.png");
            imgs[RIGHT] = new Texture("Texture/attack/b1_R.png");
            imgs[UP] = new Texture("Texture/attack/b1_U.png");
            imgs[DOWN] = new Texture("Texture/attack/b1_D.png");
        } else {
            imgs[LEFT] = new Texture("Texture/attack/b2_L.png");
            imgs[RIGHT] = new Texture("Texture/attack/b2_R.png");
            imgs[UP] = new Texture("Texture/attack/b2_U.png");
            imgs[DOWN] = new Texture("Texture/attack/b2_D.png");
        }
    }

    public Fireball(Player player, float x, float y) {
        this.player = player;
        this.x = x;
        this.y = y;
        loadTexture(player);


    }



}
