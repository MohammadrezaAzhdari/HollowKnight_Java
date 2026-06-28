package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;
import com.graphicdesign.hollowknight.model.enums.HornHeadState;
import com.graphicdesign.hollowknight.model.enums.animation.HuskHornHeadAnimation;

public class HuskHornedHead extends GroundEnemy{
    private HornHeadState currentState;
    private HornHeadState previousState;
    private float stateTime;
    private Knight player;
    private HuskHornHeadAnimation currentAnimation;

    public HuskHornedHead(World world, float x, float y, Knight player) {
        super(world, x, y);
        this.player = player;
        currentAnimation = HuskHornHeadAnimation.IDLE;
        stateTime = 0f;
        currentState = HornHeadState.IDLE;
        previousState = HornHeadState.IDLE;
        health = 100;
        type = "Husk Horned Head";
    }

    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float width = Constants.HUSK_WIDTH / Constants.PPM;
        float height = Constants.HUSK_HEIGHT / Constants.PPM;
        shape.setAsBox(width, height);

        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT |
            Constants.KNIGHT_BIT |
            Constants.CLIFF_BIT |
            Constants.DESTROYABLE_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;

        if (isDead) {
            currentAnimation = HuskHornHeadAnimation.DEATH;

            return;
        }

        if (knockBackTimer > 0) {
            knockBackTimer -= deltaTime;
            return;
        }

        switch (currentState) {
            case IDLE:
                currentAnimation = HuskHornHeadAnimation.IDLE;
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);

                if (canSeePlayer()) {
                    changeState(HornHeadState.ANTICIPATE);
                } else if (stateTime >= Constants.HUSK_IDLE_TIME) {
                    changeState(HornHeadState.WALK);
                }
                break;

            case WALK:
                currentAnimation = HuskHornHeadAnimation.WALK;
                float walkSpeed = walkRight ? Constants.HUSK_WALK_SPEED : -Constants.HUSK_WALK_SPEED;
                b2body.setLinearVelocity(walkSpeed, b2body.getLinearVelocity().y);

                if (canSeePlayer()) {
                    changeState(HornHeadState.ANTICIPATE);
                } else if (stateTime >= Constants.HUSK_WALK_TIME) {
                    changeState(HornHeadState.IDLE);
                }
                break;

            case ANTICIPATE:
                currentAnimation = HuskHornHeadAnimation.ATTACK_ANTICIPATE;
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);

                if (stateTime >= Constants.HUSK_ANTICIPATION_TIME) {
                    changeState(HornHeadState.LUNGE);
                }
                break;

            case LUNGE:
                currentAnimation = HuskHornHeadAnimation.ATTACK_LUNGE;
                float rushSpeed = walkRight ? Constants.HUSK_ATTACK_SPEED : -Constants.HUSK_ATTACK_SPEED;
                b2body.setLinearVelocity(rushSpeed, b2body.getLinearVelocity().y);

                break;

            case COOLDOWN:
                currentAnimation = HuskHornHeadAnimation.ATTACK_COOLDOWN;
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);

                if (stateTime >= Constants.HUSK_COOLDOWN_TIME) {
                    changeState(HornHeadState.WALK);
                }
                break;
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
        Vector2 huskPos = b2body.getPosition();

        float distanceX = playerPos.x - huskPos.x;
        float distanceY = Math.abs(playerPos.y - huskPos.y);

        boolean isFacingPlayer = (walkRight && distanceX > 0) || (!walkRight && distanceX < 0);
        boolean isInRange = Math.abs(distanceX) < Constants.HUSK_SIGHT_X_RANGE &&
            distanceY < Constants.HUSK_SIGHT_Y_RANGE;
        return isFacingPlayer && isInRange;
    }

    private void changeState(HornHeadState state) {
        if(currentState == state) return;
        previousState = currentState;
        currentState = state;
        stateTime = 0f;
    }

    @Override
    public void reverseDirection() {
        if(knockBackTimer <= 0 && currentState == HornHeadState.LUNGE) {
            changeState(HornHeadState.COOLDOWN);
        }
        super.reverseDirection();
    }
}
