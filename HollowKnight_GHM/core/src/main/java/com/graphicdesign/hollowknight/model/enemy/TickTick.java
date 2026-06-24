package com.graphicdesign.hollowknight.model.enemy;

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

public class TickTick extends GroundEnemy{
    private TickTickAnimation currentAnimation;

    public TickTick(World world, float x, float y) {
        super(world, x, y);
        this.currentAnimation = TickTickAnimation.WALK;
        stateTime = 0f;
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
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
            currentAnimation = TickTickAnimation.DEATH_AIR;
        }
        else if(!destroyed) {
            float velocity = walkRight ? -Constants.TICKTICK_SPEED : Constants.TICKTICK_SPEED;
            b2body.setLinearVelocity(velocity, b2body.getLinearVelocity().y);
        }
        stateTime += deltaTime;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);
        if(destroyed && animation.isAnimationFinished(stateTime)) {return;}
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
}
