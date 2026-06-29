package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;
import com.graphicdesign.hollowknight.model.enums.animation.FalseKnightAnimation;

public class FalseKnight extends GroundEnemy{
    private Knight knight;
    public float stateTime;
    private FalseKnightAnimation currentAnimation;
    private BehaviorTree<FalseKnight> behaviorTree;
    public String lastMoveName = "";
    private float deltaTime = 0f;
    public Fixture hammerSensor;
    public boolean tookHeavyDamage = false;

    public FalseKnight(World world, float x, float y, Knight knight) {
        super(world, x, y);
        this.knight = knight;
        stateTime = 0f;
        type = "False Knight";
        currentAnimation = FalseKnightAnimation.IDLE;
        health = 200;
        behaviorTree = FalseKnightAI.buildTree(this);
    }

    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float width  = Constants.FALSE_KNIGHT_WIDTH/ Constants.PPM;
        float height = Constants.FALSE_KNIGHT_HEIGHT / Constants.PPM;
        shape.setAsBox(width, height);

        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT | Constants.KNIGHT_BIT | Constants.CLIFF_BIT | Constants.DESTROYABLE_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        FixtureDef sensorDef = new FixtureDef();
        PolygonShape attackShape = new PolygonShape();

        attackShape.setAsBox(2f, 1f, new Vector2(4.2f, 0.2f), 0);
        sensorDef.shape = attackShape;
        sensorDef.isSensor = true;
        sensorDef.filter.categoryBits = Constants.ENEMY_BIT;
        sensorDef.filter.maskBits = Constants.KNIGHT_BIT;
        hammerSensor = b2body.createFixture(sensorDef);
        hammerSensor.setUserData("Hammer");

        // Disabling sensor initially :

        hammerSensor.getFilterData().categoryBits = 0;
        hammerSensor.setFilterData(hammerSensor.getFilterData());
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;
        this.deltaTime = deltaTime;

        if(isDead) {
            changeAnimation(FalseKnightAnimation.DEATH_LAND);
            return;
        }


        if(knockBackTimer > 0) {
            knockBackTimer -= deltaTime;
            return;
        }


        behaviorTree.step();
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(true) return;
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);
        TextureRegion region = animation.getKeyFrame(stateTime);

        if (walkRight && !region.isFlipX()) {
            region.flip(true, false);
        } else if (!walkRight && region.isFlipX()) {
            region.flip(true, false);
        }

        float width = region.getRegionWidth() / Constants.PPM;
        float height = region.getRegionHeight() / Constants.PPM;

        float padY = 3.3f;
        float x = b2body.getPosition().x - (width / 2f);
        float y = b2body.getPosition().y - (height / 2f) + padY;

        batch.draw(region, x, y, width, height);
    }

    public void changeAnimation(FalseKnightAnimation animation) {
        if(currentAnimation != animation) {
            currentAnimation = animation;
            stateTime = 0f;
        }
    }

    public float getDistanceToKnight() {
        return b2body.getPosition().dst(knight.b2body.getPosition());
    }

    public Knight getKnight() {
        return knight;
    }

    public boolean isAnimationFinished() {
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);

        return animation.isAnimationFinished(stateTime);
    }

    public void faceToPlayer() {
        walkRight = knight.b2body.getPosition().x > b2body.getPosition().x;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    @Override
    public void takeDamage(int amount, boolean knockRight, boolean heavyBlow) {
        super.takeDamage(amount, knockRight, heavyBlow);
        tookHeavyDamage = true;
    }
}
