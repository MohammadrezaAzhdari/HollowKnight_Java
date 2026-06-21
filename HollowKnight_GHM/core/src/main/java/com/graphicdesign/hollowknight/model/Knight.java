package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.enums.KnightAnimation;

public class Knight {
    private World world;
    public Body b2body;
    public KnightAnimation animation = KnightAnimation.IDLE; // TODO : Change it to landing
    public float stateTime = 0f;
    private boolean runningRight = true;

    public Knight (World world, Vector2 spawn) {
        this.world = world;
        defineKnight(spawn);
    }

    public void defineKnight(Vector2 spawn) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawn.x,spawn.y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        float width = Constants.KNIGHT_B2BODY_WIDTH / Constants.PPM;
        float height = Constants.KNIGHT_B2BODY_HEIGHT / Constants.PPM;
        shape.setAsBox(width, height);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        fdef.filter.categoryBits = Constants.KNIGHT_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT | Constants.DESTROYABLE_BIT | Constants.ENEMY_BIT;

        b2body.createFixture(fdef).setUserData("player");

        shape.dispose();
    }
    public void draw(SpriteBatch batch) {
        // 1. Get the current frame
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(this.animation);
        TextureRegion keyFrame = animation.getKeyFrame(this.stateTime, true);

        correctKnightDirection(keyFrame);
        // 2. Calculate scaled width/height
        float width = keyFrame.getRegionWidth() / Constants.PPM;
        float height = keyFrame.getRegionHeight() / Constants.PPM;

        float padX = -0.1f;
        float padY = 0.65f;

        // 3. Calculate X and Y from Box2D body
        float x = this.b2body.getPosition().x - (width / 2f) + padX;
        float y = this.b2body.getPosition().y - (height / 2f) + padY;

        // 4. Draw it
        batch.draw(keyFrame, x, y, width, height);
    }

    private void correctKnightDirection(TextureRegion keyFrame) {

        if(b2body.getLinearVelocity().x > 0.1f) {
            runningRight = true;
        }
        else if(b2body.getLinearVelocity().x < -0.1f) {
            runningRight = false;
        }

        if (runningRight && !keyFrame.isFlipX()) {
            keyFrame.flip(true, false);
        }
        else if (!runningRight && keyFrame.isFlipX()) {
            keyFrame.flip(true, false);
        }
    }
}
