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
import com.graphicdesign.hollowknight.model.enums.MosquitoState;
import com.graphicdesign.hollowknight.model.enums.animation.MosquitoAnimation;

public class Mosquito extends FlyingEnemy{
    private MosquitoState currentState;
    private MosquitoState previousState;

    private Knight player;
    private float stateTime;

    private float attackCooldown = 0;
    private Vector2 attackDirection;

    private MosquitoAnimation currentAnimation;

    public Mosquito(World world, float x, float y, Knight player) {
        super(world, x, y);
        this.player = player;
        currentState = MosquitoState.IDLE;
        previousState = MosquitoState.IDLE;
        stateTime = 0;
        attackDirection = new Vector2();

        currentAnimation = MosquitoAnimation.IDLE;
        health = 100;

    }

    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float width = Constants.MOSQUITO_WIDTH / Constants.PPM;
        float height = Constants.MOSQUITO_HEIGHT / Constants.PPM;

        shape.setAsBox(width, height);

        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT | Constants.KNIGHT_BIT | Constants.DESTROYABLE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float deltaTime) {

        stateTime += deltaTime;


        if (isDead) {
            changeState(MosquitoState.DEAD);
            currentAnimation = MosquitoAnimation.DEATH;
            return;
        }

        if (knockBackTimer > 0) {
            knockBackTimer -= deltaTime;
            return;
        }

        attackCooldown += deltaTime;

        Vector2 myPosition = b2body.getPosition();
        Vector2 playerPosition = player.b2body.getPosition();

        float distanceToPlayer = myPosition.dst(playerPosition);

        switch (currentState) {
            case IDLE:
            {
                b2body.setLinearVelocity(0,0);
                if(distanceToPlayer <= Constants.MOSQUITO_SEEN_RANGE) {
                    changeState(MosquitoState.CHASING);
                }
                break;
            }
            case CHASING:
            {
                Vector2 chaseDirection = new Vector2(playerPosition.x - myPosition.x,
                                                     playerPosition.y - myPosition.y).nor();
                b2body.setLinearVelocity(chaseDirection.x * Constants.MOSQUITO_CHASE_SPEED,
                                         chaseDirection.y * Constants.MOSQUITO_CHASE_SPEED);
                if(attackCooldown >= Constants.MOSQUITO_ATTACK_COOLDOWN &&
                    distanceToPlayer < Constants.MOSQUITO_ATTACK_RANGE) {
                    changeState(MosquitoState.ANTICIPATING);
                    attackDirection.set(playerPosition.x - myPosition.x,
                                        playerPosition.y - myPosition.y).nor();
                }
                break;
            }
            case ANTICIPATING:
            {
                b2body.setLinearVelocity(0, 0);
                if (stateTime >= Constants.MOSQUITO_ANTICIPATION_TIME) {
                    changeState(MosquitoState.ATTACKING);
                }
                break;
            }
            case ATTACKING:
            {
                b2body.setLinearVelocity(attackDirection.x * Constants.MOSQUITO_ATTACK_SPEED,
                                            attackDirection.y * Constants.MOSQUITO_ATTACK_SPEED);

                if(stateTime >= Constants.MOSQUITO_ATTACK_DURATION) {
                    attackCooldown = 0;
                    changeState(MosquitoState.CHASING);
                }
                break;
            }
            case DEAD:
            {
                break;
            }
        }
        switch (currentState) {
            case IDLE: {currentAnimation = MosquitoAnimation.IDLE;break;}
            case ATTACKING: {currentAnimation = MosquitoAnimation.ATTACK;break;}
            case ANTICIPATING: {currentAnimation = MosquitoAnimation.ANTICIPATE;break;}
            case DEAD: {currentAnimation = MosquitoAnimation.DEATH;break;}
            case CHASING: {currentAnimation = MosquitoAnimation.IDLE;break;}
        }
    }

    private void changeState(MosquitoState state) {
        if(currentState == state)return;
        previousState = currentState;
        currentState = state;
        stateTime = 0;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);
        TextureRegion region = animation.getKeyFrame(stateTime);


        boolean flipX = (player.b2body.getPosition().x > b2body.getPosition().x);

        float width = region.getRegionWidth() / Constants.PPM;
        float height = region.getRegionHeight() / Constants.PPM;

        batch.draw(region,
            flipX ? b2body.getPosition().x + width / 2 : b2body.getPosition().x - width / 2,
            b2body.getPosition().y - height / 2,
            flipX ? -width : width,
            height);
    }
}
