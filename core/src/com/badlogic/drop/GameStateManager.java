package com.badlogic.drop;

import gamestates.GameState;

public class GameStateManager {
    private GameState gameState;
    public final int MENU = 0;
    public final int PLAY = 1;


    public GameStateManager() {

    }

    public void setState(int state) {
        if (state == MENU) {

        }
        if (state == PLAY) {

        }
    }

    public void update(float dt) {
        gameState.draw();
    }

    public void draw() {
        gameState.draw();
    }


}
