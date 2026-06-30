package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;
import com.graphicdesign.hollowknight.model.enums.CrystalGuardianState;
import com.graphicdesign.hollowknight.model.enums.animation.CrystalGuardianAnimation;

public class CrystalGuardian extends GroundEnemy{
    private CrystalGuardianState currentState;
    private CrystalGuardianState previousState;
    private float stateTime;
    private Knight player;
    private CrystalGuardianAnimation currentAnimation;
    private boolean hasJumpedForEvade = false;
    private float laserStateTime = 0f;
    private Fixture laserSensor;


    public CrystalGuardian(World world, float x, float y, Knight player) {
        super(world, x, y);
        this.player = player;
        this.currentState = CrystalGuardianState.IDLE;
        this.previousState = CrystalGuardianState.IDLE;
        this.stateTime = 0f;
        health = 140;
        type = "Crystal Guardian";
    }

    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float width = Constants.CRYSTALLIZED_WIDTH / Constants.PPM;
        float height = Constants.CRYSTALLIZED_HEIGHT / Constants.PPM;
        shape.setAsBox(width, height);

        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT |
                                Constants.KNIGHT_BIT |
                                Constants.CLIFF_BIT |
                                Constants.DESTROYABLE_BIT |
                                Constants.PROJECTILE_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;


        if (isDead) {
            currentAnimation = CrystalGuardianAnimation.DEATH;

            return;
        }

        if (knockBackTimer > 0) {
            knockBackTimer -= deltaTime;
            return;
        }

        switch (currentState) {
            case IDLE:
            {
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
                currentAnimation = CrystalGuardianAnimation.IDLE;
                if(canSeePlayer()) {
                    changeState(CrystalGuardianState.EVADE);
                }
                break;
            }
            case EVADE:
            {
                currentAnimation = CrystalGuardianAnimation.EVADE;
                if(!hasJumpedForEvade) {
                    float jumpDirection = walkRight ? -1f : 1f;
                    b2body.applyLinearImpulse(new Vector2(
                        jumpDirection * Constants.CRYSTALLIZED_EVADE_X_FORCE,
                        Constants.CRYSTALLIZED_EVADE_Y_FORCE),
                        b2body.getWorldCenter(), true);
                    hasJumpedForEvade = true;
                }

                if(stateTime >= Constants.CRYSTALLIZED_EVADE_DURATION) {
                    hasJumpedForEvade = false;
                    changeState(CrystalGuardianState.SHOOTING);
                }
                break;
            }
            case SHOOTING:
            {
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
                currentAnimation = CrystalGuardianAnimation.SHOOT;

                updateLaser(deltaTime);

                if(stateTime >= Constants.CRYSTALLIZED_SHOOTING_DURATION + Constants.CRYSTALLIZED_SHOOT_PAUSE_DURATION) {
                    changeState(CrystalGuardianState.ENRAGED);
                }
                break;
            }
            case ENRAGED:
            {
                currentAnimation = CrystalGuardianAnimation.RUN;
                walkRight = player.b2body.getPosition().x > b2body.getPosition().x;

                float enragedSpeed = walkRight ? Constants.CRYSTALLIZED_ENRAGED_SPEED : -Constants.CRYSTALLIZED_ENRAGED_SPEED;
                b2body.setLinearVelocity(enragedSpeed, b2body.getLinearVelocity().y);

                if(stateTime >= Constants.CRYSTALLIZED_ENRAGED_DURATION) {
                    changeState(CrystalGuardianState.IDLE);
                }
                break;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        float renderTime = stateTime;

        if(currentState == CrystalGuardianState.SHOOTING) {
            float pauseTime = 3 * Constants.FRAME_DURATION;

            if(stateTime >= pauseTime && stateTime <= pauseTime + Constants.CRYSTALLIZED_SHOOT_PAUSE_DURATION) {
                renderTime = pauseTime;
            }
            else if(stateTime > pauseTime + Constants.CRYSTALLIZED_SHOOT_PAUSE_DURATION) {
                renderTime = stateTime - Constants.CRYSTALLIZED_SHOOT_PAUSE_DURATION;
            }
        }
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);
        TextureRegion region = animation.getKeyFrame(renderTime);

        if (walkRight && !region.isFlipX()) {
            region.flip(true, false);
        } else if (!walkRight && region.isFlipX()) {
            region.flip(true, false);
        }

        float width = region.getRegionWidth() / Constants.PPM;
        float height = region.getRegionHeight() / Constants.PPM;

        float x = b2body.getPosition().x - (width / 2f);
        float y = b2body.getPosition().y - (height / 2f);

        batch.draw(region, x, y, width, height);

        if(currentState == CrystalGuardianState.SHOOTING) {
            drawLaser(batch);
        }

    }

