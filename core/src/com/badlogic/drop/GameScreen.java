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
import org.w3c.dom.css.Rect;


public class GameScreen implements Screen {
    private final Cat cat;
    private int screenWidth = 1280;
    private int screenHeight = 980;

    private int height = 70;
    private int width = 80;

    private Music bgm;

    private Texture tile, wall, window, glowingtile, player1title, player2title, matt;
    private Texture couch, chair_left, chair_right, chair_up, chair_down, couchHL, couchHR,
            bookshelfS, bookshelfL, largeDrawer, smallDrawer, sidetable,
            lamp, table, tableplant;
    private float stateTime;
    private Sound powerup;
    private Sound p1meow;
    private Sound p2meow;
    private Sound glassShattering;
    private Sound hit;

    private OrthographicCamera camera1;
    private OrthographicCamera camera2;
    private OrthographicCamera camera3;
    private Rectangle player1;
    private Rectangle player2;

    private Player cat1;
    private Player cat2;

    private MiniMap minimap;

    private ArrayList<Rectangle> unbreakable;
    private ArrayList<Breakable> breakable;
    private ArrayList<PowerUpItem> powerups;

    private Array<Rectangle> p1_fireball;
    private Array<Rectangle> p2_fireball;

    private ArrayList<Heart> p1Lives;
    private ArrayList<Heart> p2Lives;

    private ArrayList<Player> player1queue;
    private ArrayList<Player> player2queue;

    private ArrayList<Fireball> fireballs;

    private float timePassed;


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
        bgm = Gdx.audio.newMusic(Gdx.files.internal("Audio/bgm.mp3"));
        glassShattering = Gdx.audio.newSound(Gdx.files.internal("Audio/break.mp3"));
        p1meow = Gdx.audio.newSound(Gdx.files.internal("Audio/p1meow.mp3"));
        p2meow = Gdx.audio.newSound(Gdx.files.internal("Audio/p2meow.mp3"));
        hit = Gdx.audio.newSound(Gdx.files.internal("Audio/crash.mp3"));

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
        player1queue = new ArrayList<>();
        player2queue = new ArrayList<>();
        fireballs = new ArrayList<>();

