package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.graphicdesign.hollowknight.view.screen.GameScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected GameScreen screen;

    public Enemy(GameScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
    }

    abstract void defineEnemy();
}
