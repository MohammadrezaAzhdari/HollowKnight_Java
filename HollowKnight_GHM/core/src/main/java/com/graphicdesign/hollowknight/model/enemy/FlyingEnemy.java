package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.physics.box2d.World;

public abstract class FlyingEnemy extends Enemy{

    public FlyingEnemy(World world, float x, float y) {
        super(world, x, y);
        // Disable gravity for flying enemies
        if (b2body != null) {
            b2body.setGravityScale(0f);
        }
    }
}
