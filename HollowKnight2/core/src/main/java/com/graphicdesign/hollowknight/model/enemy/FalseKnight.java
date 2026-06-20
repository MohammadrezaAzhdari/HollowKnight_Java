package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.enums.animation.FalseKnightAnimation;
import com.graphicdesign.hollowknight.view.screen.GameScreen;

public class FalseKnight extends Enemy{
    public Body b2body;
    public float stateTime = 0f;
    public FalseKnightAnimation animation = FalseKnightAnimation.RUN;

    public FalseKnight(GameScreen screen, float x, float y) {
        super(screen, x, y);
        defineEnemy();
    }

    public void draw(SpriteBatch batch) {
        // 1. Get the current frame
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(this.animation);
        TextureRegion keyFrame = animation.getKeyFrame(this.stateTime, true);

        // 2. Calculate scaled width/height
        float width = keyFrame.getRegionWidth() / Constants.PPM;
        float height = keyFrame.getRegionHeight() / Constants.PPM;

        // 3. Calculate X and Y from Box2D body
        float x = this.b2body.getPosition().x - (width / 2f);
        float y = this.b2body.getPosition().y - (height / 2f);

        // 4. Draw it
        batch.draw(keyFrame, x, y, width, height);
    }
    @Override
    void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(128 / Constants.PPM, 1080 / Constants.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.PLAYER_RADIUS / Constants.PPM);

        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits =
            Constants.GROUND_BIT |
             Constants.BLOCK_BIT |
            Constants.KNIGHT_BIT |
            Constants.OBJECT_BIT |
            Constants.ENEMY_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef);

    }

}
