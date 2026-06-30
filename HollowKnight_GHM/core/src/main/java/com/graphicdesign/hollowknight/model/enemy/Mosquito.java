package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
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
import com.graphicdesign.hollowknight.model.enums.animation.MosquitoAnimation;
import com.graphicdesign.hollowknight.model.enums.states.MosquitoState;

public class Mosquito extends FlyingEnemy{
    private StateMachine<Mosquito, MosquitoState> stateMachine;
    private Knight player;
    public float stateTime;
    private float attackCooldown = 0;
    private Vector2 attackDirection;
    private MosquitoAnimation currentAnimation;

    public Mosquito(World world, float x, float y, Knight player) {
        super(world, x, y);
        this.player = player;
        attackDirection = new Vector2();
        stateTime = 0;
        currentAnimation = MosquitoAnimation.IDLE;
        health = 100;
        type = "Mosquito";
        stateMachine = new DefaultStateMachine<>(this, MosquitoState.IDLE);
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
        fdef.filter.maskBits = Constants.GROUND_BIT |
            Constants.KNIGHT_BIT |
            Constants.DESTROYABLE_BIT |
            Constants.PROJECTILE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float deltaTime) {

        stateTime += deltaTime;


        if (isDead) {
            if (stateMachine.getCurrentState() != MosquitoState.DEAD) {
                stateMachine.changeState(MosquitoState.DEAD);
            }
            stateMachine.update();
            return;
        }

        if (knockBackTimer > 0) {
            knockBackTimer -= deltaTime;
            return;
        }

        attackCooldown += deltaTime;

        stateMachine.update();
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

    public void changeAnimation(MosquitoAnimation animation) {
        currentAnimation = animation;
        stateTime = 0;
    }

    public float getDistanceToPlayer() {
        return b2body.getPosition().dst(player.b2body.getPosition());
    }

    public Knight getPlayer() {
        return player;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(float cooldown) {
        this.attackCooldown = cooldown;
    }

    public Vector2 getAttackDirection() {
        return attackDirection;
    }

    public StateMachine<Mosquito, MosquitoState> getStateMachine() {
        return stateMachine;
    }


}
