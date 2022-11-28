package com.badlogic.drop;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;


public class GameScreen implements Screen {
    final Cat cat;

    int screenWidth = 800;
    int screenHeight = 630;

    int height = 70;
    int width = 80;

    Texture dropImage;

    Texture tile, wall, window, glowingtile;
    Texture cat1_l, cat1_r, rightCat1, leftCat1, upCat1, downCat1;
    Texture cat2_l, cat2_r, rightCat2, leftCat2, upCat2, downCat2;
    Texture couch, chair_left, chair_right, largeDrawer, smallDrawer, sidetable, lamp, table, tableplant;
    Texture fireball_left, fireball_right, fireball_up, fireball_down;
    float stateTime;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    Rectangle player1;
    Rectangle player2;

    Player cat1;
    Player cat2;

    ArrayList<Rectangle> unbreakable;
    ArrayList<Rectangle> breakable;

    Array<Rectangle> p1_fireball;
    Array<Rectangle> p2_fireball;



    long lastDropTime;
    int dropsGathered;
    public GameScreen(final Cat cat) {
        stateTime = 0f;
        this.cat = cat;
        this.loadTextures();
        // load the images for the droplet and the bucket, 64x64 pixels each
        unbreakable = new ArrayList<>();
        breakable = new ArrayList<>();

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 630);


        cat1 = new Player(1, cat1_l, cat1_r, rightCat1, leftCat1, upCat1, downCat1, stateTime);
        cat2 = new Player(2, cat2_l, cat2_r, rightCat2, leftCat2, upCat2, downCat2, stateTime);

