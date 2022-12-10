package objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Fireball extends GameObject {
    public boolean remove = false;

    public Texture getImg() {
        return img;
    }

    Texture img;

    public void loadTexture(Player p) {
        if (p.getPlayerNum() == 1) {
            if (p.isLeft()) img = new Texture("Texture/attack/b1_L.png");
            else if (p.isRight()) img = new Texture("Texture/attack/b1_R.png");
            else if (p.isUp()) img =  new Texture("Texture/attack/b1_U.png");
            else if (p.isDown()) img = new Texture("Texture/attack/b1_D.png");
        } else {
            if (p.isLeft()) img  = new Texture("Texture/attack/b2_L.png");
            else if (p.isRight()) img = new Texture("Texture/attack/b2_R.png");
            else if (p.isUp()) img = new Texture("Texture/attack/b2_U.png");
            else if (p.isDown()) img = new Texture("Texture/attack/b2_D.png");
        }
    }

    float ddx = 0.0f;
    float ddy = 0.0f;

    public Player owner;

    //ratio 4:9
    public Fireball(Player p) {
        this.owner = p;
        loadTexture(p);
        this.x = p.rec.getX();
        this.y = p.rec.getY();
        this.speed = 400;
        this.dx = 0.0f;
        this.dy = 0.0f;
        if (p.isLeft()) {
            this.width = 32;
            this.height = 14;
            this.x = this.x - this.width;
            this.y = this.y + (p.height - this.height) / 2.0f - 8.0f;
            this.dx = -1.0f;
        } else if (p.isRight()) {
            this.x = this.x + p.width;
            this.y = this.y + (p.height - this.height) / 2.0f - 8.0f;
            this.width = 32;
            this.height = 14;
            this.dx = 1.0f;
        } else if (p.isUp()) {
            this.x = this.x + (p.width - this.width) / 2.0f - 8.0f;
            this.y = this.y + p.height;
            this.width = 14;
            this.height = 32;
            this.dy = 1.0f;
        } else if (p.isDown()) {
            this.x = this.x + (p.width - this.width) / 2.0f - 8.0f;
            //this.y = this.y + p.height;
            this.width = 14;
            this.height = 32;
            this.dy = -1.0f;
        }
        else {
            this.dx = (float)Math.random() - 0.5f;
            this.dy = (float)Math.random() - 0.5f;
            this.width = 20;
            this.height = 20;
        }
        float curveFactor = 0.1f;
        this.ddx += ((float)Math.random() - 0.5f) * curveFactor;
        this.ddy += ((float)Math.random() - 0.5f) * curveFactor;
    }

    public void update() {
        // ^ the Player p input can be removed ^
        this.x += speed * this.dx * Gdx.graphics.getDeltaTime();
        this.y += speed * this.dy * Gdx.graphics.getDeltaTime();
        this.dx += this.ddx * Gdx.graphics.getDeltaTime();
        this.dy += this.ddy * Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(getImg(), this.x, this.y, this.width, this.height);
    }
}
