package com.badlogic.drop;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import objects.*;


public class GameScreen implements Screen {
    final Cat cat;
    int screenWidth = 1280;
    int screenHeight = 980;

    int height = 70;
    int width = 80;

    Music bgm;

    Texture tile, wall, window, glowingtile, player1title, player2title;
    Texture couch, chair_left, chair_right, largeDrawer, smallDrawer, sidetable, lamp, table, tableplant;
    Texture fireball_left, fireball_right, fireball_up, fireball_down;
    float stateTime;
    Sound powerup;
    Sound glassShattering;
    Music rainMusic;
    OrthographicCamera camera1;
    OrthographicCamera camera2;
    OrthographicCamera camera3;
    Rectangle player1;
    Rectangle player2;

    Player cat1;
    Player cat2;

    MiniMap minimap;

    ArrayList<Rectangle> unbreakable;
    ArrayList<Breakable> breakable;
    ArrayList<PowerUpItem> powerups;

    Array<Rectangle> p1_fireball;
    Array<Rectangle> p2_fireball;

    ArrayList<Heart> p1Lives;
    ArrayList<Heart> p2Lives;

    float timePassed;


    public GameScreen(final Cat cat) {
        stateTime = 0f;
        timePassed = 0f;
        this.cat = cat;
        this.loadTextures();
        // load the images for the droplet and the bucket, 64x64 pixels each
        unbreakable = new ArrayList<>();
        breakable = new ArrayList<>();
        powerups = new ArrayList<>();
        minimap = new MiniMap();

        // load the drop sound effect and the rain background "music"
        powerup = Gdx.audio.newSound(Gdx.files.internal("Audio/item.mp3"));
        bgm = Gdx.audio.newMusic(Gdx.files.internal("Audio/background.mp3"));
        glassShattering = Gdx.audio.newSound(Gdx.files.internal("Audio/break.mp3"));
        bgm.setLooping(true);

        // create the camera and the SpriteBatch
        camera1 = new OrthographicCamera();
        camera1.setToOrtho(false, 478, 550);
        camera2 = new OrthographicCamera();
        camera2.setToOrtho(false, 478, 550);
        camera3 = new OrthographicCamera();
        camera3.setToOrtho(false, 960, 196);
        camera3.update();

        cat1 = new Player(1, stateTime);
        cat2 = new Player(2, stateTime);
        player1 = cat1.getRec();
        player2 = cat2.getRec();


        p1_fireball = new Array<>();
        p2_fireball = new Array<>();
        p1Lives = new ArrayList<>();
        p2Lives = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Heart h1 = new Heart();
            Heart h2 = new Heart();
            h1.setLocation(120 + i * 55, 8);
            h2.setLocation(800 + i * 55, 8);
            p1Lives.add(h1);
            p2Lives.add(h2);
        }
        putBreakables();
        putPowerups();

    }

    public void putBreakables() {
        Breakable f1 = new Breakable(true, 640, 70);
        Breakable f2 = new Breakable(true, 240, 140);
        Breakable f3 = new Breakable(true, 560, 490);
        Breakable p1 = new Breakable(false, 95, 210);
        Breakable p2 = new Breakable(false, 240, 500);
        Breakable p3 = new Breakable(false, 720, 490);
        breakable.add(f1);
        breakable.add(f2);
        breakable.add(f3);
        breakable.add(p1);
        breakable.add(p2);
        breakable.add(p3);
    }

    public void putPowerups() {
        Catnip cp = new Catnip(600,350);
        powerups.add(cp);
        System.out.println("initializikng");

    }

    public void cameraSetup() {
        float onex = player1.getX();
        float oney = player1.getY();
        float twox = player2.getX();
        float twoy = player2.getY();
        // adjust the x & y

        if(onex < 240) onex = 240;
        if(onex > 1040) onex = 1040;
        if(twox < 240) twox = 240;
        if(twox > 1040) twox = 1040;
        if(oney < 280) oney = 280;
        if(oney > 700) oney = 700;
        if(twoy < 280) twoy = 280;
        if(twoy > 700) twoy = 700;

        camera1.position.set(onex, oney, 0);
        camera1.update();
        camera2.position.set(twox, twoy, 0);
        camera2.update();
    }

    public void drawBackground(){
        Texture background = tile;
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 16; j++) {
                if (i == 13 && (j == 0 || j == 5 || j == 11)) {
                    background = window;
                } else if (i == 13) {
                    background = wall;
                } else if (i == 12 && (j == 0 || j == 5 || j == 11)){
                    background = glowingtile;
                } else {
                    background = tile;
                }
                cat.batch.draw(background, j * 80 ,i * 70 , width, height);
            }
        }

    }



    private void loadTextures() {

        wall = new Texture(Gdx.files.internal("Texture/furniture/floor/wall.png"));
        glowingtile = new Texture(Gdx.files.internal("Texture/furniture/floor/windowshade.png"));
        tile = new Texture(Gdx.files.internal("Texture/furniture/floor/b_title.png"));
        window = new Texture(Gdx.files.internal("Texture/furniture/floor/window.png"));
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

        player1title = new Texture("Texture/buttons/p1.png");
        player2title = new Texture("Texture/buttons/p2.png");
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        cameraSetup();


        playerCrash();
        setBoundaries(cat1);
        setBoundaries(cat2);



        cat1.update(stateTime);
        cat2.update(stateTime);

        cat.batch.setProjectionMatrix(camera1.combined);
        Gdx.gl.glViewport( 0,140,960,980);
        cat.batch.begin();


        drawBackground();
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        // cat.font.draw(cat.batch, "Drops Collected: " + dropsGathered, 0, 480);
        unbreakable.clear();
        putFurnitures();
        cat.batch.draw(cat1.getCurrentFrame(), player1.x, player1.y, player1.getWidth(), player1.getHeight());
        cat.batch.draw(cat2.getCurrentFrame(), player2.x, player2.y, player2.getWidth(), player2.getHeight());


        for (Breakable b: breakable) {
            Animation anime = b.getAnime();

            if(player1.overlaps(b.getFurniture()) || player2.overlaps(b.getFurniture())){
                b.broken();

            }
            if (!b.isBroken()) {
                TextureRegion currentFrame = (TextureRegion) anime.getKeyFrame(0, true);
                cat.batch.draw(currentFrame, b.getX(), b.getY(), b.getWidth(), b.getHeight());
            } else {
                TextureRegion currentFrame = (TextureRegion) anime.getKeyFrame(10, true);
                cat.batch.draw(currentFrame, b.getX(), b.getY(), b.getWidth(), b.getHeight());

            }
        }

        for (PowerUpItem pui: powerups) {
            if (player1.overlaps(pui.getRec())) {
                pui.taken();
                pui.effect(cat1);
            }
            if (player2.overlaps(pui.getRec())) {
                pui.taken();
                pui.effect(cat2);
            }
            if (!pui.isTaken()) {
                cat.batch.draw(pui.getTexture(), pui.getX(), pui.getY(), pui.getWidth(), pui.getHeight());
            }
        }

        cat.batch.end();

        cat.batch.setProjectionMatrix(camera2.combined);
        Gdx.gl.glViewport( 964,140,960,980);
        cat.batch.begin();


        drawBackground();
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        // cat.font.draw(cat.batch, "Drops Collected: " + dropsGathered, 0, 480);
        unbreakable.clear();
        putFurnitures();
        cat.batch.draw(cat1.getCurrentFrame(), player1.x, player1.y, player1.getWidth(), player1.getHeight());
        cat.batch.draw(cat2.getCurrentFrame(), player2.x, player2.y, player2.getWidth(), player2.getHeight());
        for (PowerUpItem pui: powerups) {
            if (player1.overlaps(pui.getRec())) {
                pui.taken();
                pui.effect(cat1);
                powerup.play();
            }
            if (player2.overlaps(pui.getRec())) {
                pui.taken();
                pui.effect(cat2);
                powerup.play();
            }
            if (!pui.isTaken()) {
                cat.batch.draw(pui.getTexture(), pui.getX(), pui.getY(), pui.getWidth(), pui.getHeight());
            }}

        for (Breakable b: breakable) {
            Animation anime = b.getAnime();


            if(player1.overlaps(b.getFurniture()) || player2.overlaps(b.getFurniture())){
                b.broken();

            }
            if (!b.isBroken()) {
                TextureRegion currentFrame = (TextureRegion) anime.getKeyFrame(0, true);
                cat.batch.draw(currentFrame, b.getX(), b.getY(), b.getWidth(), b.getHeight());
            } else {
                TextureRegion currentFrame = (TextureRegion) anime.getKeyFrame(10, true);
                cat.batch.draw(currentFrame, b.getX(), b.getY(), b.getWidth(), b.getHeight());

            }
        }

        cat.batch.end();


        cat.batch.setProjectionMatrix(camera3.combined);
        Gdx.gl.glViewport( 0,0,1920,392);
        cat.batch.begin();


        for (int i = 0; i < 12; i++) {
            cat.batch.draw(wall,i*80, 0, 80, 70);
        }

        cat.batch.draw(player1title, 0,28, 0.18f*678, 0.18f*209);
        cat.batch.draw(player2title, 700,28, 0.18f*678, 0.18f*209);

        for (int i = 0; i < p1Lives.size(); i++) {
            Heart h = p1Lives.get(i);
            cat.batch.draw(h.getImg(), h.getX(), h.getY(), h.getWidth(), h.getHeight());

        }

        for (int i = 0; i < p2Lives.size(); i++) {
            Heart h = p2Lives.get(i);
            cat.batch.draw(h.getImg(), h.getX(), h.getY(), h.getWidth(), h.getHeight());
        }

        minimap.update(cat);
        Rectangle b1 = minimap.getR1();
        Rectangle b2 = minimap.getR2();


        b1.set(player1.getX()/10, player1.getY()/10, 65/10, 65/10);
        b2.set(player2.getX()/10, player2.getY()/10, 65/10, 65/10);
        cat.batch.draw(minimap.getP1(), b1.x + 416, b1.y, 65/10, 65/10);
        cat.batch.draw(minimap.getP2(), b2.x + 416, b2.y, 65/10, 65/10);


        cat.batch.end();



        for(Rectangle x : unbreakable){
            //touched(player1, x);
            //touched(player2, x);
        }
        if(player1.overlaps(player2) && p1Lives.size() > 0 ){
            p1Lives.remove(p1Lives.size()-1);
        }

        int index = -1;

        for (int i = 0; i < breakable.size(); i++){
            Breakable furniture = breakable.get(i);
            if (furniture.isBroken() && furniture.getTimePassed() > 2 ) {
               index = i;
            }
        }

        int index2 = -1;
        for (int i = 0; i < powerups.size(); i++) {
            PowerUpItem pui = powerups.get(i);
            if (pui.isTaken() ) {
                index2 = i;
            }
        }

        if(cat1.getSpecialEffect() != 0) {
            cat1.setSpecialEffect(cat1.getSpecialEffect() + Gdx.graphics.getDeltaTime());
            if (cat1.getSpecialEffect() > 5) {
               cat1.resetEffect();
            }
        }


        if(cat2.getSpecialEffect() != 0 ) {
            cat2.setSpecialEffect(cat2.getSpecialEffect() + Gdx.graphics.getDeltaTime());
            if (cat2.getSpecialEffect() > 5) {
                cat2.resetEffect();
                System.out.println("done!");
            }

        }
        //System.out.println(powerups.size());

        if (index != -1) {
            glassShattering.play();
            breakable.remove(index);
        }
        if(index2 != -1) {
            powerups.remove(index2);
            System.out.println("removing....");
        }

        if (p1Lives.size() == 0) {
            cat.setScreen(new WinnerScreen(cat, 2));
            dispose();

        }

        if (p2Lives.size() == 0) {
           cat.setScreen(new WinnerScreen(cat, 1));
           dispose();
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
        //cat.batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
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
        Rectangle player = cat.getRec();


        if ( player.x < 0 ) {
            player.x = 0;
        }

        //right
        if ( player.x > screenWidth - width ) {
            player.x = screenWidth - width;
        }

        //bottom
        if ( player.y < 0 ) {
            player.y = 0;
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

        for(Breakable b: breakable){
            Rectangle furniture = b.getFurniture();
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

    public void playerCrash() {
        collisionAvoidance(player2, player1);
        collisionAvoidance(player1, player2);


    }

    public void collisionAvoidance(Rectangle one, Rectangle two){
        if(one.overlaps(two)){
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(one, two, intersection);

            if(one.overlaps(two)){
                float playerCenterX = (one.x + one.width * 0.5f);
                float playerCenterY = (one.y + one.height * 0.5f);
                float interCenterX = (intersection.x + intersection.width * 0.5f);
                float interCenterY = (intersection.y + intersection.height * 0.5f);
                float intersectVecX = playerCenterX - interCenterX;
                float intersectVecY = playerCenterY - interCenterY;
                if (Math.abs(intersectVecX) > Math.abs(intersectVecY)) {
                    one.x += ((intersectVecX > 0) ? 1 : -1) * intersection.width/10;
                    two.x -= ((intersectVecX > 0) ? 1 : -1) ;
                } else {
                    one.y += ((intersectVecY > 0) ? 1 : -1) * intersection.height/10;
                    two.y -= ((intersectVecY > 0) ? 1 : -1) ;
                }
            }
        }
    }

/*    public void attack(Player player){
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
    }*/


    @Override
    public void show() {
        bgm.play();
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
        bgm.dispose();
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
