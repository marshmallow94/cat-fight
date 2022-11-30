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
    Texture[] black;
    Texture[] buchi;
    Texture[] white;
    Texture[] brown;
    Texture[] orange;
    Texture[] gray;

    TextureRegion currentFrame;
    TextureRegion idleleft, idleright, goleft, goright, goup, godown, shootright, shootleft, sleep;
    float lastTouched;

    float stateTime;

    private int playerNum;
    Rectangle rec;
    public Player(int playerNum, float stateTime) {
        this.playerNum = playerNum;
        black = new Texture[9];
        buchi  = new Texture[9];
        white = new Texture[9];
        brown = new Texture[9];
        orange = new Texture[9];
        gray = new Texture[9];
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
            this.x = 735;
            left = true;
        }

        this.y = 210;
        setWidth(65);
        setHeight(65);
        this.acceleration = 200;
        this.rec = new Rectangle(x, y, getWidth(), getHeight());
        wrap();
        lastTouched = 0;

    }

    public void loadTexture(){
        //orange
        orange[LIDLE] = new Texture(Gdx.files.internal("Texture/cats/orange/idle_l.png"));
        orange[RIDLE] = new Texture(Gdx.files.internal("Texture/cats/orange/idle_r.png"));
        orange[LEFT] = new Texture(Gdx.files.internal("Texture/cats/orange/left.png"));
        orange[RIGHT] = new Texture(Gdx.files.internal("Texture/cats/orange/right.png"));
        orange[DOWN] = new Texture(Gdx.files.internal("Texture/cats/orange/front.png"));
        orange[UP] = new Texture(Gdx.files.internal("Texture/cats/orange/back.png"));
        orange[LT] = new Texture(Gdx.files.internal("Texture/cats/orange/lt.png"));
        orange[RT] = new Texture(Gdx.files.internal("Texture/cats/orange/rt.png"));
        orange[ZZZ] = new Texture(Gdx.files.internal("Texture/cats/orange/sleep.png"));

        //gray
        gray[LIDLE]= new Texture(Gdx.files.internal("Texture/cats/gray/idle_l.png"));
        gray[RIDLE] = new Texture(Gdx.files.internal("Texture/cats/gray/idle_r.png"));
        gray[LEFT] = new Texture(Gdx.files.internal("Texture/cats/gray/left.png"));
        gray[RIGHT] = new Texture(Gdx.files.internal("Texture/cats/gray/right.png"));
        gray[DOWN] = new Texture(Gdx.files.internal("Texture/cats/gray/front.png"));
        gray[UP] = new Texture(Gdx.files.internal("Texture/cats/gray/back.png"));
        gray[LT] = new Texture(Gdx.files.internal("Texture/cats/gray/lt.png"));
        gray[RT] = new Texture(Gdx.files.internal("Texture/cats/gray/rt.png"));
        gray[ZZZ] = new Texture(Gdx.files.internal("Texture/cats/gray/sleep.png"));

    }

    public Rectangle getRec() {
        return rec;
    }

    public void update(float stateTime){
        setupFrame(stateTime);
        if (Gdx.input.isKeyPressed(keyleft)) {
            rec.x -= acceleration * Gdx.graphics.getDeltaTime();
            currentFrame = goleft;
            up = false;
            right = false;
            down = false;
            left = true;
            lastTouched = 0;
        }
        if (Gdx.input.isKeyPressed(keyright)) {
            rec.x += acceleration * Gdx.graphics.getDeltaTime();
            currentFrame = goright;
            up = false;
            right = true;
            down = false;
            left = false;
            lastTouched = 0;
        }
        if (Gdx.input.isKeyPressed(keydown)) {
            rec.y -= acceleration * Gdx.graphics.getDeltaTime();
            currentFrame = godown;
            up = false;
            right = false;
            down = true;
            left = false;
            lastTouched = 0;
        }
        if (Gdx.input.isKeyPressed(keyup)) {
            rec.y += acceleration * Gdx.graphics.getDeltaTime();
            currentFrame = goup;
            up = true;
            right = false;
            down = false;
            left = false;
            lastTouched = 0;
        }
        if (!Gdx.input.isKeyPressed(keyup) && !Gdx.input.isKeyPressed(keydown)
                && !Gdx.input.isKeyPressed(keyleft) && !Gdx.input.isKeyPressed(keyright)  ){
            if (right){
                currentFrame = idleright;
            } else {
                currentFrame = idleleft;
            }
            lastTouched += stateTime;
        }
        if (Gdx.input.isKeyPressed(keyshoot)) {
            //stop all the commands

            if (left) {
                currentFrame = shootleft;
            } else if (right) {
                currentFrame = shootright;
            }
            lastTouched = 0;
        }
        if (lastTouched/1000 > 4){
            currentFrame = sleep;
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }


    public TextureRegion anime(Texture sheet, int row, int col, float stateTime){
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
        Animation obj = new Animation<TextureRegion>(0.2f, frame);
        return (TextureRegion) obj.getKeyFrame(stateTime, true);
    }

    public void setupFrame(float stateTime) {
        Texture[] color = (playerNum == 1) ? orange : gray;
        idleleft = anime(color[LIDLE], 1, 2, stateTime);
        idleright = anime(color[RIDLE], 1, 2, stateTime);
        goleft = anime(color[LEFT], 1, 6, stateTime);
        goright = anime(color[RIGHT], 1, 6, stateTime);
        godown = anime(color[DOWN] , 1, 4, stateTime);
        goup = anime(color[UP],  1, 4, stateTime);
        shootright = anime(color[LT], 1, 2, stateTime);
        shootleft = anime(color[RT] , 1, 2, stateTime);;
        sleep = anime(color[ZZZ] , 1, 4, stateTime);
    }
}
