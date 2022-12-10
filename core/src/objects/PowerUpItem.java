package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public abstract class PowerUpItem extends GameObject {
    Texture texture;
    float timePassed;
    boolean isTaken;
    Rectangle rec;

    public PowerUpItem(int x, int y){
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        this.isTaken = false;
        this.rec = new Rectangle();
        rec.set(x, y, 50, 50);
    };
    public void effect(Player player){};

    public boolean isTaken() {
        return isTaken;
    }
    public void taken() {
        timePassed = Gdx.graphics.getDeltaTime();
        isTaken = true;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getRec() {
        return rec;
    }

    public float getTimePassed() {
        return timePassed;
    }



}
