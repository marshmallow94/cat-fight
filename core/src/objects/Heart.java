package objects;

import com.badlogic.drop.Cat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

public class Heart extends GameObject {

    private Texture heartimg;


    public Heart() {
        heartimg = new Texture("Texture/buttons/heart.png");
        this.height = 40;
        this.width = 40;
    }

    public Texture getImg() {
        return heartimg;
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public void delete() {
        heartimg.dispose();
    }
}
