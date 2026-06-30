package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.enemy.Enemy;
import com.graphicdesign.hollowknight.model.enums.ProjectileState;
import com.graphicdesign.hollowknight.model.enums.animation.AnimationData;
import com.graphicdesign.hollowknight.model.enums.animation.KnightAnimation;
import com.graphicdesign.hollowknight.view.PlayScreen;

public class VengefulSpirit {
    private ProjectileState currentState;
    public Body b2body;
    private World world;
    private PlayScreen screen;
    private float stateTime;
    private boolean faceRight;
    private KnightAnimation currentAnimation;

    public VengefulSpirit(PlayScreen screen, float x, float y, boolean faceRight) {
        this.screen = screen;
        this.faceRight = faceRight;
        currentState = ProjectileState.SPAWNING;
        currentAnimation = KnightAnimation.VENGEFUL_SPIRIT_START;
        stateTime = 0;
        this.world = screen.getWorld();
        defineBody(x,y);
    }

    private void defineBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x,y);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        float width = Constants.SPIRIT_WIDTH / Constants.PPM;
        float height = Constants.SPIRIT_HEIGHT / Constants.PPM;
        shape.setAsBox(width, height);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        fdef.filter.categoryBits = Constants.PROJECTILE_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT | Constants.ENEMY_BIT;

        b2body.createFixture(fdef).setUserData(this);

        float velocityX = faceRight ? Constants.SPIRIT_SPEED : -Constants.SPIRIT_SPEED;
        b2body.setLinearVelocity(velocityX, 0);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;

        switch (currentState) {
            case SPAWNING:
            {
                currentAnimation = KnightAnimation.VENGEFUL_SPIRIT_START;
                if(AssetManagerLocal.isAnimationFinished(currentAnimation, stateTime)) {
                    currentState = ProjectileState.TRAVELING;
                    stateTime = 0;
                }
                break;
            }
            case TRAVELING:
            {
                currentAnimation = KnightAnimation.VENGEFUL_SPIRIT;
                break;
            }
            case IMPACTING:
            {
                currentAnimation = KnightAnimation.VENGEFUL_SPIRIT_END;
                if(AssetManagerLocal.isAnimationFinished(currentAnimation, stateTime)) {
                    currentState = ProjectileState.DESTROYING;
                }
            }
            case DESTROYING:
            {
                break;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if(currentState == ProjectileState.DESTROYING) return;

        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);

        TextureRegion region = animation.getKeyFrame(stateTime);

        if (faceRight && region.isFlipX()) {
            region.flip(true, false);
        } else if (!faceRight && !region.isFlipX()) {
            region.flip(true, false);
        }

        float width = region.getRegionWidth() / Constants.PPM;
        float height = region.getRegionHeight() / Constants.PPM;

        float x = b2body.getPosition().x - (width / 2f);
        float y = b2body.getPosition().y - (height / 2f);

        batch.draw(region, x, y, width, height);
    }

    public void onHitWall() {
        if(currentState == ProjectileState.TRAVELING) {
            currentState = ProjectileState.IMPACTING;
            stateTime = 0;
            b2body.setLinearVelocity(0,0);
        }
    }

    public void  onHitEnemy(Enemy enemy) {
       if(currentState == ProjectileState.TRAVELING) {
           enemy.takeDamage(Constants.SPIRIT_DAMAGE, faceRight, false);
       }

    }

    public boolean destroyBody() {
        if(currentState == ProjectileState.DESTROYING) {
            world.destroyBody(b2body);
            return true;
        }
        return false;
    }

    public boolean removeProjectile() {
        return currentState == ProjectileState.DESTROYING;
    }


}
