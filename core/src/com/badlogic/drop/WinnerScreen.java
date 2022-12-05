package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class WinnerScreen implements Screen {

    final Cat cat;
    Music bgm;

    int playerNum;
    OrthographicCamera camera;
    float stateTime;

    Texture winner;
    TextureRegion currentFrameWinner;
    Texture player;
    TextureRegion currentFramePlayer;
    Texture restart;
    TextureRegion getCurrentFrameRestart;


    public WinnerScreen(final Cat game, int playerNum){
        stateTime = 0f;
        this.cat = game;
        this.playerNum = playerNum;
        bgm = Gdx.audio.newMusic(Gdx.files.internal("Audio/victory.mp3"));
        bgm.setLooping(false);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 960, 630);
        loadTexture();
    }

    @Override
    public void show() {
        bgm.play();
    }

    public void loadTexture(){
        winner = new Texture(Gdx.files.internal("Texture/buttons/winner_anime.png"));

        if(playerNum == 1){
            player = new Texture(Gdx.files.internal("Texture/buttons/player1_anime.png"));
        } else {
            player = new Texture(Gdx.files.internal("Texture/buttons/player2_anime.png"));
        }

        restart = new Texture(Gdx.files.internal("Texture/buttons/restart.png"));


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        camera.update();

        cat.batch.begin();
        stateTime += Gdx.graphics.getDeltaTime();

        currentFramePlayer = anime(player, 1, 2);
        getCurrentFrameRestart = anime(restart, 1, 2);
        currentFrameWinner = anime(winner, 1, 2);
        cat.batch.draw(currentFrameWinner, 400 - (497*0.75f)/2, 420f, 497*0.75f, 123 * 0.75f);
        cat.batch.draw(currentFramePlayer, 400 - 482/2, 315 - 123/2, 482, 123);
        cat.batch.draw(getCurrentFrameRestart, 400, 70, 1751/5, 98/5);
        cat.batch.end();
        if (Gdx.input.isTouched()) {
            cat.setScreen(new GameScreen(cat));
            dispose();
        }

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
        Animation obj = new Animation<TextureRegion>(0.2f, frame);
        return (TextureRegion) obj.getKeyFrame(stateTime, true);
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

    }
}
