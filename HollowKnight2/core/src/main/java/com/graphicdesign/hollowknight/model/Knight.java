package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.enums.animation.KnightAnimation;

public class Knight {
    public World world;
    public Body b2body;
    public KnightAnimation animation = KnightAnimation.IDLE;
    public float stateTime = 0f;

    public Knight (World world) {
        this.world = world;
        defineKnight();
    }

    public void defineKnight() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(128 / Constants.PPM, 1080 / Constants.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.PLAYER_RADIUS / Constants.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
    public void draw(SpriteBatch batch) {
        // 1. Get the current frame
        Animation<TextureRegion> animation = AssetManager.getInstance().animationMap.get(this.animation);
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
}
