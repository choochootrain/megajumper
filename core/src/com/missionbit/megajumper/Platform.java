package com.missionbit.megajumper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Platform {
    protected Texture image;
    protected Vector2 position;
    protected Rectangle bounds;

    public Platform() {
        image = new Texture("platform.png");
        position = new Vector2();
        bounds = new Rectangle();
    }
}

