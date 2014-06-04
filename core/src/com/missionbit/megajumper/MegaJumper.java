package com.missionbit.megajumper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class MegaJumper extends ApplicationAdapter {
    private static final int GRAVITY = -20;
    private static final int PLAYER_JUMP_VELOCITY = 1000;
    private static final int STATE_MENU = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_WON = 2;
    private static final int STATE_LOST = 3;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    public BitmapFont font;

    private int width;
    private int height;

    private int gameState;

    private Vector2 gravity;

    private Player player;
    private int score;

    private List<Platform> platforms;

    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(width, height);

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
        gameState = STATE_MENU;

        gravity.set(0, GRAVITY);

        camera.position.set(width/2, height/2, 0);

        player.bounds.setX(width/2);
        player.bounds.setY(0);
        player.position.set(width/2, 0);
        player.velocity.set(0, 0);
        score = 0;

        platforms.clear();
        for (int i = 0; i < 20; i++) {
            Platform p = new Platform();
            p.position.x = (float) (Math.random() * width);
            p.position.y = i * 3 * height / 10;
            p.bounds.setX(p.position.x);
            p.bounds.setY(p.position.y);
            platforms.add(p);
        }
    }

    private void updateGame() {
        if (gameState == STATE_MENU) {
            //tap to start
            if (Gdx.input.justTouched()) {
                gameState = STATE_PLAYING;
            }
        } else if (gameState == STATE_PLAYING) {
            //time elapsed since last call to render
            float deltaTime = Gdx.graphics.getDeltaTime();

            //if user hits a platform *from above*, start jumping
            for (int i = 0; i < platforms.size(); i++) {
                Platform platform = platforms.get(i);
                if (platform.bounds.overlaps(player.bounds) && platform.position.y < player.position.y) {
                    player.velocity.y = PLAYER_JUMP_VELOCITY;

                    //if its the first time reaching this platform, increment your score
                    if (score <= i)
                        score = i + 1;
                }
            }

            //also jump if you're on the ground at the start
            if (player.position.y < 0 && score == 0)
                player.velocity.y = PLAYER_JUMP_VELOCITY;

            //apply gravity
            player.velocity.add(gravity);
            player.bounds.setX(player.position.x);
            player.bounds.setY(player.position.y);

            //apply accelerometer control
            float accelX = Gdx.input.getAccelerometerX();
            player.velocity.x = -accelX * 200;

            player.position.mulAdd(player.velocity, deltaTime);

            //x position wraps around
            player.position.x = (player.position.x + width) % width;

            if (player.position.y > camera.position.y)
                camera.position.y = player.position.y;

            if (score == 20)
                gameState = STATE_WON;

            if (player.position.y < camera.position.y - height/2 - player.bounds.height)
                gameState = STATE_LOST;
        } else if (gameState == STATE_WON || gameState == STATE_LOST) {
            //tap to restart game
            if (Gdx.input.justTouched()) {
                resetGame();
                gameState = STATE_MENU;
            }
        }
    }

    private void drawGame() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.setColor(0.3f, 0.3f, 0.3f, 1);

        if (gameState == STATE_MENU) {
            font.setScale(3);
            font.draw(batch, "MEGAJUMPER", width / 2 - font.getBounds("MEGAJUMPER").width / 2, height / 2);
        } else if (gameState == STATE_PLAYING) {
            batch.draw(player.image, player.position.x, player.position.y);

            for (Platform platform : platforms)
                batch.draw(platform.image, platform.bounds.x, platform.bounds.y);

            font.setScale(4);
            font.draw(batch, "" + score, width / 2, camera.position.y + height / 2 - font.getLineHeight());
        } else if (gameState == STATE_WON) {
            font.setScale(4);
            font.draw(batch, "YOU WON", width / 2 - font.getBounds("YOU WON").width / 2, camera.position.y);
        } else if (gameState == STATE_LOST) {
            font.setScale(4);
            font.draw(batch, "YOU LOST", width / 2 - font.getBounds("YOU LOST").width / 2, camera.position.y);
        }

        batch.end();
    }
}