    private void drawLaser(SpriteBatch batch) {
        Animation<TextureRegion> laserAnimation = AssetManagerLocal.getInstance().animationMap.get(CrystalGuardianAnimation.LAZER);

        float renderTime = laserStateTime;
        float pauseTime = 8 * Constants.FRAME_DURATION;

        if(laserStateTime >= pauseTime && laserStateTime <= pauseTime + Constants.CRYSTALLIZED_LASER_PAUSE_DURATION) {
            renderTime = pauseTime;
        }
        else if(laserStateTime > pauseTime + Constants.CRYSTALLIZED_LASER_PAUSE_DURATION) {
            renderTime = laserStateTime - Constants.CRYSTALLIZED_LASER_PAUSE_DURATION;
        }

        TextureRegion region = laserAnimation.getKeyFrame(renderTime);

        float laserPosX = walkRight ? b2body.getPosition().x + 1f : b2body.getPosition().x - 3f ;
        float width = region.getRegionWidth() / Constants.PPM;
        float height = region.getRegionHeight() / (Constants.PPM);

        float laserYPad = 0.1f;
        float drawY = b2body.getPosition().y + laserYPad - (height / 2f);

        for(int i = 0; i < 5; i++) {
            float currentX = walkRight ? laserPosX + (i * width) : laserPosX - (i * width);
            batch.draw(region, currentX, drawY, width, height);
        }
    }

    private boolean canSeePlayer() {
        Vector2 playerPos = player.b2body.getPosition();
        Vector2 guardianPos = b2body.getPosition();

        float distanceX = playerPos.x - guardianPos.x;
        float distanceY = Math.abs(playerPos.y - guardianPos.y);

        boolean isFacingPlayer = (walkRight && distanceX > 0) || (!walkRight && distanceX < 0);
        boolean isInRange = Math.abs(distanceX) < Constants.CRYSTALLIZED_SIGHT_X_RANGE &&
                                     distanceY < Constants.CRYSTALLIZED_SIGHT_Y_RANGE;
        return isFacingPlayer && isInRange;
    }

    private void changeState(CrystalGuardianState state) {
        if(currentState == state) return;
        previousState = currentState;
        currentState = state;
        stateTime = 0f;
        if(state == CrystalGuardianState.SHOOTING) {
            laserStateTime = 0f;
        }
    }

    private void updateLaser(float deltaTime) {
        laserStateTime += deltaTime;
        float peakLaserTime = 8 * Constants.FRAME_DURATION;

        if(laserStateTime >= peakLaserTime && laserStateTime < peakLaserTime + Constants.CRYSTALLIZED_LASER_PAUSE_DURATION) {
            if(laserSensor == null) {
                createLaserSensor();
            }
        }
        else if(laserStateTime >= peakLaserTime + Constants.CRYSTALLIZED_LASER_PAUSE_DURATION) {
            if(laserSensor != null) {
                destroyLaserSensor();
            }
        }
    }
    private void destroyLaserSensor() {
        b2body.destroyFixture(laserSensor);
        laserSensor = null;
    }

    private void createLaserSensor() {
        Animation<TextureRegion> laserAnimation = AssetManagerLocal.getInstance().animationMap.get(CrystalGuardianAnimation.LAZER);
        TextureRegion region = laserAnimation.getKeyFrame(laserStateTime);

        float width = region.getRegionWidth() / Constants.PPM;
        float height = region.getRegionHeight() / Constants.PPM;

        float laserWidth = 5 * width;
        float hx = laserWidth / 2;
        float hy = (height / 2) * 0.7f;

        float centerX = walkRight ? (1f + hx) : ( - hx);
        Vector2 center = new Vector2(centerX, 0);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy, center, 0);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits = Constants.KNIGHT_BIT;

        laserSensor = b2body.createFixture(fdef);
        laserSensor.setUserData("Laser");
        shape.dispose();
    }

    @Override
    public void reverseDirection() {
        if (currentState == CrystalGuardianState.EVADE ||
            currentState == CrystalGuardianState.SHOOTING ||
            currentState == CrystalGuardianState.ENRAGED) {
            return;
        }
        super.reverseDirection();
    }
}
