package objects;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Player extends GameObject {
    private final int LIDLE = 0;
    private final int RIDLE = 1;
    private final int LEFT = 2;
    private final int RIGHT = 3;
    private final int UP = 4;
    private final int DOWN = 5;
    private final int LT = 6;
    private final int RT = 7;
    private final int ZZZ = 8;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private int acceleration;

    int keyup, keydown, keyleft, keyright, keyshoot;
    Texture[] imgs;

    TextureRegion currentFrame;
    TextureRegion idleleft, idleright, goleft, goright, goup, godown, shootright, shootleft, sleep;
    float lastTouched;

    float stateTime;

    float specialEffect = 0;

    private int playerNum;
    Rectangle rec;
    public Player(int playerNum, float stateTime) {
        this.playerNum = playerNum;
        imgs = new Texture[9];
        this.stateTime = stateTime;
        loadTexture();
        setupFrame(stateTime);

        if(playerNum == 1){ //setting player 1 keys & location
            keyup = Input.Keys.W;
            keydown = Input.Keys.S;
            keyleft = Input.Keys.A;
            keyright = Input.Keys.D;
            keyshoot = Input.Keys.F;
            this.x = 0;
            right = true;

        } else {            //setting player 2 keys & locatipn
            keyup = Input.Keys.UP;
            keydown = Input.Keys.DOWN;
            keyleft = Input.Keys.LEFT;
            keyright = Input.Keys.RIGHT;
            keyshoot = Input.Keys.SPACE;
            this.x = 1280 - 65;
            left = true;
        }

        this.y = 490;
        setWidth(65);
        setHeight(65);
        this.acceleration = 200;
        this.rec = new Rectangle(x, y, getWidth(), getHeight());
        wrap();
        lastTouched = 0;

    }

    public void loadTexture(){
        //orange
        if (playerNum == 1) {
            imgs[LIDLE] = new Texture(Gdx.files.internal("Texture/cats/orange/idle_l.png"));
            imgs[RIDLE] = new Texture(Gdx.files.internal("Texture/cats/orange/idle_r.png"));
            imgs[LEFT] = new Texture(Gdx.files.internal("Texture/cats/orange/left.png"));
            imgs[RIGHT] = new Texture(Gdx.files.internal("Texture/cats/orange/right.png"));
            imgs[DOWN] = new Texture(Gdx.files.internal("Texture/cats/orange/front.png"));
            imgs[UP] = new Texture(Gdx.files.internal("Texture/cats/orange/back.png"));
            imgs[LT] = new Texture(Gdx.files.internal("Texture/cats/orange/lt.png"));
            imgs[RT] = new Texture(Gdx.files.internal("Texture/cats/orange/rt.png"));
            imgs[ZZZ] = new Texture(Gdx.files.internal("Texture/cats/orange/sleep.png"));
        } else {
            //gray
            imgs[LIDLE] = new Texture(Gdx.files.internal("Texture/cats/gray/idle_l.png"));
            imgs[RIDLE] = new Texture(Gdx.files.internal("Texture/cats/gray/idle_r.png"));
            imgs[LEFT] = new Texture(Gdx.files.internal("Texture/cats/gray/left.png"));
            imgs[RIGHT] = new Texture(Gdx.files.internal("Texture/cats/gray/right.png"));
            imgs[DOWN] = new Texture(Gdx.files.internal("Texture/cats/gray/front.png"));
            imgs[UP] = new Texture(Gdx.files.internal("Texture/cats/gray/back.png"));
            imgs[LT] = new Texture(Gdx.files.internal("Texture/cats/gray/lt.png"));
            imgs[RT] = new Texture(Gdx.files.internal("Texture/cats/gray/rt.png"));
            imgs[ZZZ] = new Texture(Gdx.files.internal("Texture/cats/gray/sleep.png"));
        }
    }

    public void setSpecialEffect(float time) {
        this.specialEffect = time;
    }

    public void resetEffect() {
        this.acceleration = 200;
        this.specialEffect = 0;
    }

    public float getSpecialEffect() {
        return specialEffect;
    }

    public Rectangle getRec() {
        return rec;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setAcceleration(int speed) {
        this.acceleration = speed;

    }

    public void update(float stateTime){
        setupFrame(stateTime);

        if (Gdx.input.isKeyPressed(keyshoot)) {
            //stop all the commands
            Animation a;
            if (left) {
                currentFrame = shootright;
            } else if (right) {
                currentFrame = shootleft;
            } else if (up) {
                a =  animation(imgs[UP], 1, 4, 0.5f);
                currentFrame = (TextureRegion) a.getKeyFrame(0, true);
            }else if (down) {
                a =  animation(imgs[DOWN], 1, 4, 100f);
                currentFrame = (TextureRegion) a.getKeyFrame(0, true);
            }
            lastTouched = 0;
        } else if (Gdx.input.isKeyPressed(keyleft)) {
            rec.x -= acceleration * Gdx.graphics.getDeltaTime();
            currentFrame = goleft;
            up = false;
            right = false;
            down = false;
            left = true;
            lastTouched = 0;
        }
        else if (Gdx.input.isKeyPressed(keyright)) {
            rec.x += acceleration * Gdx.graphics.getDeltaTime();
            currentFrame = goright;
            up = false;
            right = true;
            down = false;
            left = false;
            lastTouched = 0;
        }
        else if (Gdx.input.isKeyPressed(keydown)) {
            rec.y -= acceleration * Gdx.graphics.getDeltaTime();
            currentFrame = godown;
            up = false;
            right = false;
            down = true;
            left = false;
            lastTouched = 0;
        }
        else if (Gdx.input.isKeyPressed(keyup)) {
            rec.y += acceleration * Gdx.graphics.getDeltaTime();
            currentFrame = goup;
            up = true;
            right = false;
            down = false;
            left = false;
            lastTouched = 0;
        }
        else if (!Gdx.input.isKeyPressed(keyup) && !Gdx.input.isKeyPressed(keydown)
                && !Gdx.input.isKeyPressed(keyleft) && !Gdx.input.isKeyPressed(keyright)
                && !Gdx.input.isKeyPressed(keyshoot)){
            if (right){
                currentFrame = idleright;
            } else {
                currentFrame = idleleft;
            }
            lastTouched += stateTime;
        }

        if (lastTouched/1000 > 6){
            currentFrame = sleep;
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }


    public TextureRegion anime(Texture sheet, int row, int col, float stateTime, float duration){
        Animation obj = animation(sheet, row, col, duration);
        return (TextureRegion) obj.getKeyFrame(stateTime, true);
    }

    public Animation animation(Texture sheet, int row, int col, float duration) {
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
        return new Animation<TextureRegion>(duration, frame);
    }

    public void setupFrame(float stateTime) {

        idleleft = anime(imgs[LIDLE], 1, 2, stateTime, 0.5f);
        idleright = anime(imgs[RIDLE], 1, 2, stateTime, 0.5f);
        goleft = anime(imgs[LEFT], 1, 6, stateTime, 0.2f);
        goright = anime(imgs[RIGHT], 1, 6, stateTime,0.2f);
        godown = anime(imgs[DOWN] , 1, 4, stateTime, 0.2f);
        goup = anime(imgs[UP],  1, 4, stateTime, 0.2f);
        shootright = anime(imgs[LT], 1, 2, stateTime, 0.4f);
        shootleft = anime(imgs[RT] , 1, 2, stateTime, 0.4f);;
        sleep = anime(imgs[ZZZ] , 1, 4, stateTime, 0.5f);
    }
}
