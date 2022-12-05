package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DryFood extends PowerUpItem {
    public DryFood(int x, int y) {
        super(x,y);

        this.texture = new Texture(Gdx.files.internal("Texture/furniture/powerup/speedUp.png"));
    }
}