        p1_fireball = new Array<>();
        p2_fireball = new Array<>();


    }

    public void drawBackground(){
        Texture background = tile;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 10; j++) {
                if (i == 8 && j == 7) {
                    background = window;
                } else if (i == 0 || i == 8) {
                    background = wall;
                } else if (i == 7 && j == 7){
                    background = glowingtile;
                } else {
                    background = tile;
                }
                cat.batch.draw(background, j * 80 ,i * 70 , width, height);
            }
        }

    }

    private void loadTextures() {
        cat1_r = new Texture(Gdx.files.internal("Texture/cats/orange/idle_r.png"));
        cat1_l = new Texture(Gdx.files.internal("Texture/cats/orange/idle_l.png"));
        cat2_r = new Texture(Gdx.files.internal("Texture/cats/gray/idle_r.png"));
        cat2_l = new Texture(Gdx.files.internal("Texture/cats/gray/idle_l.png"));
        wall = new Texture(Gdx.files.internal("Texture/furniture/floor/wall.png"));
        glowingtile = new Texture(Gdx.files.internal("Texture/furniture/floor/windowshade.png"));
        tile = new Texture(Gdx.files.internal("Texture/furniture/floor/b_title.png"));
        window = new Texture(Gdx.files.internal("Texture/furniture/floor/window.png"));
        leftCat1 = new Texture(Gdx.files.internal("Texture/cats/orange/left.png"));
        rightCat1 = new Texture(Gdx.files.internal("Texture/cats/orange/right.png"));
        downCat1 = new Texture(Gdx.files.internal("Texture/cats/orange/front.png"));
        upCat1 = new Texture(Gdx.files.internal("Texture/cats/orange/back.png"));
        leftCat2 = new Texture(Gdx.files.internal("Texture/cats/gray/left.png"));
        rightCat2 = new Texture(Gdx.files.internal("Texture/cats/gray/right.png"));
        downCat2 = new Texture(Gdx.files.internal("Texture/cats/gray/front.png"));
        upCat2 = new Texture(Gdx.files.internal("Texture/cats/gray/back.png"));
        couch = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/Couch.png"));
        chair_left = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/chair_left.png"));
        chair_right = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/chair_right.png"));
        largeDrawer = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/largeDrawer.png"));
        smallDrawer = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/drawer.png"));
        lamp = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/lamp.png"));
        sidetable = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/sidetable.png"));
        table = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/table.png"));
        tableplant = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/plant2.png"));
        fireball_left = new Texture(Gdx.files.internal("Texture/attack/attack_L.png"));
        fireball_right = new Texture(Gdx.files.internal("Texture/attack/attack_R.png"));
        fireball_up = new Texture(Gdx.files.internal("Texture/attack/attack_U.png"));
        fireball_down = new Texture(Gdx.files.internal("Texture/attack/attack_D.png"));



    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        camera.update();

        cat.batch.setProjectionMatrix(camera.combined);
        setBoundaries(cat1);
        setBoundaries(cat2);

        player1 = cat1.getRectngle();
        player2 = cat2.getRectngle();
        cat1.motionChange();
        cat2.motionChange();


        cat.batch.begin();

        attack(cat1);
        attack(cat2);

        drawBackground();
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        // cat.font.draw(cat.batch, "Drops Collected: " + dropsGathered, 0, 480);
        unbreakable.clear();
        breakable.clear();
        putFurnitures();
        cat.batch.draw(cat1.getCurrentFrame(), player1.x, player1.y, player1.getWidth(), player1.getHeight());
        cat.batch.draw(cat2.getCurrentFrame(), player2.x, player2.y, player2.getWidth(), player2.getHeight());

        cat.batch.end();

        for(Rectangle x : unbreakable){
            //touched(player1, x);
            //touched(player2, x);
        }

/*

        if (Gdx.input.isKeyPressed(Keys.LEFT))
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0)
                iter.remove();
            if (raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }*/
    }

    public void makeFurniture(Texture texture, int x, int y, int w, int h){
        Rectangle furniture = new Rectangle( x, y, w * width, h * height);
        cat.batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        cat.batch.draw(texture, furniture.x, furniture.y, furniture.width, furniture.height);
        unbreakable.add(furniture);
    }

    public void putFurnitures(){
        //breakable
        makeFurniture(couch, 320, 420, 2, 1);
        makeFurniture(chair_left, 160, 210, 1, 1);
        makeFurniture(chair_right, 560, 210, 1,1);
        makeFurniture(largeDrawer, 0, 490, 1, 2);
        makeFurniture(smallDrawer, 80, 490, 1,1 );
        makeFurniture(lamp, 480, 420, 1,1);
        makeFurniture(sidetable, 560, 140, 1, 1);
        makeFurniture(table, 320, 210, 2, 1);
        makeFurniture(tableplant, 160, 280, 1, 1);



    }

    public void setBoundaries(Player cat){
        Rectangle player = cat.getRectngle();

        //left
        if ( player.x < 0 ) {
            player.x = 0;
        }

        //right
        if ( player.x > screenWidth - width ) {
            player.x = screenWidth - width;
        }

        //bottom
        if ( player.y < height ) {
            player.y = height;
        }

        //top
        if ( player.y > screenHeight - height * 2 ){
            player.y = screenHeight - height * 2;
        }

        Rectangle intersection = new Rectangle();
        //furnitures
        for(Rectangle furniture: unbreakable){
            Intersector.intersectRectangles(player, furniture, intersection);

            if(player.overlaps(furniture)){
                float playerCenterX = (player.x + player.width * 0.5f);
                float playerCenterY = (player.y + player.height * 0.5f);
                float interCenterX = (intersection.x + intersection.width * 0.5f);
                float interCenterY = (intersection.y + intersection.height * 0.5f);
                float intersectVecX = playerCenterX - interCenterX;
                float intersectVecY = playerCenterY - interCenterY;
                if (Math.abs(intersectVecX) > Math.abs(intersectVecY)) {
                    player.x += ((intersectVecX > 0) ? 1 : -1) * intersection.width;
                } else {
                    player.y += ((intersectVecY > 0) ? 1 : -1) * intersection.height;
                }
                break;
            }
        }
    }

    public void attack(Player player){
        if(Gdx.input.isKeyPressed(player.shoot)){
            System.out.println("Pressed");
            Rectangle p = player.getRectngle();
            Array<Rectangle> fireballs = (player.playerNum == 1 ? p1_fireball : p2_fireball);
            Iterator<Rectangle> itr = fireballs.iterator();
            float ballx = p.x * 1.5f;
            float bally = p.y * 1.5f;

            if ( player.isUp() ) {
                //fireballs.add(new Rectangle(p.x * 1.5f - 7.5f, p.y * 1.5f - 15, 15, 30));
                fireballs.add(new Rectangle(ballx, bally, 15, 30));
                Rectangle fireball = fireballs.get(0);
                fireball.y += 300 * Gdx.graphics.getDeltaTime();
                cat.batch.draw(fireball_up, fireball.x, fireball.y, fireball.width, fireball.height);

                for( Rectangle furniture: unbreakable){
                    if(fireball.overlaps(furniture)){
                        itr.remove();
                    }
                }
            }
            if ( player.isDown() ) {

                fireballs.add(new Rectangle(ballx, bally, 15, 30));
                Rectangle fireball = fireballs.get(0);

                fireball.y -= 300 * Gdx.graphics.getDeltaTime();
                cat.batch.draw(fireball_down, fireball.x, fireball.y, fireball.width, fireball.height);

                for( Rectangle furniture: unbreakable){
                    if(fireball.overlaps(furniture)){
                        itr.remove();
                    }
                }

            }
            if ( player.isLeft() ) {
                fireballs.add(new Rectangle(ballx, bally, 30, 15));
                Rectangle fireball = fireballs.get(0);

                fireball.x -= 300 * Gdx.graphics.getDeltaTime();
                cat.batch.draw(fireball_left, fireball.x, fireball.y, fireball.width, fireball.height);

                for( Rectangle furniture: unbreakable){
                    if(fireball.overlaps(furniture)){
                        itr.remove();
                    }
                }

            }
            if ( player.isRight() ) {
                fireballs.add(new Rectangle(ballx, bally, 30, 15));
                Rectangle fireball = fireballs.get(0);
                fireball.x += 300 * Gdx.graphics.getDeltaTime();
                cat.batch.draw(fireball_up, fireball.x, fireball.y, fireball.width, fireball.height);

                for( Rectangle furniture: unbreakable){
                    if(fireball.overlaps(furniture)){
                        itr.remove();
                    }
                }

            }

        }
    }


    @Override
    public void show() {

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        dropImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        tile.dispose();
        wall.dispose();
        window.dispose();
    }

    public TextureRegion anime(Texture sheet, int row, int col){
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
        Animation obj = new Animation<TextureRegion>(0.1f, frame);
        return (TextureRegion) obj.getKeyFrame(stateTime, true);
    }
}
