package objects;

import com.badlogic.gdx.math.Rectangle;

public class GameObject {
    protected float x;
    protected float y;
    protected float dx;
    protected float dy;
    protected float radians;
    protected float speed;
    protected int width;
    protected int height;

    public int getWidth() { return width; }
    public int getHeight() {
        return height;
    }

    public void setWidth(int w) {
        this.width = w;
    }
    public void setHeight(int h) { this.height = h; }

    public float getX() { return x; }
    public float getY() {
        return y;
    }

    public Rectangle getRec() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }

    public boolean onScreen() {
        if (this.x < 0) return false;
        if (this.x > 1280 - width) return false;
        if (this.y < height) return false;
        if (this.y > 980 - height * 2) return false;
        return true;
    }

    protected void wrap() {
        if (this.x < 0) this.x = 0;
        if (this.x > 1280 - width) this.x = 1280 - width;
        if (this.y < height) this.y = height;
        if (this.y > 980 - height * 2) this.y = 980 - height * 2;
    }
}
