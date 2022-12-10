package objects;

import com.badlogic.drop.Cat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Catnip extends PowerUpItem {

    public Catnip(int x, int y) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.isTaken = false;
        this.rec = new Rectangle();
        rec.set(x, y, 30, 30);
        this.texture = new Texture(Gdx.files.internal("Texture/furniture/powerup/invinsible.png"));
    }

    @Override
    public void effect(Player player) {
        timePassed = Gdx.graphics.getDeltaTime();
        player.InvinsibleOn();
        System.out.println("Invinsible on");
        player.setSpecialEffect(timePassed);
    }


}
