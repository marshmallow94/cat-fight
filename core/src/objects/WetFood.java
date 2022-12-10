package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class WetFood extends PowerUpItem {
    public WetFood(int x, int y) {
        super(x,y);
        this.texture = new Texture(Gdx.files.internal("Texture/furniture/powerup/extraLives.png"));
    }

    @Override
    public void effect(Player player) {
        super.effect(player);
        player.extraLives();
    }
}
