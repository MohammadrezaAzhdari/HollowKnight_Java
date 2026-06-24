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

    public CrystalGuardian(World world, float x, float y, Knight player) {
        super(world, x, y);
        this.player = player;
        this.currentState = CrystalGuardianState.IDLE;
        this.previousState = CrystalGuardianState.IDLE;
        this.stateTime = 0f;
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
                                Constants.DESTROYABLE_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        // TODO -> Add sensor here if you want
    }

    @Override
    public void update(float deltaTime) {
        if(destroyed)return;
        stateTime += deltaTime;

        switch (currentState) {
            case IDLE:
            {
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
                currentAnimation = CrystalGuardianAnimation.IDLE;
                if(canSeePlayer()) {
                    changeState(CrystalGuardianState.SHOOTING);
                }
                break;
            }
            case SHOOTING:
            {
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
                currentAnimation = CrystalGuardianAnimation.SHOOT;

                // TODO -> Lazer logic goes here.

                if(stateTime >= Constants.CRYSTALLIZED_SHOOTING_DURATION) {
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
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);
        TextureRegion region = animation.getKeyFrame(stateTime);

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
    }
}
