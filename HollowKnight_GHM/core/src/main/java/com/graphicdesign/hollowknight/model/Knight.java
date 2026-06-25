package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.enemy.Enemy;
import com.graphicdesign.hollowknight.model.enums.animation.KnightAnimation;
import com.graphicdesign.hollowknight.model.enums.KnightState;

import java.util.ArrayList;
import java.util.List;

public class Knight {
    private World world;
    public Body b2body;
    public KnightAnimation animation = KnightAnimation.IDLE; // TODO : Change it to landing
    public float stateTime = 0f;
    private boolean runningRight = true;
    private boolean isAttacking = false;
    private boolean isDashing = false;
    private float attackTimer = 0f;
    private float dashTimer = 0f;
    private float dashCooldownTimer = 0f;
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
        //fdef.friction = Constants.KNIGHT_FRICTION;

        fdef.filter.categoryBits = Constants.KNIGHT_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT | Constants.DESTROYABLE_BIT | Constants.ENEMY_BIT | Constants.NPC_BIT;

        b2body.createFixture(fdef).setUserData(this);

        shape.dispose();
    }
    public void draw(SpriteBatch batch) {
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(this.animation);
        TextureRegion keyFrame = animation.getKeyFrame(this.stateTime);

        correctKnightDirection(keyFrame);
        float width = keyFrame.getRegionWidth() / Constants.PPM;
        float height = keyFrame.getRegionHeight() / Constants.PPM;

        float padX = -0.1f;
        float padY = 0.65f;

        float x = this.b2body.getPosition().x - (width / 2f) + padX;
        float y = this.b2body.getPosition().y - (height / 2f) + padY;

        batch.draw(keyFrame, x, y, width, height);
        if(isAttacking) {
            Animation<TextureRegion> attackAnimation = AssetManagerLocal.getInstance().animationMap.get(KnightAnimation.SLASH_EFFECT);
            TextureRegion attackFrame = attackAnimation.getKeyFrame(this.stateTime);

            if(runningRight && !attackFrame.isFlipX()) {
                attackFrame.flip(true, false);
            }
            else if(!runningRight && attackFrame.isFlipX()) {
                attackFrame.flip(true, false);
            }

            float effectWidth = attackFrame.getRegionWidth() / Constants.PPM;
            float effectHeight = attackFrame.getRegionHeight() / Constants.PPM;

            float offsetX = 0.5f;
            float effectX = runningRight ? x + offsetX : x - offsetX - (effectWidth - width);

            batch.draw(attackFrame, effectX, y, effectWidth, effectHeight);
        }

        if(isDashing) {
            Animation<TextureRegion> dashAnimation = AssetManagerLocal.getInstance().animationMap.get(KnightAnimation.DASH_EFFECT);
            TextureRegion dashFrame = dashAnimation.getKeyFrame(this.stateTime);

            if(!runningRight && !dashFrame.isFlipX()) {
                dashFrame.flip(true, false);
            }
            else if(runningRight && dashFrame.isFlipX()){
                dashFrame.flip(true, false);
            }

            float effectWidth = dashFrame.getRegionWidth() / Constants.PPM;
            float effectHeight = dashFrame.getRegionHeight() / Constants.PPM;

            float effectPadX = 4.0f;
            float effectX = runningRight ? x - effectWidth + effectPadX : x + width - effectPadX;
            float effectPadY = 1.0f;

            batch.draw(dashFrame, effectX, y - effectPadY, effectWidth, effectHeight);
        }
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

        if(isAttacking) {
            attackTimer += deltaTime;
            if(attackTimer >= Constants.KNIGHT_ATTACK_DURATION) {
                isAttacking = false;
            }
        }

        if(dashCooldownTimer > 0) {
            dashCooldownTimer -= deltaTime;
        }

        if(isDashing) {
            dashTimer += deltaTime;
            float dashVelocity = runningRight ? Constants.KNIGHT_DASH_SPEED : -Constants.KNIGHT_DASH_SPEED;
            b2body.setLinearVelocity(dashVelocity, 0);

            if (dashTimer >= Constants.KNIGHT_DASH_DURATION) {
                isDashing = false;
                b2body.setGravityScale(1f);
                b2body.setLinearVelocity(0, 0);
            }
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
                break;
            }
            case ATTACKING:
            {
                animation = KnightAnimation.SLASH;
                break;
            }
            case DASHING:
            {
                animation = KnightAnimation.DASH;
                break;
            }
        }
        previousState = currentState;
    }

    private KnightState getState() {

        if(isDashing) return KnightState.DASHING;
        if(isAttacking) return KnightState.ATTACKING;

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

    public void attack() {
        if(!isAttacking) {
            isAttacking = true;
            attackTimer = 0f;
            b2body.setLinearVelocity(0,0);

            Vector2 pos = b2body.getPosition();

            // -> Building required rectangle :
            float lowerX = runningRight ? pos.x : pos.x - Constants.KNIGHT_ATTACK_LENGTH;
            float upperX = runningRight ? pos.x + Constants.KNIGHT_ATTACK_LENGTH : pos.x;
            float lowerY = pos.y - (Constants.KNIGHT_ATTACK_HEIGHT / 2f);
            float upperY = pos.y + (Constants.KNIGHT_ATTACK_HEIGHT / 2f);

            world.QueryAABB(new QueryCallback() {
                @Override
                public boolean reportFixture(Fixture fixture) {
                    if (fixture.getFilterData().categoryBits == Constants.ENEMY_BIT) {
                        ((Enemy) fixture.getUserData()).takeDamage(20);
                    }
                    return true;
                }
            }, lowerX, lowerY, upperX, upperY);
        }
    }

    public void dash() {
        if(!isDashing && dashCooldownTimer <= 0) {
            isDashing = true;
            dashTimer = 0f;
            dashCooldownTimer = Constants.KNIGHT_DASH_COOLDOWN;

            b2body.setGravityScale(0f);

            float dashVelocity = runningRight ? Constants.KNIGHT_DASH_SPEED : -Constants.KNIGHT_DASH_SPEED;
            b2body.setLinearVelocity(dashVelocity, 0);
        }
    }
}
