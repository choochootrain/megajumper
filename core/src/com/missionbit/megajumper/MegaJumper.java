package com.missionbit.megajumper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MegaJumper extends ApplicationAdapter {
    private static final int GRAVITY = -20;
    private static final int PLAYER_JUMP_VELOCITY = 1000;

    private SpriteBatch batch;

    private int width;
    private int height;

    private Vector2 gravity;

    private Texture playerImage;
    private Rectangle playerBounds;
    private Vector2 playerPosition;
    private Vector2 playerVelocity;

    private Texture platformImage;
    private Rectangle platformBounds;

    @Override
    public void create () {
        batch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        gravity = new Vector2();

        playerImage = new Texture("missionbit.png");
        playerBounds = new Rectangle();
        playerPosition = new Vector2();
        playerVelocity = new Vector2();

        platformImage = new Texture("platform.png");
        platformBounds = new Rectangle();

        resetGame();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateGame();
        drawGame();
    }

    private void resetGame() {
        gravity.set(0, GRAVITY);

        playerBounds.set(width/2, 0, playerImage.getWidth(), playerImage.getHeight());
        playerPosition.set(width/2, 0);
        playerVelocity.set(0, 0);

        platformBounds.set(width/2, height/2, platformImage.getWidth(), platformImage.getHeight());
    }

    private void updateGame() {
        //time elapsed since last call to render
        float deltaTime = Gdx.graphics.getDeltaTime();

        //if user touched the screen, or you hit the platform, start jumping
        if (Gdx.input.justTouched() || platformBounds.overlaps(playerBounds)) {
            playerVelocity.y = PLAYER_JUMP_VELOCITY;
        }

        //apply the force of gravity
        playerVelocity.add(gravity);
        playerPosition.mulAdd(playerVelocity, deltaTime);
        playerBounds.setX(playerPosition.x);
        playerBounds.setY(playerPosition.y);
    }

    private void drawGame() {
        batch.begin();
        batch.draw(playerImage, playerPosition.x, playerPosition.y);
        batch.draw(platformImage, platformBounds.x, platformBounds.y);
        batch.end();
    }
}
