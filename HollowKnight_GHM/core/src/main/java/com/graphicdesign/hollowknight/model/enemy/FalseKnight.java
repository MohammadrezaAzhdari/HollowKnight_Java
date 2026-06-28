package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.graphicdesign.hollowknight.model.Knight;

public class FalseKnight extends Enemy{
    private Knight knight;

    public FalseKnight(World world, float x, float y, Knight knight) {
        super(world, x, y);
        this.knight = knight;
        defineEnemy(x,y);
        type = "False Knight";
    }

    @Override
    protected void defineEnemy(float x, float y) {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }
}
