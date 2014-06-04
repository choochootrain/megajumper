package com.missionbit.megajumper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class MegaJumper extends ApplicationAdapter {
    private static final int GRAVITY = -20;
    private static final int PLAYER_JUMP_VELOCITY = 1000;

    private SpriteBatch batch;

    private int width;
    private int height;

    private Vector2 gravity;

    private Player player;

    private List<Platform> platforms;

    @Override
    public void create () {
        batch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        gravity = new Vector2();

        player = new Player();

        platforms = new ArrayList<Platform>();

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

        player.bounds.setX(width/2);
        player.bounds.setY(0);
        player.position.set(width/2, 0);
        player.velocity.set(0, 0);

        platforms.clear();
        for (int i = 0; i < 5; i++) {
            Platform p = new Platform();
            p.position.x = (float) (Math.random() * width);
            p.position.y = i * height / 5;
            p.bounds.setX(p.position.x);
            p.bounds.setY(p.position.y);
            platforms.add(p);
        }
    }

    private void updateGame() {
        //time elapsed since last call to render
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Gdx.input.justTouched()) {
            player.velocity.y = PLAYER_JUMP_VELOCITY;
        }

        //if user hits a platform, start jumping
        for (Platform platform : platforms) {
            if (platform.bounds.overlaps(player.bounds))
                player.velocity.y = PLAYER_JUMP_VELOCITY;
        }

        //apply the force of gravity
        player.velocity.add(gravity);
        player.position.mulAdd(player.velocity, deltaTime);
        player.bounds.setX(player.position.x);
        player.bounds.setY(player.position.y);
    }

    private void drawGame() {
        batch.begin();
        batch.draw(player.image, player.position.x, player.position.y);

        for (Platform platform : platforms)
            batch.draw(platform.image, platform.bounds.x, platform.bounds.y);
        batch.end();
    }
}
