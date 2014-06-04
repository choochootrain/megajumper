package com.missionbit.megajumper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    protected Texture image;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Rectangle bounds;

    public Player() {
        image = new Texture("missionbit.png");
        position = new Vector2();
        velocity = new Vector2();
        bounds = new Rectangle();
    }
}
