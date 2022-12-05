package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Breakable extends GameObject {
    private Texture img;
    private Texture animationTexture;
    private Boolean broken;
    private TextureRegion currentFrame;
    private Rectangle furniture;

    float timePassed;

    Animation anime;

    public Breakable(boolean flower, float x, float y) {
        this.x = x;
        this.y = y;
        this.width = 60;
        this.height = 60;
        //img = (flower) ? new Texture(Gdx.files.internal("Texture/furniture/breakable/flower1.png")) :
        //        new Texture(Gdx.files.internal("Texture/furniture/breakable/plant1.png"));
        img = (flower) ? new Texture(Gdx.files.internal("Texture/furniture/breakable/flower1_anime.png")) :
                new Texture(Gdx.files.internal("Texture/furniture/breakable/plant1_anime.png"));
        furniture = new Rectangle(x, y, this.width, this.height);
        broken = false;
        anime = anime(img, 1, 4);
        timePassed = 0;
    }
    public boolean isBroken() {
        timePassed += Gdx.graphics.getDeltaTime();
        return broken;
    }

    public float getTimePassed() {
        return timePassed;
    }

    public void broken() {
        broken = true;
    }

    public Rectangle getFurniture() {
        return furniture;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Texture getImg() {
        return img;
    }
    /*public void animeSetup(float stateTime) {
        currentFrame = anime(animation, 1, 4, stateTime);
    }*/

    public Animation getAnime() {
        return anime;
    }



    public Animation anime(Texture sheet, int row, int col){
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / col,
                sheet.getHeight() / row);
        TextureRegion[] frame = new TextureRegion[col * row];
        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                frame[index++] = tmp[i][j];
            }
        }
        Animation obj = new Animation<TextureRegion>(0.5f, frame);
        return obj;
    }

}
