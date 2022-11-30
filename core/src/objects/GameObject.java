package objects;

public class GameObject {
    protected float x;
    protected float y;
    protected float dx;
    protected float dy;
    protected float radians;
    protected float speed;
    protected int width;
    protected int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int w) {
        this.width = w;
    }

    public void setHeight(int h) {
        height = h;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    protected void wrap() {
        if ( this.x < 0 ) this.x = 0;
        if ( this.x > 800 - width ) this.x = 800 - width;
        if ( this.y < height ) this.y = height;
        if ( this.y > 720 - height * 2 )this.y = 720 - height * 2;

    }
}
