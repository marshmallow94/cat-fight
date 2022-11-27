package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final Cat cat;
    public static Texture backgroundTexture;
    public static Texture titleTexture;
    public static Texture startTexture;
    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 2, FRAME_ROWS = 1;

    // Objects used
    Animation<TextureRegion> catAnimation1;
    Animation<TextureRegion> catAnimation2;
    Animation<TextureRegion> catAnimation3;
    Animation<TextureRegion> catAnimation4;

    Texture catSheet1;
    Texture catSheet2;
    Texture catSheet3;
    Texture catSheet4;

    // A variable for tracking elapsed time for the animation
    float stateTime;


    OrthographicCamera camera;

    public MainMenuScreen(final Cat game) {
        this.cat = game;
        loadTextures();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 630);
    }

    private void loadTextures() {
        backgroundTexture = new Texture(Gdx.files.internal("tile.png"));
        titleTexture = new Texture(Gdx.files.internal("title.png"));
        startTexture= new Texture(Gdx.files.internal("start.png"));
        catSheet1 = new Texture(Gdx.files.internal("Texture/cats/orange/t.png"));
        catSheet2 = new Texture(Gdx.files.internal("Texture/cats/white/t.png"));
        catSheet3 = new Texture(Gdx.files.internal("Texture/cats/buchi/t.png"));
        catSheet4 = new Texture(Gdx.files.internal("Texture/cats/brown/t.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(catSheet1,
                catSheet1.getWidth() / FRAME_COLS,
                catSheet1.getHeight() / FRAME_ROWS);

        TextureRegion[][] tmp2 = TextureRegion.split(catSheet2,
                catSheet2.getWidth() / FRAME_COLS,
                catSheet2.getHeight() / FRAME_ROWS);
        TextureRegion[][] tmp3 = TextureRegion.split(catSheet3,
                catSheet3.getWidth() / FRAME_COLS,
                catSheet3.getHeight() / FRAME_ROWS);
        TextureRegion[][] tmp4 = TextureRegion.split(catSheet4,
                catSheet4.getWidth() / FRAME_COLS,
                catSheet4.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] catFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                catFrames[index++] = tmp[i][j];
            }
        }
        TextureRegion[] catFrames2 = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index2 = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                catFrames2[index2++] = tmp2[i][j];
            }
        }
        TextureRegion[] catFrames3 = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index3 = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                catFrames3[index3++] = tmp3[i][j];
            }
        }
        TextureRegion[] catFrames4 = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index4 = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                catFrames4[index4++] = tmp4[i][j];
            }
        }



        // Initialize the Animation with the frame interval and array of frames
        catAnimation1 = new Animation<TextureRegion>(0.06f, catFrames);
        catAnimation2 = new Animation<TextureRegion>(0.045f, catFrames2);
        catAnimation3 = new Animation<TextureRegion>(0.07f, catFrames3);
        catAnimation4 = new Animation<TextureRegion>(0.05f, catFrames4);


        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        stateTime = 0f;

    }



    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 100);

        camera.update();
        cat.batch.setProjectionMatrix(camera.combined);



        cat.batch.begin();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 10; j++) {
                cat.batch.draw(backgroundTexture, j * 80, i * 70, 80, 70);
            }
        }

        //game.batch.draw(new Texture(Gdx.files.internal("game_assets/furniture/floor/smallmatt.png")),720,550,80,80);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = catAnimation1.getKeyFrame(stateTime, true);
        cat.batch.draw(currentFrame, 50, 50, 250, 250); // Draw current frame at (50, 50)

        TextureRegion currentFrame2 = catAnimation2.getKeyFrame(stateTime, true);
        cat.batch.draw(currentFrame2, 250, 50, 250, 250); // Draw current frame at (50, 50)

        TextureRegion currentFrame3 = catAnimation3.getKeyFrame(stateTime, true);
        cat.batch.draw(currentFrame3, 450, 50, 250, 250); // Draw current frame at (50, 50)

        TextureRegion currentFrame4 = catAnimation4.getKeyFrame(stateTime, true);
        cat.batch.draw(currentFrame4, 500, 50, 250, 250); // Draw current frame at (50, 50)

        cat.batch.draw(titleTexture, 200, 300, 400, 100);
        cat.batch.draw(startTexture, 300, 200, 200, 100);

        //game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
        //game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        cat.batch.end();

        if (Gdx.input.isTouched()) {
            cat.setScreen(new game(cat));
            dispose();
        }
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public void resize(int x, int y){

    }

    @Override
    public void dispose() {

    }


}