        player1queue.add(cat1);
        player2queue.add(cat2);
        for (int i = 0; i < 3; i++) {
            Heart h1 = new Heart();
            Heart h2 = new Heart();
            h1.setLocation(120 + i * 55, 8);
            h2.setLocation(910 - i * 55, 8);
            p1Lives.add(h1);
            p2Lives.add(h2);
        }
        putBreakables();
        putPowerups();

    }

    public void putBreakables() {
        Breakable f1 = new Breakable(true, 960, 770);
        Breakable f2 = new Breakable(true, 960, 840);
        Breakable f3 = new Breakable(true, 800, 420);
        Breakable f4 = new Breakable(true, 640, 350);
        Breakable f5 = new Breakable(true, 240, 420);
        Breakable p1 = new Breakable(false, 160, 0);
        Breakable p2 = new Breakable(false, 160, 70);
        Breakable p3 = new Breakable(false, 480, 840);
        Breakable p4 = new Breakable(false, 720, 70);
        Breakable p5 = new Breakable(false, 1120, 210);

        breakable.add(f1);
        breakable.add(f2);
        breakable.add(f3);
        breakable.add(f4);
        breakable.add(f5);
        breakable.add(p1);
        breakable.add(p2);
        breakable.add(p3);
        breakable.add(p4);
        breakable.add(p5);
    }

    public void putPowerups() {
        Catnip cp = new Catnip(640,840);
        Catnip cp2 = new Catnip(250, 50);
        powerups.add(cp);
        powerups.add(cp2);

        WetFood wf = new WetFood(480, 140);
        WetFood wf2 = new WetFood(880, 560);
        powerups.add(wf);
        powerups.add(wf2);

        DryFood df = new DryFood(400, 630);
        DryFood df2 = new DryFood(880, 100);
        powerups.add(df);
        powerups.add(df2);


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
        matt = new Texture(Gdx.files.internal("Texture/furniture/floor/smallmatt.png"));

        window = new Texture(Gdx.files.internal("Texture/furniture/floor/window.png"));
        couch = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/Couch.png"));
        chair_left = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/chair_left.png"));
        chair_right = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/chair_right.png"));
        chair_up = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/chair_center.png"));
        chair_down = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/chair_centerD.png"));
        largeDrawer = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/largeDrawer.png"));
        smallDrawer = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/drawer.png"));
        lamp = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/lamp.png"));
        sidetable = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/sidetable.png"));
        table = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/table.png"));
        tableplant = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/plant2.png"));
        couchHL = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/couchHL.png"));
        couchHR = new Texture(Gdx.files.internal("Texture/furniture/unbreakable/couchHR.png"));
        bookshelfS =  new Texture(Gdx.files.internal("Texture/furniture/unbreakable/bookshelf.png"));
        bookshelfL =  new Texture(Gdx.files.internal("Texture/furniture/unbreakable/largeBookshelf.png"));
        player1title = new Texture("Texture/buttons/p1.png");
        player2title = new Texture("Texture/buttons/p2.png");
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        cameraSetup();

        playerCrash();
        fireballCrash();
        setBoundaries(cat1);
        setBoundaries(cat2);

        cat1.update(stateTime);
        cat2.update(stateTime);

        cat.batch.setProjectionMatrix(camera1.combined);
        Gdx.gl.glViewport( 0,140,960,980);
        cat.batch.begin();

        drawBackground();
        cat.batch.draw(matt, 0,490,80,70);
        cat.batch.draw(matt, 1200,490,80,70);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        // cat.font.draw(cat.batch, "Drops Collected: " + dropsGathered, 0, 480);
        unbreakable.clear();
        putFurnitures();
        cat.batch.draw(cat1.getCurrentFrame(), player1.x, player1.y, player1.getWidth(), player1.getHeight());
        cat.batch.draw(cat2.getCurrentFrame(), player2.x, player2.y, player2.getWidth(), player2.getHeight());
        shoot();

        for (Breakable b: breakable) {
            Animation anime = b.getAnime();
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
        cat.batch.draw(matt, 0,490,80,70);
        cat.batch.draw(matt, 1200,490,80,70);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        // cat.font.draw(cat.batch, "Drops Collected: " + dropsGathered, 0, 480);
        unbreakable.clear();
        putFurnitures();
        cat.batch.draw(cat1.getCurrentFrame(), player1.x, player1.y, player1.getWidth(), player1.getHeight());
        cat.batch.draw(cat2.getCurrentFrame(), player2.x, player2.y, player2.getWidth(), player2.getHeight());
        shoot();

        for (PowerUpItem pui: powerups) {
            if (player1.overlaps(pui.getRec())) {
                pui.taken();
                pui.effect(cat1);
                powerup.play();
                if(pui instanceof WetFood) {
                    Heart h = new Heart();
                    h.setLocation(120 + p1Lives.size() * 55, 8);
                    p1Lives.add(h);
                }
            }
            if (player2.overlaps(pui.getRec())) {
                pui.taken();
                pui.effect(cat2);
                powerup.play();
                if(pui instanceof WetFood) {
                    Heart h = new Heart();
                    h.setLocation(910 - p2Lives.size() * 55,8);
                    p2Lives.add(h);
                }
            }
            if (!pui.isTaken()) {
                cat.batch.draw(pui.getTexture(), pui.getX(), pui.getY(), pui.getWidth(), pui.getHeight());
            }
        }

        for (Breakable b: breakable) {
            Animation anime = b.getAnime();
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
        cat.batch.draw(player2title, 650,28, 0.18f*678, 0.18f*209);

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

        if (tryBurnFireball(cat1) && !cat1.isInvinsible() && p1Lives.size() > 0 ){
            player1queue.clear();
            p1Lives.remove(p1Lives.size()-1);
            p1meow.play();
            cat1 = new Player(1, stateTime);
            player1 = cat1.getRec();
            System.out.println("new lives...");
            player1queue.add(cat1);
        }

        if (tryBurnFireball(cat2) && !cat2.isInvinsible() && p2Lives.size() > 0 ){
            player2queue.clear();
            p2Lives.remove(p2Lives.size()-1);
            p2meow.play();
            cat2 = new Player(2, stateTime);
            player2 = cat2.getRec();
            System.out.println("new lives for cat2...");
            player1queue.add(cat2);
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
        if (index2 != -1) {
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
    }

    public void makeFurniture(Texture texture, int x, int y, int w, int h){
        Rectangle furniture = new Rectangle( x, y, w * width, h * height);
        cat.batch.draw(texture, furniture.x, furniture.y, furniture.width, furniture.height);
        unbreakable.add(furniture);
    }

    public void shoot() {
        if (Gdx.input.isKeyPressed(Input.Keys.F) && cat1.tryFire()) {
            fireballs.add(new Fireball(cat1));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && cat2.tryFire()){
           fireballs.add(new Fireball(cat2));
        }
        for (Fireball fb : fireballs) {
            Rectangle fbrec = fb.getRec();
            fb.update();
            fb.draw(cat.batch);
        }
    }

    public void putFurnitures() {
        //breakable
        makeFurniture(couch, 320, 700, 2, 1);
        makeFurniture(chair_left, 160, 490, 1, 1);
        makeFurniture(chair_right, 560, 490, 1,1);
        makeFurniture(largeDrawer, 240, 840, 1, 2);
        makeFurniture(smallDrawer, 320, 840, 1,1 );
        makeFurniture(largeDrawer, 1200, 840, 1, 2);
        makeFurniture(smallDrawer, 1120, 840, 1,1 );
        makeFurniture(lamp, 480, 700, 1,1);
        makeFurniture(sidetable, 560, 420, 1, 1);
        makeFurniture(table, 320, 490, 2, 1);
        makeFurniture(tableplant, 160, 560, 1, 1);
        makeFurniture(bookshelfS, 80, 840, 1, 1);
        makeFurniture(bookshelfL, 160, 840, 1,2 );
        makeFurniture(chair_up, 960, 490, 1,1);
        makeFurniture(chair_up, 880, 490, 1,1);
        makeFurniture(couchHL, 720, 280, 1, 2);
        makeFurniture(couchHR, 1120, 280, 1, 2);
        makeFurniture(table, 880, 315, 2, 1);
        makeFurniture(tableplant, 800, 840, 1,1 );
        makeFurniture(lamp, 240, 280, 1, 1);
        makeFurniture(couch, 320, 210, 2,1);

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

        for(Rectangle furniture: unbreakable) {
            Intersector.intersectRectangles(player, furniture, intersection);
            collisionAvoidance(player, furniture, 1);
        }

        for (Breakable b: breakable) {
            if (b.isBroken()) { continue; }
            Rectangle furniture = b.getFurniture();
            Intersector.intersectRectangles(player, furniture, intersection);
            if (player.overlaps(furniture)) {
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
        collisionAvoidance(player2, player1, 10);
        collisionAvoidance(player1, player2, 10);
    }

    public void fireballCrash() {
        for (Breakable b: breakable) {
            if (b.isBroken()) { continue; }
            if (tryBurnFireball(b.getFurniture())) {
                b.broken();
            }
        }
        for (Rectangle r: unbreakable) {
            tryBurnFireball(r);
        }
        for (Fireball f : fireballs) {
            if (!f.onScreen()) {
                fireballs.remove(f);
                break;
            }
        }
    }

    public boolean tryBurnFireball(Player player) {
        for (Fireball f : fireballs) {
            if (f.owner == player) { continue; }
            if (player.getRec().overlaps(f.getRec())) {
                fireballs.remove(f);
                return true;
            }
        }
        return false;
    }

    public boolean tryBurnFireball(Rectangle region) {
        for (Fireball f : fireballs) {
            if (region.overlaps(f.getRec())) {
                if(unbreakable.contains(region)){
                    hit.play();
                }
                fireballs.remove(f);
                return true;
            }
        }
        return false;
    }

    public static void collisionAvoidance(Rectangle one, Rectangle two, int ratio){
        if(one.overlaps(two)) {
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(one, two, intersection);

            float playerCenterX = (one.x + one.width * 0.5f);
            float playerCenterY = (one.y + one.height * 0.5f);
            float interCenterX = (intersection.x + intersection.width * 0.5f);
            float interCenterY = (intersection.y + intersection.height * 0.5f);
            float intersectVecX = playerCenterX - interCenterX;
            float intersectVecY = playerCenterY - interCenterY;
            if (Math.abs(intersectVecX) > Math.abs(intersectVecY)) {
                one.x += ((intersectVecX > 0) ? 1 : -1) * intersection.width/ratio;
                two.x -= ((intersectVecX > 0) ? 1 : -1) ;
            } else {
                one.y += ((intersectVecY > 0) ? 1 : -1) * intersection.height/ratio;
                two.y -= ((intersectVecY > 0) ? 1 : -1) ;
            }
        }
    }




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
