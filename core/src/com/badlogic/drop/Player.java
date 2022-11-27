package com.badlogic.drop;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;


public class Player {

    int screenWidth = 800;
    int screenHeight = 630;
    int cellWidth = 80;
    int cellHeight = 70;

    int numLives = 3;


    final int WIDTH = 65;
    final int HEIGHT = 65;
    private boolean isUp, isDown, isLeft, isRight;
    TextureRegion currentFrame;
    TextureRegion idle_l, idle_r, walk_r, walk_l, walk_up, walk_down;
    Texture fireball_left, fireball_right, fireball_up, fireball_down;
    Rectangle player;

    Rectangle fireball;

    Cat cat;

    int playerNum;
    int up, down, left, right, shoot;


    public Player(int playerNum, Texture idleL, Texture idleR, Texture walkR, Texture walkL, Texture walkU, Texture walkD, float stateTime){
        this.idle_l = anime(idleL, 1, 2, stateTime);
        this.idle_r = anime(idleR, 1, 2, stateTime);
        this.walk_r = anime(walkR, 1, 6, stateTime);
        this.walk_l = anime(walkL, 1, 6, stateTime);
        this.walk_up = anime(walkU, 1, 4, stateTime);
        this.walk_down = anime(walkD, 1, 4, stateTime);
        this.player = new Rectangle();
        this.playerNum = playerNum;

        player.width = WIDTH;
        player.height = HEIGHT;

        if(playerNum == 1){ //setting player 1 keys & location
            up = Keys.W;
            down = Keys.S;
            left = Keys.A;
            right = Keys.D;
            shoot = Keys.F;
            player.x = 0;
            player.y = 210;
            currentFrame = idle_l;

        } else {            //setting player 2 keys & locatipn
            up = Keys.UP;
            down = Keys.DOWN;
            left = Keys.LEFT;
            right = Keys.RIGHT;
            shoot = Keys.SPACE;
            player.x = 720;
            player.y = 210;
            currentFrame = idle_r;
        }
        loadTexture();
        //borderCheck();
        //motionChange();

    }

    public void damage(){
        numLives--;
        if(numLives == 0){

        }
    }



    public void loadTexture(){
        fireball_left = new Texture(Gdx.files.internal("Texture/attack/attack_L.png"));
        fireball_right = new Texture(Gdx.files.internal("Texture/attack/attack_R.png"));
        fireball_up = new Texture(Gdx.files.internal("Texture/attack/attack_U.png"));
        fireball_down = new Texture(Gdx.files.internal("Texture/attack/attack_D.png"));
    }
    public Rectangle getRectngle(){
        return player;
    }

    public void setCurrentFrame1(TextureRegion currentFrame1) {
        this.currentFrame = currentFrame;
    }

    public TextureRegion getCurrentFrame(){
        return currentFrame;
    }


    public int getShoot(){
        return shoot;
    }


    public void motionChange(){
        if (Gdx.input.isKeyPressed(left)) {
            player.x -= 200 * Gdx.graphics.getDeltaTime();
            currentFrame = walk_l;
            isUp = false;
            isRight = false;
            isDown = false;
            isLeft = true;
        }
        if (Gdx.input.isKeyPressed(right)) {
            player.x += 200 * Gdx.graphics.getDeltaTime();
            currentFrame = walk_r;
            isUp = false;
            isRight = true;
            isDown = false;
            isLeft = false;
        }
        if (Gdx.input.isKeyPressed(down)) {
            player.y -= 200 * Gdx.graphics.getDeltaTime();
            currentFrame = walk_down;
            isUp = false;
            isRight = false;
            isDown = true;
            isLeft = false;
        }
        if (Gdx.input.isKeyPressed(up)) {
            player.y += 200 * Gdx.graphics.getDeltaTime();
            currentFrame = walk_up;
            isUp = true;
            isRight = false;
            isDown = false;
            isLeft = false;
        }
        if(!Gdx.input.isKeyPressed(up) && !Gdx.input.isKeyPressed(down)
                && !Gdx.input.isKeyPressed(left) && !Gdx.input.isKeyPressed(right)  ){
            if (isRight){
                currentFrame = idle_r;
            } else {
                currentFrame = idle_l;
            }

        }
    }



    public boolean isUp() {
        return isUp;
    }

    public boolean isDown() {
        return isDown;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setDown(boolean down) {
        this.isDown = down;
    }

    public void setLeft(boolean left) {
        this.isLeft = left;
    }

    public void setRight(boolean right) {
        this.isRight = right;
    }

    public void setUp(boolean up) {
        this.isUp = up;
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
        Animation obj = new Animation<TextureRegion>(0.1f, frame);
        return (TextureRegion) obj.getKeyFrame(stateTime, true);
    }
}
