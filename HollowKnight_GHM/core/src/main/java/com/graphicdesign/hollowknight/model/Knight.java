package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.enums.KnightAnimation;
import com.graphicdesign.hollowknight.model.enums.KnightState;

import java.util.ArrayList;
import java.util.List;

public class Knight {
    private World world;
    public Body b2body;
    public KnightAnimation animation = KnightAnimation.IDLE; // TODO : Change it to landing
    public float stateTime = 0f;
    private boolean runningRight = true;
    public int jumpCount = 0;
    public KnightState currentState;
    public KnightState previousState;
    public List<String> charms = new ArrayList<>();


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
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(this.animation);
        TextureRegion keyFrame = animation.getKeyFrame(this.stateTime, true);

        correctKnightDirection(keyFrame);
        float width = keyFrame.getRegionWidth() / Constants.PPM;
        float height = keyFrame.getRegionHeight() / Constants.PPM;

        float padX = -0.1f;
        float padY = 0.65f;

        float x = this.b2body.getPosition().x - (width / 2f) + padX;
        float y = this.b2body.getPosition().y - (height / 2f) + padY;

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

    public void update(float deltaTime) {
        currentState = getState();

        if(currentState != previousState) {
            stateTime = 0;
        }
        else {
            stateTime += deltaTime;
        }

        switch (currentState) {
            case IDLE :
            {
                animation = KnightAnimation.IDLE;
                break;
            }
            case FALLING:
            {
                animation = KnightAnimation.FALL;
                break;
            }
            case RUNNING:
            {
                animation = KnightAnimation.RUN;
                break;
            }
            case RUN_TO_IDLE:
            {
                animation = KnightAnimation.RUN_TO_IDLE;
                break;
            }
            case LANDING:
            {
                animation = KnightAnimation.LANDING;
                break;
            }
            case JUMPING:
            {
                animation = KnightAnimation.DOUBLE_JUMP;
                break;
            }
            case DOUBLE_JUMPING:
            {
                animation = KnightAnimation.DOUBLE_JUMP;
            }
        }
        previousState = currentState;
    }

    private KnightState getState() {
        Vector2 velocity = b2body.getLinearVelocity();
        Animation<TextureRegion> currentAnimation = AssetManagerLocal.getInstance().animationMap.get(animation);

        if(currentState == KnightState.LANDING) {
            if(!currentAnimation.isAnimationFinished(stateTime)) return KnightState.LANDING;
        }
        if(currentState == KnightState.RUN_TO_IDLE) {
            if (velocity.x != 0) return KnightState.RUNNING;
            if (velocity.y > 0) return (jumpCount == 2) ? KnightState.DOUBLE_JUMPING : KnightState.JUMPING;

            if (!currentAnimation.isAnimationFinished(stateTime)) return KnightState.RUN_TO_IDLE;
        }
        if (velocity.y > 0) {
            return (jumpCount == 2) ? KnightState.DOUBLE_JUMPING : KnightState.JUMPING;
        } else if (velocity.y < 0) {
            return KnightState.FALLING;
        }

        if (velocity.x != 0 && previousState != KnightState.LANDING) {
            return KnightState.RUNNING;
        } else {
            if (previousState == KnightState.FALLING) {
                jumpCount = 0;
                return KnightState.LANDING;
            }
            if (previousState == KnightState.RUNNING) {
                return KnightState.RUN_TO_IDLE;
            }

            jumpCount = 0;
            return KnightState.IDLE;
        }
    }
}
