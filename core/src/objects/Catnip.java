package objects;

import com.badlogic.drop.Cat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Catnip extends PowerUpItem {

    public Catnip(int x, int y) {
        super(x,y);
        this.texture = new Texture(Gdx.files.internal("Texture/furniture/powerup/invinsible.png"));
    }

    @Override
    public void effect(Player player) {
        timePassed = Gdx.graphics.getDeltaTime();
        player.setAcceleration(600);
        player.setSpecialEffect(timePassed);
    }


}
