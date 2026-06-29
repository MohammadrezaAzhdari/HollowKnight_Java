package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.enums.animation.TickTickAnimation;
import com.graphicdesign.hollowknight.model.enums.states.TickTickState;

public class TickTick extends GroundEnemy{
    private TickTickAnimation currentAnimation;
    public StateMachine<TickTick, TickTickState> stateMachine;

    public TickTick(World world, float x, float y) {
        super(world, x, y);
        stateTime = 0f;
        health = 60;
        type = "Tick Tick";
        changeAnimation(TickTickAnimation.WALK);
        stateMachine = new DefaultStateMachine<>(this, TickTickState.WALK);
    }


    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x,y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float width = Constants.TICKTICK_WIDTH / Constants.PPM;
        float height = Constants.TICKTICK_HEIGHT / Constants.PPM;

        shape.setAsBox(width, height);

        fdef.shape = shape;
        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits =
            Constants.DESTROYABLE_BIT |
                Constants.KNIGHT_BIT |
                Constants.GROUND_BIT |
                Constants.CLIFF_BIT;

        b2body.createFixture(fdef).setUserData(this);
        shape.dispose();
    }

    @Override
    public void update(float deltaTime) {

        if(!isDead || !isCurrentAnimationFinished()) {
            stateTime += deltaTime;
        }

        if(knockBackTimer > 0) {
            knockBackTimer -= deltaTime;
        }

        if(isDead && stateMachine.getCurrentState() != TickTickState.DEATH) {
            stateMachine.changeState(TickTickState.DEATH);
        }
        stateMachine.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);
        TextureRegion region = animation.getKeyFrame(stateTime);

        if (walkRight && region.isFlipX()) {
            region.flip(true, false);
        } else if (!walkRight && !region.isFlipX()) {
            region.flip(true, false);
        }

        float width = region.getRegionWidth() / Constants.PPM;
        float height = region.getRegionHeight() / Constants.PPM;

        float x = b2body.getPosition().x - (width / 2f);
        float y = b2body.getPosition().y - (height / 2f);

        batch.draw(region, x, y, width, height);
    }

    public void changeAnimation (TickTickAnimation animation) {
        currentAnimation = animation;
        stateTime = 0f;
    }

    public boolean isCurrentAnimationFinished() {
        Animation<TextureRegion> anim = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);
        return anim.isAnimationFinished(stateTime);
    }
}
