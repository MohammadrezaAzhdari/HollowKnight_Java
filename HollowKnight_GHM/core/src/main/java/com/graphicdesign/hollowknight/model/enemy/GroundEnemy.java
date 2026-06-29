package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.physics.box2d.World;

public abstract class GroundEnemy extends Enemy{
    public boolean walkRight = false;

    public GroundEnemy(World world, float x, float y) {
        super(world, x, y);
    }

    public void reverseDirection() {
        if (knockBackTimer <= 0 && !isDead) {
            walkRight = !walkRight;
        }
    }
}
